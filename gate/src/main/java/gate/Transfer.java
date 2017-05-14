package gate;

import com.google.protobuf.Message;
import gate.utils.ClientConnection;

import java.io.IOException;

/**
 * @Author:lzq
 * @Discription
 * @Date: Created on 2017/5/11.
 * @Modified By:
 */
@FunctionalInterface
public interface Transfer{
    void process(Message msg, ClientConnection conn) throws IOException;
}
