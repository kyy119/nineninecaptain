package GameApp.util;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class ObjectOutputStreamCustom extends ObjectOutputStream {

    public ObjectOutputStreamCustom(OutputStream out) throws IOException {
        super(out);
    }

    @Override
    protected void writeStreamHeader() throws IOException {
       reset();
    }
}
