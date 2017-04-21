package com.etc.winmin.view;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import resource.R;

import com.etc.winmin.model.GameResource;

public class AboutDialog extends JDialog implements ActionListener  {
	
	private BufferedImage bgAbout;
	public AboutDialog(MainWindow mainWindow) {
		super(mainWindow,"About扫雷",true);
		this.setResizable(false);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.bgAbout=GameResource.getImage(R.Image.BG_ABOUT);
		this.setSize(bgAbout.getWidth(), bgAbout.getHeight());
		createLayout();
		this.setLocationRelativeTo(mainWindow);
	}
	private void createLayout(){
		JPanel contentPane=new JPanel(){
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(bgAbout, 0, 0, null);
			}
		};
		SpringLayout layout=new SpringLayout();
		contentPane.setLayout(layout);
		
		JButton button=new JButton("确定");
		button.addActionListener(this);
		contentPane.add(button);
		this.setContentPane(contentPane);
		layout.putConstraint(SpringLayout.EAST, button, -50,SpringLayout.EAST , this.getContentPane());
		layout.putConstraint(SpringLayout.SOUTH, button, -20,SpringLayout.SOUTH , this.getContentPane());
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		this.dispose();
	}
}
