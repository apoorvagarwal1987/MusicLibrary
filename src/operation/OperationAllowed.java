package operation;

import java.util.HashMap;
import java.util.Map;

public enum OperationAllowed {
    ADD_PLAYLIST(0),
    REMOVE_PLAYLIST(1),
    ADD_SONG_TO_PLAYLIST(2),
    UNKNOWN_OPERATION(-1);

    private int value;
    private static Map map = new HashMap<>();

    OperationAllowed(int value) {
        this.value = value;
    }

    static {
        for (OperationAllowed operation : OperationAllowed.values()) {
            map.put(operation.value, operation);
        }
    }

    public static OperationAllowed valueOf(int operation) {
        if(map.containsKey(operation))
            return (OperationAllowed) map.get(operation);
        else
            return (OperationAllowed)map.get(-1);
    }
}
