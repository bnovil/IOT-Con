package protobuf;

import protobuf.analysis.ParseMap;
import protobuf.generate.device.device.Device;
import protobuf.generate.device.login.Auth;
import protobuf.generate.internal.Internal;

import java.io.IOException;

/**
 * Created by lzq on 2016/1/30.
 */
public class ParseRegistryMap {
    public static final int GTRANSFER = 900;
    public static final int GREET = 901;

    public static final int CLOGIN = 1000;
    public static final int CREGISTER = 1001;
    public static final int SRESPONSE = 1002;

    public static final int CPRIVATECHAT = 1003;
    public static final int SPRIVATECHAT = 1004;


    public static void initRegistry() throws IOException {
        ParseMap.register(GTRANSFER, Internal.GTransfer::parseFrom, Internal.GTransfer.class); //内部传输协议用
        ParseMap.register(GREET, Internal.Greet::parseFrom, Internal.Greet.class); //内部传输协议用

        ParseMap.register(CLOGIN, Auth.CLogin::parseFrom, Auth.CLogin.class);
        ParseMap.register(CREGISTER, Auth.CRegister::parseFrom, Auth.CRegister.class);
        ParseMap.register(SRESPONSE, Auth.SResponse::parseFrom, Auth.SResponse.class);

//        ParseMap.register(CPRIVATECHAT, Chat.CPrivateChat::parseFrom, Chat.CPrivateChat.class);
//        ParseMap.register(SPRIVATECHAT, Chat.SPrivateChat::parseFrom, Chat.SPrivateChat.class);

        ParseMap.register(CPRIVATECHAT, Device.CDevice::parseFrom, Device.CDevice.class);
        ParseMap.register(SPRIVATECHAT, Device.SPrivateChat::parseFrom, Device.SPrivateChat.class);
    }
}
