package caventa.ansheer.ndk.caventa.models;

/**
 * Created by prism on 01-11-2017.
 */

public class Work_Expense {
    int id,work_id;
    Double amount;
    String description,insertion_date_time;

    public Work_Expense(int id, int work_id, Double amount, String description, String insertion_date_time) {
        this.id = id;
        this.work_id = work_id;
        this.amount = amount;
        this.description = description;
        this.insertion_date_time = insertion_date_time;
    }

    public int getId() {
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
}
