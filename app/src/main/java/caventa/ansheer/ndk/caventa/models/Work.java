package caventa.ansheer.ndk.caventa.models;

import java.util.Date;

public class Work {
    String work_name, work_address, id;
    Date work_date;
    int sales_person_id;

    public Work(String work_name, String work_address, String id, Date work_date, int sales_person_id) {
        this.work_name = work_name;
        this.work_address = work_address;
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
