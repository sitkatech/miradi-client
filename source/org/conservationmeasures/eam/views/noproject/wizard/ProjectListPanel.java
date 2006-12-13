package org.conservationmeasures.eam.views.noproject.wizard;

import java.awt.BorderLayout;

import javax.swing.JPanel;

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
				"select it from the list below:</p>");
		
		add(intro, BorderLayout.BEFORE_FIRST_LINE);
		add(new UiScrollPane(projectList), BorderLayout.CENTER);
	}
	
	public void refresh()
	{
		projectList.refresh();
	}
	
	ProjectList projectList;
}
