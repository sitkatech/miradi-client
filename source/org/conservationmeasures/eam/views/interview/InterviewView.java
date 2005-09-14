/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.interview;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.IOException;

import javax.swing.border.LineBorder;

import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;

public class InterviewView extends UmbrellaView
{
	public InterviewView(MainWindow mainWindow) throws IOException
	{
		super(mainWindow);
		setToolBar(new InterviewToolBar(mainWindow.getActions()));
		wizard = new Wizard(mainWindow.getProject());
		wizard.showCurrentProjectStep();

		setLayout(new BorderLayout());
		add(wizard, BorderLayout.CENTER);
		setBorder(new LineBorder(Color.BLACK));
	}
	
	public String cardName()
	{
		return getViewName();
	}
	
	static public String getViewName()
	{
		return "Interview";
	}
	
	private Wizard wizard;
}
