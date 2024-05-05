package dev.codescreen.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.codescreen.transaction.ResponseCode;
import dev.codescreen.transaction.TransactionAmount;

//Event when the user has its balance loaded
public class LoadEvent extends Event{
    @JsonProperty
    private String transactionType;

    @JsonProperty
    private String amount;

    @JsonProperty
    private ResponseCode responseCode;

    public LoadEvent(String userId, String transactionId, TransactionAmount transactionAmount) {
        super(userId, transactionId);
        this.transactionType = "load";
        this.amount = transactionAmount.getAmount();
        this.responseCode = ResponseCode.APPROVED;
    }
}
