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

package com.iot.home.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.*;

import java.io.Serializable;


@DynamoDBTable(tableName = "Customer")
public class Customer implements Serializable {

    private static final long serialVersionUID = -8243145429438016232L;

    @DynamoDBHashKey
    private String emailId;

    @DynamoDBAttribute
    private Long creationDate;

    public Customer() { }

    public Customer(String emailId) {
        this.emailId = emailId;
    }

    public Customer(Long creationDate, String emailId) {
        this.creationDate = creationDate;
        this.emailId = emailId;
    }

    public Long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Long creationDate) {
        this.creationDate = creationDate;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

}
