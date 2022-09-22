package io.github.elizabethlfransen.languageserver.util;

@FunctionalInterface
public interface ThrowableRunnable {
    void run() throws Throwable;
}
