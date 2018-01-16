package com.krishtal.chat.server;

public class ServerMain {
	
	private Server server;	
	private int port;
	
	public ServerMain(int port) {
		this.port = port;
		new Server(port);		
	}
	
	public static void main(String[] args) {
		int port;
		
		if (args.length != 1) {
			System.out.println("Usage: java -jar ServerName.jar [port]");
			return;
		}
		
		port = Integer.parseInt(args[0]);
		new ServerMain(port);
	}
	
}
