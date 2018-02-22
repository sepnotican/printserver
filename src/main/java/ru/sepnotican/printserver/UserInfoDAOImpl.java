package ru.sepnotican.printserver;

import org.springframework.stereotype.Component;

@Component
public class UserInfoDAOImpl implements IUserInfoDAO {
    @Override
    public UserInfo getActiveUser(String userName) {
        return new UserInfo("ADMIN", userName, "1");
    }
}
