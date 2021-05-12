package pojo;

import java.util.Set;

public class Playlist {
    public int id;
    public int user_id;
    public Set<Integer> song_ids;

    public Playlist() {
    }

    public String toString() {
        return "" + id + "::" + user_id + "::" + song_ids;
    }
}
