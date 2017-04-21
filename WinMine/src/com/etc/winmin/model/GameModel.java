package com.etc.winmin.model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Random;

import javax.sound.sampled.Clip;
import javax.swing.Timer;

import resource.R;

import com.etc.winmin.controller.Game;

public class GameModel extends Observable implements ActionListener {
	// 游戏状态常量
	public static final int GAME_STATUS_NOT_READY = -1, GAME_STATUS_READY = 0,
			GAME_STATUS_BEGIN = 1, GAME_STATUS_PLAYING = 3,
			GAME_STATUS_LOSE = 99, GAME_STATUS_WIN = 100;
	// 随机数帮助类,用于随机生成地雷位置
	private static final Random RANDOM = new Random();
	private final Timer GAME_TIMER = new Timer(1000, this);
	{
		GAME_TIMER.setInitialDelay(0);
	}

	// 游戏当前自定义关卡对象
	public final GameLevel CUSTOME_LEVEL = GameLevel.LEVEL_CUSTOM;
	// 游戏配置,颜色,问号标记,声音
	public boolean colorEnable, questionMarkEnable, soundEnable;

	// 游戏当前关卡
	public GameLevel currentGameLevel;
	// 游戏当前关卡所有格子模型
	public MineCell[][] allCells;
	// 游戏当前关卡所有地雷格子模型
	public MineCell[] mineC1ells;
	// 游戏当前关卡剩余地雷数
	public int currentMineLeft;
	// 游戏当前关卡已完时间
	public int playTime;
	// 游戏当前关卡游戏状态
	public int gameStatus;
	// 游戏当前关卡是否准备扫荡地雷
	public boolean isSweepReady;
	// 游戏当前关卡当前将要扫荡的格子
	private MineCell willSweepCell;

	//构造器
	public GameModel() {
		{
			//初始化设置
			this.colorEnable = true;
			this.soundEnable = true;
			this.questionMarkEnable = true;
		}
		//游戏状态为未准备好
		this.gameStatus = GAME_STATUS_NOT_READY;

	}

	/**
	 * 标记一个格子模型
	 * 
	 * @param cell
	 */
	public void markACell(MineCell cell) {
		//如果是第一次标记,改变游戏状态
		if (this.gameStatus == GameModel.GAME_STATUS_READY) {
			this.gameStatus = GAME_STATUS_BEGIN;
			
		}
		//如果格子不能被标志
		if (cell.status != MineCell.STATUS_UNSWEEP) {
			return;
		}
		
		switch (cell.mark) {
		//如果无标记
		case MineCell.MARK_NO:
			//状态改为标记
			cell.mark = MineCell.MARK_SURE;
			//当前地雷数-1
			this.currentMineLeft--;
			break;
		//如果已标记为地雷	
		case MineCell.MARK_SURE:
			//如果问号启用,则标记为问号
			if (this.questionMarkEnable) {
				cell.mark = MineCell.MARK_MAYBE;
			} else {
				//否则,取消标记
				cell.mark = MineCell.MARK_NO;
			}
			//不管怎样,地雷数+1
			this.currentMineLeft++;
			break;
		//如果已标记为可能有地雷	
		case MineCell.MARK_MAYBE:
			//则取消标记
			cell.mark = MineCell.MARK_NO;
			break;
		}
		//通知格子视图改变
		cell.setChanged();
		//通知其他视图更新
		this.setChanged();
	}

	/**
	 * 新局
	 */
	public void newGame() {
		// 如果游戏状态不是已准备好,则重新布局
		if (this.gameStatus != GameModel.GAME_STATUS_READY) {
			// 如果还未设定当前关卡,则设置为初级关卡
			if (this.currentGameLevel == null) {
				this.currentGameLevel = GameLevel.LEVEL_BASIC;
			}
			// 停止定时器
			this.GAME_TIMER.stop();
			// 加载初级关卡相应数据
			this.loadGameLevel(currentGameLevel);
			// 通知视图数据模型已改变
			this.setChanged();
			// 游戏状态变为已准备好
			this.gameStatus = GAME_STATUS_READY;
		}
	}

	public void newGameWithCustomeLevel(GameLevel customeLevel) {
		if (customeLevel.maxX > GameLevel.MAX_X) {
			customeLevel.maxX = GameLevel.MAX_X;
		}
		if (customeLevel.maxX < GameLevel.MIN_X) {
			customeLevel.maxX = GameLevel.MIN_X;
		}
		if (customeLevel.maxY > GameLevel.MAX_Y) {
			customeLevel.maxY = GameLevel.MAX_Y;
		}
		if (customeLevel.maxY < GameLevel.MIN_Y) {
			customeLevel.maxY = GameLevel.MIN_Y;
		}

		if (customeLevel.mineAmount > GameLevel.MAX_AMOUNT) {
			customeLevel.mineAmount = GameLevel.MAX_AMOUNT;
		}
		if (customeLevel.mineAmount < GameLevel.MIN_AMOUNT) {
			customeLevel.mineAmount = GameLevel.MIN_AMOUNT;
		}
		this.CUSTOME_LEVEL.maxX = customeLevel.maxX;
		this.CUSTOME_LEVEL.maxY = customeLevel.maxY;
		this.CUSTOME_LEVEL.mineAmount = customeLevel.mineAmount;
		newGameWithGameLevel(this.CUSTOME_LEVEL);

	}

	public void newGameWithGameLevel(GameLevel level) {
		this.gameStatus = GAME_STATUS_NOT_READY;
		this.currentGameLevel = level;
		this.newGame();
	}

	@Override
	public void setChanged() {
		super.setChanged();
		super.notifyObservers();
	}

	/**
	 * 扫荡格子
	 */
	public void sweepACell() {
		// 如果是第一次扫荡,这游戏进入游戏中状态
		if (this.gameStatus == GameModel.GAME_STATUS_BEGIN) {
			GAME_TIMER.restart();// 重启定时器
			// 改变游戏状态
			this.gameStatus = GameModel.GAME_STATUS_PLAYING;
		}
		// 设置扫荡状态为false
		this.isSweepReady = false;
		// 如果将要扫荡的格子为空,则忽略此次事件
		if (this.willSweepCell != null) {
			int status = sweepACellAt(this.willSweepCell);
			if (status == GAME_STATUS_LOSE) {
				// 进入游戏结束逻辑
				this.gameOver();
			} else if (status == GAME_STATUS_WIN) {
				// 进入游戏胜利逻辑
				this.gameWin();
			} else {
				// 通知视图更新
				this.setChanged();
			}
			// 清空将扫荡格子
			this.willSweepCell = null;
		} else {
			// 通知视图更新
			this.setChanged();
		}

	}
	/**
	 * 批量扫荡周围地雷
	 */
	public void sweepRoundCell() {
		//如果是第一扫荡,改变游戏状态
		if (this.gameStatus == GameModel.GAME_STATUS_BEGIN) {
			GAME_TIMER.restart();
			this.gameStatus = GameModel.GAME_STATUS_PLAYING;
		}
		//调用实际的扫荡逻辑
		int staus = sweepRoundCellAt(this.willSweepCell);
		//根据扫荡的结果进行相应处理
		switch (staus) {
		case GAME_STATUS_PLAYING://继续游戏
			this.isSweepReady = false;
			this.willSweepCell = null;
			this.setChanged();//通知视图更新
			break;
		case GAME_STATUS_LOSE://游戏失败
			this.gameOver();//进人游戏逻辑
			break;
		case GAME_STATUS_WIN:
			this.gameWin();//游戏胜利,进行游戏胜利逻辑
			break;
		}
	}


	/**
	 * 将要扫荡指定格子
	 * 
	 * @param cell
	 *            格子模型
	 */
	public void willSweepACell(MineCell cell) {
		// 如果是第一个按下,改变游戏状态
		if (this.gameStatus == GameModel.GAME_STATUS_READY) {
			this.gameStatus = GameModel.GAME_STATUS_BEGIN;
		}
		// 如果之前已选择某个格子,则将之前的格子复原
		if (this.willSweepCell != null) {
			willSweepCell.status = MineCell.STATUS_UNSWEEP;
			willSweepCell.setChanged();
		}
		// 判断格子是否可以扫荡（仅有未扫荡状态的格子而且还未标记为地雷的格子可以扫荡）
		if (cell.status == MineCell.STATUS_UNSWEEP
				&& cell.mark != MineCell.MARK_SURE) {
			// 改变格子的状态为将扫荡
			cell.status = MineCell.STATUS_WILLSWEEP;
			// 保存当前格子为将要扫荡格子
			this.willSweepCell = cell;
			// 通知视图更新
			cell.setChanged();
		} else {
			// 格子不可以扫荡,清空将要扫荡格子变量
			this.willSweepCell = null;
		}
		// 游戏扫荡状态为true
		this.isSweepReady = true;
		// 通知视图更新
		this.setChanged();
	}

	/**
	 * 对指定格子和其周围进行复原,即取消将要扫荡状态
	 * 
	 * @param cell
	 */
	private void cancelWillSweepRoundCell(MineCell cell) {
		MineCell[] rounds = this.getRoundCellAt(cell);
		for (MineCell rCell : rounds) {
			if (rCell != null) {
				if (rCell.status == MineCell.STATUS_WILLSWEEP) {
					rCell.status = MineCell.STATUS_UNSWEEP;
					rCell.setChanged();
				}
			}
		}
		if (cell.status == MineCell.STATUS_WILLSWEEP) {
			cell.status = MineCell.STATUS_UNSWEEP;
			cell.setChanged();
		}
	}

	/**
	 * 准备批量扫荡某个格子周围的地雷
	 * 
	 * @param cell
	 */
	public void willSweepRoundCell(MineCell cell) {
		// 如果是第一次左右双键按下,改变游戏状态
		if (this.gameStatus == GameModel.GAME_STATUS_READY) {
			this.gameStatus = GameModel.GAME_STATUS_BEGIN;
		}
		// 如果是之前已准备扫荡某个格子周围,则把其复原
		if (this.willSweepCell != null) {
			cancelWillSweepRoundCell(this.willSweepCell);
		}
		// 处理当前要准备扫荡的格子
		// 如果当前格子可以扫荡,则把当前格子变为将要扫荡状态
		if (cell.status == MineCell.STATUS_UNSWEEP
				|| (cell.status == MineCell.STATUS_SWEEPED && cell.roundMineAmount != 0)) {
			// 把其周围可以扫荡的格子变为将要扫荡状态
			MineCell[] rounds = this.getRoundCellAt(cell);
			for (MineCell rCell : rounds) {
				if (rCell != null && rCell.status == MineCell.STATUS_UNSWEEP
						&& rCell.mark != MineCell.MARK_SURE) {
					rCell.status = MineCell.STATUS_WILLSWEEP;
					rCell.setChanged();
				}
			}
			// 如果当前格子可以扫荡,则同时改变当前格子
			if (cell.status == MineCell.STATUS_UNSWEEP
					&& cell.mark != MineCell.MARK_SURE) {
				cell.status = MineCell.STATUS_WILLSWEEP;
				cell.setChanged();
			}
		}
		this.isSweepReady = true;
		this.willSweepCell = cell;
		this.setChanged();
	}

	/**
	 * 游戏失败逻辑实现
	 */
	private void gameOver() {
		// 遍历所有格子
		MineCell[][] allCells = this.allCells;
		MineCell[] rowCells = null;
		MineCell cell = null;
		for (int i = allCells.length; i-- > 0;) {
			rowCells = allCells[i];
			for (int j = rowCells.length; j-- > 0;) {
				cell = rowCells[j];
				// 冻结所有格子
				cell.freezed = true;
				// 如果格子有地雷,则标记为地雷
				if (cell.hasMine) {
					cell.mark = MineCell.MARK_IS_A_MINE;
				} else {
					// 如果无地雷却标记为有地雷,则标记格子为错误标记地雷
					if (cell.mark == MineCell.MARK_SURE) {
						cell.mark = MineCell.MARK_WRONG_MARK;
					}
				}
				// 通知所有格子视图更新
				cell.setChanged();
			}
		}
		// 改变游戏状态为失败
		this.gameStatus = GameModel.GAME_STATUS_LOSE;
		// 停止定时器
		GAME_TIMER.stop();
		// 通知视图更新
		this.setChanged();
		// 播放音效
		playSound((Clip) GameResource.getResource(R.Sound.GAME_LOSE));
	}

	/**
	 * 游戏胜利逻辑
	 */
	private void gameWin() {
		// 遍历所有格子
		MineCell[][] allCells = this.allCells;
		MineCell[] rowCells = null;
		MineCell cell = null;
		for (int i = allCells.length; i-- > 0;) {
			rowCells = allCells[i];
			for (int j = rowCells.length; j-- > 0;) {
				cell = rowCells[j];
				// 冻结所有格子
				cell.freezed = true;
				// 如果格子含有地雷,则标记
				if (cell.hasMine) {
					cell.mark = MineCell.MARK_SURE;
				}
				// 通知视图已改变
				cell.setChanged();
			}
		}
		// 改变游戏状态为胜利
		this.gameStatus = GameModel.GAME_STATUS_WIN;
		// 停止定时器
		GAME_TIMER.stop();
		// 通知视图更新
		this.setChanged();
		// 播放音效
		playSound((Clip) GameResource.getResource(R.Sound.GAME_WIN));
		// 发出游戏胜利时间,便于记录模型判断是否游戏胜利
		Game.dispatchEvent("gameWin");
	}

	private MineCell getCellAt(int x, int y) {
		GameLevel level = this.currentGameLevel;
		if (x < 0 || x >= level.maxX || y < 0 || y >= level.maxY)
			return null;
		return allCells[y][x];
	}

	private MineCell[] getRoundCellAt(MineCell cell) {
		int x = cell.x;
		int y = cell.y;
		MineCell[] roundCells = new MineCell[8];
		roundCells[0] = this.getCellAt(x, y - 1);
		roundCells[1] = this.getCellAt(x + 1, y - 1);
		roundCells[2] = this.getCellAt(x + 1, y);
		roundCells[3] = this.getCellAt(x + 1, y + 1);
		roundCells[4] = this.getCellAt(x, y + 1);
		roundCells[5] = this.getCellAt(x - 1, y + 1);
		roundCells[6] = this.getCellAt(x - 1, y);
		roundCells[7] = this.getCellAt(x - 1, y - 1);
		return roundCells;
	}

	/**
	 * 加载指定关卡的数据
	 * 
	 * @param gameLevel
	 *            游戏关卡
	 */
	private void loadGameLevel(GameLevel gameLevel) {
		// 根据关卡信息,创建所有格子数据
		MineCell[][] allCells = new MineCell[gameLevel.maxY][gameLevel.maxX];
		for (int y = 0; y < allCells.length; y++) {
			for (int x = 0; x < allCells[y].length; x++) {
				allCells[y][x] = new MineCell(x, y);
			}
		}
		// 创建随机地雷
		int mineAmount = gameLevel.mineAmount;
		int rx, ry;
		for (int i = 0; i < mineAmount;) {
			rx = RANDOM.nextInt(gameLevel.maxX);
			ry = RANDOM.nextInt(gameLevel.maxY);
			if (allCells[ry][rx].hasMine) {
				continue;
			} else {
				allCells[ry][rx].hasMine = true;
				i++;
			}
		}
		// 设置当前关卡相关数据
		this.currentGameLevel = gameLevel;
		this.allCells = allCells;
		this.currentMineLeft = mineAmount;
		this.playTime = 0;
	}

	/**
	 * 工具方法,播放音效
	 * @param clip
	 */
	private void playSound(Clip clip) {
		if (this.soundEnable) {
			clip.setFramePosition(0);
			clip.start();
		}
	}

	/**
	 * 扫荡指定格子
	 * 
	 * @param cell
	 * @return 返回游戏状态,胜利,失败,游戏中
	 */
	private int sweepACellAt(MineCell cell) {
		// 保存静态变量,提高效率
		final int SWEEPED = MineCell.STATUS_SWEEPED;
		try {
			// 改变格子状态为已扫荡
			cell.status = SWEEPED;
			// 如果有地雷,则返回游戏失败
			if (cell.hasMine) {
				return GAME_STATUS_LOSE;
			} else {
				// 无地雷,计算周围地雷数
				int roundCount = 0;
				MineCell[] roundCells = this.getRoundCellAt(cell);
				for (MineCell rCell : roundCells) {
					if (rCell != null && rCell.hasMine) {
						roundCount++;
					}
				}
				cell.roundMineAmount = roundCount;
				// 如果周围地雷数为0,表示周围无地雷,把其周边检查一遍
				if (roundCount == 0) {
					for (MineCell rCell : roundCells) {
						if (rCell != null && rCell.status != SWEEPED) {
							// 递归扫雷
							sweepACellAt(rCell);
						}
					}
				}
				// 完毕后,检查是否游戏胜利,即是否所有不是地雷的格子都已被扫荡
				MineCell cel = null;
				for (int i = allCells.length; i-- > 0;) {
					for (int j = allCells[i].length; j-- > 0;) {
						cel = allCells[i][j];
						if ((!cel.hasMine) && cel.status != SWEEPED) {
							// 只有其中1个不是,则游戏继续
							return GAME_STATUS_PLAYING;
						}
					}
				}
				// 所有都被扫荡,游戏胜利
				return GAME_STATUS_WIN;
			}
		} finally {
			// 最后,不管怎样,通知格子视图更新
			cell.setChanged();
		}

	}

	/**
	 * 批量扫荡指定格子周围格子
	 * 
	 * @param cell
	 * @return 扫荡后游戏状态,成功,失败,继续游戏？
	 */
	private int sweepRoundCellAt(MineCell cell) {
		//获取格子周围的格子
		MineCell[] rounds = this.getRoundCellAt(cell);
		// 先全部复原
		this.cancelWillSweepRoundCell(cell);
		// 如果当前格子已扫荡,且周围地雷数不为0,则表示可以扫荡
		if (cell.status == MineCell.STATUS_SWEEPED && cell.roundMineAmount != 0) {
			// 判断格子周围标记的地雷数量是否和实际数量吻合,否则忽略此事件
			int alreadySure = 0;
			for (MineCell rCell : rounds) {
				if (rCell != null && rCell.mark == MineCell.MARK_SURE) {
					alreadySure++;
				}
			}
			// 相同,进行批量扫荡其周围
			if (alreadySure == cell.roundMineAmount) {
				for (MineCell rCell : rounds) {
					if (rCell != null
							&& rCell.status == MineCell.STATUS_UNSWEEP
							&& rCell.mark != MineCell.MARK_SURE) {
						// 调用扫荡逻辑
						int status = this.sweepACellAt(rCell);
						// 如果其中一个是失败或已经胜利,则停止扫荡
						if (status != GAME_STATUS_PLAYING) {
							return status;
						}
					}
				}
			}
		}
		// 其余情况,继续游戏
		return GAME_STATUS_PLAYING;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (this.playTime == 999) {
			this.gameOver();
			return;
		}
		if (this.soundEnable) {
			playSound((Clip) GameResource.getResource(R.Sound.TIME_TICK));
		}
		this.playTime++;
		this.setChanged();
	}

}
