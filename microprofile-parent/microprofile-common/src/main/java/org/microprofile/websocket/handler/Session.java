package org.microprofile.websocket.handler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.UUID;

import org.microprofile.common.constant.Constants;
import org.microprofile.nio.handler.ChannelContext;
import org.microprofile.websocket.utils.MessageUtils;

public class Session {
    private String id;
    private ChannelContext<WebSocketFrame> channelContext;
    private Map<String, String> headers;
    private String url;

    public Session(ChannelContext<WebSocketFrame> channelContext) {
        this.id = UUID.randomUUID().toString();
        this.channelContext = channelContext;
    }

    /**
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @param key
     * @return the header value
     */
    public String getHeader(String key) {
        return null == headers ? null : headers.get(key);
    }

    /**
     * @param headers
     *            the headers to set
     */
    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    /**
     * @return
     */
    public boolean isOpen() {
        return channelContext.isOpen();
    }

    /**
     * @param data
     * @throws IOException
     */
    public void sendString(String data) throws IOException {
        send(new Message(true, 0, Message.OPCODE_STRING, data.getBytes(Constants.DEFAULT_CHARSET)));
    }

    /**
     * @param data
     * @throws IOException
     */
    public void sendByte(byte[] data) throws IOException {
        send(new Message(true, 0, Message.OPCODE_BYTE, data));
    }

    /**
     * @param message
     * @throws IOException
     */
    public void send(Message message) throws IOException {
        channelContext.write(MessageUtils.wrapMessage(message, false, true));
    }

    /**
     * @param byteBuffer
     * @throws IOException
     */
    public void send(ByteBuffer byteBuffer) throws IOException {
        channelContext.write(byteBuffer);
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url
     *            the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @throws IOException
     */
    public void close() throws IOException {
        channelContext.close();
    }
}