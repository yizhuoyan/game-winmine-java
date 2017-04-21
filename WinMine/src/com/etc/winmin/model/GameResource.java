package com.etc.winmin.model;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.Clip;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import resource.R;

public class GameResource {
	//定义游戏主题,对应的是主题资源的路径
	public static final String THEME_NO_COLOR = "/resource/nocolor/",
			THEME_DEFAULT = "/resource/default/";
	//定义默认游戏主题资源
	private static final R DEFAULT_RESOURCE = new R(THEME_DEFAULT);
	//定义当前游戏主题资源
	private static R CURRENT_RESOURCE = DEFAULT_RESOURCE;
	//缓存主题和资源
	private final static Map<String, R> RESOURCE_MAP = new HashMap<String, R>();
	/**
	 * 改变游戏资源主题
	 * @param theme
	 */
	public static final void setTheme(String theme) {
		R resource = null;
		//如果设置主题为默认主题
		if (THEME_DEFAULT.equals(theme)) {
			CURRENT_RESOURCE = RESOURCE_MAP.get(THEME_DEFAULT);
			//如果默认资源未加载,则加载默认资源包
			if (resource == null) {
				resource = new R(THEME_DEFAULT);
				RESOURCE_MAP.put(THEME_DEFAULT, resource);
			}
		} else {
			//如果当前主题资源未加载,则加载
			resource = RESOURCE_MAP.get(THEME_NO_COLOR);
			if (resource == null) {
				resource = new R(THEME_NO_COLOR);
				RESOURCE_MAP.put(THEME_NO_COLOR, resource);
			}
		}
		//设置当前资源包
		CURRENT_RESOURCE = resource;
	}
	/**
	 * 获取游戏资源的核心方法
	 * @param id 资源id 对应R类的静态变量
	 * @return
	 */
	public static <T> T getResource(int id) {
		//调用当前主题对应资源包的方法来获取资源
		Object r = CURRENT_RESOURCE.getResource(id);
		//如果当前主题对应资源包无此资源,则使用默认资源包重新获取一次
		if (r == null && CURRENT_RESOURCE != DEFAULT_RESOURCE) {
			return (T) DEFAULT_RESOURCE.getResource(id);
		}
		//然后资源
		return (T) r;
	}
	/**
	 * 获取游戏图标资源的简便方法
	 * @param id
	 * @return
	 */
	public static ImageIcon getIcon(int id) {
		return (ImageIcon) getResource(id);
	}
	/**
	 * 获取游戏图片资源的简便方法
	 * @param id
	 * @return
	 */
	public static BufferedImage getImage(int id) {
		return (BufferedImage) getResource(id);
	}
	/**
	 * 获取游戏声音资源的简便方法
	 * @param id
	 * @return
	 */
	public static Clip getSound(int id) {
		return (Clip) getResource(id);
	}
	/**
	 * 获取游戏URL资源的简便方法
	 * @param id
	 * @return
	 */
	public static URL getURL(int id) {
		return (URL) getResource(id);
	}

}
