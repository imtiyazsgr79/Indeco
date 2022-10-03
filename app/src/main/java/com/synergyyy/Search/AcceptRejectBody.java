package com.synergyyy.Search;

import java.util.List;

public class AcceptRejectBody {


    private List<String> remarks = null;
    String frId, observation, actionTaken;
    String fmmDocForAuthorize;

    public AcceptRejectBody(List<String> remarks, String frId, String observation, String actionTaken, String fmmDocForAuthorize) {
        this.remarks = remarks;
        this.frId = frId;
        this.observation = observation;
        this.actionTaken = actionTaken;
        this.fmmDocForAuthorize = fmmDocForAuthorize;
    }
}
