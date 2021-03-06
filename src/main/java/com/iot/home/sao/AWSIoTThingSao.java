package com.iot.home.sao;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.iotdata.AWSIotDataClient;
import com.amazonaws.services.iotdata.model.GetThingShadowRequest;
import com.amazonaws.services.iotdata.model.GetThingShadowResult;
import com.amazonaws.services.iotdata.model.UpdateThingShadowRequest;
import com.amazonaws.services.iotdata.model.UpdateThingShadowResult;
import org.apache.log4j.Logger;

import java.nio.ByteBuffer;

public class AWSIoTThingSao implements ThingSao {

    private static final Logger log = Logger.getLogger(AWSIoTThingSao.class);

    private static volatile AWSIoTThingSao instance;

    private static final Regions MY_REGION = Regions.US_WEST_2;
    private static final String ENDPOINT = "a3b4k2bwh9z4oj.iot.us-west-2.amazonaws.com";

    AWSIotDataClient iotDataClient;


    public static AWSIoTThingSao instance() {

        if (instance == null) {
            synchronized(AWSIoTThingSao.class) {
                if (instance == null)
                    instance = new AWSIoTThingSao();
            }
        }
        return instance;
    }

    private AWSIoTThingSao() {

        iotDataClient = new AWSIotDataClient();
        iotDataClient.setRegion(Region.getRegion(Regions.US_WEST_2));
        iotDataClient.setEndpoint(ENDPOINT);
    }

    @Override
    public String getShadow(String thingName) {

        try {
            GetThingShadowRequest getThingShadowRequest = new GetThingShadowRequest()
                                                                  .withThingName(thingName);
            GetThingShadowResult result = iotDataClient.getThingShadow(getThingShadowRequest);
            byte[] bytes = new byte[result.getPayload().remaining()];
            result.getPayload().get(bytes);
            String resultString = new String(bytes);
            log.info("getShadow success for thing: " + thingName);

            return resultString;
        } catch (Exception e) {
            log.error("getShadow failed for thing: " + thingName + ", with error: " + e.toString());
        }
        return null;
    }

    @Override
    public String updateShadow(String thingName, String desiredState) {

        try {
            UpdateThingShadowRequest updateThingShadowRequest = new UpdateThingShadowRequest()
                                                                  .withThingName(thingName);

            ByteBuffer payloadBuffer = ByteBuffer.wrap(desiredState.getBytes());
            updateThingShadowRequest.setPayload(payloadBuffer);

            UpdateThingShadowResult result = iotDataClient.updateThingShadow(updateThingShadowRequest);
            byte[] bytes = new byte[result.getPayload().remaining()];
            result.getPayload().get(bytes);
            String resultString = new String(bytes);
            log.info("updateShadow success for thing: " + thingName);

            return resultString;
        } catch (Exception e) {
            log.error("updateShadow failed for thing: " + thingName + ", with error: " + e.toString());
        }
        return null;
    }

}
