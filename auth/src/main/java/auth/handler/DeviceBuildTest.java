package auth.handler;

import com.google.protobuf.Message;
import io.netty.buffer.ByteBuf;
import protobuf.ParseRegistryMap;
import protobuf.Utils;
import protobuf.generate.device.device.Device;
import protobuf.generate.internal.Internal;

/**
 * Created by L on 2017/5/17.
 */
public class DeviceBuildTest {
    public static void main(String[] args) {
        Device.CDevice.Builder sp = Device.CDevice.newBuilder();
//        sp.setContent(msg.getContent());
        sp.setContent("auth send to server ");
        sp.setSelf("2");
        sp.setDest("3");


//        byteBuf = Utils.pack2Server(sp.build(), ParseRegistryMap.SPRIVATECHAT, netid, Internal.Dest.Gate, dest);
        Message m = sp.build();
        ByteBuf byteBuf = Utils.pack2Server(m, ParseRegistryMap.SPRIVATECHAT, 2, Internal.Dest.Gate, "1");
    }
}
