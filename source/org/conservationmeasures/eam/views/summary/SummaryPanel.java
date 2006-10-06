/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.summary;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.HtmlBuilder;
import org.martus.swing.HtmlViewer;
import org.martus.swing.HyperlinkHandler;

public class SummaryPanel extends JPanel implements HyperlinkHandler
{
	public SummaryPanel(Project projectToUse)
	{
		super(new BorderLayout());
		project = projectToUse;

		htmlViewer = new HtmlViewer(SummaryText.build(project), this);
		JScrollPane scrollPane = new JScrollPane(htmlViewer);
		add(scrollPane);
	}

	public void linkClicked(String linkDescription)
	{
	}
	
	public void valueChanged(String widget, String newValue)
	{
	}

	public void buttonPressed(String buttonName)
	{
	}

	Project project;
	HtmlViewer htmlViewer;
}

class SummaryText extends HtmlBuilder
{
	public static String build(Project project)
	{
		return font("Arial", 
						heading("Project Summary") +
						table(
							tableRow(
									tableCell("Filename:") +
									tableCell(project.getFilename())
							) +
							tableRow(
									tableCell("Project Name:") +
									tableCell(project.getMetadata().getProjectName())
							)
						) +
						newline() +
						"")
		;
	}
}
