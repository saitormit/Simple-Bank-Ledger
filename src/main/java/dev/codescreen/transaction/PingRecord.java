package dev.codescreen.transaction;

public class PingRecord {
     private String serverTime;

    public PingRecord(String serverTime) {
        this.serverTime = serverTime;
    }

    public String getServerTime() {
        return serverTime;
    }

    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }
}
