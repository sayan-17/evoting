import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import sun.net.httpserver.HttpServerImpl;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.Timer;

public class Server {
	
	private final ServerSocket server;
	
	public Server() throws IOException {
		this.server = new ServerSocket(8080);
	}
	
	public void runServer() throws IOException {
		setPeriodicScheduler();
		HttpServer server = HttpServerImpl.create(new InetSocketAddress(8000), 0);
		HttpContext contextRoot = server.createContext("/");
		contextRoot.setHandler(new RequestHandler());
		server.start();
	}
	
	private void setPeriodicScheduler() {
		Timer timer = new Timer();
		PeriodicScheduler scheduler = new PeriodicScheduler();
		timer.schedule(scheduler, 120000);
	}
	
	public static void main(String[] args){
		try {
			Server server = new Server();
			server.runServer();
		}catch (IOException e){
			System.out.println(e);
		}
	}
	
}

