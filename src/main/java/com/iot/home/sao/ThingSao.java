package com.iot.home.sao;

public interface ThingSao {

    String getShadow(String thingName);

    String updateShadow(String thingName, String desiredState);
}
