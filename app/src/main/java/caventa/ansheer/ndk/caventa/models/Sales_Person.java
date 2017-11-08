package caventa.ansheer.ndk.caventa.models;

/**
 * Created by Lincoln on 18/05/16.
 */
public class Sales_Person {
    private String name,icon_URL;
//    private int numOfSongs;
//    private int thumbnail;

    public Sales_Person() {
    }

    public Sales_Person(String name, String icon_URL) {
        this.name = name;
//        this.numOfSongs = numOfSongs;
//        this.thumbnail = thumbnail;
        this.icon_URL=icon_URL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
//
//    public int getNumOfSongs() {
//        return numOfSongs;
//    }
//
//    public void setNumOfSongs(int numOfSongs) {
//        this.numOfSongs = numOfSongs;
//    }
//
//    public int getThumbnail() {
//        return thumbnail;
//    }
//
//    public void setThumbnail(int thumbnail) {
//        this.thumbnail = thumbnail;
//    }


    public String getIcon_URL() {
        return icon_URL;
    }

    public void setIcon_URL(String icon_URL) {
        this.icon_URL = icon_URL;
    }
}
