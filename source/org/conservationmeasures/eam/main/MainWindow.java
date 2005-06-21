/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

import java.awt.Dimension;
import java.awt.HeadlessException;

import javax.swing.JFrame;
import javax.swing.JTextArea;

public class MainWindow extends JFrame
{
	public MainWindow() throws HeadlessException
	{
		setTitle(EAM.text("Title|CMP eAdaptiveManagement"));
		setSize(new Dimension(700, 500));
		getContentPane().add(new JTextArea(EAM.text("This is great sample\nmulti-line text")));
	}
}
