import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import sun.net.httpserver.HttpServerImpl;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

public class Server {
	
	private final ServerSocket server;
	
	public Server() throws Exception{
		this.server = new ServerSocket(8080);
	}
	
	public void runServer() throws IOException {
		HttpServer server = HttpServerImpl.create(new InetSocketAddress(8000), 0);
		HttpContext contextRoot = server.createContext("/");
		contextRoot.setHandler(new RequestHandler());
		server.start();
	}
	
	public static void main(String[] args){
		try {
			Server server = new Server();
			server.runServer();
		}catch (Exception e){
			System.out.println(e);
		}
	}
	
}

