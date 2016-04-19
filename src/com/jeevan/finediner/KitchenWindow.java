package com.jeevan.finediner;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JSlider;

public class KitchenWindow {

	private JFrame frame;
	private Socket soc;
	private ObjectOutputStream outStream;
	private ObjectInputStream inStream;
	public int x = 50;
	public ArrayList<Table> tables = new ArrayList<>();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					KitchenWindow window = new KitchenWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public KitchenWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		frame = new JFrame();
		frame.setBounds(100, 100, 609, 448);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		try {
			soc = new Socket("localhost", 3223);
			outStream = new ObjectOutputStream(soc.getOutputStream());
			inStream = new ObjectInputStream(soc.getInputStream());
			sendRequest(new Request(Request.KITCHEN_CONNECT,"sd"));
			new Listen().start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
	public Request receive(){
		try {
			return (Request) inStream.readObject();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	public void sendRequest(Request req) {
		try {
			outStream.writeObject(req);
			outStream.flush();
			outStream.reset();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public Table findTable(int tn){
		for (int i=0; i<tables.size();i++) {
			if(tables.get(i).tableno == tn){
				return tables.get(i);
			}
		}
		return null;
	}
	public class Listen extends Thread {
		public void run(){
			while(true){
				Request req = receive();
				switch(req.getType()){
				case Request.NEW_CUSTOMER:
					Table t = new Table((int) req.getContent());
					tables.add(t);
					break;
				case Request.NEW_ORDER:
					findTable((int) req.getContent()).addOrder((ArrayList<Item>) req.getSecondContent());;
					break;
				}

			}
		}
	}
	class Table {
		int tableno;
		ArrayList<Item> orders = new ArrayList<Item>();
		JPanel panel;
		JTextPane textPane;
		JSlider slider;
		JLabel lblNewLabel ;
		Timer tt;


		public Table(int a){
			tableno = a;
			x = x+160;
		}
		public long compareTime(ArrayList<Item> a, long total) {
			for (Item item: a) {
				if (total < item.getTime()) {
					total = item.getTime();
				}
			}
			return total;
		}


		public void addOrder(ArrayList<Item> it) {
			if (panel==null){
				panel = new JPanel();
				panel.setBounds(x, 11, 160, 387);
				panel.setLayout(null);
				slider = new JSlider(0,(int)compareTime(it,0),1);
				slider.setBounds(10, 282, 141, 42);
				panel.add(slider);
				lblNewLabel = new JLabel("Table No");
				lblNewLabel.setBounds(54, 11, 96, 14);
				panel.add(lblNewLabel);
				frame.getContentPane().add(panel);
				slider.addMouseListener(new MouseListener()
				{
					public void mousePressed(MouseEvent event) {
						//Mouse Pressed Functionality add here
						
							
					}

					@Override
					public void mouseClicked(MouseEvent arg0) {
						// TODO Auto-generated method stub

					}
					@Override
					public void mouseEntered(MouseEvent arg0) {
						// TODO Auto-generated method stub
					}
					@Override
					public void mouseExited(MouseEvent arg0) {
						// TODO Auto-generated method stub
					}

					@Override
					public void mouseReleased(MouseEvent arg0) {
						// TODO Auto-generated method stub
						if (tt!=null){
							int w = slider.getValue();
							lblNewLabel.setText(""+w);
							sendRequest(new Request(Request.NEW_ORDER,tableno,w));

						}
						else
						{
							slider.setValue(0);
							sendRequest(new Request(Request.NEW_ORDER,tableno,slider.getMaximum()));

						}
					}

				});

				panel.revalidate();
				panel.repaint();
				textPane = new JTextPane();
				textPane.setBounds(10, 45, 141, 226);
				panel.add(textPane);


			}
			long remaining;
			if(tt==null){
				remaining = compareTime(it, 0);
			}
			else {
				tt.stop();
				remaining  = compareTime(it, (long) slider.getMaximum()-slider.getValue());
			}

			orders.addAll(it);
			for (int i=0; i<it.size();i++) {
				textPane.setText(textPane.getText()+"\n" +it.get(i).getQuantity()+" "+it.get(i).getName());

			}


			slider.setMaximum(slider.getValue()+ (int)remaining);
			if (!slider.isEnabled()){
				slider.setEnabled(true);
				slider.setValue(0);

				panel.revalidate();
				panel.repaint();

			}

			tt = new Timer(100, new ActionListener() {// 50 ms interval in each increase.
				public void actionPerformed(ActionEvent e) {
					if (slider.isEnabled()){
						if (slider.getMaximum() > slider.getValue()) {
							slider.setValue(slider.getValue() + 100);
						} else {
							tt.stop();
				            EventQueue.invokeLater(new Runnable() {
				                @Override
				                public void run() {
				                    slider.dispatchEvent(
				                                   new MouseEvent(slider, 
				                                   MouseEvent.MOUSE_RELEASED,
				                                   0,0,0,0,1,false));
				                    slider.setEnabled(false);
				                    slider.setValue(0);
				               }
				            });
				            tt = null;
						}  
					}

				}
			});

			tt.start();

			panel.revalidate();
			panel.repaint();

		}
		public ArrayList<Item> getOrder(){
			return orders;

		}


	}



}
