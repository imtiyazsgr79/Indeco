package com.synergyyy.General;

public class NewPasswordRequestBody {
    private String userName;


    private String newpassword;

    private String confirmNewpassowrd;

    public NewPasswordRequestBody(String userName, String newpassword, String confirmNewpassowrd) {
        this.userName = userName;
        this.newpassword = newpassword;
        this.confirmNewpassowrd = confirmNewpassowrd;
    }
}
