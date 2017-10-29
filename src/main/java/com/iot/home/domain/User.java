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
import java.util.List;


@DynamoDBTable(tableName = "Customer")
public class User implements Serializable {

    private static final long serialVersionUID = -8243145429438016232L;

    @DynamoDBHashKey
    private String emailId;

    @DynamoDBAttribute
    private String displayName;

    @DynamoDBAttribute
    private List<Asset> assets;


    public User() { }

    public User(String emailId) {
        this.emailId = emailId;
    }

    public User(String emailId, String displayName, List<Asset> assets) {
        this.emailId = emailId;
        this.displayName = displayName;
        this.assets = assets;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<Asset> getAssets() {
        return assets;
    }

    public void setAssets(List<Asset> assets) {
        this.assets = assets;
    }

}
