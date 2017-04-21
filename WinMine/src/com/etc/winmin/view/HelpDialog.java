package com.etc.winmin.view;

import java.awt.Rectangle;
import java.io.IOException;
import java.net.URL;

import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import resource.R;

import com.etc.winmin.model.GameResource;

public class HelpDialog extends JDialog{

	
	public HelpDialog(JFrame window) {
		super(window, "游戏帮助");
		this.setResizable(true);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		createLayout();
		Rectangle bounds=window.getBounds();
		bounds.x+=bounds.width;
		bounds.width=400;
		bounds.height=400;
		this.setBounds(bounds);
			
	}
	private void createLayout(){
		JEditorPane editorPane=null;
		try {
			editorPane=new JEditorPane(GameResource.getURL(R.URL.HTML_HELP));
		} catch (IOException e) {
			editorPane=new JEditorPane("text/html", "<center><h1>无法加载帮助文件,请检查配置</h1></center>");
		}
		editorPane.setContentType("text/html;charset=utf-8");
		editorPane.setEditable(false);
		JScrollPane jScrollPane=new JScrollPane(editorPane);
		this.add(jScrollPane);

	}
}
