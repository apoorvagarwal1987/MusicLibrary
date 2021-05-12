package operation;

import fileprocessing.Data;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public interface MusicLibraryOperations {
    Data processChangeFile(String fileName, Data data) throws IOException, ParseException;
}
