package ffWork.domain.user;

public class IndividualUser extends User {
    private int studentId;

    public IndividualUser(String email, String displayName) {
        super(email, displayName);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        return sb.toString();
    }
}
