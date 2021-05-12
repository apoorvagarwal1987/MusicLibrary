package fileprocessing;

import constants.Constants;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import pojo.Playlist;
import pojo.Song;
import pojo.User;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Class is responsible to parse JSON file to convert into input file data type.
 * The class is also responsible to convert the data type back into the JSON file.
 */
public class ProcessFile {
    private final String FILENAME = "../resources/output/change_file_";
    public ProcessFile() {
    }

    public String generateOutputFile(final Data data) throws IOException{
        // Create a new FileWriter object
        String filename = FILENAME + new Timestamp(System.currentTimeMillis()).toInstant().toEpochMilli() + ".json";

        FileWriter fileWriter = new FileWriter(filename);
        File file = new File(filename);


        JSONObject jsonFileObject = new JSONObject();
        jsonFileObject.put(Constants.USERS, convertUserInformation(data.usersMap));
        jsonFileObject.put(Constants.SONGS, convertSongInformation(data.songsMap));
        jsonFileObject.put(Constants.PLAYLISTS, convertPlayListInformation(data.playListsMap));

        fileWriter.write(jsonFileObject.toJSONString());

        fileWriter.close();

        return file.getAbsolutePath();

    }

    private JSONArray convertPlayListInformation(final Map<Integer, Playlist> playListsMap) {
        JSONArray jsonArray = new JSONArray();
        for(int playListID : playListsMap.keySet()) {
            JSONObject playListJsonObject = new JSONObject();
            playListJsonObject.put(Constants.ID, String.valueOf(playListID));
            playListJsonObject.put(Constants.USER_ID, String.valueOf(playListsMap.get(playListID).user_id));

            List<String> listOfSongs = playListsMap.get(playListID).song_ids.stream()
                                                                  .map(song -> String.valueOf(song))
                                                                  .collect(Collectors.toList());

            playListJsonObject.put(Constants.SONG_IDS, listOfSongs);
            jsonArray.add(playListJsonObject);
        }

        return jsonArray;
    }

    private JSONArray convertSongInformation(final Map<Integer, Song> songsMap) {
        JSONArray jsonArray = new JSONArray();
        for(int songID : songsMap.keySet()) {
            JSONObject songJsonObject = new JSONObject();
            songJsonObject.put(Constants.ID, String.valueOf(songID));
            songJsonObject.put(Constants.ARTIST, songsMap.get(songID).artist);
            songJsonObject.put(Constants.TITLE, songsMap.get(songID).title);

            jsonArray.add(songJsonObject);
        }

        return jsonArray;
    }

    private JSONArray convertUserInformation(final Map<Integer, User> usersMap) {
        JSONArray jsonArray = new JSONArray();
        for(int userID : usersMap.keySet()) {
            JSONObject userJsonObject = new JSONObject();
            userJsonObject.put(Constants.ID, String.valueOf(userID));
            userJsonObject.put(Constants.NAME, usersMap.get(userID).name);

            jsonArray.add(userJsonObject);
        }

        return jsonArray;
    }

    public Data processInputFile(final String fileName) throws IOException, ParseException {
        Data data = new Data();

        JSONParser parser = new JSONParser();
        Object object = parser.parse(new FileReader(fileName));
        JSONObject jsonObject = (JSONObject) object;


        data.usersMap = getUsers(jsonObject);
        data.songsMap = getSongs(jsonObject);
        data.playListsMap = getPlaylists(jsonObject);

        return data;
    }


    private Map<Integer, Playlist> getPlaylists(final JSONObject jsonObject) {
        Map<Integer, Playlist> playlistMap = new HashMap<>();

        JSONArray playlistsJson = (JSONArray) jsonObject.get(Constants.PLAYLISTS);
        Iterator<Object> playlistsJsonIterator = playlistsJson.iterator();
        while(playlistsJsonIterator.hasNext()) {

            JSONObject playlistJsonObject = (JSONObject)playlistsJsonIterator.next();

            final Playlist playlist = new Playlist();
            playlist.id = Integer.parseInt((String) playlistJsonObject.get(Constants.ID));
            playlist.user_id = Integer.parseInt((String) playlistJsonObject.get(Constants.USER_ID));
            playlist.song_ids = new HashSet<>();

            JSONArray playlistsSongsJson = (JSONArray)playlistJsonObject.get(Constants.SONG_IDS);
            Iterator<Object> playlistsSongsJsonIterator = playlistsSongsJson.iterator();
            while (playlistsSongsJsonIterator.hasNext()) {
                playlist.song_ids.add(Integer.parseInt((String)playlistsSongsJsonIterator.next()));
            }

            playlistMap.put(playlist.id, playlist);
        }

        return playlistMap;
    }

    private Map<Integer, Song> getSongs(final JSONObject jsonObject) {
        Map<Integer, Song> songsMap = new HashMap<>();

        JSONArray songsJson = (JSONArray) jsonObject.get(Constants.SONGS);
        Iterator<Object> songJsonIterator = songsJson.iterator();
        while(songJsonIterator.hasNext()) {

            JSONObject songJsonObject = (JSONObject)songJsonIterator.next();

            final Song song = new Song();
            song.id = Integer.parseInt((String) songJsonObject.get(Constants.ID));
            song.artist = (String) songJsonObject.get(Constants.ARTIST);
            song.title = (String) songJsonObject.get(Constants.TITLE);

            songsMap.put(song.id, song);
        }

        return songsMap;
    }


    private Map<Integer, User> getUsers(final JSONObject jsonObject) {
        Map<Integer, User> usersMap = new HashMap<>();

        JSONArray usersJson = (JSONArray) jsonObject.get(Constants.USERS);
        Iterator<Object> userJsonIterator = usersJson.iterator();
        while(userJsonIterator.hasNext()) {

            JSONObject userJsonObject = (JSONObject)userJsonIterator.next();

            final User user = new User();
            user.id = Integer.parseInt((String) userJsonObject.get(Constants.ID));
            user.name = (String) userJsonObject.get(Constants.NAME);

            usersMap.put(user.id, user);
        }

        return usersMap;
    }
}
