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


package com.iot.home.function.customer;


import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.iot.home.dao.DynamoDBCustomerDao;
import com.iot.home.domain.Customer;
import org.apache.log4j.Logger;

import java.util.Map;


public class SaveUpdateCustomer implements RequestHandler<Map<String, Object>, Boolean> {

    private static final Logger log = Logger.getLogger(SaveUpdateCustomer.class);

    private static final DynamoDBCustomerDao eventDao = DynamoDBCustomerDao.instance();

    public void execute(Customer customer) {

        if (null == customer) {
            log.error("SaveEvent received null input");
            throw new IllegalArgumentException("Cannot save null object");
        }

        log.info("Saving or updating event for team = " + customer.getEmailId());
        eventDao.saveOrUpdateCustomer(customer);
        log.info("Successfully saved/updated event");
    }
    

    @Override
    public Boolean handleRequest(Map<String, Object> s, Context context) {

        try {
            execute((Customer) s.get("customer"));
        } catch (Exception e) {
            log.info("Failed to save/update customer" + e.toString());
            return false;
        }

        return true;
    }
}
