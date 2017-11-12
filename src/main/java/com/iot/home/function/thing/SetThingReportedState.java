// Copyright 2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License"). You may
// not use this file except in compliance with the License. A copy of the
// License is located at
//
//	  http://aws.amazon.com/apache2.0/
//
// or in the "license" file accompanying this file. This file is distributed
// on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
// express or implied. See the License for the specific language governing
// permissions and limitations under the License.


package com.iot.home.function.thing;


import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iot.home.domain.AWSOnOffButton;
import com.iot.home.sao.AWSIoTThingSao;
import com.iot.home.sao.ThingSao;
import org.apache.log4j.Logger;

import java.util.Map;

public class SetThingReportedState implements RequestHandler<Map<String, Object>, Boolean> {

    private static final Logger log = Logger.getLogger(SetThingReportedState.class);

    private static final ThingSao thingSao = AWSIoTThingSao.instance();
    private ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);



    public void execute(String thingId, String desiredState) throws Exception {

        if (null == thingId || null == desiredState || thingId.isEmpty() || desiredState.isEmpty()) {
            log.error("Invalid SetThingDesiredState");
            throw new IllegalArgumentException("thingId or desiredState invalid");
        }

        log.info("SetThingDesiredState for thingId = " + thingId + ", desiredState = " + desiredState);

        String stringState = thingSao.getShadow("RO_Button");

        AWSOnOffButton state = mapper.readValue(stringState, AWSOnOffButton.class);
        state.getState().getDesired().setStatus(desiredState);

        thingSao.updateShadow(thingId, mapper.writeValueAsString(state));
        log.info("SetThingDesiredState success");
    }

    @Override
    public Boolean handleRequest(Map<String, Object> inputMap, Context context) {

        try {
            execute((String) inputMap.get("thingId"), (String) inputMap.get("desiredState"));
        } catch (Exception e) {
            log.error("Failed to update thing state: " + e.toString());
            return false;
        }

        return true;
    }
}
