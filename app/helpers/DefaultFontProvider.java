package helpers;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.FontProvider;
import com.itextpdf.text.pdf.BaseFont;
import lombok.SneakyThrows;

import java.util.Objects;

public class DefaultFontProvider implements FontProvider {

    private static final String FONT_FAMILY = "Noto Sans";
    private static final String FONT_FAMILY_PATH = "/public/fonts/NotoSans-Black.ttf";

    public DefaultFontProvider() {
        FontFactory.register(Objects.requireNonNull(getClass().getResource(FONT_FAMILY_PATH)).toString(), FONT_FAMILY);
    }

    @Override
    public boolean isRegistered(String fontName) {
        return false;
    }

    @SneakyThrows
    @Override
    public Font getFont(String fontName, String encoding, boolean embedded, float size, int style, BaseColor color) {
        BaseFont bf = BaseFont.createFont(Objects.requireNonNull(getClass().getResource(FONT_FAMILY_PATH)).toString(), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        return new Font(bf, size, style, color);
    }

}
