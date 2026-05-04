package ffWork.domain.user;

public class User {
    private String email;
    private String displayName;

    public User(String email, String displayName) {
        this.email = email;
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("user: ")
                .append(displayName)
                .append(", email: ")
                .append(email);
        return sb.toString();
    }
}
