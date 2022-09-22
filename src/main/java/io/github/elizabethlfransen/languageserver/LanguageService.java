package io.github.elizabethlfransen.languageserver;

import org.jline.reader.Highlighter;
import org.jline.utils.AttributedString;

public class LanguageService {
    private final LanguageClient client;
    private final Highlighter highlighter;
    private final Interpreter interpreter;

    public LanguageService(LanguageClient client, Highlighter highlighter, Interpreter interpreter) {
        this.client = client;
        this.highlighter = highlighter;
        this.interpreter = interpreter;
    }

    public AttributedString highlight(String line) {
        if(highlighter == null)
            return AttributedString.fromAnsi(line);
        return highlighter.highlight(null, line);
    }

    public String interpret(String program) throws ProgramInterpretException {
        return interpreter.interpret(program,client);
    }
}
