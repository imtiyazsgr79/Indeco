package com.synergyyy.Search;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.synergyyy.General.APIClient;
import com.synergyyy.General.MyBaseActivity;
import com.synergyyy.R;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadFile extends MyBaseActivity {
    private FloatingActionButton browsePdfBtn, uploadPdfBtn, downloadPdfBtn;
    private Intent uploadFileIntent;
    private PDFView pdfView;
    private String frid;
    private ProgressDialog progressDialog;
    private MaterialButton accetQuoteBtn, rejectQuoteBtn;
    private TextInputEditText remarksQuotationEditText;
    private TextView errorMsgTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View viewLayout = layoutInflater.inflate(R.layout.uploadfileonlyview, null, false);
        drawer.addView(viewLayout, 0);

        pdfView = findViewById(R.id.pdfView);

        errorMsgTv = findViewById(R.id.errormsgtv);
        activityNameTv.setText(" View Quotation");
        setSupportActionBar(toolbar);

        progressDialog = new ProgressDialog(UploadFile.this);
        progressDialog.setCancelable(false);
        Intent intent = getIntent();
        frid = intent.getStringExtra("frId");
        checkIfPdfAvailable();
    }


    private void checkIfPdfAvailable() {
        progressDialog.setTitle("Loading Quotation...");
        progressDialog.show();
        Call<ResponseBody> call = APIClient.getUserServices().searchForQuoatePdf(frid, token, workspace, role);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    try {
                        pdfView.fromBytes(response.body().bytes()).load();
                        progressDialog.dismiss();
                    } catch (IOException e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
                    }
                } else {
                    errorMsgTv.setVisibility(View.VISIBLE);
                    errorMsgTv.setText("No Quotation Uploaded Yet");
                    Toast.makeText(UploadFile.this, "No Quotation Uploaded Yet", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(UploadFile.this, "Failed to load Quotation", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        });
    }
}


