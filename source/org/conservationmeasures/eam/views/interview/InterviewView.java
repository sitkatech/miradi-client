/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.interview;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.border.LineBorder;

import org.conservationmeasures.eam.main.CommandExecutedListener;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;

public class InterviewView extends UmbrellaView implements CommandExecutedListener
{
	public InterviewView(MainWindow mainWindow) throws Exception
	{
		super(mainWindow);
		setBorder(new LineBorder(Color.BLACK));
		setToolBar(new InterviewToolBar(mainWindow.getActions()));
		setLayout(new BorderLayout());
		
		panel = new InterviewPanel();
		add(panel, BorderLayout.CENTER);
	}
	
	public String cardName()
	{
		return getViewName();
	}
	
	static public String getViewName()
	{
		return "Interview";
	}
	
	public void becomeActive() throws Exception
	{
	}

	public void becomeInactive() throws Exception
	{
	}

	InterviewPanel panel;
}
