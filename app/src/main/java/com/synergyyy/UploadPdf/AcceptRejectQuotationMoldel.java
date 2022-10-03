package com.synergyyy.UploadPdf;

import java.util.List;

public class AcceptRejectQuotationMoldel {


  private   String frId,quotationStatus;
  private List remarks;

    public AcceptRejectQuotationMoldel(String frId, String quotationStatus, List remarks) {
        this.frId = frId;
        this.quotationStatus = quotationStatus;
        this.remarks = remarks;
    }

}
