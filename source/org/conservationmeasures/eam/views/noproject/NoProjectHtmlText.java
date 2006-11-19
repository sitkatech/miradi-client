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

import java.io.File;
import java.util.Date;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.utils.HtmlBuilder;
import org.conservationmeasures.eam.views.umbrella.CreateProjectDialog;
import org.martus.util.MultiCalendar;

class NoProjectHtmlText extends HtmlBuilder
{
	public NoProjectHtmlText()
	{
		text = 
			font("Arial", 
				heading("Existing Projects:") +
				existingProjectTable()
				);

	}
	
	public String getText()
	{
		return text;
	}
	
	public String existingProjectTable()
	{
		File[] projectDirectories = getProjectDirectories();
		String rows = tableRow(tableHeader("Project Filename") + tableHeader("Last Modified") + tableHeader("File Location") + "</tr>");
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
		File home = EAM.getHomeDirectory();
		return home.listFiles(new CreateProjectDialog.DirectoryFilter());

	}
	
	public static final String NEW_PROJECT = "NewProject";
	public static final String OPEN_PREFIX = "Open:";
	public static final String IMPORT_ZIP = "ImportZip";
	public static final String IMPORT_TNC_CAP_PROJECT = "ImportTncCapProject";

	String text;
}
