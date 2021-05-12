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
 * The class is responsible to remove all the playlist in the
 * change file. This before deleting the playlist make a check that
 * the playlist is owned by the user or not. In the case if the playlist
 * is not owned by the user the playlist is not deleted.
 *
 * As this is a batch processing application the records which cannot be processed
 * are notified to the user on the console itself instead of failing the whole batch.
 */
public class RemovePlaylist implements MusicLibraryOperations{
    @Override
    public Data processChangeFile(String fileName, Data data) throws IOException, ParseException {

        JSONParser parser = new JSONParser();
        Object object = parser.parse(new FileReader(fileName));
        JSONObject jsonObject = (JSONObject) object;

        Map<Integer, Playlist> deletePlaylistMap = GeneratePlaylists.getPlaylists(jsonObject);

        for(int playlistID : deletePlaylistMap.keySet()) {
            if(data.playListsMap.containsKey(playlistID)) {
                if(data.playListsMap.get(playlistID).user_id ==  deletePlaylistMap.get(playlistID).user_id)
                    data.playListsMap.remove(playlistID);
                else
                    System.out.println(String.format("Requested playlist %d is not owned by the user %d" +
                            " so not deleted", playlistID, deletePlaylistMap.get(playlistID).user_id));
            } else {
                System.out.println(String.format("Requested playlist %d is not present" +
                        " in original file", playlistID));
            }
        }

        return data;
    }
}
