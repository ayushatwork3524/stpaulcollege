package com.main.stpaul.services.serviceInterface;


import java.util.List;


import com.main.stpaul.entities.Receipt;

public interface ReceiptService {
    Receipt addReceipt(Receipt receipt);
    Receipt getReceiptById(String id);
    List<Receipt> getReceiptByPaymentDetail(String id);
}
