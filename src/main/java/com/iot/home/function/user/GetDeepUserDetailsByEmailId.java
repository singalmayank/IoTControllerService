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


import com.iot.home.dao.DynamoDBUserDao;
import com.iot.home.domain.User;
import com.iot.home.util.AmazonAPIConstants;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;
import java.util.Optional;


public class GetDeepUserDetailsByEmailId implements RequestHandler<Map<String, Object>, User> {

    private static final Logger log = Logger.getLogger(GetDeepUserDetailsByEmailId.class);

    private static final DynamoDBUserDao userDao = DynamoDBUserDao.instance();

    public User execute(String emailId) throws UnsupportedEncodingException {

        if (null == emailId || emailId.isEmpty() || emailId.equals(AmazonAPIConstants.UNDEFINED)) {
            log.error("GetDeepUserDetailsByEmailId received null or empty emailId");
            throw new IllegalArgumentException("emailId name cannot be null or empty");
        }

        String name = URLDecoder.decode(emailId, "UTF-8");
        log.info("GetDeepUserDetailsByEmailId invoked for user with name = " + name);
        Optional<User> oUser = userDao.findUserByEmailId(name);

        if (oUser.isPresent()) {
            log.info("Found user for emailId = " + emailId);
            return oUser.get();
        }

        log.info("No user found for emailId = " + emailId);

        return null;
    }

    @Override
    public User handleRequest(Map<String, Object> s, Context context) {

        try {
            return execute((String) s.get("emailId"));
        } catch (Exception e) {
            log.info("Failed to find user: " + e.toString());
        }

        return null;
    }
}
