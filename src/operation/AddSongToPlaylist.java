package operation;

import fileprocessing.Data;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import pojo.Playlist;
import utility.GeneratePlaylists;

import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

/**
 * The class is responsible to add songs in the playlist. Following checks are added in place
 *
 * 1. Check is made to make sure playlist provided is present in the original file.
 * 2. Check is made to make sure song requested to be added in the file is present in original file.
 * 3. Check is made to make sure playlist is modified by the owner of the playlist only.
 * 4. Check is made to make sure song already present in the playlist is added again.
 *
 * As this is a batch processing application the records which cannot be processed
 * are notified to the user on the console itself instead of failing the whole batch.
 */
public class AddSongToPlaylist implements MusicLibraryOperations {
    @Override
    public Data processChangeFile(String fileName, Data data) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        Object object = parser.parse(new FileReader(fileName));
        JSONObject jsonObject = (JSONObject) object;

        Map<Integer, Playlist> addSongsToPlaylistMap = GeneratePlaylists.getPlaylists(jsonObject);

        for(int playlistID : addSongsToPlaylistMap.keySet()) {
            if(data.playListsMap.containsKey(playlistID)) {
                if(data.playListsMap.get(playlistID).user_id == addSongsToPlaylistMap.get(playlistID).user_id) {
                    for(Integer songID : addSongsToPlaylistMap.get(playlistID).song_ids) {
                        if(data.songsMap.containsKey(songID)) {
                            if(data.playListsMap.get(playlistID).song_ids.contains(songID)) {
                                System.out.println(String.format("Requested song id %d for playlist %d is already in " +
                                        "the playlist", songID, playlistID));
                            } else {
                                data.playListsMap.get(playlistID).song_ids.add(songID);
                            }
                        } else {
                            System.out.println(String.format("Requested song id %d for playlist %d is not present " +
                                    "in original file", songID, playlistID));
                        }
                    }
                } else {
                    System.out.println(String.format("Requested playlist %d is not owned by the user %d" +
                            " so not modified", playlistID, addSongsToPlaylistMap.get(playlistID).user_id));
                }
            } else {
                System.out.println(String.format("Requested Playlist %d is not present in the " +
                        "original file", playlistID));
            }
        }

        return data;
    }
}
