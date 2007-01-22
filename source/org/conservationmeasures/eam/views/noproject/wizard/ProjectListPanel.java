/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.noproject.wizard;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

import org.conservationmeasures.eam.views.umbrella.WizardHtmlViewer;
import org.martus.swing.HyperlinkHandler;
import org.martus.swing.UiScrollPane;

public class ProjectListPanel extends JPanel
{
	public ProjectListPanel(HyperlinkHandler handlerToUse)
	{
		super(new BorderLayout());
		projectList = new ProjectList(handlerToUse);
		
		WizardHtmlViewer intro = new WizardHtmlViewer(handlerToUse);

		intro.setText("<p>To <strong>continue working on an existing project</strong>, " +
				"click on the name in the list below. To <strong>copy, rename, export " +
				"to zip, or delete a file</strong>, right click (alt-click) on the name.</p>");
		
		
		add(intro, BorderLayout.BEFORE_FIRST_LINE);
		add(new UiScrollPane(projectList), BorderLayout.CENTER);
		Border lineBorder = BorderFactory.createLineBorder(Color.BLACK, 2);
		Border emptyBorder = BorderFactory.createEmptyBorder(3,3,3,3);
		setBorder(BorderFactory.createCompoundBorder(emptyBorder, lineBorder));
	}
	
	public void refresh()
	{
		projectList.refresh();
	}
	
	ProjectList projectList;
}
