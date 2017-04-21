package com.etc.winmin.view;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import com.etc.winmin.controller.Game;
import com.etc.winmin.model.GameModel;

public class GameMenuBar extends JMenuBar implements ActionListener {
	private JMenuItem levelBasicMI, levelIntermediateMI, levelExpenseMI,
			levelCustomMI;
	private JMenuItem toggleQuestionFlagMI, toggleSoundMI;
	private JMenuItem sweepHerosListMI;

	public GameMenuBar(GameModel model) {
		this.createGameMenu(model);
		this.createHelpMenu();
		this.allMenuItemAddListener();
	}

	private void allMenuItemAddListener() {
		Component[] menus = this.getComponents();
		JMenu menu = null;
		Component[] menuItems;
		for (int i = 0; i < menus.length; i++) {
			menu = (JMenu) menus[i];
			menuItems = menu.getMenuComponents();
			for (int j = 0; j < menuItems.length; j++) {
				if (menuItems[j] instanceof JMenuItem) {
					((JMenuItem) menuItems[j]).addActionListener(this);
				}
			}
		}
	}

	private void createGameMenu(GameModel config) {
		JMenu gameMenu = new JMenu("游戏(G)");
		gameMenu.setMnemonic('G');
		this.add(gameMenu);

		JMenuItem newGameMI = new JMenuItem("开局(N)", 'N');
		newGameMI.setAccelerator(KeyStroke.getKeyStroke("F2"));
		newGameMI.setActionCommand("newGame");
		gameMenu.add(newGameMI);

		gameMenu.addSeparator();

		levelBasicMI = new JCheckBoxMenuItem("初级(B)");
		levelBasicMI.setMnemonic('B');
		levelBasicMI.setActionCommand("setBasicLevel");
		levelBasicMI.setSelected(true);
		gameMenu.add(levelBasicMI);

		levelIntermediateMI = new JCheckBoxMenuItem("中级(I)");
		levelIntermediateMI.setMnemonic('I');
		levelIntermediateMI.setActionCommand("setIntermediateLevel");
		gameMenu.add(levelIntermediateMI);

		levelExpenseMI = new JCheckBoxMenuItem("高级(E)");
		levelExpenseMI.setMnemonic('E');
		levelExpenseMI.setActionCommand("setExpenseLevel");
		gameMenu.add(levelExpenseMI);

		levelCustomMI = new JCheckBoxMenuItem("自定义(C)...");
		levelCustomMI.setMnemonic('C');
		levelCustomMI.setActionCommand("showCustomeLevelDialog");
		gameMenu.add(levelCustomMI);

		// set button group
		ButtonGroup levelButtonGroup = new ButtonGroup();
		levelButtonGroup.add(levelBasicMI);
		levelButtonGroup.add(levelIntermediateMI);
		levelButtonGroup.add(levelExpenseMI);
		levelButtonGroup.add(levelCustomMI);
		

		gameMenu.addSeparator();

		toggleQuestionFlagMI = new JCheckBoxMenuItem("标记(?)(M)");
		toggleQuestionFlagMI.setMnemonic('M');
		toggleQuestionFlagMI.setActionCommand("toggleQuestionMarkEnable");
		toggleQuestionFlagMI.setSelected(config.questionMarkEnable);
		gameMenu.add(toggleQuestionFlagMI);
		
		JMenuItem toggleColorMI=new JCheckBoxMenuItem("颜色(L)");
		toggleColorMI.setMnemonic('L');
		toggleColorMI.setActionCommand("toggleColorEnable");
		toggleColorMI.setSelected(config.colorEnable);
		
		gameMenu.add(toggleColorMI);
		
		toggleSoundMI = new JCheckBoxMenuItem("声音(S)");
		toggleSoundMI.setMnemonic('S');
		toggleSoundMI.setActionCommand("toggleSoundEnable");
		toggleSoundMI.setSelected(config.soundEnable);
		gameMenu.add(toggleSoundMI);

		gameMenu.addSeparator();

		sweepHerosListMI = new JMenuItem("扫雷英雄榜(T)...", 'T');
		gameMenu.add(sweepHerosListMI);
		sweepHerosListMI.setActionCommand("showHeroListDialog");

		gameMenu.addSeparator();

		JMenuItem exitMI = new JMenuItem("退出(X)", 'X');
		exitMI.setActionCommand("exitGame");
		gameMenu.add(exitMI);
	}

	private void createHelpMenu() {
		// help menu
		JMenu helpMenu = new JMenu("帮助(H)");
		helpMenu.setMnemonic('H');
		this.add(helpMenu);

		JMenuItem helpMI = new JMenuItem("使用帮助(H)", 'H');
		helpMI.setAccelerator(KeyStroke.getKeyStroke("F1"));
		helpMI.setActionCommand("showHelpDialog");
		helpMenu.add(helpMI);

		helpMenu.addSeparator();

		JMenuItem aboutMI = new JMenuItem("关于扫雷(A)...", 'A');
		aboutMI.setActionCommand("showAboutDialog");
		helpMenu.add(aboutMI);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		Game.dispatchEvent(event.getActionCommand());
	}
}
