package ffWork.domain.user;

public class IndividualUser extends User {
    private int studentId;

    public IndividualUser(String email, String displayName, int studentId) {
        super(email, displayName);
        this.studentId = studentId;
    }

    public int getStudentId() {
        return studentId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString())
                .append(", student ID: ")
                .append(studentId);
        return sb.toString();
    }
}
