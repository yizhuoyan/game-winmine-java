package com.etc.winmin.view;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Icon;
import javax.swing.JLabel;

import resource.R;

import com.etc.winmin.controller.Game;
import com.etc.winmin.model.GameResource;
import com.etc.winmin.model.MineCell;

public class MineCellLabel extends JLabel implements MouseListener, Observer {
	private static final Icon[] MINEAMOUNT_ICONS = new Icon[9];
	private static Icon ICON_MARK_SURE, ICON_MARK_MAYBE, ICON_MINE_MAYBE_SWEEP, ICON_MINE_UNKNOWN_SWEEP,
			ICON_MINE_UNKNOWN, ICON_MARK_MINE, ICON_MINE_EXPLODE, ICOM_MARK_WRONG_MARK;

	public static void loadResource() {
		{
			MINEAMOUNT_ICONS[0] = GameResource.getResource(R.Icon.MINE_0);
			MINEAMOUNT_ICONS[1] = GameResource.getResource(R.Icon.MINE_1);
			MINEAMOUNT_ICONS[2] = GameResource.getResource(R.Icon.MINE_2);
			MINEAMOUNT_ICONS[3] = GameResource.getResource(R.Icon.MINE_3);
			MINEAMOUNT_ICONS[4] = GameResource.getResource(R.Icon.MINE_4);
			MINEAMOUNT_ICONS[5] = GameResource.getResource(R.Icon.MINE_5);
			MINEAMOUNT_ICONS[6] = GameResource.getResource(R.Icon.MINE_6);
			MINEAMOUNT_ICONS[7] = GameResource.getResource(R.Icon.MINE_7);
			MINEAMOUNT_ICONS[8] = GameResource.getResource(R.Icon.MINE_8);
		}
		{
			ICON_MARK_MINE = GameResource.getResource(R.Icon.MINE_MARK);
			ICON_MINE_EXPLODE = GameResource.getResource(R.Icon.MINE_EXPLODE);
			ICOM_MARK_WRONG_MARK = GameResource.getResource(R.Icon.MINE_MARK_WRONG);
			ICON_MARK_SURE = GameResource.getResource(R.Icon.MINE_MARK_SURE);
			ICON_MARK_MAYBE = GameResource.getResource(R.Icon.MINE_MARK_MAYBE);
			ICON_MINE_MAYBE_SWEEP = GameResource.getResource(R.Icon.MINE_MARK_MAYBE_DOWN);
			ICON_MINE_UNKNOWN = GameResource.getResource(R.Icon.MINE_UNKNOWN);
			ICON_MINE_UNKNOWN_SWEEP = GameResource.getResource(R.Icon.MINE_0);

		}
	}

	public final MineCell model;


	public MineCellLabel(MineCell model) {
		this.model = model;
		this.setIcon(ICON_MINE_UNKNOWN);
		model.addObserver(this);
		this.addMouseListener(this);
	}

	@Override
	public void update(Observable o, Object arg) {
		MineCell cell = (MineCell) o;
		switch (cell.status) {
		//未扫荡
		case MineCell.STATUS_UNSWEEP:
			switch (cell.mark) {//标记状态
			case MineCell.MARK_NO://无标记
				this.setIcon(ICON_MINE_UNKNOWN);
				break;
			case MineCell.MARK_MAYBE://可能是
				this.setIcon(ICON_MARK_MAYBE);
				break;
			case MineCell.MARK_SURE://确定是
				this.setIcon(ICON_MARK_SURE);
				break;
			case MineCell.MARK_IS_A_MINE://是地雷
				this.setIcon(ICON_MARK_MINE);
				break;
			case MineCell.MARK_WRONG_MARK://错误标记地雷
				this.setIcon(ICOM_MARK_WRONG_MARK);
				break;
			}
			break;
		case MineCell.STATUS_WILLSWEEP: //将扫荡
			if (cell.mark == MineCell.MARK_MAYBE) {//如果已标记为可能,变为可能标记按下状态
				this.setIcon(ICON_MINE_MAYBE_SWEEP);
			} else if (cell.mark == MineCell.MARK_NO) {//如果无标记,变为按下状态
				this.setIcon(ICON_MINE_UNKNOWN_SWEEP);
			}//已标记不能被扫荡
			break;
		case MineCell.STATUS_SWEEPED://已扫荡
			if (cell.hasMine) {//如果有地雷,则爆炸
				this.setIcon(ICON_MINE_EXPLODE);
			} else {//否则,显示其周围地雷数
				this.setIcon(MINEAMOUNT_ICONS[cell.roundMineAmount]);
			}
			break;
		}
		//如果是冻结状态,则删除鼠标监听
		if (cell.freezed) {
			this.removeMouseListener(this);
		}
	}

	@Override
	public void setIcon(Icon icon) {
		if (this.getIcon() != icon) {
			super.setIcon(icon);
		}
	}

	private static long lastMousePressedTime = 0;
	private static boolean mouse1And3DoublePressed = false;
	private static boolean mouse1Pressed = false;
	private static boolean mouse3Pressed = false;
	@Override
	public void mouseClicked(MouseEvent e) {
	}
	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {//当前是左键按下
			if (mouse3Pressed) {//如果右键已经按下
				//判断时间间隔是否小于500ms,如果是,则为双键同时按下,否则仅为左键按下
				if (System.currentTimeMillis() - lastMousePressedTime < 500) {
					//设置左右双键同时按下
					mouse1And3DoublePressed = true;
					//如果双键同时按下,则忽略单键按下
					mouse1Pressed = false;
					mouse3Pressed = false;
					//发出双键同时按下事件
					Game.dispatchEvent("willSweepRoundCell", this.model);
				}
			} else {
				mouse1Pressed = true;//左键按下
				//发出将要扫雷的事件
				Game.dispatchEvent("willSweepACell", this.model);
			}
		} else if(e.getButton() == MouseEvent.BUTTON3){//当前是右键按下
			if (mouse1Pressed) {//如果左键已经按下
				//判断时间间隔是否小于500ms,如果是,则为双键同时按下,否则仅为右键按下
				if (System.currentTimeMillis() - lastMousePressedTime < 500) {
					//设置左右双键同时按下
					mouse1And3DoublePressed = true;
					//如果双键同时按下,则忽略单键按下
					mouse1Pressed = false;
					mouse3Pressed = false;
					//发出双键同时按下事件
					Game.dispatchEvent("willSweepRoundCell", this.model);
				}
			} else 
				mouse3Pressed = true;//右键按下,当前无事件发出
		}
		//记录按下时间
		lastMousePressedTime = System.currentTimeMillis();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		//不管当前是那个键先释放,如果双键已同时按下,则表示双键已释放
		if (mouse1And3DoublePressed) {
			//发出双键扫荡周围事件
			Game.dispatchEvent("sweepRoundCell");
			//取消设置双键同时按下
			mouse1And3DoublePressed = false;
		} else {
			//如果左键已按下,且当前未左键
			if (mouse1Pressed&&e.getButton() == MouseEvent.BUTTON1) {
					Game.dispatchEvent("sweepACell");
					mouse1Pressed = false;
			//如果右键已按下,且当前为右键		
			} else if (mouse3Pressed&&e.getButton() == MouseEvent.BUTTON3) {
					Game.dispatchEvent("markACell", this.model);
					mouse3Pressed=false;
			}
		}

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		//左右双键同时按下进入
		if (mouse1And3DoublePressed) {
			//发出将要扫荡周围的事件
			Game.dispatchEvent("willSweepRoundCell", this.model);
		} else {
			//仅在鼠标左键按下的情况才发出事件
			if (mouse1Pressed) {
				Game.dispatchEvent("willSweepACell", this.model);
			}
		}

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

}
