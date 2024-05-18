package dev.bankledger.model;

import com.fasterxml.jackson.annotation.JsonProperty;

//Event when the user does not have enough balance and authorization process is denied
public class AuthorizationDeclinedEvent extends Event {
    @JsonProperty
    private String transactionType;

    @JsonProperty
    private String amount;

    @JsonProperty
    private ResponseCode responseCode;

    public AuthorizationDeclinedEvent(String userId, String transactionId, TransactionAmount transactionAmount) {
        super(userId, transactionId);
        this.transactionType = "authorization";
        this.amount = transactionAmount.getAmount();
        this.responseCode = ResponseCode.DECLINED;
    }
}
