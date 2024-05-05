package dev.codescreen.model;

//Schema to confirm the connection with the server by returning its current time
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
