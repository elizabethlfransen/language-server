package io.github.elizabethlfransen.languageserver.util;

import org.jline.utils.AttributedStyle;

public class ColorUtil {
    public static AttributedStyle colorAsStyle(int color) {
        return AttributedStyle.DEFAULT.foregroundRgb(color);
    }
}
