package logic;

import com.google.protobuf.Message;
import io.netty.channel.ChannelHandlerContext;
import logic.handler.CDeviceHandler;
import logic.handler.GreetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protobuf.analysis.ParseMap;
import protobuf.generate.device.Device;
import protobuf.generate.internal.Internal;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Dell on 2016/3/4.
 */
public class HandlerManager {
    private static final Logger logger = LoggerFactory.getLogger(HandlerManager.class);

    private static final Map<Integer, Constructor<? extends IMHandler>> _handlers = new HashMap<>();

    public static void register(Class<? extends Message> msg, Class<? extends IMHandler> handler) {
        int num = ParseMap.getPtoNum(msg);
        try {
            Constructor<? extends IMHandler> constructor = handler.getConstructor(String.class, long.class, Message.class, ChannelHandlerContext.class);
            _handlers.put(num, constructor);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static IMHandler getHandler(int msgNum, String userId, long netId,
                                       Message msg, ChannelHandlerContext ctx)
            throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<? extends IMHandler> constructor = _handlers.get(msgNum);
        if(constructor == null) {
            logger.error("handler not exist, Message Number: {}", msgNum);
            return null;
        }

        return constructor.newInstance(userId, netId, msg, ctx);
    }

    /**
     * 处理消息的Handler
     */

    public static void initHandlers() {
        HandlerManager.register(Internal.Greet.class, GreetHandler.class);
        HandlerManager.register(Device.DeviceMessage.class, CDeviceHandler.class);
    }
}
