package logic.handler;

import com.google.protobuf.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import logic.IMHandler;
import logic.Worker;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protobuf.ParseRegistryMap;
import protobuf.Utils;
import protobuf.generate.device.Auth;
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
    protected void excute(Worker worker) throws TException {
        Device.DeviceMessage msg = (Device.DeviceMessage) _msg;
        ByteBuf byteBuf = null;

        //转发给auth
        byteBuf = Utils.pack2Server(_msg, ParseRegistryMap.DEVICEMESSAGE, Internal.Dest.Auth, msg.getDest());
        LogicServerHandler.getAuthLogicConnection().writeAndFlush(byteBuf);

        //回应
        Auth.SResponse.Builder sr = Auth.SResponse.newBuilder();
        sr.setCode(200);
        sr.setDesc("Logic server received message successed");
        byteBuf = Utils.pack2Client(sr.build());
        _ctx.writeAndFlush(byteBuf);
    }
}
