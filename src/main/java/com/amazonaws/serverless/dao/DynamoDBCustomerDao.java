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


package com.amazonaws.serverless.dao;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.serverless.domain.Customer;
import com.amazonaws.serverless.manager.DynamoDBManager;
import org.apache.log4j.Logger;

import java.util.*;


public class DynamoDBCustomerDao implements CustomerDao {

    private static final Logger log = Logger.getLogger(DynamoDBCustomerDao.class);

    private static final DynamoDBMapper mapper = DynamoDBManager.mapper();

    private static volatile DynamoDBCustomerDao instance;


    private DynamoDBCustomerDao() { }

    public static DynamoDBCustomerDao instance() {

        if (instance == null) {
            synchronized(DynamoDBCustomerDao.class) {
                if (instance == null)
                    instance = new DynamoDBCustomerDao();
            }
        }
        return instance;
    }

    @Override
    public Optional<Customer> findCustomerByEmailId(String emailId) {

        Customer customer = mapper.load(Customer.class, emailId);

        return Optional.ofNullable(customer);
    }

    @Override
    public void saveOrUpdateCustomer(Customer customer) {

        mapper.save(customer);
    }

    @Override
    public void deleteCustomer(String emailId) {

        Optional<Customer> oCustomer = findCustomerByEmailId(emailId);
        if (oCustomer.isPresent()) {
            mapper.delete(oCustomer.get());
        }
        else {
            log.error("Could not delete customer, no such customer");
            throw new IllegalArgumentException("Delete failed for nonexistent customer");
        }
    }
}
