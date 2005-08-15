/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.interview;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;

public class InterviewView extends UmbrellaView
{
	public InterviewView(MainWindow mainWindow)
	{
		super(mainWindow);
		setToolBar(new InterviewToolBar());
	}

	public String cardName()
	{
		return getViewName();
	}
	
	static public String getViewName()
	{
		return "interview";
	}

	static class DiagramButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent arg0)
		{
			// TODO Auto-generated method stub
			
		}
	}
}
