package com.etc.winmin.controller;

import java.lang.reflect.Method;

import com.etc.winmin.model.GameLevel;
import com.etc.winmin.model.GameModel;
import com.etc.winmin.model.GameRecord;
import com.etc.winmin.model.GameRecords;
import com.etc.winmin.model.GameResource;
import com.etc.winmin.model.MineCell;
import com.etc.winmin.view.AboutDialog;
import com.etc.winmin.view.CustomLevelDialog;
import com.etc.winmin.view.GameControlPanel;
import com.etc.winmin.view.HelpDialog;
import com.etc.winmin.view.HeroListDialog;
import com.etc.winmin.view.MainWindow;
import com.etc.winmin.view.MineCellLabel;
import com.etc.winmin.view.RecordBreakerDialog;

/**
 * 控制器
 * 
 */
public class Game {
	//控制器当前实例
	private static Game GAME_INSTANCE;
	// 模型
	public GameModel model;//游戏主模型
	public GameRecords records;//游戏记录模型
	// 视图
	private MainWindow mainWindow; //主窗口视图
	private CustomLevelDialog customLevelDialog; //自定义关卡视图
	private HeroListDialog heroListDialog;//英雄榜视图
	private AboutDialog aboutDialog; //关于对话框视图
	private HelpDialog helpDialog;//帮助对话框视图
	private RecordBreakerDialog recordBreakerDialog;//打破记录视图
	
	/**
	 * 用于Game控制器接收视图或模型发出的事件,然后调用控制器实例完成事件处理
	 * @param eventType 事件类型,对应game中的相应方法
	 * @param args 事件相关参数
	 */
	public static void dispatchEvent(String eventType, Object... args) {
		try {
			//获取参数类型
			Class[] parameterTypes = new Class[args.length];
			for (int i = 0; i < args.length; i++) {
				parameterTypes[i] = args[i].getClass();
			}
			//找到时间类型对应的方法
			Method method = Game.class.getDeclaredMethod(eventType,
					parameterTypes);
			//执行事件处理
			method.invoke(GAME_INSTANCE, args);
		} catch (Exception e) {
			//如果未找到相应方法,忽略事件
			//e.printStackTrace();
		}
	}
	/**
	 * 构造器,完成模型和视图的初始化
	 */
	public Game() {
		//保存当前游戏示例
		GAME_INSTANCE = this;
		//加载对应视图的游戏
		MineCellLabel.loadResource();
		GameControlPanel.loadResource();
		//初始化游戏模型
		model = new GameModel();
		//初始化记录模型
		records=new GameRecords();
		//初始化主窗口视图
		mainWindow = new MainWindow(model);
	}
	/**
	 * 新局
	 */
	public void newGame() {
		//调用模型的新局方法
		model.newGame();
		//更新视图
		mainWindow.pack();
		//如果视图还未显示,调整主窗口视图位置
		if (!mainWindow.isVisible()) {
			mainWindow.setLocationRelativeTo(null);
			mainWindow.setVisible(true);
		}
	}

	public void setBasicLevel() {
		model.newGameWithGameLevel(GameLevel.LEVEL_BASIC);
		mainWindow.pack();
	}

	public void setIntermediateLevel() {
		model.newGameWithGameLevel(GameLevel.LEVEL_INTERMEDIATE);
		mainWindow.pack();
	}

	public void setExpenseLevel() {
		model.newGameWithGameLevel(GameLevel.LEVEL_EXPENSE);
		mainWindow.pack();
	}

	/**
	 * 标记一个格子
	 * @param cell 格子模型
	 */
	public void markACell(MineCell cell) {
		
		model.markACell(cell);
	}

	/**
	 * 将要扫荡某个格子
	 * @param cell
	 */
	public void willSweepACell(MineCell cell) {
		//直接调用模型方法
		model.willSweepACell(cell);
	}
	

	/**
	 * 扫荡格子
	 */
	public void sweepACell() {
		//调用模型方法
		model.sweepACell();
	}
	/**
	 * 批量扫荡格子
	 */
	public void sweepRoundCell(){
		model.sweepRoundCell();
	}
	/**
	 * 将要批量扫荡指定格子周围
	 * @param cell
	 */
	public void willSweepRoundCell(MineCell cell){
		model.willSweepRoundCell(cell);
	}
	/**
	 *切换问号标记功能 
	 */
	public void toggleQuestionMarkEnable() {
		model.questionMarkEnable = !model.questionMarkEnable;
		model.setChanged();
	}
	/**
	 * 切换音乐功能
	 */
	public void toggleSoundEnable() {
		model.soundEnable = !model.soundEnable;
		model.setChanged();
	}
	/**
	 * 切换颜色功能
	 */
	public void toggleColorEnable() {
		model.colorEnable = !model.colorEnable;
		//根据是否启用颜色,切换不同的主题
		if (model.colorEnable) {
			GameResource.setTheme(GameResource.THEME_DEFAULT);
		} else {
			GameResource.setTheme(GameResource.THEME_NO_COLOR);
		}
		//所有视图重新加载资源
		MineCellLabel.loadResource();
		GameControlPanel.loadResource();
		//通知所有视图,模型已更新
		MineCell[][] allCells = model.allCells;
		for (int y = 0; y < allCells.length; y++) {
			for (int x = 0; x < allCells[y].length; x++) {
				allCells[y][x].setChanged();
			}
		}
		model.setChanged();
	}
	/**
	 * 退出游戏
	 */
	public void exitGame() {
		//保存游戏记录
		records.saveRecords();
		System.exit(0);
	}
	
	public void showCustomeLevelDialog() {
		if (customLevelDialog == null) {
			customLevelDialog = new CustomLevelDialog(this.mainWindow);
		}
		customLevelDialog.showView(model.CUSTOME_LEVEL);
	}

	public void setCustomeLevel(GameLevel customLevel) {
		model.newGameWithCustomeLevel(customLevel);
		mainWindow.pack();
	}

	public void showHeroListDialog() {
		if (heroListDialog == null) {
			heroListDialog = new HeroListDialog(this.mainWindow);
		}
		
		heroListDialog.updateView(this.records);
		heroListDialog.setVisible(true);
	}
	public void showHelpDialog() {
		if (helpDialog == null) {
			helpDialog = new HelpDialog(this.mainWindow);
		}
		helpDialog.setVisible(true);
	}

	public void resetRecords() {
		records.resetRecords();
		heroListDialog.updateView(records);
	}

	public void showAboutDialog() {
		if (aboutDialog == null) {
			aboutDialog = new AboutDialog(this.mainWindow);
		}
		aboutDialog.setVisible(true);
	}
	
	public void gameWin() {
		GameLevel currentLevel = model.currentGameLevel;
		GameRecord oldRecord = records.getGameRecordByLevel(currentLevel.no);
		if (model.playTime < oldRecord.playTime) {
			this.showRecordDialog();
		}
	}

	public void saveGameTime(Integer time) {
		this.model.playTime = time;
	}
	public void saveNewRecord(String playerName) {
		GameRecord record = new GameRecord(model.currentGameLevel.no, playerName, model.playTime);
		records.saveNewRecord(record);
		showHeroListDialog();
	}

	private void showRecordDialog() {
		if (recordBreakerDialog == null) {
			recordBreakerDialog = new RecordBreakerDialog();
		}
		recordBreakerDialog.setVisible(true);
	}

}
