package models;

import lombok.Data;

@Data
public class ReceiptReservation {

    private String trackingId;

    private String totalAmount;

    private String packImage;

    private String toName;

    private String toContact;

    private String toAddress1;

    private String deliveryFee;

    private String insuranceValue;

    private String gstValue;

    private String codValue;

    private String codTotal;

    private String imageUrl;

    private String weight;

    private String gstDescription;

    private String codDescription;


    public String getTrackingId() {
        return trackingId;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public String getPackImage() {
        return packImage;
    }

    public String getToName() {
        return toName;
    }

    public String getToContact() {
        return toContact;
    }

    public String getToAddress1() {
        return toAddress1;
    }

    public String getDeliveryFee() {
        return deliveryFee;
    }

    public String getInsuranceValue() {
        return insuranceValue;
    }

    public String getGstValue() {
        return gstValue;
    }

    public String getCodValue() {
        return codValue;
    }

    public String getCodTotal() {
        return codTotal;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getWeight() {
        return weight;
    }

    public String getGstDescription() {
        return gstDescription;
    }

    public String getCodDescription() {
        return codDescription;
    }
}
