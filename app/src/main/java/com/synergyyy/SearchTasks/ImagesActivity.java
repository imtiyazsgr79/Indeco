package com.synergyyy.SearchTasks;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.kyanogen.signatureview.SignatureView;
import com.synergyyy.General.APIClient;
import com.synergyyy.General.Constants;

import com.synergyyy.FaultReport.UploadPictureRequest;
import com.synergyyy.General.LogoutClass;
import com.synergyyy.General.MyBaseActivity;
import com.synergyyy.R;
import com.synergyyy.Search.AuthorizerClass;
import com.synergyyy.Search.BeforeImageResponse;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImagesActivity extends MyBaseActivity {

    private List<String> mUrls;
    private ViewPager mVpMain;
    private int indexToDelete = -1;
    private final List<String> base64List = new ArrayList<>();
    private VpAdapter adp;
    private TextView authName, authNo;
    private String frId, value;
    private ProgressDialog progressDialog;
    private DotsIndicator dotsIndicator;
    private FloatingActionButton deleteBtn;
    private String finalValueType;
    private TextInputLayout nricNameLay, nricContactLayout;
    private List<String> arralistimages = new ArrayList<String>();
    private List<AuthorizerClass> authListDetail = new ArrayList<AuthorizerClass>();


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View viewLayout = layoutInflater.inflate(R.layout.activity_images, null, false);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        drawer.addView(viewLayout, 0);
        Intent intent = getIntent();
        frId = intent.getStringExtra("frId");
        value = intent.getStringExtra("value");
        workspace = intent.getStringExtra("workspace");
        String previousActivity = intent.getStringExtra("fromFrDetail");
        String status = intent.getStringExtra("status");
        activityNameTv.setText(value + " Image");
        value = value.toLowerCase();
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);

        ImageView galleryButton = findViewById(R.id.galleryButton);
        ImageView cameraButton = findViewById(R.id.cameraButton);
        deleteBtn = findViewById(R.id.fabDelete);
        dotsIndicator = findViewById(R.id.dot);
        authName = findViewById(R.id.authorizename);
        authNo = findViewById(R.id.authorizerno);
//        galleryButton.setVisibility(View.GONE);
        cameraButton.setVisibility(View.GONE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        def();
        retrieveImagesMethod();


        if (role.equals(Constants.ROLE_MANAGING_AGENT) || previousActivity.equals("FrDetailPaGE") ||
                status.equals("Completed") || status.equals("Closed") || status.equals("Pause Requested")) {
            cameraButton.setVisibility(View.GONE);
            galleryButton.setVisibility(View.GONE);
            deleteBtn.setVisibility(View.GONE);
        }
//changed on 25 nov by faizan on assignment of altaf sir

        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                //chnaged from baase64list to arralist 24 nov faizan
                if (arralistimages.size() < 1) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 13);
                } else
                    Toast.makeText(ImagesActivity.this, "Maximum Images added already!", Toast.LENGTH_LONG).show();
            }
        });

        cameraButton.setOnClickListener(new View.OnClickListener() {
            //changed on 25 nov by faizan on assignment of altaf sir
            @Override
            public void onClick(View v) {
                if (base64List.size() < 1) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 1);
                    /*Intent intent1 = new Intent(ImagesActivity.this, Camera2Api.class);
                    startActivityForResult(intent1, 121);*/

                } else
                    Toast.makeText(ImagesActivity.this, "Maximum Images added already!", Toast.LENGTH_LONG).show();
            }
        });


    }

    private void retrieveImagesMethod() {
        adp = new VpAdapter();
        base64List.clear();
        mVpMain.setAdapter(adp);

        Call<List<BeforeImageResponse>> callImage = APIClient.getUserServices().getBeforeImage(
                value,
                frId,
                workspace,
                token);
        callImage.enqueue(new Callback<List<BeforeImageResponse>>() {
            @Override
            public void onResponse(Call<List<BeforeImageResponse>> call, Response<List<BeforeImageResponse>> response) {
                if (response.code() == 200) {
                    List<BeforeImageResponse> beforeImageResponseList = response.body();
                    arralistimages.clear();
                    authListDetail.clear();
                    for (int i = 0; i < beforeImageResponseList.size(); i++) {
                        String img = beforeImageResponseList.get(i).getImage();
                        arralistimages.add(img);
                        AuthorizerClass authorizerClass = new AuthorizerClass(beforeImageResponseList.get(i).getName(), beforeImageResponseList.get(i).getContactNo());
                        authListDetail.add(authorizerClass);
                    }
                    if (arralistimages.size() < 1) {
                        Bitmap noImageBitmap = getBitmapFromVectorDrawable(ImagesActivity.this, R.drawable.noimage);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        noImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] b = baos.toByteArray();
                        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
                        base64List.add(imageEncoded);
                        adp.notifyDataSetChanged();
                        deleteBtn.setEnabled(false);
                        Toast.makeText(ImagesActivity.this, "No Images Available!", Toast.LENGTH_LONG).show();
                    } else {
                        deleteBtn.setEnabled(true);
                        progressDialog.show();
                        for (int i = 0; i < arralistimages.size(); i++) {
                            Call<ResponseBody> callBase = APIClient.getUserServices().getImageBase64(arralistimages.get(i).toString(), workspace, token);
                            callBase.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                    if (response.code() == 200) {
                                        Log.d("TAG", "onResponse: " + response.body().byteStream());
                                        Bitmap bmp = BitmapFactory.decodeStream(response.body().byteStream());
                                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                        byte[] b = baos.toByteArray();
                                        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
                                        base64List.add(imageEncoded);
                                        adp.notifyDataSetChanged();
                                        dotsIndicator.setViewPager(mVpMain);
                                    } else if (response.code() == 401) {
                                        Toast.makeText(ImagesActivity.this, Constants.ERROR_CODE_401_MESSAGE, Toast.LENGTH_SHORT).show();
                                    } else if (response.code() == 500) {
                                        Toast.makeText(ImagesActivity.this, Constants.ERROR_CODE_500_MESSAGE, Toast.LENGTH_SHORT).show();
                                    } else if (response.code() == 404) {
                                        Toast.makeText(ImagesActivity.this, Constants.ERROR_CODE_404_MESSAGE, Toast.LENGTH_SHORT).show();
                                    } else
                                        Toast.makeText(ImagesActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Toast.makeText(ImagesActivity.this, "Error: " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();
                                }
                            });
                        }
                    }
                } else if (response.code() == 401) {
                    Toast.makeText(ImagesActivity.this, Constants.ERROR_CODE_401_MESSAGE, Toast.LENGTH_SHORT).show();
                    LogoutClass logoutClass = new LogoutClass();
                    logoutClass.logout(ImagesActivity.this);
                } else if (response.code() == 500) {
                    Toast.makeText(ImagesActivity.this, Constants.ERROR_CODE_500_MESSAGE, Toast.LENGTH_SHORT).show();
                } else if (response.code() == 404) {
                    Toast.makeText(ImagesActivity.this, Constants.ERROR_CODE_404_MESSAGE, Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(ImagesActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<BeforeImageResponse>> call, Throwable t) {
                Toast.makeText(ImagesActivity.this, "Error: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                Log.d("TAG", "onFailure: " + t.getCause());
                Log.d("TAG", "onFailure: " + t.getMessage());
                progressDialog.dismiss();
            }
        });
    }

    private void def() {
        mVpMain = findViewById(R.id.vpMain);
        mUrls = base64List;
        mVpMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                if (!authListDetail.isEmpty()) {
                    authName.setText(authListDetail.get(i).getName());
                    authNo.setText(authListDetail.get(i).getContactNo());
                } else {
                    authNo.setText("");
                    authName.setText("");
                }
            }

            @Override
            public void onPageSelected(int i) {
            }

            @Override
            public void onPageScrollStateChanged(int i) {


                if (indexToDelete != -1 && i == ViewPager.SCROLL_STATE_IDLE) {
                    mUrls.remove(indexToDelete);
                    adp.notifyDataSetChanged();

                    if (indexToDelete == 0) {
                        mVpMain.setCurrentItem(indexToDelete, false);
                    }
                    indexToDelete = -1;
                }
            }
        });
    }


    class VpAdapter extends PagerAdapter {
        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, final int position) {
             View v = getLayoutInflater().inflate(R.layout.thumb_item, container, false);
            ImageView imageSlider = v.findViewById(R.id.imgSlider);
            byte[] decodedByte = Base64.decode(mUrls.get(position), 0);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
            Glide.with(container.getContext()).asBitmap().load(bitmap).into(imageSlider);
            container.addView(v);

            String valueType = "FR-AI-";

            if (value.equals("before")) {
                valueType = "FR-BI-";
            }


            finalValueType = valueType;
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(ImagesActivity.this);
                    alertDialog.setTitle("Delete Image")
                            .setMessage("Do you want to delete this image ?")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            deleteImageMethod();

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
            return v;
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            if (mUrls.indexOf(object) == -1)
                return POSITION_NONE;
            else
                return super.getItemPosition(object);
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return mUrls.size();
        }
    }

    private void deleteImageMethod() {
        ProgressDialog progressD = new ProgressDialog(ImagesActivity.this);
        progressD.setCancelable(false);
        progressD.setMessage("Deleting image..");
        progressD.setIndeterminate(true);
        progressD.show();
        DeleteImageRequest deleteImageRequest = new DeleteImageRequest(arralistimages.get(mVpMain.getCurrentItem()), frId, finalValueType);
        Call<Void> deleteCall = APIClient.getUserServices().postDeleteImage(
                deleteImageRequest,
                workspace,
                token,
                role);
        deleteCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    arralistimages.remove(mVpMain.getCurrentItem());
                    Toast.makeText(ImagesActivity.this, "Deleted!", Toast.LENGTH_LONG).show();
                    retrieveImagesMethod();
                } else if (response.code() == 401) {
                    Toast.makeText(ImagesActivity.this, Constants.ERROR_CODE_401_MESSAGE, Toast.LENGTH_SHORT).show();
                    LogoutClass logoutClass = new LogoutClass();
                    logoutClass.logout(ImagesActivity.this);
                } else if (response.code() == 500) {
                    Toast.makeText(ImagesActivity.this, Constants.ERROR_CODE_500_MESSAGE, Toast.LENGTH_SHORT).show();
                } else if (response.code() == 404) {
                    Toast.makeText(ImagesActivity.this, Constants.ERROR_CODE_404_MESSAGE, Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(ImagesActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                progressD.dismiss();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ImagesActivity.this, "Error: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                progressD.dismiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
/*
            if (requestCode == 121 && resultCode == RESULT_OK) {
                //image from custom camera
                byte[] im = data.getByteArrayExtra("image");
                Bitmap bitmap = BitmapFactory.decodeByteArray(im, 0, im.length);
                try {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
                    StringBuilder base = new UploadTaskImageActivity().bmpToString(bitmap);
                    uploadAlertMethod(base);
                } catch (Exception e) {
                    Log.d("TAG", "onActivityResult: " + e.getLocalizedMessage());
                }

            }
*/

            if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                try {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
                    StringBuilder base = new UploadImagesTaskActivity().bmpToString(photo);
                    uploadAlertMethod(base);

                } catch (Exception e) {
                    Log.d("TAG", "onActivityResult: " + e.getLocalizedMessage());
                }
            } else if (requestCode == 13 && resultCode == Activity.RESULT_OK) {
                Uri selectedImage = data.getData();

                Bitmap photoBmp = null;
                try {
                    photoBmp = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), selectedImage);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    photoBmp.compress(Bitmap.CompressFormat.JPEG, 30, byteArrayOutputStream);

                    StringBuilder uploadBase = new UploadImagesTaskActivity().bmpToString(photoBmp);
                    uploadAlertMethod(uploadBase);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void uploadAlertMethod(StringBuilder photo) {
        //23 jan
        final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(ImagesActivity.this);
        final View dialogView = getLayoutInflater().inflate(R.layout.custom_dialog_authorize_image, null);
        final TextInputEditText nameEt = dialogView.findViewById(R.id.nameauth);
        final TextInputEditText contactEt = dialogView.findViewById(R.id.contactauth);
        final TextInputEditText division = dialogView.findViewById(R.id.division_alert_box_edittext);
        final TextInputEditText rank = dialogView.findViewById(R.id.rank_alert_b0x_edittext);
        final TextInputLayout nameLay = dialogView.findViewById(R.id.nricname_layout);
        final TextInputLayout contactLay = dialogView.findViewById(R.id.nriccontact_layout);
        final TextInputLayout divisionLay = dialogView.findViewById(R.id.division_alert_box);
        final TextInputLayout rankLay = dialogView.findViewById(R.id.rank_alert_b0x);
        final SignatureView signatureView = dialogView.findViewById(R.id.signatureEdit);
        final TextView signTv = dialogView.findViewById(R.id.signtv);

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

                Button b = alert.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = null;
                        String contact = null;
                        String rankString = null;
                        String divisionString = null;
                        String sign = null;


                        if (!TextUtils.isEmpty(nameEt.getText()) && !TextUtils.isEmpty(contactEt.getText())
                                && !TextUtils.isEmpty(rank.getText()) && !TextUtils.isEmpty(division.getText())
                                && !signatureView.isBitmapEmpty()) {
                            name = nameEt.getText().toString();
                            contact = contactEt.getText().toString();
                            rankString = rank.getText().toString();
                            divisionString = division.getText().toString();
                            if (contact.length() == 8) {
                                compressImageMethod(signatureView, photo, name,
                                        rankString, divisionString, contact);
                                alert.dismiss();
                            } else {
                                contactLay.setError("Enter 8-digit contact number");
                            }
                        } else if (TextUtils.isEmpty(nameEt.getText()) && TextUtils.isEmpty(contactEt.getText())
                                && TextUtils.isEmpty(rank.getText()) && TextUtils.isEmpty(division.getText())
                                && signatureView.isBitmapEmpty()) {
                            uploadImageMethod(photo, name, contact, rankString, divisionString, sign);
                            alert.dismiss();
                        } else if (TextUtils.isEmpty(nameEt.getText())) {
                            //   nameEt.setError("Required");
                            nameLay.setError("Required");
                        } else if (TextUtils.isEmpty(contactEt.getText())) {
                            contactEt.setError("Required 8-digit  Contact No");
                        } else if (TextUtils.isEmpty(division.getText())) {
                            division.setError("Required");
                        } else if (TextUtils.isEmpty(rank.getText())) {
                            rank.setError("Required");
                        } else if (signatureView.isBitmapEmpty()) {
                            signTv.setText("Please sign here ");
                            signTv.setTextColor(Color.parseColor("#FA0C0C"));
                        }
                    }
                });
            }
        });
        alert.show();
    }

    private void uploadImageMethod(StringBuilder encodedString, String name, String contact, String rank, String division, String sign) {
        ProgressDialog mProgress = new ProgressDialog(this);
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);
        mProgress.show();
        UploadPictureRequest uploadPictureRequest = new UploadPictureRequest(frId, encodedString, name, contact, rank, division, sign);
        value = value.toLowerCase();
        Call<Void> uploadImageCall = APIClient.getUserServices().uploadCaptureImage(value, token, workspace, uploadPictureRequest);

        uploadImageCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    Toast.makeText(ImagesActivity.this, "Image saved successfully", Toast.LENGTH_LONG).show();
                    retrieveImagesMethod();
                    new AlertDialog.Builder(ImagesActivity.this)
                            .setTitle("Image saved successfully")
                            .setMessage("Want to upload more pictures?")
                            .setIcon(R.drawable.ic_error)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    })
                            .show();
                } else if (response.code() == 401) {
                    Toast.makeText(ImagesActivity.this, Constants.ERROR_CODE_401_MESSAGE, Toast.LENGTH_SHORT).show();
                    LogoutClass logoutClass = new LogoutClass();
                    logoutClass.logout(ImagesActivity.this);
                } else if (response.code() == 500) {
                    Toast.makeText(ImagesActivity.this, Constants.ERROR_CODE_500_MESSAGE, Toast.LENGTH_SHORT).show();
                } else if (response.code() == 404) {
                    Toast.makeText(ImagesActivity.this, Constants.ERROR_CODE_404_MESSAGE, Toast.LENGTH_SHORT).show();
                } else if (response.code() == 406) {
                    Toast.makeText(ImagesActivity.this, "Cannot add more than 5 pictures", Toast.LENGTH_SHORT).show();
                    new AlertDialog.Builder(ImagesActivity.this)
                            .setMessage("Cannot add more than 5 pictures")
                            .setTitle("Alert")
                            .setIcon(R.drawable.ic_error)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                }
                            })
                            .show();
                } else
                    Toast.makeText(ImagesActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                mProgress.dismiss();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ImagesActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                mProgress.dismiss();
            }
        });

    }

    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    private void compressImageMethod(SignatureView signatureView, StringBuilder photo, String name, String rankString, String divisionString, String contact) {
        String encodedStringBuilder = null;

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            signatureView.getSignatureBitmap().compress(Bitmap.CompressFormat.PNG, 50, baos); // bm is the bitmap object
            byte[] imageBytes = baos.toByteArray();

            encodedStringBuilder = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            uploadImageMethod(photo, name, contact, rankString, divisionString, encodedStringBuilder);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}