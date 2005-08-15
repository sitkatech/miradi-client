/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.interview;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;

import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;

public class InterviewView extends UmbrellaView
{
	public InterviewView(MainWindow mainWindow)
	{
		super(mainWindow);
		setToolBar(new InterviewToolBar(mainWindow.getActions()));
		JLabel welcome = new JLabel("<html><h1>Interview</h1>" +
				"<p>This view will walk the user through a series " +
				"of questions.</p></html>");
		add(welcome, BorderLayout.CENTER);
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
