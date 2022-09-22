package io.github.elizabethlfransen.languageserver;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Token;
import org.jline.utils.AttributedStyle;

import java.util.*;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

public class AntlrHighlighterBuilder {
    private static class Instance extends AntlrHighlighter {
        private final Map<Integer, AttributedStyle> styles;
        private final AttributedStyle defaultStyle;

        public Instance(Function<String, Lexer> lexerFactory, Map<Integer, AttributedStyle> styles, AttributedStyle defaultStyle) {
            super(lexerFactory);
            this.styles = styles;
            this.defaultStyle = defaultStyle;
        }

        @Override
        protected AttributedStyle getStyle(Token token) {
            return styles.getOrDefault(token.getType(), this.defaultStyle);
        }
    }

    private final Map<Integer, AttributedStyle> styles = new HashMap<>();
    private AttributedStyle defaultStyle = AttributedStyle.DEFAULT;

    private CommonTokenScheme commonTokenScheme;

    private Map<Integer, Function<CommonTokenScheme, AttributedStyle>> commonTokens = new HashMap<>();

    private Function<String, Lexer> lexerFactory;

    public AntlrHighlighterBuilder withCommonTokenScheme(CommonTokenScheme commonTokenScheme) {
        this.commonTokenScheme = commonTokenScheme;
        return this;
    }


    public AntlrHighlighterBuilder withKeyword(int... tokenTypes) {
        return withCommonToken(x -> x.keyword, tokenTypes);
    }

    public AntlrHighlighterBuilder withIdentifier(int... tokenTypes) {
        return withCommonToken(x -> x.identifier, tokenTypes);
    }

    public AntlrHighlighterBuilder withString(int... tokenTypes) {
        return withCommonToken(x -> x.string, tokenTypes);
    }

    public AntlrHighlighterBuilder withNumber(int... tokenTypes) {
        return withCommonToken(x -> x.number, tokenTypes);
    }

    public AntlrHighlighterBuilder withBlockComment(int... tokenTypes) {
        return withCommonToken(x -> x.blockComment, tokenTypes);
    }

    public AntlrHighlighterBuilder withLineComment(int... tokenTypes) {
        return withCommonToken(x -> x.lineComment, tokenTypes);
    }

    private AntlrHighlighterBuilder withCommonToken(Function<CommonTokenScheme, AttributedStyle> commonToken, int... tokenTypes) {
        Arrays.stream(tokenTypes).forEach(tokenType -> commonTokens.put(tokenType, commonToken));
        return this;
    }


    public AntlrHighlighterBuilder defaultStyle(AttributedStyle style) {
        this.defaultStyle = style;
        return this;
    }

    public AntlrHighlighterBuilder withTokenStyle(AttributedStyle style, int... tokenTypes) {
        Arrays.stream(tokenTypes).forEach(tokenType ->
                this.styles.put(tokenType, style)
        );
        return this;
    }

    public AntlrHighlighterBuilder lexerFactory(Function<String, Lexer> lexerFactory) {
        this.lexerFactory = lexerFactory;
        return this;
    }

    private Map<Integer, AttributedStyle> buildStyles() {
        if (commonTokenScheme == null)
            return styles;
        Map<Integer, AttributedStyle> commonStyles = commonTokens
                .entrySet()
                .stream()
                .map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue().apply(commonTokenScheme)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        commonStyles.putAll(styles);
        return commonStyles;
    }

    public AntlrHighlighter build() {
        return new Instance(
                Objects.requireNonNull(lexerFactory, "Lexer factory cannot be null"),
                buildStyles(),
                defaultStyle
        );
    }
}
