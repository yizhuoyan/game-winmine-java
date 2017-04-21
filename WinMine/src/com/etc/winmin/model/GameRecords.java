package com.etc.winmin.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Observable;

public class GameRecords extends Observable {
	private static final File recordFile = createRecordFile();
	public GameRecord basicLevelRecord;
	public GameRecord intermediateLevelRecord;
	public GameRecord expenseLevelRecord;

	private static File createRecordFile() {
		try {
			return new File(System.getenv("TEMP"),"sweepGame.record");
		} catch (Exception e) {
		}
		return new File("sweepGame.record");
	}

	public GameRecords() {
		this.loadGameRecords();
	}

	public void resetRecords() {
		basicLevelRecord = new GameRecord(GameLevel.BASIC, "匿名", 999);
		intermediateLevelRecord = new GameRecord(GameLevel.INTERMEDIATE, "匿名", 999);
		expenseLevelRecord = new GameRecord(GameLevel.EXPENSE, "匿名", 999);
	}

	private void loadGameRecords() {
		ObjectInputStream in = null;
		try {
			in = new ObjectInputStream(new FileInputStream(recordFile));
			this.basicLevelRecord = (GameRecord) in.readObject();
			this.intermediateLevelRecord = (GameRecord) in.readObject();
			this.expenseLevelRecord = (GameRecord) in.readObject();
		} catch (Exception e) {
			this.resetRecords();
		} finally {
			try {
				in.close();
			} catch (Exception e) {
			}
		}
	}

	public void saveRecords() {
		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(new FileOutputStream(recordFile));
			out.writeObject(this.basicLevelRecord);
			out.writeObject(intermediateLevelRecord);
			out.writeObject(expenseLevelRecord);
		} catch (Exception e) {
			e.printStackTrace();
			//do noting
		} finally {
			try {
				out.close();
			} catch (Exception e) {
			}
		}
	}

	public void saveNewRecord(GameRecord record) {
		switch (record.levelNo) {
		case GameLevel.BASIC:
			this.basicLevelRecord = record;
			break;
		case GameLevel.INTERMEDIATE:
			this.intermediateLevelRecord = record;
			break;
		case GameLevel.EXPENSE:
			this.expenseLevelRecord = record;
			break;
		}
	}
	public GameRecord getGameRecordByLevel(int no){
		switch (no) {
		case GameLevel.BASIC:
			return this.basicLevelRecord;
		case GameLevel.INTERMEDIATE:
			return intermediateLevelRecord;
		case GameLevel.EXPENSE:
			return expenseLevelRecord ;
		}
		return null;
	}
}
