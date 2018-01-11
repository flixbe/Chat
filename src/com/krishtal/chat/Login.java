package com.krishtal.chat;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class Login extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	private JButton buttonLogin;
	private JTextField textAddress;
	private JTextField textPort;
	private JTextField textName;
	private JLabel labelName;
	private JLabel labelIPAddress;
	private JLabel labelPort;
	private JLabel labelAddressDescription;
	private JLabel labelPortDescription;
	
	public Login() {
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		setResizable(false);
		setTitle("Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(300, 400);
		setLocationRelativeTo(null);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
				
		labelName = new JLabel("Name:");
		labelName.setBounds(125, 50, 70, 20);
		contentPane.add(labelName);
		
		textName = new JTextField();
		textName.setBounds(70, 70, 150, 30);
		textName.setColumns(10);
		contentPane.add(textName);
		
		labelIPAddress = new JLabel("IP Address:");
		labelIPAddress.setBounds(115, 115, 70, 20);
		contentPane.add(labelIPAddress);
		
		textAddress = new JTextField();
		textAddress.setBounds(70, 135, 150, 30);
		contentPane.add(textAddress);
		textAddress.setColumns(10);
		
		labelAddressDescription = new JLabel("(Ex: 192.168.0.2)");
		labelAddressDescription.setBounds(105, 170, 110, 15);
		contentPane.add(labelAddressDescription);
		
		labelPort = new JLabel("Port:");
		labelPort.setBounds(130, 190, 40, 16);
		contentPane.add(labelPort);
		
		textPort = new JTextField();
		textPort.setColumns(10);
		textPort.setBounds(70, 205, 150, 30);
		contentPane.add(textPort);
					
		labelPortDescription = new JLabel("(Ex: 7787)");
		labelPortDescription.setBounds(120, 240, 70, 15);
		contentPane.add(labelPortDescription);
		
		buttonLogin = new JButton("Login");
		buttonLogin.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String name = textName.getText();
				String address = textAddress.getText();
				int port = Integer.parseInt(textPort.getText());
				login(name, address, port);
			}
		});
		buttonLogin.setBounds(85, 280, 120, 30);
		contentPane.add(buttonLogin);
	}
	
	private void login(String name, String address, int port) {
		System.out.println(name + ", " + address + ", " + port);
		
		dispose();
		new Client(name, address, port);
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				Login frame = new Login();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
}