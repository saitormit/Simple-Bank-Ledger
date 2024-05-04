package dev.codescreen.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.codescreen.transaction.ResponseCode;
import dev.codescreen.transaction.TransactionAmount;

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
