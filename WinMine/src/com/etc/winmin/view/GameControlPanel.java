package com.etc.winmin.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;

import javax.sound.sampled.Clip;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import resource.R;

import com.etc.winmin.controller.Game;
import com.etc.winmin.model.GameModel;
import com.etc.winmin.model.GameResource;

public class GameControlPanel extends JPanel implements Observer {
	private static final Icon[] NUM_ICONS = new ImageIcon[11];
	private static Icon FACE_SMILE, FACE_SMILE_DOWN, FACE_SWEEP, FACE_WIN, FACE_LOSE;
	private JLabel[] mineAmountLabels;
	private JLabel[] timeCounterLabels;
	private JLabel gameStateButton;

	private int mineAmount = 0;
	private int playTime = 0;
	private boolean colorEnable = true;
	private int gameStatus;

	public GameControlPanel(GameModel model) {
		this.setOpaque(false);
		this.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLoweredSoftBevelBorder(),
				BorderFactory.createEmptyBorder(3, 3, 3, 3)));
		this.setLayout(new BorderLayout());
		this.mineAmountLabels = this.createMineAmountView();
		this.timeCounterLabels = this.createTimeCounterView();
		this.gameStateButton = this.createGameStateButton();
		this.colorEnable = model.colorEnable;
		model.addObserver(this);
	}

	/**
	 * 加载资源
	 */
	public static void loadResource() {
		{
			NUM_ICONS[0] = GameResource.getResource(R.Icon.NUM_0);
			NUM_ICONS[1] = GameResource.getResource(R.Icon.NUM_1);
			NUM_ICONS[2] = GameResource.getResource(R.Icon.NUM_2);
			NUM_ICONS[3] = GameResource.getResource(R.Icon.NUM_3);
			NUM_ICONS[4] = GameResource.getResource(R.Icon.NUM_4);
			NUM_ICONS[5] = GameResource.getResource(R.Icon.NUM_5);
			NUM_ICONS[6] = GameResource.getResource(R.Icon.NUM_6);
			NUM_ICONS[7] = GameResource.getResource(R.Icon.NUM_7);
			NUM_ICONS[8] = GameResource.getResource(R.Icon.NUM_8);
			NUM_ICONS[9] = GameResource.getResource(R.Icon.NUM_9);
			NUM_ICONS[10] = GameResource.getResource(R.Icon.NUM_);
		}
		{
			FACE_LOSE = GameResource.getResource(R.Icon.FACE_LOSE);
			FACE_SWEEP = GameResource.getResource(R.Icon.FACE_SWEEP);
			FACE_WIN = GameResource.getResource(R.Icon.FACE_WIN);
			FACE_SMILE = GameResource.getResource(R.Icon.FACE_SMILE);
			FACE_SMILE_DOWN = GameResource.getResource(R.Icon.FACE_SMILE_DOWN);
		}
	}

	/**
	 * 数据模型改变,视图更新方法
	 */
	@Override
	public void update(Observable o, Object arg) {
		GameModel status = (GameModel) o;
		// 如果颜色配置改变
		if (status.colorEnable != this.colorEnable) {
			this.colorEnable = status.colorEnable;
			this.updateMineLeftView(status.currentMineLeft);
			this.updateTimeView(this.playTime);
		}
		// 如果剩余地雷数量改变
		if (this.mineAmount != status.currentMineLeft) {
			this.updateMineLeftView(status.currentMineLeft);
		}
		// 如果游戏时间改变
		if (this.playTime != status.playTime) {
			this.updateTimeView(status.playTime);
		}
		
		// 如果游戏状态改变
		if (this.gameStatus != status.gameStatus) {
			switch (status.gameStatus) {
			case GameModel.GAME_STATUS_WIN:
				this.gameStateButton.setIcon(FACE_WIN);
				break;
			case GameModel.GAME_STATUS_LOSE:
				this.gameStateButton.setIcon(FACE_LOSE);
				break;
			}
			this.gameStatus = status.gameStatus;
		}else if(status.gameStatus==GameModel.GAME_STATUS_PLAYING){
			// 如果是准备扫荡状态
			if (status.isSweepReady) {
				this.gameStateButton.setIcon(FACE_SWEEP);
			} else {
				this.gameStateButton.setIcon(FACE_SMILE);
			}
		}

	}

	private JLabel[] createMineAmountView() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		panel.setBorder(BorderFactory.createLoweredBevelBorder());
		JLabel[] labels = new JLabel[3];
		for (int i = 0; i < 3; i++) {
			JLabel label = new JLabel(NUM_ICONS[0]);
			panel.add(label);
			labels[i] = label;
		}
		this.add(panel, BorderLayout.WEST);
		return labels;
	}

	private JLabel[] createTimeCounterView() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		panel.setBorder(BorderFactory.createLoweredBevelBorder());
		JLabel[] labels = new JLabel[3];
		for (int i = 0; i < 3; i++) {
			JLabel label = new JLabel(NUM_ICONS[0]);
			panel.add(label);
			labels[i] = label;
		}
		this.add(panel, BorderLayout.EAST);
		return labels;
	}

	private void updateMineLeftView(int amount) {
		if (amount < 0) {
			amount = -amount;
			mineAmountLabels[0].setIcon(NUM_ICONS[10]);// nagitive flag
			mineAmountLabels[1].setIcon(NUM_ICONS[amount / 10]);
			mineAmountLabels[2].setIcon(NUM_ICONS[amount % 10]);
		} else {
			mineAmountLabels[0].setIcon(NUM_ICONS[amount / 100]);
			mineAmountLabels[1].setIcon(NUM_ICONS[amount % 100 / 10]);
			mineAmountLabels[2].setIcon(NUM_ICONS[amount % 10]);
		}
		this.mineAmount = amount;
	}

	public void updateTimeView(int time) {
		timeCounterLabels[0].setIcon(NUM_ICONS[time / 100]);
		timeCounterLabels[1].setIcon(NUM_ICONS[time % 100 / 10]);
		timeCounterLabels[2].setIcon(NUM_ICONS[time % 10]);
		this.playTime = time;

	}

	private JLabel createGameStateButton() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		panel.setOpaque(false);
		final JLabel label = new JLabel(FACE_SMILE);
		label.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				label.setIcon(FACE_SMILE_DOWN);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				label.setIcon(FACE_SMILE);
				Game.dispatchEvent("newGame");
			}
		});
		this.add(panel, BorderLayout.CENTER);
		panel.add(label);
		return label;
	}

}
