package auth.handler;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import io.netty.buffer.ByteBuf;
import protobuf.ParseRegistryMap;
import protobuf.Utils;
import protobuf.generate.device.Device;
import protobuf.generate.internal.Internal;

/**
 * Created by L on 2017/5/17.
 */
public class DeviceBuildTest {
    public static void main(String[] args) throws InvalidProtocolBufferException {
        Device.DeviceMessage.Builder sp = Device.DeviceMessage.newBuilder();
//        sp.setContent(msg.getContent());
        sp.setContent("auth send to server ");
        sp.setSelf("2");
        sp.setDest("3");
        System.out.println(sp.getContent());

//        byteBuf = Utils.pack2Server(sp.build(), ParseRegistryMap.RESPONSE, netid, Internal.Dest.Gate, dest);

//        Message m = sp.build();
//        System.out.printf(sp.getContent());
//        ByteBuf byteBuf = Utils.pack2Server(m, ParseRegistryMap.RESPONSE, 2, Internal.Dest.Gate, "1");

        byte[] temp = sp.build().toByteArray();
        for (int i = 0; i <temp.length ; i++) {
            System.out.print(temp[i]);
        }

        Device.DeviceMessage dd = Device.DeviceMessage.parseFrom(temp);
        System.out.println(dd.getContent());
        System.out.println(dd.getDest());
    }
}
