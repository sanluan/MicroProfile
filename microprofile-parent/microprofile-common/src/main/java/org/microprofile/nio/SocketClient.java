package org.microprofile.nio;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

import org.microprofile.nio.handler.ChannelContext;
import org.microprofile.nio.handler.ProtocolHandler;
import org.microprofile.nio.handler.SocketProcesser;

public class SocketClient extends SocketProcesser implements Closeable {
    protected ChannelContext<?> channelContext;

    public SocketClient(String host, int port, ExecutorService pool, ProtocolHandler<?> protocolHandler) throws IOException {
        this(host, port, pool, protocolHandler, null, false, 0);
    }

    public SocketClient(String host, int port, ExecutorService pool, ProtocolHandler<?> protocolHandler, SSLContext sslContext,
            boolean needClientAuth) throws IOException {
        this(host, port, pool, protocolHandler, sslContext, needClientAuth, 0);
    }

    public SocketClient(String host, int port, ExecutorService pool, ProtocolHandler<?> protocolHandler, int maxPending)
            throws IOException {
        this(host, port, pool, protocolHandler, null, false, maxPending);
    }

    public SocketClient(String host, int port, ExecutorService pool, ProtocolHandler<?> protocolHandler, SSLContext sslContext,
            boolean needClientAuth, int maxPending) throws IOException {
        this(new InetSocketAddress(host, port), pool, protocolHandler, sslContext, needClientAuth, maxPending);
    }

    public SocketClient(SocketAddress socketAddress, ExecutorService pool, ProtocolHandler<?> protocolHandler,
            SSLContext sslContext, boolean needClientAuth, int maxPending) throws IOException {
        super(pool, protocolHandler, sslContext, maxPending);
        SocketChannel socketChannel = SocketChannel.open(socketAddress);
        channelContext = new ChannelContext<>(protocolHandler, this, socketChannel, createSSLEngine(sslContext, needClientAuth),
                DEFAULT_BLOCK_SIZE);
        register(socketChannel.configureBlocking(false), channelContext);
    }

    protected SSLEngine createSSLEngine(SSLContext sslContext, boolean needClientAuth) {
        SSLEngine sslEngine = null;
        if (null != sslContext) {
            sslEngine = sslContext.createSSLEngine();
            sslEngine.setUseClientMode(true);
            sslEngine.setNeedClientAuth(needClientAuth);
        }
        return sslEngine;
    }

    public String getName() throws IOException {
        StringBuilder sb = new StringBuilder("Thread [Client ");
        sb.append(channelContext.getSocketChannel().getLocalAddress()).append(" to server ")
                .append(channelContext.getSocketChannel().getRemoteAddress()).append(" listener]");
        return sb.toString();
    }

    public SocketClient sendMessage(String message) throws IOException {
        return sendMessage(message.getBytes());
    }

    public SocketClient sendMessage(byte[] message) throws IOException {
        if (null != channelContext) {
            channelContext.write(ByteBuffer.wrap(message));
        }
        return this;
    }

    public boolean isOpen() throws IOException {
        return channelContext.isOpen();
    }

    public void reConnect() throws IOException {
        if (!channelContext.isOpen()) {
            channelContext.getSocketChannel().connect(channelContext.getSocketChannel().getRemoteAddress());
        }
    }

    public void close() throws IOException {
        if (channelContext.isOpen()) {
            channelContext.close();
        }
        super.close();
    }

    public ChannelContext<?> getChannelContext() {
        return channelContext;
    }
}
