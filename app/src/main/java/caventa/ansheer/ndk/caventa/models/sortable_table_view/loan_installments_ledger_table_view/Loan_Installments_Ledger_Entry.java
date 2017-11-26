package caventa.ansheer.ndk.caventa.models.sortable_table_view.loan_installments_ledger_table_view;

import java.util.Date;

public class Loan_Installments_Ledger_Entry {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Loan_Installments_Ledger_Entry(int id, Date insertion_date, String receipt_number, double payed_amount, double principle_amount, double interest_amount, String remarks) {
        this.id = id;
        this.insertion_date = insertion_date;
        this.receipt_number = receipt_number;
        this.payed_amount = payed_amount;
        this.principle_amount = principle_amount;
        this.interest_amount = interest_amount;
        this.remarks = remarks;
    }

    int id;
    Date insertion_date;
    String receipt_number;
    double payed_amount;
    double principle_amount;
    double interest_amount;
    String remarks;

    public double getInterest_amount() {
        return interest_amount;
    }

    public void setInterest_amount(double interest_amount) {
        this.interest_amount = interest_amount;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public double getPrinciple_amount() {
        return principle_amount;
    }

    public void setPrinciple_amount(double principle_amount) {
        this.principle_amount = principle_amount;
    }

    public String getReceipt_number() {
        return receipt_number;
    }

    public void setReceipt_number(String receipt_number) {
        this.receipt_number = receipt_number;
    }

    public double getPayed_amount() {
        return payed_amount;
    }

    public void setPayed_amount(double payed_amount) {
        this.payed_amount = payed_amount;
    }

    public Date getInsertion_date() {
        return insertion_date;
    }

    public void setInsertion_date(Date insertion_date) {
        this.insertion_date = insertion_date;
    }


}
