package com.etc.winmin.model;

import java.io.Serializable;
import java.util.Observable;

/**
 * 游戏关卡数据模型
 * 
 */
public class GameLevel implements Serializable {
	private static final long serialVersionUID = 977431119185358265L;
	// 关卡号常量
	public static final int CUSTOM = 0, BASIC = 1, INTERMEDIATE = 2,
			EXPENSE = 3;
	// 游戏关卡默认数据
	public static final GameLevel LEVEL_BASIC = new GameLevel(GameLevel.BASIC,
			"初级", 10, 9, 9), LEVEL_CUSTOM = new GameLevel(GameLevel.CUSTOM,
			"自定义", 40, 16, 16), LEVEL_EXPENSE = new GameLevel(
			GameLevel.EXPENSE, "高级", 99, 36, 16),
			LEVEL_INTERMEDIATE = new GameLevel(GameLevel.INTERMEDIATE, "中级",
					40, 16, 16);

	// 自定义关卡最大,最小值
	public static final int MAX_X = 30, MAX_Y = 24, MAX_AMOUNT = 667,
			MIN_X = 9, MIN_Y = 9, MIN_AMOUNT = 10;
	
	//关卡默认地雷数
	public int mineAmount;
	//关卡格子最大宽度和高度
	public int maxX, maxY;
	//关卡号
	public int no;
	//关卡名称
	public String name;

	public GameLevel(int level, String name, int mineAmount, int maxX, int maxY) {
		this.mineAmount = mineAmount;
		this.maxX = maxX;
		this.maxY = maxY;
		this.no = level;
		this.name = name;
	}

	public GameLevel() {
		this.name = null;
	}

	public static final GameLevel getGameLevelByNo(int no) {
		switch (no) {
		case GameLevel.BASIC:
			return GameLevel.LEVEL_BASIC;
		case GameLevel.INTERMEDIATE:
			return GameLevel.LEVEL_INTERMEDIATE;
		case GameLevel.EXPENSE:
			return GameLevel.LEVEL_EXPENSE;
		}
		return GameLevel.LEVEL_CUSTOM;
	}
}
