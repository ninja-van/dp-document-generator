package models;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ReceiptRequest {

    private String invoiceNumber;

    private String invoiceDate;

    private String invoiceTime;

    private String language;

    private String locationUrl;

    private String locationContact;

    private String landingPageUrl;

    private String companyUrl;

    private String supportEmail;

    private String companyName;

    private String taxIdentification;

    private boolean receiptUpdated;

    private String gstDescription;

    private String codDescription;

    private String grandTotal;

    private ReceiptLedger receiptLedger;

    private List<ReceiptReservation> reservations = new ArrayList<>();

    private String fromName;

    private String idNumber;

    private String fromContact;

    private String fromEmail;

    private String billingAddress;

    private String billingPostcode;

    private boolean prepaid;

    private String country;

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public String getInvoiceTime() {
        return invoiceTime;
    }

    public String getLanguage() {
        return language;
    }

    public String getLocationUrl() {
        return locationUrl;
    }

    public String getLocationContact() {
        return locationContact;
    }

    public String getLandingPageUrl() {
        return landingPageUrl;
    }

    public String getCompanyUrl() {
        return companyUrl;
    }

    public String getSupportEmail() {
        return supportEmail;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getTaxIdentification() {
        return taxIdentification;
    }

    public boolean isReceiptUpdated() {
        return receiptUpdated;
    }

    public String getGstDescription() {
        return gstDescription;
    }

    public String getCodDescription() {
        return codDescription;
    }

    public String getGrandTotal() {
        return grandTotal;
    }

    public ReceiptLedger getReceiptLedger() {
        return receiptLedger;
    }

    public List<ReceiptReservation> getReservations() {
        return reservations;
    }

    public String getFromName() {
        return fromName;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public String getFromContact() {
        return fromContact;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public String getBillingPostcode() {
        return billingPostcode;
    }

    public boolean isPrepaid() {
        return prepaid;
    }

    public String getCountry() {
        return country;
    }
}
