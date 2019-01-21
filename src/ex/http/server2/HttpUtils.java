package ex.http.server2;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class HttpUtils {
    public static boolean isRequestEnd(byte[] request, int len) {
        if (len < 4) {
            return false;
        }
        return  request[len - 4] == '\r' &&
                request[len - 3] == '\n' &&
                request[len - 2] == '\r' &&
                request[len - 1] == '\n';
    }

    public static byte[] readRequestFully(InputStream in) throws IOException {
        byte[] buff = new byte[8192];
        int headerLen = 0;
        while (true) {
            int count = in.read(buff);
            if(count < 0){
                throw new RuntimeException("Incoming connection closed");
            }else {
                headerLen += count;
                if(isRequestEnd(buff,headerLen)){
                    return Arrays.copyOfRange(buff,0,headerLen);
                }
            }if(headerLen == buff.length){
                throw new RuntimeException("Header too big");
            }
        }

    }
}
