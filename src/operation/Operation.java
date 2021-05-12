package operation;

import fileprocessing.Data;
import fileprocessing.ProcessFile;

import static operation.OperationAllowed.ADD_PLAYLIST;

public class Operation {
    public static void main(String[] args) {
        if(args.length != 3) {
            System.out.println("Please provide input in the following order:");
            System.out.println("Input_file_name.json");
            System.out.println("Change_file_name.json");
            System.out.println("Operation_to_be_perform (0: ADD_PLAYLIST, 1: REMOVE_PLAYLIST, 2: ADD_SONGS_TO_PLAYLIST)");

            return;
        }

        clearScreen();

        String inputFileName = args[0];
        String changeFileName = args[1];
        int operation = Integer.parseInt(args[2]);
        Data modifiedData;

        String outputFileName;
        try {
            Data originalData = readFile(inputFileName);
            switch (OperationAllowed.valueOf(operation)) {
                case ADD_PLAYLIST:
                    System.out.println("========Processing Batch command============");
                    System.out.println("============================================");
                    System.out.println("================ADD PLAYLIST================");
                    System.out.println("============================================");
                    modifiedData = new AddNewPlaylist().processChangeFile(changeFileName, originalData);
                    outputFileName = writeFile(modifiedData);
                    break;

                case REMOVE_PLAYLIST:
                    System.out.println("========Processing Batch command============");
                    System.out.println("============================================");
                    System.out.println("============REMOVE PLAYLIST=================");
                    System.out.println("============================================");
                    modifiedData = new RemovePlaylist().processChangeFile(changeFileName, originalData);
                    outputFileName = writeFile(modifiedData);
                    break;

                case ADD_SONG_TO_PLAYLIST:
                    System.out.println("========Processing Batch command============");
                    System.out.println("============================================");
                    System.out.println("===========ADD SONG TO PLAYLIST=============");
                    System.out.println("============================================");
                    modifiedData = new AddSongToPlaylist().processChangeFile(changeFileName, originalData);
                    outputFileName = writeFile(modifiedData);
                    break;

                default:
                    System.out.println("Operation not supported");
                    return;
            }

            if(outputFileName == null) {
                System.out.println("No file generated");
            }
            System.out.println("Change file location: " + outputFileName);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static String writeFile(final Data data) throws Exception {
        ProcessFile processFile = new ProcessFile();
        return processFile.generateOutputFile(data);
    }

    private static Data readFile(final String inputFile) throws Exception{
        ProcessFile processFile = new ProcessFile();
        Data data = processFile.processInputFile(inputFile);
        return data;
    }

}
