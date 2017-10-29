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


package com.iot.home.dao;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.iot.home.domain.User;
import com.iot.home.manager.DynamoDBManager;
import org.apache.log4j.Logger;

import java.util.*;


public class DynamoDBUserDao implements UserDao {

    private static final Logger log = Logger.getLogger(DynamoDBUserDao.class);

    private static final DynamoDBMapper mapper = DynamoDBManager.mapper();

    private static volatile DynamoDBUserDao instance;


    private DynamoDBUserDao() { }

    public static DynamoDBUserDao instance() {

        if (instance == null) {
            synchronized(DynamoDBUserDao.class) {
                if (instance == null)
                    instance = new DynamoDBUserDao();
            }
        }
        return instance;
    }

    @Override
    public Optional<User> findUserByEmailId(String emailId) {

        User user = mapper.load(User.class, emailId);

        return Optional.ofNullable(user);
    }

    @Override
    public void saveOrUpdateUser(User user) {

        mapper.save(user);
    }

    @Override
    public void deleteUser(String emailId) {

        Optional<User> oCustomer = findUserByEmailId(emailId);
        if (oCustomer.isPresent()) {
            mapper.delete(oCustomer.get());
        }
        else {
            log.error("Could not delete customer, no such customer");
            throw new IllegalArgumentException("Delete failed for nonexistent customer");
        }
    }
}
