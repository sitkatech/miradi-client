/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.conservationmeasures.eam.actions.ActionExit;
import org.conservationmeasures.eam.actions.ActionNewProject;
import org.conservationmeasures.eam.actions.ActionOpenProject;

public class NoProjectView extends JPanel
{
	public NoProjectView(MainWindow mainWindow)
	{
		JButton openButton = new JButton(mainWindow.getActions().get(ActionOpenProject.class));
		JLabel openText = new JLabel(EAM.text("Open an existing project"));
		
		JButton newButton = new JButton(mainWindow.getActions().get(ActionNewProject.class));
		JLabel newText = new JLabel(EAM.text("Create a new project"));
		
		JButton exitButton = new JButton(mainWindow.getActions().get(ActionExit.class));
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
}

