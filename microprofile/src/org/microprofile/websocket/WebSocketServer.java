package org.microprofile.websocket;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.Executors;

import org.microprofile.nio.SocketServer;
import org.microprofile.websocket.handler.MessageHandler;
import org.microprofile.websocket.handler.ServerProtocolHandler;

public class WebSocketServer implements Closeable {
	private SocketServer socketServer;

	public WebSocketServer(int port, int poolSize, MessageHandler messageHandler) throws IOException {
		this(null, port, poolSize, messageHandler);
	}

	public WebSocketServer(String host, int port, int poolSize, MessageHandler messageHandler) throws IOException {
		if (null == messageHandler) {
			throw new IllegalArgumentException("messageHandler can't be null");
		}
		this.socketServer = new SocketServer(host, port, Executors.newFixedThreadPool(poolSize), new ServerProtocolHandler(messageHandler));
	}

	public void listen() throws IOException {
		socketServer.listen();
	}

	public void close() throws IOException {
		socketServer.close();
	}

}