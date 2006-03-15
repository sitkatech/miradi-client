/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.noproject;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;
import java.util.Date;

import javax.swing.Action;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.conservationmeasures.eam.actions.ActionNewProject;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.umbrella.ProjectChooser;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;
import org.martus.util.MultiCalendar;

public class NoProjectView extends UmbrellaView
{
	public NoProjectView(MainWindow mainWindow)
	{
		super(mainWindow);
		
		setToolBar(new NoProjectToolBar(getActions()));

		String text = 
			font("Arial", 
				heading("Welcome to e-Adaptive Management!") + 
				horizontalLine() + heading("Get Started") +
				indent(
						paragraph("To begin a new " + anchorTag("DefineProject", "project") + 
								", select " + bold("Start Project") + ".") +
								centered(anchorTag("NewProject", image("StartProject.png"))) +
						paragraph("New to e-Adaptive Management? See an " +
								anchorTag("DefineEAM", "Overview of e-AM") + ".")
					) +
				horizontalLine() +
				heading("Work on Existing Project") + 
				indent(
						paragraph("To work on an existing project, choose it from the list below:") +
						newline() + 
						existingProjectTable()
						) +
				horizontalLine() +
				paragraph(anchorTag("BrowseProjects", "Browse to find other projects")) +
				paragraph(anchorTag("ManageProjects", "Copy, rename, or delete projects"))
				)
			;
			
		
		JEditorPane contents = new HTMLViewer(text);
		contents.setBackground(Color.LIGHT_GRAY);
		contents.addHyperlinkListener(new LinkListener());
		
		JScrollPane scrollPane = new JScrollPane(contents);
		
		setLayout(new BorderLayout());
		add(scrollPane, BorderLayout.CENTER);
	}
	
	public String font(String font, String text)
	{
		return "<div style='font-family: arial;'>" + text + "</div>";
	}
	
	public String heading(String text)
	{
		return "<h2>" + text + "</h2>";
	}
	
	public String horizontalLine()
	{
		return "<hr></hr>";
	}
	
	public String button(String text)
	{
		return "<input type='submit' value=" + text + "></input>";
		//return "<input class='z' type='submit' name='z' value=" + text + ">";
	}
	
	public String paragraph(String text)
	{
		return "<p>" + text + "</p>";
	}
	
	public String bold(String text)
	{
		return "<strong>" + text + "</strong>";
	}
	
	public String anchorTag(String target, String text)
	{
		return "<a href='" + target + "'>" + text + "</a>";
	}
	
	public String image(String name)
	{
		String url = getClass().getResource(name).toExternalForm();
		return "<img src='" + url + "'></img>";
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

	public String clickableProject(String name)
	{
		return anchorTag("Open:" + name, name);
	}
	public String table(String tableData)
	{
		return "<table>" + tableData + "</table>";
	}
	
	public String tableRow(String cells)
	{
		return "<tr>" + cells + "</tr>";
	}
	
	public String tableCell(String text)
	{
		return "<td>" + text + "</td>";
	}
	
	public String tableHeader(String text)
	{
		return "<th align='left'>" + text + "</th>";
	}
	
	public String radioButton(String group, String text)
	{
		return "<input type='radio' name=" + group + ">" + text + "</input>";
	}
	
	public String centered(String text)
	{
		return "<p align='center'>" + text + "</p>";
	}
	
	public String indent(String text)
	{
		return "<table><tr><td width='25'></td><td>" + text + "</td></tr></table>";
	}
	
	public String newline()
	{
		return "<br></br>";
	}
	
	public File[] getProjectDirectories()
	{
		File home = ProjectChooser.getHomeDirectory();
		return home.listFiles(new ProjectChooser.DirectoryFilter());

	}
	
	class LinkListener implements HyperlinkListener
	{
		public void hyperlinkUpdate(HyperlinkEvent e)
		{
			if(e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED))
			{
				String clicked = e.getDescription(); 
				if(clicked.equals("NewProject"))
				{
					Action action = new ActionNewProject(getMainWindow());
					action.actionPerformed(null);
					return;
				}
				if(clicked.startsWith("Open:"))
				{
					String projectName = clicked.substring("Open:".length());
					File projectDirectory = new File(ProjectChooser.getHomeDirectory(), projectName);
					getMainWindow().createOrOpenProject(projectDirectory);
				}
			}
		}
	}
	
	class HTMLViewer extends JEditorPane
	{
		public HTMLViewer(String htmlSource)
		{
			setContentType("text/html");
			setEditable(false);
			setText(htmlSource);
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

