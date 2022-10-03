package com.synergyyy.PurchaseOder;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.synergyyy.General.APIClient;
import com.synergyyy.General.Constants;
import com.synergyyy.General.MyBaseActivity;
import com.synergyyy.R;
import com.synergyyy.Search.EditFaultOnSearchActivity;
import com.synergyyy.Search.UploadFileRequest;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Intent.ACTION_GET_CONTENT;
import static com.synergyyy.General.MainActivityLogin.SHARED_PREFS;

public class UploadPurchasePdf extends MyBaseActivity {
    private FloatingActionButton browsePdfBtnop, uploadPdfBtnop;
    Intent uploadFileIntent;
    private PDFView pdfView;
    String frid, token, workspace, role;
    ProgressDialog progressDialog;
    private TextView errorMsgTv;
    private TextView fridTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View viewLayout = layoutInflater.inflate(R.layout.upload_purchase_pdf, null, false);
        drawer.addView(viewLayout, 0);
        browsePdfBtnop = findViewById(R.id.getpdfofpurchase);
        uploadPdfBtnop = findViewById(R.id.uploadpdfofpurchase);
        pdfView = findViewById(R.id.pdfView);
        errorMsgTv=findViewById(R.id.errormsgtv);
        fridTv=findViewById(R.id.frIdpur);
        activityNameTv.setText("Upload Purchase PDF");

        uploadPdfBtnop.setEnabled(false);
        progressDialog = new ProgressDialog(UploadPurchasePdf.this);
        progressDialog.setTitle("Uploading Purchase Order...");
        progressDialog.setCancelable(false);

        Intent intent = getIntent();
        frid = intent.getStringExtra("frId");
        workspace = intent.getStringExtra("workspace");
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        role = sharedPreferences.getString("role", "");
        token = sharedPreferences.getString("token", "");
        fridTv.setText(frid);
        String msgId=intent.getStringExtra("msgId");
        if (msgId!=null){
            EditFaultOnSearchActivity editFaultOnSearchActivity=new EditFaultOnSearchActivity();
            editFaultOnSearchActivity.callReadMessage(msgId);
        }

        if (role.equals(Constants.ROLE_MANAGING_AGENT)) {
            browsePdfBtnop.setVisibility(View.GONE);
            uploadPdfBtnop.setVisibility(View.GONE);
            checkIfPdfAvailable();

        } else {
            browsePdfBtnop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getPdfFromPhone();
                }
            });
            checkIfPdfAvailable();
        }
    }

    private void checkIfPdfAvailable() {
        progressDialog.setTitle("Loading Purchase Order...");
        progressDialog.show();
        Call<ResponseBody> call = APIClient.getUserServices().searchForPurchasePdf(frid, token, workspace, role);
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
                    errorMsgTv.setText("No Purchase Order Uploaded yet");
                    Toast.makeText(UploadPurchasePdf.this, "No Purchase Order Uploaded yet", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();

            }
        });


    }


    private void getPdfFromPhone() {
        uploadFileIntent = new Intent(ACTION_GET_CONTENT);
        uploadFileIntent.setType("application/pdf");

        startActivityForResult(Intent.createChooser(uploadFileIntent, "Select file"), 10);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == RESULT_OK && data != null) {
            assert data != null;
            uploadPdfBtnop.setEnabled(true);
            Uri uri = data.getData();

            try {

                InputStream inputStream = this.getContentResolver().openInputStream(uri);
                byte[] pdfInBytes = new byte[inputStream.available()];

                pdfView.recycle();
                pdfView.fromBytes(pdfInBytes).load();
                errorMsgTv.setVisibility(View.GONE);

                inputStream.read(pdfInBytes);
                String path = android.util.Base64.encodeToString(pdfInBytes, android.util.Base64.DEFAULT);
                uploadPdfBtnop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        uploadFileMethod(path, frid);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    private void uploadFileMethod(String encodedString, String frid) {
        progressDialog.show();

        UploadFileRequest uploadFileRequest = new UploadFileRequest(encodedString, frid);
        Call<ResponseBody> call = APIClient.getUserServices().uploadPurchasePdf(uploadFileRequest, token, workspace, role);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.code() == 200) {
                    progressDialog.dismiss();
                    Toast.makeText(UploadPurchasePdf.this, "Successfully Uploaded Purchase Order", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UploadPurchasePdf.this, SearchForPurchase.class);
                    intent.putExtra("workspace", workspace);
                    startActivity(intent);
                    finish();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(UploadPurchasePdf.this, "Fialed to Upload Purchase Order : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(UploadPurchasePdf.this, SearchForPurchase.class);
        intent.putExtra("workspace", this.workspace);
        startActivity(intent);
        finish();
    }
}


