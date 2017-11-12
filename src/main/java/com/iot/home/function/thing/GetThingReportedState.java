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

public class GetThingReportedState implements RequestHandler<Map<String, Object>, String> {

    private static final Logger log = Logger.getLogger(GetThingReportedState.class);

    private static final ThingSao thingSao = AWSIoTThingSao.instance();
    private ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);



    public String execute(String thingId) throws Exception {

        if (null == thingId || thingId.isEmpty()) {
            log.error("Invalid GetThingReportedState request");
            throw new IllegalArgumentException("thingId invalid");
        }

        log.info("GetThingReportedState for thingId = " + thingId);

        String stringState = thingSao.getShadow("RO_Button");

        AWSOnOffButton state = mapper.readValue(stringState, AWSOnOffButton.class);

        log.info("GetThingReportedState success");

        return state.getState().getReported().getStatus();
    }

    @Override
    public String handleRequest(Map<String, Object> inputMap, Context context) {

        try {
            return execute((String) inputMap.get("thingId"));
        } catch (Exception e) {
            log.error("Failed to update thing state: " + e.toString());
        }

        return "";
    }
}
