package com.synergyyy.Search;

public class fmm {
    String username;
    int id;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    @Override
    public String toString() {
        return username;
    }

    public fmm(String username, int id) {
        this.username = username;
        this.id = id;
    }
}
