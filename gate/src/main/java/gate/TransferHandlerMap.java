package gate;

import com.google.protobuf.Message;
import gate.handler.GateAuthConnectionHandler;
import gate.handler.GateLogicConnectionHandler;
import gate.utils.ClientConnection;
import gate.utils.ClientConnectionMap;
import io.netty.buffer.ByteBuf;
import protobuf.Utils;
import protobuf.analysis.ParseMap;
import protobuf.generate.device.Auth;
import protobuf.generate.device.Device;
import protobuf.generate.internal.Internal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @Author:lzq
 * @Discription
 * @Date: Created on 2017/5/11.
 * @Modified By:
 */
public class TransferHandlerMap {
    private static final Logger logger = LoggerFactory.getLogger(ClientMessage.class);

    public static void initRegistry() throws IOException {

        //传递消息到Auth
        Transfer t2Auth = (Message msg, ClientConnection conn) -> {
            ByteBuf byteBuf = null;
            if (msg instanceof Auth.CLogin) {
                String userId = ((Auth.CLogin) msg).getUserid();
                byteBuf = Utils.pack2Server(msg, ParseMap.getPtoNum(msg), conn.getNetId(), Internal.Dest.Auth, userId);
                ClientConnectionMap.registerUserid(userId, conn.getNetId());
            } else if (msg instanceof Auth.CRegister) {
                byteBuf = Utils.pack2Server(msg, ParseMap.getPtoNum(msg), conn.getNetId(), Internal.Dest.Auth, ((Auth.CRegister) msg).getUserid());
            }
            GateAuthConnectionHandler.getGateAuthConnection().writeAndFlush(byteBuf);
        };

        //传递消息到Logic
        Transfer t2Logic = (Message msg, ClientConnection conn) -> {
            ByteBuf byteBuf = null;
            if (conn.getUserId() == null) {
                logger.error("User not login.");
                return;
            }
            if (msg instanceof Device.DeviceMessage) {
                byteBuf = Utils.pack2Server(msg, ParseMap.getPtoNum(msg), conn.getNetId(), Internal.Dest.Logic, conn.getUserId());
            }
            GateLogicConnectionHandler.getGatelogicConnection().writeAndFlush(byteBuf);
        };

        ClientMessage.registerTranferHandler(1000, t2Auth, Auth.CLogin.class);
        ClientMessage.registerTranferHandler(1001, t2Auth, Auth.CRegister.class);
        ClientMessage.registerTranferHandler(1003, t2Logic, Device.DeviceMessage.class);

//        ClientMessage.registerTranferHandler(1000, ClientMessage::transfer2Auth, Auth.CLogin.class);
//        ClientMessage.registerTranferHandler(1001, ClientMessage::transfer2Auth, Auth.CRegister.class);
//        ClientMessage.registerTranferHandler(1003, ClientMessage::transfer2Logic, Chat.CPrivateChat.class);

    }
}
