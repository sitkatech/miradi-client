/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.noproject;

import java.awt.Dimension;
import java.io.File;
import java.util.Date;

import javax.swing.ImageIcon;

import org.conservationmeasures.eam.utils.HtmlBuilder;
import org.conservationmeasures.eam.views.umbrella.ProjectChooser;
import org.martus.util.MultiCalendar;

class NoProjectHtmlText extends HtmlBuilder
{
	public NoProjectHtmlText()
	{
		String startButtonLocation = "images/StartProject.png";
		ImageIcon startButtonIcon = new ImageIcon(startButtonLocation);
		Dimension startButtonSize = new Dimension(startButtonIcon.getIconWidth(), startButtonIcon.getIconHeight());
		text = 
			font("Arial", 
				heading("Welcome to e-Adaptive Management!") + 
				horizontalLine() + heading("Get Started") +
				indent(
						paragraph("To begin a new " + 
								definition("Definition:Project", "project", "A project is...") + 
								", select " + bold("Start Project") + ".") +
								centered(anchorTag(NEW_PROJECT, 
										image(startButtonLocation, startButtonSize))) +
						paragraph("New to e-Adaptive Management? See an " +
								definition("Definition:EAM", "Overview of e-AM", "e-Adaptive Management is...") + 
								".")
					) +
				horizontalLine() +
				heading("Work on Existing Project") + 
				indent(
						paragraph("To work on an existing project, choose it from the list below:") +
						newline() + 
						existingProjectTable()
						) +
				horizontalLine() +
				paragraph(
					definition("Definition:BrowseProject", "Browse to find other projects", "Browse to existing...")) +
				paragraph(
					definition("Definition:ManageProject", "Copy, rename, or delete projects", "Manage projects..."))
				);

	}
	
	public String getText()
	{
		return text;
	}
	
	public String existingProjectTable()
	{
		File[] projectDirectories = getProjectDirectories();
		String rows = tableRow(tableHeader("ProjectName") + tableHeader("Last Modified") + tableHeader("File Location") + "</tr>");
		for(int i = 0; i < projectDirectories.length; ++i)
		{
			File projectFile = projectDirectories[i];
			String name = projectFile.getName();
			MultiCalendar date = new MultiCalendar(new Date(projectFile.lastModified()));
			String isoDate = date.toIsoDateString();
			String path = projectFile.getAbsolutePath();
			rows += tableRow(tableCell(clickableProject(name)) + tableCell(isoDate) + tableCell(path));
		}
		return table(rows);
	}

	static public String clickableProject(String name)
	{
		return anchorTag(OPEN_PREFIX + name, name);
	}
	
	public File[] getProjectDirectories()
	{
		File home = ProjectChooser.getHomeDirectory();
		return home.listFiles(new ProjectChooser.DirectoryFilter());

	}
	
	public static final String NEW_PROJECT = "NewProject";
	public static final String OPEN_PREFIX = "Open:";

	String text;
}