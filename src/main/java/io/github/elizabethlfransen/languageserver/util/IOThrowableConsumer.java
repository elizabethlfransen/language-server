package io.github.elizabethlfransen.languageserver.util;

import java.io.IOException;

@FunctionalInterface
public interface IOThrowableConsumer<T> {
    void accept(T input) throws IOException;
}
