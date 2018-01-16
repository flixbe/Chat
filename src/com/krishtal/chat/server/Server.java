package com.krishtal.chat.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class Server {

	private List<ServerClient> clients = new ArrayList<ServerClient>();
	
	private DatagramSocket socket;
	private Thread serverRun, manageClients, receiveData;
	
	private int port;
	private boolean running = false;
	
	public Server(int port) {
		this.port = port;
		
		try { 
			socket = new DatagramSocket(port);
		} catch (SocketException e) {
			e.printStackTrace();
			return;
		}
		
		serverRun = new Thread(() -> {
			running = true;
			System.out.println("Server started on port: " + port);
			manage();
			receive();
		}, "Run");
		serverRun.start();
	}
	
	private void manage() {
		manageClients = new Thread(() -> {
			while (running) {
				//TODO: Managing
			}
		}, "Manage");
		manageClients.start();
	}
	
	private void receive() {
		receiveData = new Thread(() -> {
			while (running) {
				//TODO: Receiving
				byte[] data = new byte[1024];
				DatagramPacket packet = new DatagramPacket(data, data.length);
				try {
					socket.receive(packet);
				} catch (IOException e) {
					e.printStackTrace();
				}
				process(packet);
				clients.add(new ServerClient("Anonymous", packet.getAddress(), packet.getPort(), 1337));
				System.out.println(clients.get(0).address.toString() + " : " + clients.get(0).port);
			}
		}, "Receive");
		receiveData.start();
	}
	
	private void process(DatagramPacket packet) {
		String str = new String(packet.getData());
		
		if (str.startsWith("/c/")) {
			int id = UniqueIdentifier.getID();
			clients.add(new ServerClient(str.substring(3, str.length()), packet.getAddress(), packet.getPort(), id));
			System.out.println("ID: " + id);
			System.out.println(str.substring(3, str.length()));			
		} else {
			System.out.println(str);
		}
	}
}
