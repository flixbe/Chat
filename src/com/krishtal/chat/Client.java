package com.krishtal.chat;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.krishtal.chat.net.Net;

public class Client extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	private JTextField textMessage;
	private JTextArea history;
	
	private String name, address;
	private int port;
	private boolean connected = false;
	private boolean running = false;
	
	private Thread run, listen;
	private Net net = null;
	
	public Client(String name, String address, int port) {
		this.name = name;
		this.address = address;
		this.port = port;	
		
		net = new Net(port);
		connected = net.openConnection(address);
		
		if (!connected) {
			System.out.println("Connection failed...");
			console("Connection failed...");
		}		
		createWindow();
		
		console("You are trying to connect to: " + address + ", port: " + port + ", user name: " + name);
		String connectionPacket = "/c/" + name + "/e/";
		net.send(connectionPacket.getBytes());
		
		run = new Thread(() -> {
			running = true;
			listen();
		}, "Running");
		run.start();
	}
	
	private void createWindow() {
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		setResizable(false);
		setTitle("Messenger Client");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(900, 600);
		setLocationRelativeTo(null);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		GridBagLayout gblContentPane = new GridBagLayout();
		gblContentPane.columnWidths = new int[] {16, 857, 7};
		gblContentPane.columnWidths = new int[] {16, 857, 30, 7};
		gblContentPane.rowHeights = new int[] {35, 475, 40};
		gblContentPane.columnWeights = new double[] {1.0, Double.MIN_VALUE};
		gblContentPane.columnWeights = new double[] {1.0, 1.0};
		gblContentPane.rowWeights = new double[] {1.0, Double.MIN_VALUE};
		
		contentPane.setLayout(gblContentPane);
		
		history = new JTextArea();
		history.setEditable(false);
		history.setFont(new Font("consolas", Font.PLAIN, 14));
		
		JScrollPane scroll = new JScrollPane(history);
		GridBagConstraints scrollConstraints = new GridBagConstraints();
		scrollConstraints.insets = new Insets(0, 0, 5, 5);
		scrollConstraints.fill = GridBagConstraints.BOTH;
		scrollConstraints.gridx = 0;
		scrollConstraints.gridy = 0;
		scrollConstraints.gridwidth = 3;
		scrollConstraints.gridheight = 2;
		scrollConstraints.insets = new Insets(0, 7, 0, 0);
		contentPane.add(scroll, scrollConstraints);
		
		textMessage = new JTextField();
		textMessage.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					send(textMessage.getText(), true);
				}
			}
		});
		
		GridBagConstraints gbcTextMessage = new GridBagConstraints();
		gbcTextMessage.insets = new Insets(0, 0, 0, 5);
		gbcTextMessage.fill = GridBagConstraints.HORIZONTAL;
		gbcTextMessage.gridx = 0;
		gbcTextMessage.gridy = 2;
		gbcTextMessage.gridwidth = 2;
		contentPane.add(textMessage, gbcTextMessage);
		textMessage.setColumns(10);
		
		JButton buttonSend = new JButton("Send");
		buttonSend.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				send(textMessage.getText(), true);
			}
		});
		
		GridBagConstraints gbcButtonSend = new GridBagConstraints();
		gbcButtonSend.insets = new Insets(0, 0, 0, 5);
		gbcButtonSend.gridx = 2;
		gbcButtonSend.gridy = 2;
		contentPane.add(buttonSend, gbcButtonSend);
		setVisible(true);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				String disconect = "/d/" + net.getID() + "/e/";
				send(disconect, false);
				net.close();
				running = false;
			}
		});
		
		textMessage.requestFocusInWindow();
	}

	public void send(String message, boolean text) {
		if (message.equals("")) return;
		if (text) {
			message = name + ": " + message;
			message = "/m/" + message;
			textMessage.setText("");
		}
		net.send(message.getBytes());
	}
	
	public void listen() {
		listen = new Thread(() -> {
			while (running) {
				String message = net.receive();								
				if (message.startsWith("/c/")) {
					net.setID(Integer.parseInt(message.split("/c/|/e/")[1]));
					console("Successfuly connected to server! ID: " + net.getID());
				} else if (message.startsWith("/m/")) {
					String text = message.substring(3);
					text = text.split("/e/")[0];
					console(text);
				} else if (message.startsWith("/i/")) {
					String text = "/i/" + net.getID() + "/e/";
					send(text, false);
				}
			}
		}, "Listen");
		listen.start();
	}
	
	public void console(String message) {
		history.append(message + "\n\r");
		history.setCaretPosition(history.getDocument().getLength());
	}
	
}
