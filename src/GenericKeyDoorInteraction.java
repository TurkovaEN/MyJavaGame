import java.io.InputStream;

public class GenericKeyDoorInteraction<T> extends KeyDoorInteraction {
    private T keyType;

    public GenericKeyDoorInteraction(InputStream keyPath, InputStream doorClosedPath, InputStream doorOpenedPath, String fontPath, T keyType) {
        super(keyPath, doorClosedPath, doorOpenedPath, fontPath);
        this.keyType = keyType;
    }

    public T getKeyType() {
        return keyType;
    }

}