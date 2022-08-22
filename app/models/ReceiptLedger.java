package models;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ReceiptLedger {

    private String title;

    private String description;

    private String totalCost;

    private String columnDateTime;

    private String columnTrackingId;

    private String columnFieldDescription;

    private String columnDeliveryFee;

    private List<ReceiptLedgerRow> rows = new ArrayList<>();

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getTotalCost() {
        return totalCost;
    }

    public String getColumnDateTime() {
        return columnDateTime;
    }

    public String getColumnTrackingId() {
        return columnTrackingId;
    }

    public String getColumnFieldDescription() {
        return columnFieldDescription;
    }

    public String getColumnDeliveryFee() {
        return columnDeliveryFee;
    }

    public List<ReceiptLedgerRow> getRows() {
        return rows;
    }
}
