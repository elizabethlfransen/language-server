package io.github.elizabethlfransen.languageserver;

import java.io.IOException;

public class LanguagePacketHandler {
    private final LanguageClient client;
    private final LanguageService service;

    public LanguagePacketHandler(LanguageClient client, LanguageService service) {
        this.client = client;
        this.service = service;
    }


    public void handleFunction() throws IOException {
        String functionName = client.input.readUTF();
        if (functionName.equals("highlight")) {
            handleHighlight();
            return;
        }
        if(functionName.equals("interpret")) {
            handleInterpret();
        }
    }

    private void handleHighlight() throws IOException {
        String line = client.input.readUTF();
        client.sendHighlightResult(service.highlight(line));
    }

    private void handleInterpret() throws IOException {
        String program = client.input.readUTF();
        try {
            client.sendOutput(service.interpret(program));
        } catch (ProgramInterpretException e) {
            client.sendError(e.getMessage());
        }
    }
}
