package com.etc.winmin.view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import com.etc.winmin.controller.Game;
import com.etc.winmin.model.GameLevel;
import com.etc.winmin.model.GameRecord;
import com.etc.winmin.model.GameRecords;

public class HeroListDialog extends JDialog implements ActionListener {
	private JButton resetRecordBtn, confirmBtn;
	private JPanel recordsPanel;
	private static final Font FONT_DEFAULT = new Font("宋体", Font.PLAIN, 12);

	public HeroListDialog(JFrame window) {
		super(window, true);
		this.setTitle("扫雷英雄榜");
		this.setResizable(false);
		this.createLayout();
		this.confirmBtn.requestFocusInWindow();
		this.setLocationRelativeTo(window);
		this.resetRecordBtn.addActionListener(this);
		this.confirmBtn.addActionListener(this);

		this.pack();
	}

	

	private void createLayout() {
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BorderLayout());
		contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 20, 15));

		recordsPanel = new JPanel();
		recordsPanel.setLayout(new GridBagLayout());
		contentPanel.add(recordsPanel, BorderLayout.NORTH);
		JPanel btnsPanel = this.createBtnsPanel();
		contentPanel.add(btnsPanel);
		this.setContentPane(contentPanel);
	}

	public  void updateView(GameRecords record) {
		recordsPanel.removeAll();
		showRecord(record.basicLevelRecord,1);
		showRecord(record.intermediateLevelRecord,2);
		showRecord(record.expenseLevelRecord,3);
		this.pack();
	}
	private void showRecord(GameRecord record,int y){
		GridBagConstraints gbc = new GridBagConstraints();
		Insets insets = new Insets(0, 0, 0, 0);
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = insets;
		gbc.ipadx = 0;
		gbc.gridx = 1;
		gbc.gridy = y;
		gbc.weightx = 0;
		insets.left = 0;
		GameLevel level=GameLevel.getGameLevelByNo(record.levelNo);
		this.createLabelCell(recordsPanel, level.name + ":", gbc);
		insets.left = 12;
		gbc.weightx = 0.5;
		gbc.gridx = 2;
		this.createLabelCell(recordsPanel, record.playTime + " 秒", gbc);
		gbc.gridx = 3;
		gbc.weightx = 0.5;
		insets.left = 50;
		gbc.ipadx = 80;
		this.createLabelCell(recordsPanel, record.player, gbc);
	}
	private JLabel createLabelCell(JPanel panel, String txt,
			GridBagConstraints gbc) {
		JLabel lbl = new JLabel(txt);
		lbl.setFont(FONT_DEFAULT);
		panel.add(lbl, gbc);
		return lbl;

	}

	private JPanel createBtnsPanel() {
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));
		SpringLayout layout = new SpringLayout();
		panel.setLayout(layout);
		this.resetRecordBtn = new JButton("重新计分(R)");
		this.resetRecordBtn.setMnemonic('R');
		this.resetRecordBtn.setFont(FONT_DEFAULT);
		panel.add(this.resetRecordBtn);
		this.confirmBtn = new JButton("确定");
		this.confirmBtn.setFont(FONT_DEFAULT);
		panel.add(this.confirmBtn);
		layout.putConstraint(SpringLayout.WEST, this.resetRecordBtn, 20,
				SpringLayout.WEST, panel);
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, this.resetRecordBtn,
				0, SpringLayout.VERTICAL_CENTER, panel);
		layout.putConstraint(SpringLayout.EAST, confirmBtn, -20,
				SpringLayout.EAST, panel);
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, this.confirmBtn, 0,
				SpringLayout.VERTICAL_CENTER, panel);
		return panel;

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.confirmBtn) {
			this.dispose();
		} else if (e.getSource() == this.resetRecordBtn) {
			Game.dispatchEvent("resetRecords");
		}
	}
}
