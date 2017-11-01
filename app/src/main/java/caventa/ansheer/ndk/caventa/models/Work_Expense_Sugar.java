package caventa.ansheer.ndk.caventa.models;

import com.orm.SugarRecord;

/**
 * Created by prism on 01-11-2017.
 */

public class Work_Expense_Sugar extends SugarRecord {
    int id,work_id;
    Double amount;
    String description,insertion_date_time;


    public int get_Id() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWork_id() {
        return work_id;
    }

    public void setWork_id(int work_id) {
        this.work_id = work_id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInsertion_date_time() {
        return insertion_date_time;
    }

    public void setInsertion_date_time(String insertion_date_time) {
        this.insertion_date_time = insertion_date_time;
    }

    public Work_Expense_Sugar() {
    }
}
