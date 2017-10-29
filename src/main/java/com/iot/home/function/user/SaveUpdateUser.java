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


package com.iot.home.function.user;


import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iot.home.dao.DynamoDBUserDao;
import com.iot.home.domain.User;
import org.apache.log4j.Logger;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;


public class SaveUpdateUser implements RequestStreamHandler {

    private static final Logger log = Logger.getLogger(SaveUpdateUser.class);

    private static final DynamoDBUserDao userDao = DynamoDBUserDao.instance();

    private ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);


    public void execute(User user) {

        if (null == user) {
            log.error("SaveEvent received null input");
            throw new IllegalArgumentException("Cannot save null object");
        }

        log.info("Saving or updating event for team = " + user.getEmailId());
        userDao.saveOrUpdateUser(user);
        log.info("Successfully saved/updated event");
    }
    

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) {

        try {
            User user = mapper.readValue(inputStream, User.class);
            execute(user);
        } catch (Exception e) {
            log.info("Failed to save/update user: " + e.toString());
        }

        try {

            int letter =1;
            outputStream.write(Character.toUpperCase(letter));
        } catch (Exception e) {
            log.info("Failed to write to outputStream: " + e.toString());
        }
    }

}
