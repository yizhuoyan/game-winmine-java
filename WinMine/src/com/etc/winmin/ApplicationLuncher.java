package com.etc.winmin;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.etc.winmin.controller.Game;

public class ApplicationLuncher {
	public static void main(String[] args) {

		try {
			// 设置窗口风格
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); 
		} catch (Exception e) {
		}
		//在EDT线程中启动游戏
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				//创建游戏控制器对象
				Game game = new Game();
				//调用newGame方法开启游戏
				game.newGame();
			}
		});

	}
}
