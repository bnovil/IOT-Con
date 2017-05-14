package auth.handler;

import auth.IMHandler;
import auth.Worker;
import com.google.protobuf.Message;
import io.netty.channel.ChannelHandlerContext;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by lzq on 2017/3/5.
 * 建立服务器间的连接
 */
public class GreetHandler extends IMHandler {
    private static final Logger logger = LoggerFactory.getLogger(GreetHandler.class);

    public GreetHandler(String userid, long netid, Message msg, ChannelHandlerContext ctx) {
        super(userid, netid, msg, ctx);
    }

    /**
     *
     * @param worker
     * @throws TException
     */
    @Override
    protected void excute(Worker worker) throws TException {
        AuthServerHandler.setGateAuthConnection(_ctx);
        logger.info("[Gate-Auth] connection is established");
    }
}
