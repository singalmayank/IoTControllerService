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


import com.iot.home.dao.DynamoDBCustomerDao;
import com.iot.home.domain.Customer;
import com.iot.home.util.AmazonAPIConstants;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;
import java.util.Optional;


public class GetDeepCustomerDetailsByEmailId implements RequestHandler<Map<String, Object>, Customer> {

    private static final Logger log = Logger.getLogger(GetDeepCustomerDetailsByEmailId.class);

    private static final DynamoDBCustomerDao eventDao = DynamoDBCustomerDao.instance();

    private Customer execute(String emailId) throws UnsupportedEncodingException {

        if (null == emailId || emailId.isEmpty() || emailId.equals(AmazonAPIConstants.UNDEFINED)) {
            log.error("GetDeepCustomerDetailsByEmailId received null or empty emailId");
            throw new IllegalArgumentException("emailId name cannot be null or empty");
        }

        String name = URLDecoder.decode(emailId, "UTF-8");
        log.info("GetDeepCustomerDetailsByEmailId invoked for customer with name = " + name);
        Optional<Customer> oCustomer = eventDao.findCustomerByEmailId(name);

        if (oCustomer.isPresent()) {
            log.info("Found customer for emailId = " + emailId);
            return oCustomer.get();
        }

        log.info("No customer found for emailId = " + emailId);

        return null;
    }

    @Override
    public Customer handleRequest(Map<String, Object> s, Context context) {

        try {
            return execute((String) s.get("emailId"));
        } catch (Exception e) {
            log.info("Failed to find customer" + e.toString());
        }

        return null;
    }
}
