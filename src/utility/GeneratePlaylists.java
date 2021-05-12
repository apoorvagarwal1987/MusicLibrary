package utility;

import constants.Constants;
import exception.MissingKeyFieldInFile;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import pojo.Playlist;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public class GeneratePlaylists {
    public static Map<Integer, Playlist> getPlaylists(final JSONObject jsonObject) {
        Map<Integer, Playlist> playlistMap = new HashMap<>();

        JSONArray playlistsJson = (JSONArray) jsonObject.get(Constants.PLAYLISTS);
        Iterator<Object> playlistsJsonIterator = playlistsJson.iterator();
        while(playlistsJsonIterator.hasNext()) {

            JSONObject playlistJsonObject = (JSONObject)playlistsJsonIterator.next();
            if(playlistJsonObject.get(Constants.ID) == null) {
                throw new MissingKeyFieldInFile("Batch cannot be processed as missing key ID field");
            }

            final Playlist playlist = new Playlist();
            playlist.id = Integer.parseInt((String) playlistJsonObject.get(Constants.ID));
            playlist.user_id = Integer.parseInt((String) playlistJsonObject.get(Constants.USER_ID));

            if(playlistJsonObject.get(Constants.SONG_IDS) != null) {
                playlist.song_ids = new HashSet<>();
                JSONArray playlistsSongsJson = (JSONArray)playlistJsonObject.get(Constants.SONG_IDS);
                Iterator<Object> playlistsSongsJsonIterator = playlistsSongsJson.iterator();
                while (playlistsSongsJsonIterator.hasNext()) {
                    playlist.song_ids.add(Integer.parseInt((String)playlistsSongsJsonIterator.next()));
                }

            }
            playlistMap.put(playlist.id, playlist);
        }
        return playlistMap;
    }
}
