package caventa.ansheer.ndk.caventa.models;

/**
 * Created by Srf on 21-11-2017.
 */

public class Work_Overview {
    String work_name;
    String work_address;

    public String getWork_date() {
        return work_date;
    }

    public void setWork_date(String work_date) {
        this.work_date = work_date;
    }

    String work_date;
    double total_advance,total_expense;

    public String getWork_name() {
        return work_name;
    }

    public void setWork_name(String work_name) {
        this.work_name = work_name;
    }

    public String getWork_address() {
        return work_address;
    }

    public void setWork_address(String work_address) {
        this.work_address = work_address;
    }

    public double getTotal_advance() {
        return total_advance;
    }

    public void setTotal_advance(double total_advance) {
        this.total_advance = total_advance;
    }

    public double getTotal_expense() {
        return total_expense;
    }

    public void setTotal_expense(double total_expense) {
        this.total_expense = total_expense;
    }

    public Work_Overview(String work_name, String work_address, String work_date, double total_advance, double total_expense) {
        this.work_name = work_name;
        this.work_address = work_address;
        this.work_date = work_date;
        this.total_advance = total_advance;
        this.total_expense = total_expense;
    }
}
