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


package com.amazonaws.serverless.function;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Optional;

import com.amazonaws.serverless.dao.DynamoDBCustomerDao;
import com.amazonaws.serverless.util.AmazonAPIConstants;
import org.apache.log4j.Logger;

import com.amazonaws.serverless.domain.Customer;


public class CustomerFunctions {

    private static final Logger log = Logger.getLogger(CustomerFunctions.class);

    private static final DynamoDBCustomerDao eventDao = DynamoDBCustomerDao.instance();

    public Customer getDeepCustomerDetailsByEmailId(String emailId) throws UnsupportedEncodingException {

        if (null == emailId || emailId.isEmpty() || emailId.equals(AmazonAPIConstants.UNDEFINED)) {
            log.error("GetDeepCustomerDetailsByEmailId received null or empty emailId");
            throw new IllegalArgumentException("emailId name cannot be null or empty");
        }

        String name = URLDecoder.decode(emailId, "UTF-8");
        log.info("GetDeepCustomerDetailsByEmailId invoked for city with name = " + name);
        Optional<Customer> oCustomer = eventDao.findCustomerByEmailId(name);

        if (oCustomer.isPresent()) {
            log.info("Found customer for emailId = " + emailId);
            return oCustomer.get();
        }

        log.info("No customer found for emailId = " + emailId);

        return null;
    }

    public void saveOrUpdateEvent(Customer customer) {

        if (null == customer) {
            log.error("SaveEvent received null input");
            throw new IllegalArgumentException("Cannot save null object");
        }

        log.info("Saving or updating event for team = " + customer.getEmailId());
        eventDao.saveOrUpdateCustomer(customer);
        log.info("Successfully saved/updated event");
    }

    public void deleteEvent(Customer customer) {

        if (null == customer) {
            log.error("DeleteEvent received null input");
            throw new IllegalArgumentException("Cannot delete null object");
        }

        log.info("Deleting event for team = " + customer.getEmailId());
        eventDao.deleteCustomer(customer.getEmailId());
        log.info("Successfully deleted event");
    }

}
