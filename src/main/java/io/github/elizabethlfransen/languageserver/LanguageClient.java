package io.github.elizabethlfransen.languageserver;

import org.jline.utils.AttributedString;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public final class LanguageClient implements Closeable {
    public final DataInputStream input;
    private final DataOutputStream output;
    private final Socket socket;

    public LanguageClient(Socket socket) throws IOException {
        this.socket = socket;
        this.input = new DataInputStream(socket.getInputStream());
        this.output = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }

    public boolean isClosed() {
        return socket.isClosed();
    }

    public void sendHighlightResult(AttributedString result) throws IOException {
        this.output.writeUTF("highlight");
        this.output.writeUTF(result.toAnsi());
    }

    public void sendOutput(String output) throws IOException {
        this.output.writeUTF("output");
        this.output.writeUTF(output);
    }

    public void sendOutput(AttributedString result) throws IOException {
        sendOutput(result.toAnsi());
    }

    public void sendError(String error) throws IOException {
        this.output.writeUTF("error");
        this.output.writeUTF(error);
    }
}
