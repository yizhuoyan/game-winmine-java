package com.etc.winmin.model;

import java.util.Arrays;
import java.util.Observable;

public class MineCell extends Observable {
	//格子标记状态常量
	public static final int MARK_NO=0, MARK_MAYBE = 1,MARK_SURE = 2,MARK_IS_A_MINE=3,MARK_WRONG_MARK=4;
	//格子状态常量
	public static final int STATUS_UNSWEEP = 1,STATUS_WILLSWEEP=2, STATUS_SWEEPED = 3;
	//格子在整个格子中的x,y坐标
	public final int x, y;
	//格子是否埋了地雷
	public boolean hasMine;
	//格子处于冻结状态
	public boolean freezed;
	//格子当前状态,默认为未扫荡
	public int status=STATUS_UNSWEEP;
	//格子标记状态,默认未无标记
	public int mark=MARK_NO;
	//如果当前格子无地雷,则格子周围地雷数
	public int roundMineAmount;

	/**
	 * 格子构造器
	 * @param x 格子x坐标
	 * @param y 格子y坐标
	 */
	public MineCell(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public void setChanged() {
		super.setChanged();
		super.notifyObservers();
	}
}
