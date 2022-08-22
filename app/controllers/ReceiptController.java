package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.ElementList;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import helpers.DefaultFontProvider;
import helpers.PdfUtils;
import helpers.ReceiptHelper;
import lombok.SneakyThrows;
import models.*;
import play.i18n.Lang;
import play.i18n.Messages;
import play.i18n.MessagesApi;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static config.Constants.SOURCE_CODE_URL;
import static helpers.ReceiptHelper.*;
import static play.mvc.Controller.request;
import static play.mvc.Http.Context.Implicit.response;

public class ReceiptController {

    private static final String FONT_FAMILY = "Noto Sans";

    private static final String FONT_FAMILY_PATH = "/public/fonts/NotoSans-Black.ttf";

    private static final String CHARSET_NAME = "UTF-8";

    private static final String CSS_PATH = "/public/css/orders_receipt.css";

    @SneakyThrows
    public static void main(String[] args) {
        ReceiptRequest request = new ReceiptRequest();

        ReceiptLedger receiptLedger = new ReceiptLedger();
        ReceiptLedgerRow row =new ReceiptLedgerRow();
        ReceiptLedgerRowChange change = new ReceiptLedgerRowChange();

        row.getChanges().add(change);
        receiptLedger.getRows().add(row);

        request.setReceiptLedger(receiptLedger);

        request.getReservations().add(new ReceiptReservation());

        System.out.println(Json.mapper().writeValueAsString(request));
    }

    public Result getReceipt() {
        String json = request().body().asJson().toString();
        ObjectMapper objectMapper = new ObjectMapper();

        ReceiptRequest request;
        try {
            request = objectMapper.readerFor(ReceiptRequest.class).readValue(json);
        } catch (IOException e) {
            return Results.status(500);
        }

        byte[] pdf = generateReceipt(request);

        response().setHeader("Content-Disposition", "inline; filename=receipt.pdf");

        Result playResult = Results.status(Http.Status.OK, pdf);
        return playResult.as("application/pdf");
    }

    @SneakyThrows
    private byte[] generateReceipt(ReceiptRequest request) {
        ReceiptHelper receiptHelper = new ReceiptHelper(new DefaultFontProvider(), getDefaultCssStyle());

        receiptHelper.setFooterHtml(getFooter(request));

        Document document = new Document(PAGE_SIZE, MARGIN_LEFT, MARGIN_RIGHT, MARGIN_TOP, MARGIN_BOTTOM);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter pdfWriter = PdfWriter.getInstance(document, byteArrayOutputStream);
        pdfWriter.setPageEvent(receiptHelper);

        document.open();

        String html = getHtml(request);

        // XML Worker
        XMLWorkerHelper worker = XMLWorkerHelper.getInstance();
        String cssStyle = getDefaultCssStyle();
        FontFactory.register(Objects.requireNonNull(getClass().getResource(FONT_FAMILY_PATH)).toString(), FONT_FAMILY);
        PdfUtils.parseXHtml(worker, pdfWriter, document, new ByteArrayInputStream(html.getBytes(CHARSET_NAME)), new ByteArrayInputStream(cssStyle.getBytes(CHARSET_NAME)), Charset.forName(CHARSET_NAME), new DefaultFontProvider());

        document.close();

        // Generate Page Numbering - independent of all variable above this line
        int pageCount = pdfWriter.getPageNumber();
        byte[] pdfAsBytes = byteArrayOutputStream.toByteArray();

        PdfReader reader = new PdfReader(pdfAsBytes);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream output = new DataOutputStream(outputStream);

        document = new Document();
        document.open();
        PdfStamper stamper = new PdfStamper(reader, output);

        for (int i = 1; i <= pageCount; i++) {
            ColumnText ct = new ColumnText(stamper.getOverContent(i));
            String header = getHeader(request, i, pageCount);
            ElementList elements = receiptHelper.getElementList(header, cssStyle);
            ct.setSimpleColumn(new Rectangle(HEADER_LLX, HEADER_LLY, HEADER_URX, HEADER_URY));
            for (Element e : elements) {
                ct.addElement(e);
            }
            ct.go();
        }
        stamper.close();

        document.addAuthor(SOURCE_CODE_URL + " licensed under the MIT License");
        document.addCreator("Please access " + SOURCE_CODE_URL + " for source codes of this service");

        document.close();

        return outputStream.toByteArray();
    }

    @SuppressWarnings("ConstantConditions")
    private String getHtml(ReceiptRequest receiptRequest) {
        return views.html.orders_receipt.render(receiptRequest).body();
    }

    private String getHeader(ReceiptRequest receiptRequest, int pageNumber, int pageCount) {
        return views.html.orders_receipt_header.render(receiptRequest, pageNumber, pageCount).body();
    }

    private String getFooter(ReceiptRequest receiptRequest) {
        return views.html.orders_receipt_footer.render(receiptRequest).body();
    }

    @SneakyThrows
    private String getDefaultCssStyle() {
        return new String(Files.readAllBytes(Paths.get(Objects.requireNonNull(getClass().getResource(CSS_PATH)).getPath())));
    }
}