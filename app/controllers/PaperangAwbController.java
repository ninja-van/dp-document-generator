package controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.Document;
import com.itextpdf.text.FontProvider;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import helpers.DefaultFontProvider;
import helpers.PdfUtils;
import lombok.SneakyThrows;
import models.PaperangAwb;
import models.PaperangAwbRequest;
import play.Logger;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import static play.mvc.Controller.request;
import static play.mvc.Http.Context.Implicit.response;

public class PaperangAwbController {


    private static final String CHARSET_NAME = "UTF-8";
    private static final String CSS_PATH = "/public/css/paperang_awb.css";
    private static final Rectangle PAGE_SIZE = PageSize.A7.rotate();
    private static final float MARGIN = 0;

    private final FontProvider fontProvider = new DefaultFontProvider();

    public static void main(String[] args) throws JsonProcessingException {
        PaperangAwbRequest request = new PaperangAwbRequest();
        request.getAwbs().add(new PaperangAwb());


        System.out.println(Json.mapper().writeValueAsString(request));
    }

    public Result getPaperangAwb() {
        String json = request().body().asJson().toString();
        ObjectMapper objectMapper = new ObjectMapper();

        PaperangAwbRequest request;
        try {
            request = objectMapper.readerFor(PaperangAwbRequest.class).readValue(json);
        } catch (IOException e) {
            return Results.status(500);
        }

        byte[] content = renderPaperangAwbs(request.getAwbs());
        response().setHeader("Content-Disposition", "inline; filename=paperang_awb.pdf");

        Result playResult = Results.status(Http.Status.OK, content);
        return playResult.as("application/pdf");
    }

    @SneakyThrows
    private byte[] renderPaperangAwbs(List<PaperangAwb> paperangAwbs) {
        Document document = new Document(PAGE_SIZE, MARGIN, MARGIN, MARGIN, MARGIN);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter pdfWriter = PdfWriter.getInstance(document, byteArrayOutputStream);

        document.open();

        String html = PdfUtils.getHtml(paperangAwbs);

        XMLWorkerHelper worker = XMLWorkerHelper.getInstance();
        String cssStyle = getDefaultCssStyle();
        PdfUtils.parseXHtml(
            worker,
            pdfWriter,
            document,
            new ByteArrayInputStream(html.getBytes(CHARSET_NAME)),
            new ByteArrayInputStream(cssStyle.getBytes(CHARSET_NAME)),
            Charset.forName(CHARSET_NAME),
            fontProvider
        );

        document.close();

        byte[] pdfAsBytes = byteArrayOutputStream.toByteArray();
        PdfReader reader = new PdfReader(pdfAsBytes);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream output = new DataOutputStream(outputStream);

        PdfStamper stamper = new PdfStamper(reader, output);
        stamper.close();

        return outputStream.toByteArray();
    }

    private String getDefaultCssStyle() {
        try {
            return new String(Files.readAllBytes(Paths.get(Objects.requireNonNull(getClass().getResource(CSS_PATH)).getPath())));
        } catch (IOException | FileSystemNotFoundException e) {
            Logger.error(CSS_PATH + " does not exist", e);
            return "";
        }
    }

}
