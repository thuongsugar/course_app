package com.example.course_mobile.model.user;

import com.google.gson.annotations.SerializedName;


public class UserLogin {
    private static final String CLIENT_ID = "9kPpmn0ugzucrv9kclJ8kzc1G4o1s2O9et90hVmw";
    private static final String CLIENT_SECRET = "ChFbej6mm9OBKkjRwVWkU9JZ9ko7HyN7hb0UEoSJBj4zhDJVLOqrT5OM0BPrElOeoQ1Q9xHdmNDZlm42kGRaLyddPYlanRVtcGijaFMnMOEncg3FFxgbKSZTAkGcQOfT";
    private static final String GRANT_TYPE = "password";
    @SerializedName("username")
    private String userName;

    @SerializedName("password")
    private String passWord;

    @SerializedName("client_id")
    private String clientId;

    @SerializedName("client_secret")
    private String clientSecret;

    @SerializedName("grant_type")
    private String grantType;

    public UserLogin(String userName, String passWord) {
        this.userName = userName;
        this.passWord = passWord;
        this.clientId = CLIENT_ID;
        this.clientSecret = CLIENT_SECRET;
        this.grantType = GRANT_TYPE;
    }

    public static String getClientId() {
        return CLIENT_ID;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public static String getClientSecret() {
        return CLIENT_SECRET;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public static String getGrantType() {
        return GRANT_TYPE;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
}
