package client;

import com.google.protobuf.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protobuf.Utils;
import protobuf.generate.device.Auth;
import protobuf.generate.device.Device;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by lzq on 2016/2/15.
 * 智能硬件设备与服务器的通信
 */
public class ClientHandler extends SimpleChannelInboundHandler<Message> {
    private static ChannelHandlerContext _gateClientConnection;
    //消息计数
    private static int n = 0;

    private static final Logger logger = LoggerFactory.getLogger(ClientHandler.class);
    private String _deviceId = "";
    private boolean _verify = false;
    private static int count = 0;

    private static AtomicLong increased = new AtomicLong(1);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws IOException {
        _gateClientConnection = ctx;
        String passwd = "123";
        _deviceId = Long.toString(increased.getAndIncrement());


//        sendCRegister(ctx, _deviceId, passwd);
        sendCLogin(ctx, _deviceId, passwd);
        //        sendMessage();
    }

    private void sendCRegister(ChannelHandlerContext ctx, String userid, String passwd) {
        Auth.CRegister.Builder cb = Auth.CRegister.newBuilder();
        cb.setUserid(userid);
        cb.setPasswd(passwd);

        ByteBuf byteBuf = Utils.pack2Client(cb.build());
        ctx.writeAndFlush(byteBuf);
        logger.info("send CRegister userid:{}", _deviceId);
    }

    private void sendCLogin(ChannelHandlerContext ctx, String userid, String passwd) {
        Auth.CLogin.Builder loginInfo = Auth.CLogin.newBuilder();
        loginInfo.setUserid(userid);
        loginInfo.setPasswd(passwd);
//        loginInfo.setPlatform("ios");
//        loginInfo.setAppVersion("1.0.0");

        ByteBuf byteBuf = Utils.pack2Client(loginInfo.build());
        ctx.writeAndFlush(byteBuf);
        logger.info("send CLogin userid:{}", _deviceId);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message msg) throws Exception {
        logger.info("received message: {}", msg.getClass());
        if (msg instanceof Auth.SResponse) {
            Auth.SResponse sp = (Auth.SResponse) msg;
            int code = sp.getCode();
            String desc = sp.getDesc();
            switch (code) {
                //device verified
                case Common.VERYFY_PASSED:
                    logger.info("device verified, description: {}", desc);
                    _verify = true;
                    break;
                //device registry
                case Common.REGISTER_OK:
                    logger.info("device registerd successd, description: {}", desc);
                    break;
                case Common.Msg_SendSuccess:
                    logger.info("Message Send Successed, description: {}", desc);
                default:
                    logger.info("Unknow code: {}", code);
            }
        } else if (msg instanceof Device.Response) {

            System.out.println("device received: " + ((Device.Response) msg).getContent());
            logger.info("{} receiced device message: {}.Total:{}", _deviceId, ((Device.Response) msg).getContent(), ++count);
        }

        //这样设置的原因是，防止两方都阻塞在输入上
        if (_verify) {
            sendMessage();
            Thread.sleep(Client.frequency);
        }
        sendMessage();
    }

    private void sendMessage() {
        n++;
        String content = "This is device:" + n + ", Message number: " + n;
        Device.DeviceMessage.Builder cd = Device.DeviceMessage.newBuilder();
        cd.setContent(content);
        cd.setSelf(_deviceId);
        cd.setDest(_deviceId);

        ByteBuf byteBuf = Utils.pack2Client(cd.build());
        _gateClientConnection.writeAndFlush(byteBuf);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}
