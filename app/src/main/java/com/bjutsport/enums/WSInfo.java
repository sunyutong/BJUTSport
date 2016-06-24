package com.bjutsport.enums;

/**
 * Created by hudiyu on 16/6/24.
 */
public enum WSInfo {

    WSDL("http://192.168.1.100:8080/BJUTSport/services/BJUTSportWebServiceImplPort?wsdl"),
    NAMESPACE("http://services.bjutsport.com/");

    private final String address;

    WSInfo(String address) {
        this.address = address;
    }

    public String getAddress() {
        return this.address;
    }
}
