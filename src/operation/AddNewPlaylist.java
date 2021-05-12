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
 * The class is responsible to add new playlist. Following checks are added in place
 *
 * 1. Check is made to make sure playlist already present in the original file is not overwritten.
 * 2. Check is made to make sure playlist getting added does have at least 1 song.
 *
 * As this is a batch processing application the records which cannot be processed
 * are notified to the user on the console itself instead of failing the whole batch.
 */
public class AddNewPlaylist implements MusicLibraryOperations{
    @Override
    public Data processChangeFile(String fileName, Data data) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        Object object = parser.parse(new FileReader(fileName));
        JSONObject jsonObject = (JSONObject) object;

        Map<Integer, Playlist> newPlaylistMap = GeneratePlaylists.getPlaylists(jsonObject);

        for(int playListID : newPlaylistMap.keySet()) {
            if(!data.playListsMap.containsKey(playListID)) {
                if(newPlaylistMap.get(playListID).song_ids.size() > 0) {
                    data.playListsMap.put(playListID, newPlaylistMap.get(playListID));
                } else {
                    System.out.println(String.format("Requested Playlist id %d is not added as " +
                                    "as it no song in it "
                            , playListID));
                }
            }
            else {
                System.out.println(String.format("Requested Playlist id %d is already present in the " +
                        "original file", playListID));
            }
        }

        return data;
    }
}
