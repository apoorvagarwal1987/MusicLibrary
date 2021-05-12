package fileprocessing;

import pojo.Playlist;
import pojo.Song;
import pojo.User;

import java.util.HashMap;

import java.util.Map;

public class Data {
    public Map<Integer, Song> songsMap;
    public Map<Integer, Playlist> playListsMap;
    public Map<Integer, User> usersMap;

    public Data() {
        songsMap = new HashMap<>();
        playListsMap = new HashMap<>();
        usersMap = new HashMap<>();
    }
}
