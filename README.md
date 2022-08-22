# DP Document Generator

## Overview

This service generates a DP documents based on request parameters passed in via a REST API call.

## Prerequisites

* Java JDK 8.x

## Getting started

### Step 1.

Clone this repository, and run with the following

```bash
sbt clean update compile run
```

## Usage examples

### Airway Bill (Paperang Format)

Generate a Airway Bill in paperang format with the following. This assumes that you are running the service locally.

```bash
curl --location --request POST 'localhost:9000/1.0/generate-awb' \
--header 'Content-Type: application/json' \
--data-raw '{
    "awbs": [
        {
            "trackingId": "A0000001",
            "fromName": "Shipper A",
            "postalCode": "Consignee A'\''s Postal Code",
            "toAddress": "Consignee A'\''s Address",
            "toContact": "Consignee A'\''s Contact",
            "toName": "Consignee A",
            "sortCode": "Consignee A'\''s Sort Code",
            "isCod": false
        }
    ]
}'
```

### Receipt

Generate a receipt with the following. This assumes that you are running the service locally.

```bash
curl --location --request POST 'localhost:9000/1.0/generate-receipt' \
--header 'Content-Type: application/json' \
--data-raw '{
    "invoiceNumber": "INV000001",
    "invoiceDate": "2020-08-01",
    "invoiceTime": "10:11 AM1",
    "language": "EN",
    "locationUrl": "https://www.google.com",
    "locationContact": "Location'\''s Contact",
    "landingPageUrl": "https://www.google.com",
    "companyUrl": "https://www.google.com",
    "supportEmail": "support@here.unknown",
    "companyName": "Company A",
    "taxIdentification": null,
    "receiptUpdated": false,
    "gstDescription": "Standard GST",
    "codDescription": null,
    "grandTotal": "100 SGD",
    "receiptLedger": {},
    "reservations": [
        {
            "trackingId": "A0000001",
            "totalAmount": "49.90 SGD",
            "packImage": null,
            "toName": "Consignee A",
            "toContact": "Consignee A'\''s Contact",
            "toAddress1": "Consignee A'\''s Address",
            "deliveryFee": "10.00 SGD",
            "insuranceValue": "2.00 SGD",
            "gstValue": "1.00 SGD",
            "codValue": null,
            "codTotal": null,
            "imageUrl": null,
            "weight": "1 KG",
            "gstDescription": "Standard GST",
            "codDescription": null
        },
        {
            "trackingId": "A0000002",
            "totalAmount": "51.10 SGD",
            "packImage": null,
            "toName": "Consignee A",
            "toContact": "Consignee A'\''s Contact",
            "toAddress1": "Consignee A'\''s Address",
            "deliveryFee": "10.00 SGD",
            "insuranceValue": "2.00 SGD",
            "gstValue": "1.00 SGD",
            "codValue": null,
            "codTotal": null,
            "imageUrl": null,
            "weight": "1 KG",
            "gstDescription": "Standard GST",
            "codDescription": null
        }
    ],
    "fromName": "Shipper A",
    "idNumber": "A1023",
    "fromContact": "Shipper A'\''s Contact",
    "fromEmail": "Shipper A'\''s Email",
    "billingAddress": "Consignee A'\''s Billing Address",
    "billingPostcode": "Consignee A'\''s Billing Postcode",
    "prepaid": false,
    "country": "SG"
}'
```

## License

This source codes are published by Ninja Logistics Pte. Ltd. on open source licence terms pursuant to MIT License, and
the codes incorporates `com.itextpdf.itextpdf 5.5.13`, `com.itextpdf.tool.xmlworker 5.5.13` GNU AGPL V.3"

## Contributing

Pull requests are welcome. For major changes, please start a conversation first to discuss what you would like to
change.
