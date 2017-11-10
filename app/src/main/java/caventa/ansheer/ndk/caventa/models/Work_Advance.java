package caventa.ansheer.ndk.caventa.models;

public class Work_Advance {
    Double amount;
    String description;

    public Work_Advance(Double amount, String description) {
        this.amount = amount;
        this.description = description;
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

}
