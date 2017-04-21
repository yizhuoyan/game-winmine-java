package com.etc.winmin.view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.etc.winmin.controller.Game;
import com.etc.winmin.model.GameLevel;

public class CustomLevelDialog extends JDialog implements ActionListener {
	private static final Font FONT_LABLE = new Font("宋体", Font.PLAIN, 12);
	private JTextField hTF,wTF,mTF;
	private JButton confirmBtn,cancelBtn;
	/**
	 * Create the dialog.
	 */
	public CustomLevelDialog(Frame window) {
		super(window, true);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setTitle("自定义雷区");
		this.setResizable(false);
		JPanel contentPane = new JPanel();
		contentPane.setBorder(BorderFactory.createEmptyBorder(30, 15, 30, 15));
		contentPane.setLayout(new GridBagLayout());
		this.setContentPane(contentPane);
		this.createLayout();
		this.confirmBtn.addActionListener(this);
		this.cancelBtn.addActionListener(this);
		this.pack();
		this.setLocationRelativeTo(window);

	}
	private void setDefaultData(GameLevel customLevel){
		this.wTF.setText(String.valueOf(customLevel.maxX));
		this.hTF.setText(String.valueOf(customLevel.maxY));
		this.mTF.setText(String.valueOf(customLevel.mineAmount));
	}
	private void createLayout() {
		JLabel hLabel = new JLabel("高度(H):");
		hLabel.setFont(FONT_LABLE);
		hLabel.setDisplayedMnemonic('H');

		GridBagConstraints gbc_hLabel = new GridBagConstraints();
		gbc_hLabel.insets = new Insets(0, 0, 5, 5);
		gbc_hLabel.anchor = GridBagConstraints.EAST;
		gbc_hLabel.gridx = 0;
		gbc_hLabel.gridy = 0;
		getContentPane().add(hLabel, gbc_hLabel);

		hTF = new JTextField();
		GridBagConstraints gbc_hTF = new GridBagConstraints();
		gbc_hTF.insets = new Insets(0, 0, 5, 5);
		gbc_hTF.fill = GridBagConstraints.HORIZONTAL;
		gbc_hTF.gridx = 1;
		gbc_hTF.gridy = 0;
		getContentPane().add(hTF, gbc_hTF);
		hTF.setColumns(4);

		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridheight = 3;
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 2;
		gbc_panel.gridy = 0;
		hLabel.setLabelFor(hTF);
		getContentPane().add(panel, gbc_panel);
		panel.setLayout(new BorderLayout(0, 0));
		panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
		confirmBtn = new JButton("确定");
		confirmBtn.setFont(FONT_LABLE);
		
		panel.add(confirmBtn, BorderLayout.NORTH);

		cancelBtn = new JButton("取消");
		cancelBtn.setFont(FONT_LABLE);
		cancelBtn.setVerticalAlignment(SwingConstants.BOTTOM);
		panel.add(cancelBtn, BorderLayout.SOUTH);

		JLabel wLabel = new JLabel("宽度(W):");
		wLabel.setFont(FONT_LABLE);
		wLabel.setDisplayedMnemonic('W');
		GridBagConstraints gbc_wLabel = new GridBagConstraints();
		gbc_wLabel.anchor = GridBagConstraints.EAST;
		gbc_wLabel.insets = new Insets(0, 0, 5, 5);
		gbc_wLabel.gridx = 0;
		gbc_wLabel.gridy = 1;
		getContentPane().add(wLabel, gbc_wLabel);

		wTF = new JTextField();
		GridBagConstraints gbc_wTF = new GridBagConstraints();
		gbc_wTF.insets = new Insets(0, 0, 5, 5);
		gbc_wTF.fill = GridBagConstraints.HORIZONTAL;
		gbc_wTF.gridx = 1;
		gbc_wTF.gridy = 1;
		wLabel.setLabelFor(wTF);
		getContentPane().add(wTF, gbc_wTF);
		wTF.setColumns(4);

		JLabel mLable = new JLabel("地雷(M):");
		mLable.setFont(FONT_LABLE);
		mLable.setDisplayedMnemonic('M');
		
		GridBagConstraints gbc_mLable = new GridBagConstraints();
		gbc_mLable.anchor = GridBagConstraints.EAST;
		gbc_mLable.insets = new Insets(0, 0, 0, 5);
		gbc_mLable.gridx = 0;
		gbc_mLable.gridy = 2;
		getContentPane().add(mLable, gbc_mLable);

		mTF = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 0, 5);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 2;
		mLable.setLabelFor(this.mTF);
		getContentPane().add(mTF, gbc_textField);
		mTF.setColumns(4);
	}
	public void showView(GameLevel customLevel){
		this.setDefaultData(customLevel);
		this.setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==this.cancelBtn){
			this.dispose();
		}else if(e.getSource()==this.confirmBtn){
			GameLevel customLevel=this.getCustomLevelFromView();
			if(customLevel!=null){
				Game.dispatchEvent("setCustomeLevel", customLevel);
			}
			this.dispose();
		}
		
	}
	
	private GameLevel getCustomLevelFromView(){
		try {
			int maxX = Integer.parseInt(this.wTF.getText());
			int maxY = Integer.parseInt(this.hTF.getText());
			int mineAmount = Integer.parseInt(this.mTF.getText());
			GameLevel customLevel = new GameLevel();
			customLevel.maxX=maxX;
			customLevel.maxY=maxY;
			customLevel.mineAmount=mineAmount;
			return customLevel;
		} catch (Exception e) {//if there any exception ,ignore
			
		}
		return null;
	}

}
