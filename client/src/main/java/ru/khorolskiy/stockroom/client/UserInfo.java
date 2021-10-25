package ru.khorolskiy.stockroom.client;


public class UserInfo {
    private static int userID;

    public static int getUserID() {
        return userID;
    }

    public static void setUserID(int userID) {
        UserInfo.userID = userID;
    }
}
