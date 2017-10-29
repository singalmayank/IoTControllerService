/**
 * null
 */
package com.iot.home.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

import java.io.Serializable;
import java.util.List;

@DynamoDBDocument
public class Thing implements Serializable {

    private String currentState;

    private java.util.List<String> possibleStates;

    private String thingDisplayName;

    private String thingId;

    private String typeId;

    public Thing() {
    }

    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

    public List<String> getPossibleStates() {
        return possibleStates;
    }

    public void setPossibleStates(List<String> possibleStates) {
        this.possibleStates = possibleStates;
    }

    public String getThingDisplayName() {
        return thingDisplayName;
    }

    public void setThingDisplayName(String thingDisplayName) {
        this.thingDisplayName = thingDisplayName;
    }

    public String getThingId() {
        return thingId;
    }

    public void setThingId(String thingId) {
        this.thingId = thingId;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

}
