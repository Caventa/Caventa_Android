package caventa.ansheer.ndk.caventa.models.sortable_table_view.loan_ledger_table_view;

import java.util.Date;

public class Loan_Ledger_Entry {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Loan_Ledger_Entry(int id, Date insertion_date, String particulars, double loan_amount, double installment_amount) {
        this.id = id;
        this.insertion_date = insertion_date;
        this.particulars = particulars;
        this.loan_amount = loan_amount;
        this.installment_amount = installment_amount;
    }

    int id;
    Date insertion_date;
    String particulars;
    double loan_amount;
    double installment_amount;

    public double getInstallment_amount() {
        return installment_amount;
    }

    public void setInstallment_amount(double installment_amount) {
        this.installment_amount = installment_amount;
    }

    public String getParticulars() {
        return particulars;
    }

    public void setParticulars(String particulars) {
        this.particulars = particulars;
    }

    public double getLoan_amount() {
        return loan_amount;
    }

    public void setLoan_amount(double loan_amount) {
        this.loan_amount = loan_amount;
    }

    public Date getInsertion_date() {
        return insertion_date;
    }

    public void setInsertion_date(Date insertion_date) {
        this.insertion_date = insertion_date;
    }


}
