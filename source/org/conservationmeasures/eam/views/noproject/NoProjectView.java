/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.noproject;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.Action;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;

import org.conservationmeasures.eam.actions.ActionNewProject;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.utils.HtmlViewer;
import org.conservationmeasures.eam.utils.HyperlinkHandler;
import org.conservationmeasures.eam.views.umbrella.ProjectChooser;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;

public class NoProjectView extends UmbrellaView implements HyperlinkHandler
{
	public NoProjectView(MainWindow mainWindow)
	{
		super(mainWindow);
		
		setToolBar(new NoProjectToolBar(getActions()));
		
		String htmlText = new NoProjectHtmlText().getText();
		JEditorPane contents = new HtmlViewer(htmlText, this);
		
		JScrollPane scrollPane = new JScrollPane(contents);
		
		setLayout(new BorderLayout());
		add(scrollPane, BorderLayout.CENTER);
	}
	
	public void clicked(String linkDescription)
	{
		if(linkDescription.equals(NoProjectHtmlText.NEW_PROJECT))
		{
			Action action = new ActionNewProject(getMainWindow());
			action.actionPerformed(null);
			return;
		}
		if(linkDescription.startsWith(NoProjectHtmlText.OPEN_PREFIX))
		{
			String projectName = linkDescription.substring(NoProjectHtmlText.OPEN_PREFIX.length());
			File projectDirectory = new File(ProjectChooser.getHomeDirectory(), projectName);
			getMainWindow().createOrOpenProject(projectDirectory);
		}
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

