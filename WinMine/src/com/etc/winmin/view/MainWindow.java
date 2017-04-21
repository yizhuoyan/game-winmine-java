package com.etc.winmin.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import resource.R;

import com.etc.winmin.controller.Game;
import com.etc.winmin.model.GameModel;
import com.etc.winmin.model.GameResource;

/**
 * 游戏主窗口
 * 
 * @author ben
 * 
 */
public class MainWindow extends JFrame  {
	private static final ImageIcon ICON_APP=GameResource.getResource(R.Icon.APP);
	
	public GameMenuBar menuBar;
	public GameControlPanel controlPanel;
	public GameMinePanel minePanel;

	public MainWindow(GameModel model) {
		this.setTitle("扫雷");
		this.setIconImage(ICON_APP.getImage());
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.initContentPanel();
		// init menuBar
		this.menuBar = new GameMenuBar(model);
		this.setJMenuBar(this.menuBar);
		// init control panel
		this.controlPanel = new GameControlPanel(model);
		this.add(controlPanel, BorderLayout.NORTH);
		// init mine panel
		this.minePanel = new GameMinePanel(model);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				Game.dispatchEvent("exitGame");	
			}
		});
		this.add(minePanel);
	}
	private void initContentPanel(){
		JPanel contentPanel = new JPanel(new BorderLayout());
		contentPanel.setBackground(new Color(0xc0c0c0));
		contentPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(2, 2, 0, 0, Color.WHITE),
				BorderFactory.createEmptyBorder(6, 6, 6, 6)));
		this.setContentPane(contentPanel);
	}
	
	
}
