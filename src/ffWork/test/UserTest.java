package ffWork.test;

import ffWork.domain.CompanyUser;
import ffWork.domain.IndividualUser;
import ffWork.domain.User;

class UserTest {
    public static void main(String[] args) {
        testUser();
        testIndividualUser();
        testCompanyUser();
        testPolymorphism();
    }

    static void testUser() {
        User user = new User("john@example.com", "John");

        System.out.println("User:");
        System.out.println(user);
        System.out.println("Email: " + user.getEmail());
        System.out.println("Display name: " + user.getDisplayName());
        System.out.println();
    }

    static void testIndividualUser() {
        IndividualUser student = new IndividualUser(
                "student@example.com",
                "Alice",
                12345
        );

        System.out.println("IndividualUser:");
        System.out.println(student);
        System.out.println("Student ID: " + student.getStudentId());
        System.out.println();
    }

    static void testCompanyUser() {
        CompanyUser company = new CompanyUser(
                "contact@company.com",
                "Bob",
                "ACME Corp",
                "123-456-78-90"
        );

        System.out.println("CompanyUser:");
        System.out.println(company);
        System.out.println("Company name: " + company.getCompanyName());
        System.out.println("Tax ID: " + company.getTaxId());
        System.out.println();
    }

    static void testPolymorphism() {
        System.out.println("Polymorphism test:");

        User[] users = {
                new User("u1@mail.com", "User1"),
                new IndividualUser("u2@mail.com", "User2", 111),
                new CompanyUser("u3@mail.com", "User3", "Firm", "999-999")
        };

        for (User u : users) {
            System.out.println(u);
        }

        System.out.println();
    }
}
