package com.synergyyy.Search;

import java.util.List;

public class PauseRequestBody {

    String eotType, eotTime;
    private List<String> remarks = null;
    String frId, observation, actionTaken;
    private fmm fmm;

    public PauseRequestBody(String eotType, List<String> remarks, String frId, String observation, String actionTaken, String eotTime,fmm fmm) {
        this.eotType = eotType;
        this.eotTime = eotTime;
        this.remarks = remarks;
        this.frId = frId;
        this.observation = observation;
        this.actionTaken = actionTaken;
        this.fmm=fmm;
    }
}
