package com.synergyyy.UploadPdf;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.synergyyy.General.APIClient;
import com.synergyyy.General.Constants;
import com.synergyyy.General.MyBaseActivity;
import com.synergyyy.PurchaseOder.UploadPurchasePdf;
import com.synergyyy.R;
import com.synergyyy.Search.EditFaultOnSearchActivity;
import com.synergyyy.Search.UploadFileRequest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Intent.ACTION_GET_CONTENT;

public class UploadPdf extends MyBaseActivity {
    private static final String TAG = "upload pdf";
    private FloatingActionButton browsePdfBtn, uploadPdfBtn, downloadPdfBtn;
    private Intent uploadFileIntent;
    private PDFView pdfView;
    private String frid, remarks, title,workspace;
    private ProgressDialog progressDialog;
    private MaterialButton accetQuoteBtn, rejectQuoteBtn;
    private TextInputEditText remarksQuotationEditText;
    private TextView errorMsgTv;
    private TextView fridtv;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View viewLayout = layoutInflater.inflate(R.layout.activity_upload_pdf, null, false);
        drawer.addView(viewLayout, 0);

        // setContentView(R.layout.activity_upload_pdf);
        browsePdfBtn = findViewById(R.id.getpdf);
        uploadPdfBtn = findViewById(R.id.uploadpdf);
        pdfView = findViewById(R.id.pdfView);
        accetQuoteBtn = findViewById(R.id.acceptquote);
        downloadPdfBtn = findViewById(R.id.downloadpdfbtn);
        errorMsgTv = findViewById(R.id.errormsgtv);
        rejectQuoteBtn = findViewById(R.id.rejectquote);
        remarksQuotationEditText = findViewById(R.id.remarksquotation);
        fridtv = findViewById(R.id.frIdEditText);

        activityNameTv.setText("Upload Quote PDF");

        Intent intent = getIntent();
        remarks = intent.getStringExtra("remarks");
        uploadPdfBtn.setEnabled(false);
        progressDialog = new ProgressDialog(UploadPdf.this);
        progressDialog.setCancelable(false);
        frid = intent.getStringExtra("frId");
        title = intent.getStringExtra("title");
        workspace=intent.getStringExtra("workspace");
        Log.d(TAG, "onCreate: "+title);
        fridtv.setText(frid);
        String msgId=intent.getStringExtra("msgId");
        if (msgId!=null){
            EditFaultOnSearchActivity editFaultOnSearchActivity=new EditFaultOnSearchActivity();
            editFaultOnSearchActivity.callReadMessage(msgId);
        }

        if (role.equals(Constants.ROLE_MANAGING_AGENT)) {
            browsePdfBtn.setVisibility(View.GONE);
            uploadPdfBtn.setVisibility(View.GONE);
            downloadPdfBtn.setVisibility(View.VISIBLE);
            accetQuoteBtn.setVisibility(View.VISIBLE);
            rejectQuoteBtn.setVisibility(View.VISIBLE);
            remarksQuotationEditText.setVisibility(View.VISIBLE);
            downloadPdfBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    downloadPdfMethod();
                }
            });
            accetQuoteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    acceptQuoteMethod();
                }
            });
            rejectQuoteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rejectQuoteMethod();
                }
            });
            checkIfPdfAvailable();

        } else if (role.equals(Constants.ROLE_TECHNICIAN)) {
            if (title !=null  && title.equals("Quotation  Accepted")) {
                uploadPdfBtn.setVisibility(View.GONE);
                browsePdfBtn.setVisibility(View.GONE);
                CreateAlertDialog();

            } else {
                browsePdfBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getPdfFromPhone();
                    }
                });
            }

            checkIfPdfAvailable();
        }

    }

    private void CreateAlertDialog() {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage("Please upload purchase order for further action")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(UploadPdf.this, UploadPurchasePdf.class);
                            intent.putExtra("frId", frid);
                            intent.putExtra("workspace", workspace);
                            startActivity(intent);
                            finish();


                    }
                })
                .create().show();

    }


    private void rejectQuoteMethod() {
        progressDialog.setTitle("Rejecting Quotation");
        progressDialog.show();
        String remarks = remarksQuotationEditText.getText().toString();
        String status = "Rejected";
        List listremarks = new ArrayList();
        listremarks.add(remarks);
        AcceptRejectQuotationMoldel quotationMoldel = new AcceptRejectQuotationMoldel(frid, status, listremarks);
        Call<Void> call = APIClient.getUserServices().quotationAccept(token, workspace, role, quotationMoldel);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    progressDialog.dismiss();
                    Toast.makeText(UploadPdf.this, "Quotation has been Rejected Successfully",
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UploadPdf.this, SearchForQuote.class);
                    intent.putExtra("workspace", workspace);
                    startActivity(intent);
                    finish();
                } else
                    progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(UploadPdf.this, "Error while Rejecting Quotation " + t.getCause(), Toast.LENGTH_SHORT).show();


            }
        });

    }

    private void acceptQuoteMethod() {
        progressDialog.setTitle("Accepting Quotation");
        progressDialog.show();
        String remarks = remarksQuotationEditText.getText().toString();
        String status = "Accepted";
        List listremarks = new ArrayList();
        listremarks.add(remarks);
        AcceptRejectQuotationMoldel quotationMoldel = new AcceptRejectQuotationMoldel(frid, status, listremarks);
        Call<Void> call = APIClient.getUserServices().quotationAccept(token, workspace, role, quotationMoldel);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    progressDialog.dismiss();
                    Toast.makeText(UploadPdf.this, "Quotation has been Accepted Successfully",
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UploadPdf.this, SearchForQuote.class);
                    intent.putExtra("workspace", workspace);
                    startActivity(intent);
                    finish();
                } else
                    progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(UploadPdf.this, "Error while Accepting Quotation " + t.getCause(), Toast.LENGTH_SHORT).show();

            }
        });


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
                        // fridtv.setText(frid);

                        if (role.equals(Constants.ROLE_TECHNICIAN)) {
                            if (remarks != null) {
                                remarksQuotationEditText.setVisibility(View.VISIBLE);
                                remarksQuotationEditText.setText(remarks);
                            }
                        }
                        progressDialog.dismiss();
                    } catch (IOException e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
                    }
                } else {
                    errorMsgTv.setVisibility(View.VISIBLE);
                    errorMsgTv.setText("No Quotation Uploaded Yet");
                    Toast.makeText(UploadPdf.this, "No Quotation Uploaded Yet", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(UploadPdf.this, "Failed to load Quotation", Toast.LENGTH_SHORT).show();
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
            uploadPdfBtn.setEnabled(true);
            Uri uri = data.getData();

            try {
                InputStream inputStream = this.getContentResolver().openInputStream(uri);
                byte[] pdfInBytes = new byte[inputStream.available()];

                pdfView.recycle();
                pdfView.fromBytes(pdfInBytes).load();
                errorMsgTv.setVisibility(View.GONE);

                inputStream.read(pdfInBytes);
                String path = android.util.Base64.encodeToString(pdfInBytes, android.util.Base64.DEFAULT);
                uploadPdfBtn.setOnClickListener(new View.OnClickListener() {
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
        progressDialog.setTitle("Uploading Quotation...");
        progressDialog.show();

        UploadFileRequest uploadFileRequest = new UploadFileRequest(encodedString, frid);

        Call<ResponseBody> call = APIClient.getUserServices().uploadFilePdf(uploadFileRequest, token, workspace, role);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.code() == 200) {
                    progressDialog.dismiss();
                    Toast.makeText(UploadPdf.this, "Successfully Uploaded Quotation", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UploadPdf.this, SearchForQuote.class);
                    intent.putExtra("workspace", workspace);
                    startActivity(intent);
                    finish();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(UploadPdf.this, "Fialed to Upload Quotation: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

    }


    @SuppressLint("NewApi")
    private void downloadPdfMethod() {
        //  Dowmloadtry(response);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            WriteToExternalStoragepermission();
            Log.d("TAG", "downloadPdfMethod:  give permission");
            //   ActivityCompat.requestPermissions(UploadPdf.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.d("TAG", "Permission is granted");
                progressDialog.setTitle("Downloading Quotation");
                progressDialog.show();
                Call<ResponseBody> call = APIClient.getUserServices().searchForQuoatePdf(frid, token, workspace, role);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.code() == 200) {
                            progressDialog.dismiss();
                            Dowmloadtry(response);

                           /* String fileNmame = new SimpleDateFormat("yyyyMMdd_HHmmss",
                                    Locale.getDefault()).format(System.currentTimeMillis());
                            String mFielPath = Environment.getExternalStorageDirectory() + "/" + fileNmame + ".pdf";
                            Log.d("TAG", "onResponse: file name " + fileNmame);

                            try {
                                FileOutputStream fileOutputStream = new FileOutputStream(mFielPath);
                                fileOutputStream.write(response.body().bytes());
                                fileOutputStream.close();
                                Log.d("TAG", "onResponse: Saved file" + mFielPath);
                                Toast.makeText(UploadPdf.this, "saved file" + mFielPath, Toast.LENGTH_SHORT).show();
                                notification(mFielPath);
                            } catch (FileNotFoundException e) {
                                Log.d("TAG", "onResponse: " + e.getMessage());
                                e.printStackTrace();
                            } catch (IOException e) {
                                Log.d("TAG", "onResponse: " + e.getMessage());
                                e.printStackTrace();
                            }
                            Log.d("TAG", "onResponse: finish");
                        }*/
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(UploadPdf.this, "Failed to Download Quotation", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    }
                });

            }
        }
    }

    private void WriteToExternalStoragepermission() {
        new AlertDialog.Builder(this)
                .setTitle("Permission needed")
                .setCancelable(false)
                .setMessage("This permission is needed for Downloading Pdf")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(UploadPdf.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,}, 1);
                    }
                })
                .create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == 1) {
                //resume tasks needing this permission
                progressDialog.setTitle("Downloading Quotation");
                progressDialog.show();
                Call<ResponseBody> call = APIClient.getUserServices().searchForQuoatePdf(frid, token, workspace, role);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.code() == 200) {
                            Dowmloadtry(response);
                            progressDialog.dismiss();
                           /* String fileNmame = new SimpleDateFormat("dd-MM-yyyy_HHmm",
                                    Locale.getDefault()).format(System.currentTimeMillis());
                            String mFielPath = Environment.getExternalStorageDirectory() + "/" + fileNmame + "quotation" + ".pdf";

                            try {
                                FileOutputStream fileOutputStream = new FileOutputStream(mFielPath);
                                fileOutputStream.write(response.body().bytes());
                                fileOutputStream.close();
                                Log.d("TAG", "onResponse: pdf file " + fileOutputStream);
                                Toast.makeText(UploadPdf.this, "saved file here : " + mFielPath, Toast.LENGTH_LONG).show();
                                notification(mFielPath);
                            } catch (FileNotFoundException e) {
                                Log.d("TAG", "onResponse:  after request gran exception file io" + e.getMessage());
                                e.printStackTrace();
                            } catch (IOException e) {
                                Log.d("TAG", "onResponse:  after request gran io exception" + e.getMessage());
                                e.printStackTrace();
                            }
                            Log.d("TAG", "onResponse:  after request grant finish");
*/
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(UploadPdf.this, "Failed to Download Quotation", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    }
                });

            }
        }
    }

    private void notification(String path) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "my_channel_id";
        CharSequence channelName = "My Channel";
        int importance = 0;
        NotificationChannel notificationChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            importance = NotificationManager.IMPORTANCE_MAX;
            notificationChannel = new NotificationChannel(channelId, channelName, importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("Quotation Downloaded")
                .setSmallIcon(R.drawable.i_cmms)
                .setContentText(path)
                .setSortKey("downloadquoatation")
                .setChannelId(channelId)
                .build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(0, notification);
        //     openFolder();
    }

    private void Dowmloadtry(Response<ResponseBody> response) {

        //changed here
        File path = new File(Environment.getExternalStorageDirectory(), "i-cmms");
        if (!path.exists()) {
            path.mkdirs();
            Log.d("TAG", "Dowmloadtry: no file" + path.mkdirs());
        }
        String fileName = "quotation_" + frid + ".pdf";
        final File localFile = new File(path, fileName);
        Log.d("TAG", "Dowmloadtry:  file " + localFile);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(localFile);
            fileOutputStream.write(response.body().bytes());
            fileOutputStream.close();
            notification(localFile.getPath());
            Toast.makeText(UploadPdf.this, "saved file : " + localFile, Toast.LENGTH_LONG).show();
            notification(localFile.getPath());
        } catch (FileNotFoundException e) {
            Log.d("TAG", "onResponse:  after request gran exception file io downloads" + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Log.d("TAG", "onResponse:  after request gran io exception downloads" + e.getMessage());
            e.printStackTrace();
        }
        Log.d("TAG", "onResponse:  after request grant finish downloads");
    }

    public void openFolder() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/i-cmms/");
        intent.setDataAndType(uri, ".pdf");
        startActivity(Intent.createChooser(intent, "Open folder"));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(UploadPdf.this, SearchForQuote.class);
        intent.putExtra("workspace", workspace);
        startActivity(intent);
        finish();
    }
}


