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

import org.conservationmeasures.eam.utils.FastScrollPane;
import org.conservationmeasures.eam.wizard.WizardHtmlViewer;
import org.martus.swing.HyperlinkHandler;

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
		add(new FastScrollPane(projectList), BorderLayout.CENTER);
		setBackground(Color.WHITE);
		setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
	}
	
	public void refresh()
	{
		projectList.refresh();
	}
	
	ProjectList projectList;
}
