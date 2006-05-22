/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.noproject;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.Action;
import javax.swing.JScrollPane;

import org.conservationmeasures.eam.actions.ActionNewProject;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.utils.HtmlViewer;
import org.conservationmeasures.eam.utils.HyperlinkHandler;
import org.conservationmeasures.eam.views.umbrella.ProjectChooser;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;

public class NoProjectView extends UmbrellaView implements HyperlinkHandler
{
	public NoProjectView(MainWindow mainWindow) throws Exception
	{
		super(mainWindow);
		
		setToolBar(new NoProjectToolBar(getActions()));
		becomeActive();
	}
	
	public void linkClicked(String linkDescription)
	{
		if(linkDescription.equals(NoProjectHtmlText.NEW_PROJECT))
		{
			Action action = new ActionNewProject(getMainWindow());
			action.actionPerformed(null);
		}
		else if(linkDescription.startsWith(NoProjectHtmlText.OPEN_PREFIX))
		{
			String projectName = linkDescription.substring(NoProjectHtmlText.OPEN_PREFIX.length());
			File projectDirectory = new File(ProjectChooser.getHomeDirectory(), projectName);
			getMainWindow().createOrOpenProject(projectDirectory);
		}
		else if(linkDescription.equals("Definition:Project"))
		{
			EAM.okDialog("Definition: Project", new String[] {"A project is..."});
		}
		else if(linkDescription.equals("Definition:EAM"))
		{
			EAM.okDialog("Definition: e-Adaptive Management", new String[] {"e-Adaptive Management is..."});
		}
		else
		{
			EAM.okDialog("Not implemented yet", new String[] {"Not implemented yet"});
		}
	}

	public void becomeActive() throws Exception
	{
		removeAll();
		String htmlText = new NoProjectHtmlText().getText();
		HtmlViewer contents = new HtmlViewer(htmlText, this);
		
		JScrollPane scrollPane = new JScrollPane(contents);
		
		setLayout(new BorderLayout());
		add(scrollPane, BorderLayout.CENTER);
	}
	
	public void becomeInactive() throws Exception
	{
		// nothing to do...would clear all view data
	}

	public void valueChanged(String widget, String newValue)
	{
	}

	public void buttonPressed(String buttonName)
	{
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

