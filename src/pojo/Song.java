package pojo;

public class Song {
    public int id;
    public String artist;
    public String title;

    public Song() {
    }

    public String toString() {
        return "" + id + "::" + artist + "::" + title;
    }
}
