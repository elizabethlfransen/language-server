package io.github.elizabethlfransen.languageserver;

import io.github.elizabethlfransen.languageserver.util.IOThrowableConsumer;
import io.github.elizabethlfransen.languageserver.util.LambdaUtil;
import io.github.elizabethlfransen.languageserver.util.ThrowableRunnable;
import org.jline.reader.Highlighter;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;
import java.util.Vector;
import java.util.function.Consumer;

public final class LanguageServer implements Closeable {
    private final ServerSocket serverSocket;
    private final Highlighter highlighter;
    private final Interpreter interpreter;
    private final Vector<LanguageClient> clients = new Vector<>();

    private final Vector<IOThrowableConsumer<LanguageClient>> connectHandlers = new Vector<>();

    private LanguageServer(int port, Highlighter highlighter, Interpreter interpreter) throws IOException {
        serverSocket = new ServerSocket(port, 0, InetAddress.getLocalHost());
        this.highlighter = highlighter;
        this.interpreter = interpreter;
    }

    public void acceptClients() throws IOException {
        while (!serverSocket.isClosed()) {
            acceptClient(serverSocket.accept());
        }
    }

    public void onConnect(IOThrowableConsumer<LanguageClient> handler) {
        connectHandlers.add(handler);
    }

    private void acceptClient(Socket socket) throws IOException {
        LanguageClient client = new LanguageClient(socket);
        LanguageService service = new LanguageService(client, highlighter, interpreter);
        for (IOThrowableConsumer<LanguageClient> handler : connectHandlers) {
            handler.accept(client);
        }
        LanguagePacketHandler packetHandler = new LanguagePacketHandler(client, service);
        clients.add(client);
        Thread clientThread = new Thread(() -> {
            while (!socket.isClosed()) {
                try {
                    packetHandler.handleFunction();
                } catch (IOException e) {
                    break;
                }
            }
            clients.remove(client);
        });
        clientThread.start();
    }



    @Override
    public void close() throws IOException {
        serverSocket.close();
        for (LanguageClient client : clients) {
            if (!client.isClosed())
                client.close();
        }
    }

    public static final class Builder {
        private int port = 3420;
        private Highlighter highlighter;
        private Interpreter interpreter;

        public Builder port(int port) {
            this.port = port;
            return this;
        }

        public Builder highlighter(Highlighter highlighter) {
            this.highlighter = highlighter;
            return this;
        }

        public Builder interpreter(Interpreter interpreter) {
            this.interpreter = interpreter;
            return this;
        }

        public LanguageServer build() throws IOException {
            return new LanguageServer(
                    port,
                    highlighter,
                    Objects.requireNonNull(interpreter, "Interpreter must be specified")
            );
        }

        private Builder() {
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
