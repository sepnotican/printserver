package ru.sepnotican.printserver;

public class UserInfo {
    private String role;
    private String userName;
    private String password;

    public UserInfo() {
    }

    public UserInfo(String role, String userName, String password) {
        this.role = role;
        this.userName = userName;
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
