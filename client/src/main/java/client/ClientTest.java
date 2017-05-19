package client;

import protobuf.generate.device.Auth;

/**
 * Created by L on 2017/5/16.
 */
public class ClientTest {
    public static void main(String[] args) {
        Auth.CRegister.Builder cb = Auth.CRegister.newBuilder();
        cb.setUserid("123");
        cb.setPasswd("123");
    }

}
