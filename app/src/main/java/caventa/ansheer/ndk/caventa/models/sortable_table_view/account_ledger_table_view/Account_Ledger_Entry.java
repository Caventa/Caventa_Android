package caventa.ansheer.ndk.caventa.models.sortable_table_view.account_ledger_table_view;

import java.util.Date;

public class Account_Ledger_Entry {

    Date insertion_date;
    String particulars;
    double debit_amount;
    double credit_amount;
    double balance;

    public double getCredit_amount() {
        return credit_amount;
    }

    public void setCredit_amount(double credit_amount) {
        this.credit_amount = credit_amount;
    }

    public Account_Ledger_Entry(Date insertion_date, String particulars, double debit_amount, double credit_amount, double balance) {
        this.insertion_date = insertion_date;
        this.particulars = particulars;
        this.debit_amount = debit_amount;
        this.credit_amount = credit_amount;
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }



    public String getParticulars() {
        return particulars;
    }

    public void setParticulars(String particulars) {
        this.particulars = particulars;
    }

    public double getDebit_amount() {
        return debit_amount;
    }

    public void setDebit_amount(double debit_amount) {
        this.debit_amount = debit_amount;
    }

    public Date getInsertion_date() {
        return insertion_date;
    }

    public void setInsertion_date(Date insertion_date) {
        this.insertion_date = insertion_date;
    }


}
