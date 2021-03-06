package auth.handler;

import com.google.protobuf.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import auth.IMHandler;
import auth.Worker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protobuf.ParseRegistryMap;
import protobuf.Utils;
import protobuf.generate.device.Device;
import protobuf.generate.internal.Internal;

/**
 * Created by win7 on 2016/3/5.
 */
public class CDeviceHandler extends IMHandler{
    private static final Logger logger = LoggerFactory.getLogger(CDeviceHandler.class);

    public CDeviceHandler(String userid, long netid, Message msg, ChannelHandlerContext ctx) {
        super(userid, netid, msg, ctx);
    }

    @Override
//    protected void excute(Worker worker) throws TException {
    protected void excute()
    {
        Device.DeviceMessage msg = (Device.DeviceMessage) _msg;
        ByteBuf byteBuf;

        String dest = msg.getDest();
        Long netid = AuthServerHandler.getNetidByUserid(dest);
        if(netid == null) {
            logger.error("Dest User not online");
            return;
        }

        Device.Response.Builder sp = Device.Response.newBuilder();
        sp.setContent("auth send to server "+msg.getContent());

//        byteBuf = Utils.pack2Server(sp.build(), ParseRegistryMap.RESPONSE, netid, Internal.Dest.Gate, dest);
        byteBuf = Utils.pack2Server(sp.build(), ParseRegistryMap.RESPONSE, netid, Internal.Dest.Gate, msg.getDest());
        _ctx.writeAndFlush(byteBuf);

        logger.info("message has send from {} to {},{}", msg.getSelf(), msg.getDest(),msg.getContent());
    }
}
