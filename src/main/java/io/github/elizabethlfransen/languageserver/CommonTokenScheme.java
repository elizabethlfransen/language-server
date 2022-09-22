package io.github.elizabethlfransen.languageserver;

import org.jline.utils.AttributedStyle;

import static io.github.elizabethlfransen.languageserver.util.ColorUtil.colorAsStyle;

public class CommonTokenScheme {
    public final AttributedStyle keyword;
    public final AttributedStyle identifier;
    public final AttributedStyle string;
    public final AttributedStyle number;
    public final AttributedStyle blockComment;
    public final AttributedStyle lineComment;

    public static final CommonTokenScheme DARK = new CommonTokenScheme(
            colorAsStyle(0xCC7832),
            colorAsStyle(0x9876AA),
            colorAsStyle(0x6A8759),
            colorAsStyle(0x6897BB),
            colorAsStyle(0x808080),
            colorAsStyle(0x808080)
    );
    public static final CommonTokenScheme LIGHT = new CommonTokenScheme(
            colorAsStyle(0x0033B3),
            colorAsStyle(0x871094),
            colorAsStyle(0x067D17),
            colorAsStyle(0x1750EB),
            colorAsStyle(0x8C8C8C),
            colorAsStyle(0x8C8C8C)
    );

    public CommonTokenScheme(AttributedStyle keyword, AttributedStyle constant, AttributedStyle string, AttributedStyle number, AttributedStyle blockComment, AttributedStyle lineComment) {
        this.keyword = keyword;
        this.identifier = constant;
        this.string = string;
        this.number = number;
        this.blockComment = blockComment;
        this.lineComment = lineComment;
    }
}
