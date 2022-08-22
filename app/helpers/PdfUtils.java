package helpers;

import com.itextpdf.text.Document;
import com.itextpdf.text.FontProvider;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.css.CssFilesImpl;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.HTML;
import com.itextpdf.tool.xml.html.TagProcessorFactory;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import models.PaperangAwb;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

import static com.itextpdf.tool.xml.XMLWorkerHelper.getCSS;

public class PdfUtils {

    public static String getHtml(List<PaperangAwb> paperangAwbs) {
        return views.html.paperang_awbs.render(paperangAwbs).body();
    }

    public static void parseXHtml(
        XMLWorkerHelper xmlWorkerHelper,
        PdfWriter writer,
        Document doc,
        InputStream in,
        InputStream inCssFile,
        Charset charset,
        FontProvider fontProvider
    ) throws IOException {
        final TagProcessorFactory tagProcessorFactory = Tags.getHtmlTagProcessorFactory();
        tagProcessorFactory.removeProcessor(HTML.Tag.IMG);

        // pass custom tag processor that supports rendering base 64 image on <img>
        tagProcessorFactory.addProcessor(new CustomTagProcessor(), HTML.Tag.IMG);

        CssFilesImpl cssFiles = new CssFilesImpl();
        if (inCssFile != null) {
            cssFiles.add(getCSS(inCssFile));
        } else {
            cssFiles.add(xmlWorkerHelper.getDefaultCSS());
        }

        StyleAttrCSSResolver cssResolver = new StyleAttrCSSResolver(cssFiles);
        HtmlPipelineContext hpc = new HtmlPipelineContext(new CssAppliersImpl(fontProvider));
        hpc.setAcceptUnknown(true).autoBookmark(true).setTagFactory(tagProcessorFactory);
        HtmlPipeline htmlPipeline = new HtmlPipeline(hpc, new PdfWriterPipeline(doc, writer));
        CssResolverPipeline pipeline = new CssResolverPipeline(cssResolver, htmlPipeline);
        XMLWorker worker = new XMLWorker(pipeline, true);
        XMLParser p = new XMLParser(true, worker, charset);
        if (charset != null) {
            p.parse(in, charset);
        } else {
            p.parse(in);
        }
    }

}
