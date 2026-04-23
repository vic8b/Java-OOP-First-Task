package ffWork.domain.user;

public class CompanyUser extends User {
    private String companyName;
    private String taxId;

    public CompanyUser(String email, String displayName, String companyName, String taxId) {
        super(email, displayName);
        this.companyName = companyName;
        this.taxId = taxId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getTaxId() {
        return taxId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString())
                .append(", company name: ")
                .append(companyName)
                .append(", tax ID: ")
                .append(taxId);
        return sb.toString();
    }
}
