package models;

import lombok.Data;

@Data
public class ReceiptLedgerRowChange {

    private String title;

    private String oldValue;

    private String newValue;

    public String getTitle() {
        return title;
    }

    public String getOldValue() {
        return oldValue;
    }

    public String getNewValue() {
        return newValue;
    }
}
