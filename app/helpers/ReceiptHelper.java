package helpers;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.ElementList;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.css.CssFile;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.html.CssAppliers;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.ElementHandlerPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import lombok.SneakyThrows;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;

public class ReceiptHelper extends PdfPageEventHelper {
    public static final Rectangle PAGE_SIZE = PageSize.A4;

    public static final float MARGIN_TOP = 116;
    public static final float MARGIN_BOTTOM = 82;
    public static final float MARGIN_LEFT = 70;
    public static final float MARGIN_RIGHT = 70;

    public static final float HEADER_LLX = MARGIN_LEFT;
    public static final float HEADER_LLY = PAGE_SIZE.getTop() - MARGIN_TOP;
    public static final float HEADER_URX = PAGE_SIZE.getRight() - MARGIN_RIGHT;
    public static final float HEADER_URY = PAGE_SIZE.getTop() - 16;

    private static final float FOOTER_LLX = MARGIN_LEFT;
    private static final float FOOTER_LLY = PAGE_SIZE.getBottom();
    private static final float FOOTER_URX = PAGE_SIZE.getRight() - MARGIN_RIGHT;
    private static final float FOOTER_URY = MARGIN_BOTTOM;

    private static final String LOGO = "/public/images/logo.png";
    private static final float LOGO_HORIZONTAL_POSITION = PAGE_SIZE.getBottom() + MARGIN_LEFT;
    private static final float LOGO_VERTICAL_POSITION = PAGE_SIZE.getBottom() + 20;

    private static final float LOGO_WIDTH = 100;
    private static final float LOGO_HEIGHT = 60;

    private String css;
    private String footer;
    private FontProvider fontProvider;

    public ReceiptHelper(FontProvider fontProvider, String css) {
        this.fontProvider = fontProvider;
        this.css = css;
    }

    public void setFooterHtml(String footer) {
        this.footer = footer;
    }

    @SneakyThrows
    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        ColumnText ct = new ColumnText(writer.getDirectContent());

        //footer
        ElementList elements = getElementList(this.footer, this.css);
        ct.setSimpleColumn(new Rectangle(FOOTER_LLX, FOOTER_LLY, FOOTER_URX, FOOTER_URY));
        for (Element e : elements) {
            ct.addElement(e);
        }
        ct.go();

        //logo
        URL url = getClass().getResource(LOGO);
        Image logo = Image.getInstance(url);
        logo.scaleToFit(LOGO_WIDTH, LOGO_HEIGHT);
        logo.setAbsolutePosition(LOGO_HORIZONTAL_POSITION, LOGO_VERTICAL_POSITION);
        PdfContentByte canvas = writer.getDirectContentUnder();
        canvas.addImage(logo);
    }

    public ElementList getElementList(String html, String css) throws IOException {
        CSSResolver cssResolver = new StyleAttrCSSResolver();
        CssFile cssFile = XMLWorkerHelper.getCSS(new ByteArrayInputStream(css.getBytes()));
        cssResolver.addCss(cssFile);

        CssAppliers cssAppliers = new CssAppliersImpl(fontProvider);
        HtmlPipelineContext htmlContext = new HtmlPipelineContext(cssAppliers);
        htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());

        ElementList elements = new ElementList();
        ElementHandlerPipeline end = new ElementHandlerPipeline(elements, null);
        HtmlPipeline htmlPipeline = new HtmlPipeline(htmlContext, end);
        CssResolverPipeline cssPipeline = new CssResolverPipeline(cssResolver, htmlPipeline);

        XMLWorker xmlWorker = new XMLWorker(cssPipeline, true);
        XMLParser p = new XMLParser(xmlWorker);
        p.parse(new ByteArrayInputStream(html.getBytes()));

        return elements;
    }

}
