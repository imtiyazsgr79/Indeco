package com.synergyyy.General;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.synergyyy.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewPasswordActivity extends AppCompatActivity {

    private static final String TAG = "njnn";
    private TextInputEditText newPassword1, newpassword2, userName;
    private MaterialButton confrimBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);

        newPassword1 = findViewById(R.id.newpassword_1);
        newpassword2 = findViewById(R.id.newpassword_2);
        confrimBtn = findViewById(R.id.confirmBtn);
        userName = findViewById(R.id.userName);

        Uri uri = getIntent().getData();
        String path = null;
        if (uri != null) {
            path = uri.toString();
            String[] str = path.split("=");
            String str1 = str[1];
            checkLinkIsvalid(str1);

        }

        newPassword1 = findViewById(R.id.newpassword_1);
        newpassword2 = findViewById(R.id.newpassword_2);
        confrimBtn = findViewById(R.id.confirmBtn);
        userName = findViewById(R.id.userName);
        confrimBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateInputs();
            }
        });
    }

    private void checkLinkIsvalid(String path) {
        Call<Void> call = APIClient.getUserServices().checkLinkAvailable(path);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if (response.code() == 403) {
                    confrimBtn.setEnabled(false);
                    Toast.makeText(NewPasswordActivity.this, "Sorry!! Link Expired", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(NewPasswordActivity.this, MainActivityLogin.class);
                    startActivity(intent);
                    finish();

                }
            }


            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(NewPasswordActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void validateInputs() {
        String p1, p2, userNameStr = null;
        p1 = newPassword1.getText().toString();
        p2 = newpassword2.getText().toString();
        userNameStr = userName.getText().toString();

        if (TextUtils.isEmpty(userName.getText())) {
            userName.setError("Username required");
            Toast.makeText(this, "Please add userName", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(newPassword1.getText())) {
            newPassword1.setError("Password required");
            Toast.makeText(this, "Please add new password", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(newpassword2.getText())) {
            newpassword2.setError("Confirm Password required");
            Toast.makeText(this, "Please confirm password", Toast.LENGTH_SHORT).show();
        } else if (!(p1.equals(p2))) {
            Toast.makeText(this, "Password does not match", Toast.LENGTH_SHORT).show();
            newPassword1.setError("Password does not match");
            newpassword2.setError("Password does not match");
        } else {
            ProgressDialog progressDialog = new ProgressDialog(NewPasswordActivity.this);
            progressDialog.setTitle("Changing Password");
            progressDialog.setMessage("Please Wait ...");
            progressDialog.show();
            NewPasswordRequestBody newPasswordRequestBody = new NewPasswordRequestBody(userNameStr, p1, p2);
            Call<Void> call = APIClient.getUserServices().newPassword(newPasswordRequestBody);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    progressDialog.dismiss();
                    if (response.code() == 200) {
                        Toast.makeText(NewPasswordActivity.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(NewPasswordActivity.this, MainActivityLogin.class);
                        startActivity(intent);
                        finish();
                    } else if (response.code() == 400) {
                        Toast.makeText(NewPasswordActivity.this, "User not valid", Toast.LENGTH_SHORT).show();

                    } else
                        Toast.makeText(NewPasswordActivity.this, "Failed due to :" + response.errorBody(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(NewPasswordActivity.this, "failed to change password due to :" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}