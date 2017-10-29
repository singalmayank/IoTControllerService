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
import com.iot.home.dao.DynamoDBUserDao;
import org.apache.log4j.Logger;

import java.util.Map;


public class DeleteUserByEmailId implements RequestHandler<Map<String, Object>, Boolean> {

    private static final Logger log = Logger.getLogger(DeleteUserByEmailId.class);

    private static final DynamoDBUserDao userDao = DynamoDBUserDao.instance();

    private void execute(String emailId) {

        if (null == emailId) {
            log.error("DeleteEvent received null input");
            throw new IllegalArgumentException("Cannot delete null object");
        }

        log.info("Deleting user with email = " + emailId);
        userDao.deleteUser(emailId);
        log.info("Successfully deleted event");
    }

    @Override
    public Boolean handleRequest(Map<String, Object> s, Context context) {

        try {
            execute((String) s.get("emailId"));
        } catch (Exception e) {
            log.info("Failed to delete user: " + e.toString());
            return false;
        }
        return true;
    }
}
