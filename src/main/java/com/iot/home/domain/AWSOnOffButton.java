package com.iot.home.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AWSOnOffButton {

    public State state = new State();

    @Data
    @NoArgsConstructor
    public static class State {
        public Document reported = new Document();
        public Document desired = new Document();
    }

    @Data
    @NoArgsConstructor
    public static class Document {
        public String status;
    }

}