package praktikum.model;

public class UserResponse {
    private boolean success;
    private String accessToken;
    private String refreshToken;

    public boolean isSuccess() {
        return success;
    }
    public String getAccessToken() {
        return accessToken;
    }
    public String getRefreshToken() {
        return refreshToken;
    }
}
