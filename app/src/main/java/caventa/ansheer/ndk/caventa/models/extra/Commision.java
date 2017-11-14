package caventa.ansheer.ndk.caventa.models.extra;


import java.io.Serializable;

public class Commision implements Serializable {

    private static final long serialVersionUID = 1L;

    private String work;

    private String commision;


    public Commision(String work, String commision) {

        this.work = work;
        this.commision = commision;

    }


    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getCommision() {
        return commision;
    }

    public void setCommision(String commision) {
        this.commision = commision;
    }

}