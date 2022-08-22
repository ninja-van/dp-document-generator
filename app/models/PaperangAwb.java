package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.BarcodeQRCode;
import helpers.Images;
import lombok.Data;
import lombok.SneakyThrows;
import org.apache.commons.lang3.BooleanUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

@Data
public class PaperangAwb {

    private static final String LOGO_PATH = "/public/images/logo.png";

    private String trackingId;
    private String sortCode;
    private String postalCode;
    private String toAddress;
    private String toContact;
    private String toName;
    private String fromName;
    private Boolean isCod;

    public String getTrackingId() {
        if (trackingId == null) {
            return "";
        }
        return trackingId;
    }

    public String getSortCode() {
        if (sortCode == null) {
            return "";
        }
        return sortCode;
    }

    public String getPostalCode() {
        if (postalCode == null) {
            return "";
        }
        return postalCode;
    }

    public String getToAddress() {
        if (toAddress == null) {
            return "";
        }
        return toAddress;
    }

    public String getToContact() {
        if (toContact == null) {
            return "";
        }
        return toContact;
    }

    public String getToName() {
        if (toName == null) {
            return "";
        }
        return toName;
    }

    public String getFromName() {
        if (fromName == null) {
            return "";
        }
        return fromName;
    }

    @JsonIgnore
    public String getQrCodeImg() {
        BarcodeQRCode qrCode = new BarcodeQRCode(getTrackingId(), 500, 500, null);
        java.awt.Image qrCodeImg = qrCode.createAwtImage(Color.BLACK, Color.WHITE);
        String imgString = Images.encodeToString(qrCodeImg, "PNG");
        return getBase64ImageData(imgString);
    }

    @JsonIgnore
    public String getBarcodeImg() {
        Barcode128 code128 = new Barcode128();
        code128.setCode(getTrackingId());
        java.awt.Image barcode1DImg = code128.createAwtImage(Color.BLACK, Color.WHITE);
        String imgString = Images.encodeToString(barcode1DImg, "PNG");
        return getBase64ImageData(imgString);
    }

    @SneakyThrows
    @JsonIgnore
    public String getLogoImg() {
        String imgString = "";
        URL url = getClass().getResource(LOGO_PATH);
        if (url != null) {
            BufferedImage bufferedImage = ImageIO.read(url);
            imgString = Images.encodeToString(bufferedImage, "PNG");
        }
        return getBase64ImageData(imgString);
    }

    public boolean getIsCod() {
        return BooleanUtils.isTrue(isCod);
    }

    @JsonIgnore
    private String getBase64ImageData(String imageStr) {
        return String.format("data:image/png;base64,%s", imageStr);
    }

}
