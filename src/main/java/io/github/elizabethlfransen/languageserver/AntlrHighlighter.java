package io.github.elizabethlfransen.languageserver;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Token;
import org.jline.reader.Highlighter;
import org.jline.reader.LineReader;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

import java.util.function.Function;
import java.util.regex.Pattern;

public abstract class AntlrHighlighter implements Highlighter {

    private final Function<String, Lexer> lexerFactory;

    public AntlrHighlighter(Function<String, Lexer> lexerFactory) {
        this.lexerFactory = lexerFactory;
    }

    @Override
    public AttributedString highlight(LineReader reader, String buffer) {
        Lexer lexer = lexerFactory.apply(buffer);
        AttributedStringBuilder builder = new AttributedStringBuilder();
        int bufferIndex = 0;
        while(!lexer._hitEOF) {
            Token token = lexer.nextToken();
            if(token.getType() == Token.EOF) break;
            if(token.getStartIndex() != bufferIndex) {
                builder.append(buffer.substring(bufferIndex, token.getStartIndex()));
                bufferIndex = token.getStartIndex();
            }
            builder.append(buffer.substring(bufferIndex, token.getStopIndex() + 1), getStyle(token));
            bufferIndex = token.getStopIndex() + 1;
        }
        if(bufferIndex != buffer.length())
            builder.append(buffer.substring(bufferIndex));
        return builder.toAttributedString();
    }

    protected abstract AttributedStyle getStyle(Token token);

    @Override
    public void setErrorPattern(Pattern errorPattern) {

    }

    @Override
    public void setErrorIndex(int errorIndex) {

    }
}
