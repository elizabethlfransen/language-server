package io.github.elizabethlfransen.languageserver;

public interface Interpreter {
    String interpret(String program, LanguageClient client) throws ProgramInterpretException;
}
