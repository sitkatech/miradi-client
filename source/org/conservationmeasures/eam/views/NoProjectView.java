/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import org.conservationmeasures.eam.actions.ActionExit;
import org.conservationmeasures.eam.actions.ActionNewProject;
import org.conservationmeasures.eam.actions.ActionOpenProject;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.utils.DialogLayout;
import org.conservationmeasures.eam.views.umbrella.About;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;

public class NoProjectView extends UmbrellaView
{
	public NoProjectView(MainWindow mainWindow)
	{
		super(mainWindow);
		
		setToolBar(new NoProjectToolBar(getActions()));

		JButton openButton = new JButton(getActions().get(ActionOpenProject.class));
		JLabel openText = new JLabel(EAM.text("Open an existing project"));
		
		JButton newButton = new JButton(getActions().get(ActionNewProject.class));
		JLabel newText = new JLabel(EAM.text("Create a new project"));
		
		JButton exitButton = new JButton(getActions().get(ActionExit.class));
		JLabel exitText = new JLabel(EAM.text("Exit e-AdaptiveManagement"));
		
		JPanel buttons = new JPanel();
		buttons.setBorder(new LineBorder(getForeground()));
		DialogLayout layout = new DialogLayout();
		buttons.setLayout(layout);
		buttons.add(openButton);
		buttons.add(openText);
		buttons.add(newButton);
		buttons.add(newText);
		buttons.add(exitButton);
		buttons.add(exitText);
		layout.adjustSizes(buttons);

		JLabel license = new JLabel("<html><table><tr><td align='center'>" +
				"<br><br><br><br>" + 
				About.licenseText + 
				"</td></tr></table></html>"
				);
		
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		buttons.setAlignmentX(.5f);
		license.setAlignmentX(.5f);
		
		add(buttons);
		add(license);
	}
	
	public String cardName()
	{
		return getViewName();
	}
	
	static public String getViewName()
	{
		return "";
	}

}

