package GameApp.user;

import GameApp.util.CreatedAt;
import java.io.Serializable;

public class User extends CreatedAt implements Serializable {
    public static final int ACTIVE = 1;
    public static final int INACTIVE = 0;

    private String userId;
    private String userPw;
    private String userName;
    private int userStatus;


    public User( String userId, String userPw, String userName,String createdAt, int userStatus) {
        super();
        this.userId = userId;
        this.userPw = userPw;
        this.userName = userName;
        this.userStatus = userStatus;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return userPw;
    }

    public String getUserName() {
        return userName;
    }


    public int getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(int userStatus) {
        this.userStatus = userStatus;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setPassword(String userPw) {
        this.userPw = userPw;
    }


}