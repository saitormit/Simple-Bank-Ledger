package dev.bankledger.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//Parent class of possible events of transaction. Timestamp is taken whenever its child class is instantiated
public class Event {
    private String userId;
    private String transactionId;
    private String timeStamp;


    public Event(String userId, String transactionId) {
        this.userId = userId;
        this.transactionId = transactionId;
        this.timeStamp = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
    }

    public String getUserId() {
        return userId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

}
