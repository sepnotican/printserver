package ru.sepnotican.printserver;

public interface IUserInfoDAO {
    UserInfo getActiveUser(String userName);
}
