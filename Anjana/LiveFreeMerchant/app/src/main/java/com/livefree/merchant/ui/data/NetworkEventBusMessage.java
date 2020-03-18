package com.livefree.merchant.ui.data;

public class NetworkEventBusMessage {
    private int resultCode;
    private String resultValue;

    public NetworkEventBusMessage(int resultCode, String resultValue) {
        this.resultCode = resultCode;
        this.resultValue = resultValue;
    }

    public NetworkEventBusMessage(int eventConnectivityLost) {
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultValue() {
        return resultValue;
    }

    public void setResultValue(String resultValue) {
        this.resultValue = resultValue;
    }
}
