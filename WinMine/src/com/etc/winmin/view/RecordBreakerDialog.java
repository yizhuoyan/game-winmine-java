package com.etc.winmin.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.etc.winmin.controller.Game;

public class RecordBreakerDialog extends JDialog implements ActionListener {
	private JTextField nameTF;
	private JButton confirmBtn;
	public RecordBreakerDialog() {
		this.setUndecorated(true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.createLayout();
		this.nameTF.requestFocusInWindow();
		this.confirmBtn.addActionListener(this);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	private void createLayout(){
		JPanel panel=new JPanel();
		panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
		panel.setBorder(BorderFactory.createEmptyBorder(15,20,40,20));
		this.setContentPane(panel);
		JLabel label=new JLabel("已破初级记录。"); 
		label.setAlignmentX(CENTER_ALIGNMENT);
		this.add(label);
		label=new JLabel("请留尊姓大名。"); 
		label.setAlignmentX(CENTER_ALIGNMENT); 	
		this.add(label);
		this.add(Box.createVerticalStrut(40));
		nameTF=new JTextField("匿名",10);
		this.nameTF.selectAll();
		this.add(nameTF);
		this.add(Box.createVerticalStrut(16));
		confirmBtn=new JButton("确定");
		this.confirmBtn.setAlignmentX(CENTER_ALIGNMENT);
		this.add(confirmBtn);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==this.confirmBtn){
			String name=this.nameTF.getText();
			this.dispose();
			Game.dispatchEvent("saveNewRecord", name);
		}
	}
}
