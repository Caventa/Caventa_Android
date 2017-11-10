package caventa.ansheer.ndk.caventa.models;

import java.util.Date;

public class Work {
    String work_name, work_address, advances_json, expenses_json,id;
    Date work_date;
    int sales_person_id;

    public Work(String work_name, String work_address, String advances_json, String expenses_json, String id, Date work_date, int sales_person_id) {
        this.work_name = work_name;
        this.work_address = work_address;
        this.advances_json = advances_json;
        this.expenses_json = expenses_json;
        this.id = id;
        this.work_date = work_date;
        this.sales_person_id = sales_person_id;
    }

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

    public String getAdvances_json() {
        return advances_json;
    }

    public void setAdvances_json(String advances_json) {
        this.advances_json = advances_json;
    }

    public String getExpenses_json() {
        return expenses_json;
    }

    public void setExpenses_json(String expenses_json) {
        this.expenses_json = expenses_json;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getWork_date() {
        return work_date;
    }

    public void setWork_date(Date work_date) {
        this.work_date = work_date;
    }

    public int getSales_person_id() {
        return sales_person_id;
    }

    public void setSales_person_id(int sales_person_id) {
        this.sales_person_id = sales_person_id;
    }
}
