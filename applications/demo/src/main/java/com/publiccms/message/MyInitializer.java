package com.publiccms.message;

import java.util.HashMap;
import java.util.Map;

import org.microprofile.message.initializer.ThinAppHandler;
import org.microprofile.message.initializer.ThinAppInitializer;
import org.microprofile.websocket.handler.MessageHandler;

public class MyInitializer implements ThinAppInitializer {

    public void start(String appPath, ThinAppHandler handler) {
    }

    public Map<MessageHandler, String[]> register() {
        HashMap<MessageHandler, String[]> map = new HashMap<>();
        map.put(new MyMessageHandler(), new String[] { "/*" });
        return map;
    }

    public void stop() {
    }
}
