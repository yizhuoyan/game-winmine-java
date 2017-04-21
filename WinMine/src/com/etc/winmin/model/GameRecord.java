package com.etc.winmin.model;

import java.io.Serializable;

public class GameRecord implements Serializable {

	private static final long serialVersionUID = 4409646707273573307L;
	public int levelNo;
	public String player;
	public int playTime;

	public GameRecord(int levelNo, String name, int playTime) {
		this.levelNo=levelNo;
		this.player = name;
		this.playTime = playTime;
	}
}
