package caventa.ansheer.ndk.caventa.models.sortable_table_view.other_expense_ledger_table_view;

import java.util.Date;

public class Other_Expense_Ledger_Entry {

    Date insertion_date;
    String particulars;
    double amount;

    public String getParticulars() {
        return particulars;
    }

    public void setParticulars(String particulars) {
        this.particulars = particulars;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getInsertion_date() {
        return insertion_date;
    }

    public void setInsertion_date(Date insertion_date) {
        this.insertion_date = insertion_date;
    }

    public Other_Expense_Ledger_Entry(Date insertion_date, String particulars, double amount) {
        this.insertion_date = insertion_date;
        this.particulars = particulars;
        this.amount = amount;
    }
}
