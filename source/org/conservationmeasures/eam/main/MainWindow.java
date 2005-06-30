/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

import java.awt.Dimension;
import java.awt.HeadlessException;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JTextArea;

public class MainWindow extends JFrame
{
	public MainWindow() throws HeadlessException
	{
		setTitle(EAM.text("Title|CMP e-Adaptive Management"));
		setSize(new Dimension(700, 500));
		getContentPane().add(new JTextArea(EAM.text("This is great sample\nmulti-line text")));
		setJMenuBar(new MainMenuBar(this));
	}

	public void exitNormally()
	{
		System.exit(0);
	}

	public static abstract class Action extends AbstractAction
	{
		public Action(MainWindow mainWindowToUse, String label)
		{
			super(label);
			mainWindow = mainWindowToUse;
		}
		
		public MainWindow getMainWindow()
		{
			return mainWindow;
		}
		
		MainWindow mainWindow;
	}
}
