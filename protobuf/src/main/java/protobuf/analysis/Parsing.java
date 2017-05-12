package protobuf.analysis;

import com.google.protobuf.Message;

import java.io.IOException;

/**
 * @Author:lzq
 * @Discription
 * @Date: Created on 2017/5/11.
 * @Modified By:
 */
@FunctionalInterface
public interface Parsing{
    Message process(byte[] bytes) throws IOException;
}
