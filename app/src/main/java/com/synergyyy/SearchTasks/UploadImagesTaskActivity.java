package com.synergyyy.SearchTasks;

import androidx.annotation.Nullable;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.synergyyy.General.APIClient;
import com.synergyyy.General.Constants;
import com.synergyyy.EquipmentSearch.PmTaskActivity;
import com.synergyyy.EquipmentSearch.UploadImageRequest;
import com.synergyyy.General.MyBaseActivity;
import com.synergyyy.R;

import java.io.ByteArrayOutputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadImagesTaskActivity extends MyBaseActivity {

    private ImageView beforeIV;
    private FloatingActionButton uploadBeforeFB, deleteBeforeFB;
    private String sourceValue, source;
    private TextView nameTV, contactTv;
    private String taskId;
    private String taskNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View viewLayout = layoutInflater.inflate(R.layout.activity_upload_images_task, null, false);
        drawer.addView(viewLayout, 0);


        beforeIV = findViewById(R.id.imageview_before);
        uploadBeforeFB = findViewById(R.id.uploadImageTask);
        deleteBeforeFB = findViewById(R.id.deleteImageTask);
        deleteBeforeFB.setVisibility(View.GONE);
        uploadBeforeFB.setVisibility(View.GONE);
        nameTV = findViewById(R.id.authorizenameTask);
        contactTv = findViewById(R.id.authorizernoTask);

        if (role.equals(Constants.ROLE_TECHNICIAN)) {
            uploadBeforeFB.setVisibility(View.VISIBLE);
        }

        Intent intent = getIntent();
        taskId = intent.getStringExtra("taskId");
        String workspace = intent.getStringExtra("workspace");
        String imageName = intent.getStringExtra("imageName");
        sourceValue = intent.getStringExtra("sourceValue");
        int imageId = intent.getIntExtra("imageId", 0);
        String authName = intent.getStringExtra("authName");
        String authContact = intent.getStringExtra("authContact");
        taskNumber = intent.getStringExtra("taskNumber");
        source = intent.getStringExtra("source");

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        toolbar.setTitle(sourceValue + " Image");
        setSupportActionBar(toolbar);

        if (imageName != null) {
            retrieveImage(imageName, role, token, workspace, beforeIV, authContact, authName);
        }

        if (source.equals("search")) {
            uploadBeforeFB.setVisibility(View.GONE);
            deleteBeforeFB.setVisibility(View.GONE);
        }

        uploadBeforeFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 1);
            }
        });

        deleteOnClickMethod(imageName, taskId, imageId);

    }

    private void deleteMethod(String imageName, String taskId, int imageId) {

        String type = "TASK-BI-";
        if (sourceValue.equals("After")) {
            type = "TASK-AI-";
        }

        ProgressDialog mProgress = new ProgressDialog(this);
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);
        mProgress.show();

        DeleteTaskImageRequest deleteTaskImageRequest = new DeleteTaskImageRequest(Long.parseLong(taskId), imageName, type, imageId);

        Call<Void> callDeleteImage = APIClient.getUserServices().getDeleteTaskImage(deleteTaskImageRequest, workspace, token, role);
        callDeleteImage.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    beforeIV.setImageResource(R.drawable.noimage);
                    Toast.makeText(UploadImagesTaskActivity.this, "Image Deleted", Toast.LENGTH_SHORT).show();
                    nameTV.setText(null);
                    contactTv.setText(null);
                    deleteBeforeFB.setVisibility(View.GONE);
                    if (role.equals(Constants.ROLE_TECHNICIAN)) {
                        uploadBeforeFB.setVisibility(View.VISIBLE);
                    }
                } else
                    Toast.makeText(UploadImagesTaskActivity.this, "Error: " + response.code(), Toast.LENGTH_LONG).show();
                mProgress.dismiss();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(UploadImagesTaskActivity.this, "Error: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                mProgress.dismiss();
            }
        });
    }


    private void retrieveImage(String imageName, String role, String token, String workspace, ImageView imageView, String authContact, String authName) {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait....");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();

        Call<ResponseBody> callTaskImage = APIClient.getUserServices().getTaskImage(imageName, role, token, workspace);
        callTaskImage.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.code() == 200) {
                    Bitmap bmp = BitmapFactory.decodeStream(response.body().byteStream());
                    imageView.setBackground(null);
                    imageView.setImageBitmap(bmp);
                    nameTV.setText(authName);
                    contactTv.setText(authContact);
                    if (role.equals(Constants.ROLE_TECHNICIAN) && source.equals("scan")) {
                        deleteBeforeFB.setVisibility(View.VISIBLE);
                    }
                    uploadBeforeFB.setVisibility(View.GONE);
                } else if (response.code() == 401) {
                    Toast.makeText(UploadImagesTaskActivity.this, Constants.ERROR_CODE_401_MESSAGE, Toast.LENGTH_SHORT).show();
                } else if (response.code() == 500) {
                    Toast.makeText(UploadImagesTaskActivity.this, Constants.ERROR_CODE_500_MESSAGE, Toast.LENGTH_SHORT).show();
                } else if (response.code() == 404) {
                    Toast.makeText(UploadImagesTaskActivity.this, Constants.ERROR_CODE_404_MESSAGE, Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(UploadImagesTaskActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(UploadImagesTaskActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();

                String encodedStringBuilder = Base64.encodeToString(byteArray, Base64.DEFAULT);

                uploadAlertMethod(encodedStringBuilder, photo, beforeIV, sourceValue.toLowerCase());

            } catch (Exception e) {
                Log.d("TAG", "onActivityResult: " + e.getLocalizedMessage());
            }
        }

    }

    private void uploadAlertMethod(String encodedString, Bitmap bitmap, ImageView beforeImageView, String imageViewValue) {
        final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        final View dialogView = getLayoutInflater().inflate(R.layout.custom_dialog_authorize_tasks, null);
        final TextInputEditText nameEt = dialogView.findViewById(R.id.nameauth);
        final TextInputEditText contactEt = dialogView.findViewById(R.id.contactauth);
        final TextInputEditText divisionEt = dialogView.findViewById(R.id.division_alert_box_edittext);
        final TextInputEditText rankEt = dialogView.findViewById(R.id.rank_alert_b0x_edittext);
        final TextInputLayout nameLay = dialogView.findViewById(R.id.nricname_layout);
        final TextInputLayout contactLay = dialogView.findViewById(R.id.nriccontact_layout);

        builder.setTitle("Authorize Image")
                .setView(dialogView)
                .setCancelable(false)
                .setIcon(R.drawable.ic_error)
                .setPositiveButton("Upload", null)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        androidx.appcompat.app.AlertDialog alert = builder.create();
        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

                Button b = alert.getButton(android.app.AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = null;
                        String contact = null;
                        String rankString = null;
                        String divisionString = null;


                        if (!TextUtils.isEmpty(nameEt.getText()) && !TextUtils.isEmpty(contactEt.getText())
                                && !TextUtils.isEmpty(rankEt.getText()) && !TextUtils.isEmpty(divisionEt.getText())) {
                            name = nameEt.getText().toString();
                            contact = contactEt.getText().toString();
                            rankString = rankEt.getText().toString();
                            divisionString = divisionEt.getText().toString();
                            if (contact.length() == 8) {
                                uploadPicture(encodedString, bitmap, beforeImageView, imageViewValue, divisionString, name, contact, rankString);
                                alert.dismiss();
                            } else {
                                contactLay.setError("Enter 8-digit contact number");
                            }
                        } else if (TextUtils.isEmpty(nameEt.getText()) && TextUtils.isEmpty(contactEt.getText())
                                && TextUtils.isEmpty(rankEt.getText()) && TextUtils.isEmpty(divisionEt.getText())) {
                            uploadPicture(encodedString, bitmap, beforeImageView, imageViewValue, divisionString, name, contact, rankString);
                            alert.dismiss();
                        } else if (TextUtils.isEmpty(nameEt.getText())) {
                            nameLay.setError("Required");
                        } else if (TextUtils.isEmpty(contactEt.getText())) {
                            contactEt.setError("Required 8-digit  Contact No");
                        } else if (TextUtils.isEmpty(divisionEt.getText())) {
                            divisionEt.setError("Required");
                        } else if (TextUtils.isEmpty(rankEt.getText())) {
                            rankEt.setError("Required");
                        }
                    }
                });
            }
        });
        alert.show();
    }

    private void uploadPicture(String encodedStringBuilder, Bitmap photo, ImageView imageView,
                               String imageViewValue, String divisionString, String name, String contact, String rank) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setTitle("Uploading");
        progressDialog.show();
        UploadImageRequest uploadImageRequest = new UploadImageRequest(taskId, encodedStringBuilder, contact, rank, divisionString, name, "");

        Call<UploadTaskImageResponse> callUploadPicture = APIClient.getUserServices().taskImageUpload(role, token, imageViewValue, uploadImageRequest);
        callUploadPicture.enqueue(new Callback<UploadTaskImageResponse>() {
            @Override
            public void onResponse(Call<UploadTaskImageResponse> call, Response<UploadTaskImageResponse> response) {
                if (response.code() == 200) {
                    imageView.setBackground(null);
                    imageView.setImageBitmap(photo);
                    uploadBeforeFB.setVisibility(View.GONE);

                    if (source.equals("scan") && role.equals(Constants.ROLE_TECHNICIAN)) {
                        deleteBeforeFB.setVisibility(View.VISIBLE);
                    }
                    nameTV.setText(name);
                    contactTv.setText(contact);

                    UploadTaskImageResponse beforeImage = response.body();
                    if (beforeImage != null) {
                        long taskId = beforeImage.getTaskId();
                        String imageName = beforeImage.getImage();
                        int id = beforeImage.getId();
                        deleteOnClickMethod(imageName, String.valueOf(taskId), id);
                    }

                } else if (response.code() == 401) {
                    Toast.makeText(UploadImagesTaskActivity.this, Constants.ERROR_CODE_401_MESSAGE, Toast.LENGTH_SHORT).show();
                } else if (response.code() == 500) {
                    Toast.makeText(UploadImagesTaskActivity.this, Constants.ERROR_CODE_500_MESSAGE, Toast.LENGTH_SHORT).show();
                } else if (response.code() == 404) {
                    Toast.makeText(UploadImagesTaskActivity.this, Constants.ERROR_CODE_404_MESSAGE, Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(UploadImagesTaskActivity.this, "Error: " + response.code(), Toast.LENGTH_LONG).show();

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<UploadTaskImageResponse> call, Throwable t) {
                Toast.makeText(UploadImagesTaskActivity.this, "Error: " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
    }

    private void deleteOnClickMethod(String imageName, String taskId, int id) {
        deleteBeforeFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder alertDialog = new AlertDialog.Builder(UploadImagesTaskActivity.this);
                alertDialog.setTitle("Delete Image")
                        .setMessage("Do you want to delete this image ?")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        deleteMethod(imageName, (taskId), id);
                                    }
                                }
                        )
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, PmTaskActivity.class);
        intent.putExtra("taskId", Integer.parseInt(taskId));
        intent.putExtra("workspace", workspace);
        intent.putExtra("taskNumber", taskNumber);
        intent.putExtra("source", source);
        startActivity(intent);
        finish();
    }

    public StringBuilder bmpToString(Bitmap photoBmp) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        photoBmp.compress(Bitmap.CompressFormat.JPEG, 30, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        StringBuilder encodedStringBuilder = new StringBuilder()
                .append(Base64.encodeToString(byteArray, Base64.DEFAULT));
        return encodedStringBuilder;
    }
}