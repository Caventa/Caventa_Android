package caventa.ansheer.ndk.caventa.models;

public class Sales_Person {
    private String name;
    private String id;


    public Sales_Person(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public Sales_Person() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
