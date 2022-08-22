package models;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ReceiptLedgerRow {
    private String timestamp;

    private String trackingId;

    private String description;

    private String deliveryFee;

    private List<ReceiptLedgerRowChange> changes = new ArrayList<>();

    public String getTimestamp() {
        return timestamp;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public String getDescription() {
        return description;
    }

    public String getDeliveryFee() {
        return deliveryFee;
    }

    public List<ReceiptLedgerRowChange> getChanges() {
        return changes;
    }
}
