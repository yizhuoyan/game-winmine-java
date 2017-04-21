package resource;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import com.etc.winmin.model.GameResource;

public class R {
	private static int beginIndex = 0;
	//图标资源
	public static class Icon {
		public static final int APP = beginIndex++;
		private static int faceIndex = beginIndex;
		public static final int FACE_SMILE_DOWN = beginIndex++,
				FACE_WIN = beginIndex++, FACE_LOSE = beginIndex++,
				FACE_SWEEP = beginIndex++, FACE_SMILE = beginIndex++;
		private static int mineIndex = beginIndex;
		public static final int MINE_UNKNOWN = beginIndex++,
				MINE_MARK_SURE = beginIndex++, MINE_MARK_MAYBE = beginIndex++,
				MINE_EXPLODE = beginIndex++, MINE_MARK_WRONG = beginIndex++,
				MINE_MARK = beginIndex++, MINE_MARK_MAYBE_DOWN = beginIndex++,
				MINE_8 = beginIndex++, MINE_7 = beginIndex++,
				MINE_6 = beginIndex++, MINE_5 = beginIndex++,
				MINE_4 = beginIndex++, MINE_3 = beginIndex++,
				MINE_2 = beginIndex++, MINE_1 = beginIndex++,
				MINE_0 = beginIndex++;
		private static int numIndex = beginIndex;
		public static final int NUM_ = beginIndex++, NUM_NONE = beginIndex++,
				NUM_9 = beginIndex++, NUM_8 = beginIndex++,
				NUM_7 = beginIndex++, NUM_6 = beginIndex++,
				NUM_5 = beginIndex++, NUM_4 = beginIndex++,
				NUM_3 = beginIndex++, NUM_2 = beginIndex++,
				NUM_1 = beginIndex++, NUM_0 = beginIndex++;
	}
	//图片资源
	public static class Image {
		public static final int BG_ABOUT = beginIndex++;
	}
	//声音资源
	public static class Sound {
		public static final int GAME_WIN = beginIndex++,
				GAME_LOSE = beginIndex++, TIME_TICK = beginIndex++;
	}
	//URL资源
	public static class URL{
		public static final int HTML_HELP = beginIndex++;
		
	}
	//资源包
	private final Map<Integer, Object> RESOUCE_MAP = new HashMap<Integer, Object>();
	//资源扫描路径常量
	private final static String SCAN_DIR_IMAGE = "image/",
			SCAN_DIR_SOUND = "sound/", SCAN_DIR_ICON = "icon/",
			SCAN_DIR_HTML = "html/";
	//当前资源对应的主题
	private String theme;
	
	public R(String theme) {
		this.theme = theme;
		this.loadResource();
	}
	public Object getResource(int id){
		return RESOUCE_MAP.get(id);
	}
	//加载所有资源
	private void loadResource() {
		try {
			loadIconResource("face.png", R.Icon.faceIndex,5);
			loadIconResource("mine.png", R.Icon.mineIndex,16);
			loadIconResource("timeno.png", R.Icon.numIndex,12);
			loadImageResource("about.png", R.Image.BG_ABOUT);
			loadIconResource("app.png", R.Icon.APP,1);
			loadSoundResource("game_lose.wav", R.Sound.GAME_LOSE);
			loadSoundResource("game_win.wav", R.Sound.GAME_WIN);
			loadSoundResource("time_tick.wav", R.Sound.TIME_TICK);
			loadURLResource("help.html", R.URL.HTML_HELP);
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}

	private void loadIconResource(String fileName,int beginIndex,int tiles)
			throws Exception {
		BufferedImage image = ImageIO.read(loadURL(this.theme
				+ SCAN_DIR_ICON + fileName));
		
		int tw = image.getWidth();
		int th = image.getHeight()/tiles;
		for (int i = 0; i < tiles; i++) {
			ImageIcon icon = new ImageIcon(image.getSubimage(0, i * th, tw, th));
			RESOUCE_MAP.put(beginIndex + i, icon);
		}
	}

	private void loadImageResource(String fileName, int beginIndex) {
		try {
			BufferedImage image = ImageIO.read(loadURL(this.theme
					+ SCAN_DIR_IMAGE + fileName));
			RESOUCE_MAP.put(beginIndex, image);
		} catch (Exception e) {
		}
	}

	private void loadSoundResource(String fileName, int beginIndex) {
		try {
			java.net.URL url = loadURL(this.theme + SCAN_DIR_SOUND
					+ fileName);
			if (url != null) {
				AudioInputStream audioInputStream = AudioSystem
						.getAudioInputStream(url);
				Clip clip = (Clip) AudioSystem.getLine(new DataLine.Info(Clip.class,
						audioInputStream.getFormat()));
				clip.open(audioInputStream);
				RESOUCE_MAP.put(beginIndex, clip);
			}
		} catch (Exception e) {
		}
	}
	
	
	private void loadURLResource(String fileName,int index){
		java.net.URL url = loadURL(this.theme + SCAN_DIR_HTML
				+ fileName);
		if (url != null) {
			RESOUCE_MAP.put(index, url);
		}
	}
	
	private static java.net.URL loadURL(String path) {
		return R.class.getResource(path);
	}
	
}
