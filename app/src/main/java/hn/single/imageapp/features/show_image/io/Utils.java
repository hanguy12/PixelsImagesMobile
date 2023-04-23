package hn.single.imageapp.features.show_image.io;

import java.io.InputStream;
import java.io.OutputStream;

import hn.single.imageapp.common.utils.Logger;

public class Utils {

    private Utils() {
    }

    public static void copyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            while (true) {
                //Read byte from input stream
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                //Write byte from output stream
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
            Logger.INSTANCE.d("LoadImage -- exception = " + ex.getMessage());
        }
    }
}
