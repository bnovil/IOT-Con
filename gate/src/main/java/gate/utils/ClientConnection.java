package gate.utils;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import thirdparty.threedes.ThreeDES;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by lzq on 2016/1/29.
 * 客户端连接的封装类
 */

public class ClientConnection {
    private static final Logger logger = LoggerFactory.getLogger(ClientConnection.class);
    private static final AtomicLong netidGenerator = new AtomicLong(0);
    private String _userId;
    private long _netId;  //connection id
    private ChannelHandlerContext _ctx;

    public static AttributeKey<ThreeDES> ENCRYPT = AttributeKey.valueOf("encrypt");
    public static AttributeKey<Long> NETID = AttributeKey.valueOf("netid");

    ClientConnection(ChannelHandlerContext c) {
        _netId = netidGenerator.incrementAndGet();
        _ctx = c;
//        _ctx.attr(ClientConnection.NETID).set(_netId);
        c.channel().attr(ClientConnection.NETID).set(_netId);
    }




    public long getNetId() {
        return _netId;
    }

    public String getUserId() {
        return _userId;
    }

    public void setUserId(String userId) {
        _userId = userId;
    }

    public void readUserIdFromDB() {

    }

    public void setCtx(ChannelHandlerContext ctx) {_ctx = ctx;}
    public ChannelHandlerContext getCtx() {
        return _ctx;
    }
}
