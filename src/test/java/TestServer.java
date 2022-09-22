import io.github.elizabethlfransen.languageserver.LanguageServer;
import org.jline.reader.Highlighter;
import org.jline.reader.LineReader;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.jline.utils.Colors;

import java.io.IOException;
import java.util.regex.Pattern;

public class TestServer {
    public static void main(String[] args) throws IOException {
        LanguageServer server = LanguageServer.builder()
                .interpreter((program, client) -> "HI")
                .highlighter(new Highlighter() {
                    @Override
                    public AttributedString highlight(LineReader reader, String buffer) {
                        return new AttributedStringBuilder()
                                .append(buffer, AttributedStyle.DEFAULT.foreground(AttributedStyle.RED))
                                .toAttributedString();
                    }

                    @Override
                    public void setErrorPattern(Pattern errorPattern) {

                    }

                    @Override
                    public void setErrorIndex(int errorIndex) {

                    }
                })
                .build();
    }
}
