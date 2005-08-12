/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;

import org.conservationmeasures.eam.actions.ActionExit;
import org.conservationmeasures.eam.actions.ActionNewProject;
import org.conservationmeasures.eam.actions.ActionOpenProject;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.utils.DialogLayout;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;

public class NoProjectView extends UmbrellaView
{
	public NoProjectView(MainWindow mainWindow)
	{
		super(mainWindow);
		setBorder(new LineBorder(getForeground()));

		JButton openButton = new JButton(getActions().get(ActionOpenProject.class));
		JLabel openText = new JLabel(EAM.text("Open an existing project"));
		
		JButton newButton = new JButton(getActions().get(ActionNewProject.class));
		JLabel newText = new JLabel(EAM.text("Create a new project"));
		
		JButton exitButton = new JButton(getActions().get(ActionExit.class));
		JLabel exitText = new JLabel(EAM.text("Exit e-AdaptiveManagement"));
		
		DialogLayout layout = new DialogLayout();
		setLayout(layout);
		add(openButton);
		add(openText);
		add(newButton);
		add(newText);
		add(exitButton);
		add(exitText);
		layout.adjustSizes(this);
	}
	
	public String cardName()
	{
		return "No project";
	}
}

