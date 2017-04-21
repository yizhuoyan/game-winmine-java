package com.etc.winmin.view;

import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.etc.winmin.model.GameLevel;
import com.etc.winmin.model.GameModel;
import com.etc.winmin.model.MineCell;

public class GameMinePanel extends JPanel implements Observer {

	private MineCell[][] allCells;
	
	public GameMinePanel(GameModel  model) {
		this.setOpaque(false);
		this.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(3, 0, 0, 0),
				BorderFactory.createLoweredSoftBevelBorder()));
			model.addObserver(this);
	}

	@Override
	public void update(Observable o, Object arg) {
			GameModel status=(GameModel)o;
			if(status.allCells!=this.allCells){
				changeLayout(status);
			}
	}
	//根据模型改变布局
	private void changeLayout(GameModel status) {
		this.removeAll();
		GameLevel level=status.currentGameLevel;
		MineCell[][] allCells=status.allCells;
		this.setLayout(new GridLayout(level.maxY, level.maxX));
		MineCellLabel cell=null;
		for (int y = 0; y <allCells.length; y++) {
			for (int x = 0; x <allCells[y].length; x++) {
				cell=new MineCellLabel(allCells[y][x]);
				this.add(cell);
			}
		}
		this.revalidate();
		this.repaint();
		this.allCells=status.allCells;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
