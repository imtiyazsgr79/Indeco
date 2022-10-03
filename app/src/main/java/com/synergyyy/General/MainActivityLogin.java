package com.synergyyy.General;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.messaging.FirebaseMessaging;
import com.synergyyy.R;
import com.synergyyy.Workspace.WorkspaceActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivityLogin extends AppCompatActivity {

    private ProgressDialog mProgress;
    private final int STORAGE_PERMISSION_CODE = 1;
    private TextInputEditText editTextName;
    private TextInputLayout passwordTextName, usernameTextName;
    private EditText passwordEdit;
    private Button buttonLogin;
    private String nameString, passwordString, deviceToken;
    public static final String SHARED_PREFS = "sharedPrefs";
    private SharedPreferences.Editor editor;
    private final CheckInternet checkInternet = new CheckInternet();
    private TextView forgetPassword;

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(checkInternet, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(checkInternet);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        FirebaseApp.initializeApp(this);
        FirebaseInstallations.getInstance().getToken(true);

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (task.isSuccessful()) {
                    deviceToken = task.getResult();
                }
            }
        });


        buttonLogin = findViewById(R.id.btn_login);
        editTextName = findViewById(R.id.editTextUsername);
        passwordEdit = findViewById(R.id.editTextPassword);
        passwordTextName = findViewById(R.id.login_password);
        usernameTextName = findViewById(R.id.login_username);
        ConstraintLayout constraintLayout = findViewById(R.id.loginConstraint);
        forgetPassword = findViewById(R.id.forgetid_txt);
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callForgetMethod();
            }
        });


        editTextName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                usernameTextName.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        passwordEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordTextName.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        //

        //


        if (!(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_NOTIFICATION_POLICY) == PackageManager.PERMISSION_GRANTED)) {
            requestStoragePermission();
            buttonLogin.setEnabled(false);
        } else {
            buttonLogin.setEnabled(true);
        }
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nameString = editTextName.getText().toString();
                passwordString = passwordEdit.getText().toString();
                UserRequest userRequest = new UserRequest(nameString, passwordString, deviceToken);
                if (!editTextName.getText().toString().isEmpty() || !passwordEdit.getText().toString().isEmpty()) {
                    usernameTextName.setErrorEnabled(false);
                    passwordTextName.setErrorEnabled(false);
                    loginUser(userRequest);
                } else {
                    Toast.makeText(MainActivityLogin.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    usernameTextName.setError("Required");
                    passwordTextName.setError("Required");
                }
            }
        });
    }


    public void requestStoragePermission() {

        new AlertDialog.Builder(this)
                .setTitle("Permission needed")
                .setCancelable(false)
                .setMessage("This Application needs location and other permissions")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(MainActivityLogin.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.CAMERA,
                                        Manifest.permission.ACCESS_NOTIFICATION_POLICY}, STORAGE_PERMISSION_CODE);
                    }
                })
                .create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED
                    && grantResults[3] == PackageManager.PERMISSION_GRANTED) {
                buttonLogin.setEnabled(true);
            } else {
                new AlertDialog.Builder(this)
                        .setTitle("Permission Denied")
                        .setMessage("Please enable the permissions in settings")
                        .setCancelable(false)
                        .setPositiveButton("ok", null)
                        .create().show();
            }
        }
    }

    public static String getToken(Context context) {
        return context.getSharedPreferences("_", MODE_PRIVATE).getString("fcm_token", "empty");
    }

    public void loginUser(UserRequest userRequest) {

        mProgress = new ProgressDialog(MainActivityLogin.this);
        mProgress.setTitle("Authenticating...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);
        mProgress.show();

        Call<UserResponse> call = APIClient.getUserServices().saveUser(userRequest);

        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.code() == 200) {
                    UserResponse userResponse = response.body();
                    String token = userResponse.getToken();
                    String role = userResponse.getRole();
                    String username = userResponse.getUsername();
                    editor.clear();
                    editor.putString("token", token);
                    editor.putString("role", role);
                    editor.putString("username", username);
                    editor.putString("devicetoken", deviceToken);
                    editor.apply();

                    if (role.equals(Constants.ROLE_TECHNICIAN) || role.equals(Constants.ROLE_MANAGING_AGENT) || role.equals(Constants.Coordinator)) {

                        Intent intent = new Intent(getApplicationContext(), WorkspaceActivity.class);
                        intent.putExtra("devicetoken", deviceToken);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(MainActivityLogin.this, "You can't access this application", Toast.LENGTH_SHORT).show();
                    }

                } else if (response.code() == 202) {
                    Toast.makeText(MainActivityLogin.this, "Please check the username and password", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 401) {
                    Toast.makeText(MainActivityLogin.this, Constants.ERROR_CODE_401_MESSAGE_LOGIN, Toast.LENGTH_SHORT).show();
                } else if (response.code() == 500) {
                    Toast.makeText(MainActivityLogin.this, Constants.ERROR_CODE_500_MESSAGE, Toast.LENGTH_SHORT).show();
                } else if (response.code() == 404) {
                    Toast.makeText(MainActivityLogin.this, Constants.ERROR_CODE_404_MESSAGE, Toast.LENGTH_SHORT).show();
                } else if (response.code() == 400) {
                    Toast.makeText(MainActivityLogin.this, Constants.ERROR_CODE_400_MESSAGE, Toast.LENGTH_SHORT).show();
                }
                mProgress.dismiss();
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                mProgress.dismiss();
                Toast.makeText(MainActivityLogin.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void callForgetMethod() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivityLogin.this);
        final View dialogView = getLayoutInflater().inflate(R.layout.resetp_password, null);
        final TextInputEditText emailEt = dialogView.findViewById(R.id.emailEt);
        builder.setTitle("Reset your Account")
                .setView(dialogView)
                .setCancelable(false)
                .setPositiveButton("Send", null)

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button b = alert.getButton(android.app.AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (TextUtils.isEmpty(emailEt.getText())) {
                            emailEt.setError("Please enter Registered E-mail");
                            Toast.makeText(MainActivityLogin.this, "Please enter Registered E-mail", Toast.LENGTH_LONG).show();
                        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailEt.getText()).matches()) {
                            emailEt.setError("Please enter valid E-mail");
                            Toast.makeText(MainActivityLogin.this, "Please enter valid E-mail", Toast.LENGTH_SHORT).show();
                        } else if (!TextUtils.isEmpty(emailEt.getText())) {
                            requestForget(emailEt.getText().toString());
                            alert.dismiss();
                        }
                    }
                });
            }
        });
        alert.show();
    }

    private void requestForget(String toString) {
        ProgressDialog progressDialog = new ProgressDialog(MainActivityLogin.this);
        progressDialog.setTitle("Sending request");
        progressDialog.setMessage("Please wait ..");
        progressDialog.show();

        Call<Void> call = APIClient.getUserServices().resetPassword(toString);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                progressDialog.dismiss();
                if (response.code() == 200) {

                    new AlertDialog.Builder(MainActivityLogin.this)
                            .setTitle("Reset Link Sent")
                            .setMessage("Please check your registered E-mail")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.dismiss();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_email)
                            .show();
                } else if (response.code() == 404) {
                    Toast.makeText(MainActivityLogin.this, "User with this E-mail not found", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(MainActivityLogin.this, "Failed to reset :" + response.errorBody(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(MainActivityLogin.this, "Failed to reset due to :" + t.getCause(), Toast.LENGTH_SHORT).show();

            }
        });

    }
}
