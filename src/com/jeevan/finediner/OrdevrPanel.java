package com.jeevan.finediner;

import javax.swing.JPanel;
import javax.swing.JSlider;

import java.awt.EventQueue;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.*;
import javax.swing.JTextPane;

public class OrdevrPanel extends JPanel {

	/**
	 * Create the panel.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JFrame frame = new JFrame();
					frame.setBounds(100, 100, 609, 448);
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.getContentPane().setLayout(null);
					frame.setVisible(true);
					JPanel panel = new JPanel();
					panel.setBounds(132, 11, 160, 387);
					panel.setLayout(null);
					final JSlider slider = new JSlider();
					slider.setBounds(10, 282, 141, 42);
					panel.add(slider);
					final Timer increaseValue = new Timer(1000, new ActionListener() {// 50 ms interval in each increase.
				        public void actionPerformed(ActionEvent e) {
				            if (slider.getMaximum() != slider.getValue()) {
				            	slider.setValue(slider.getValue() + 100);
				            } else {
				                ((Timer) e.getSource()).stop();
				            }
				        }
				    });
		            increaseValue.start();

					frame.getContentPane().add(panel);

					
					panel.revalidate();
					panel.repaint();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	OrdevrPanel(int s){
		setBounds(s, 11, 160, 387);
		setLayout(null);
		JSlider slider = new JSlider();
		slider.setBounds(10, 282, 141, 42);
		add(slider);

		
		JTextPane textPane = new JTextPane();
		textPane.setBounds(10, 45, 141, 226);
		add(textPane);
		
		JLabel lblNewLabel = new JLabel("Table No");
		lblNewLabel.setBounds(54, 11, 96, 14);
		add(lblNewLabel);
		
		
		
		revalidate();
		repaint();
		
	}
}
