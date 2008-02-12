/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.wizard.noproject.projectlist;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;

import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.wizard.WizardHtmlViewer;
import org.miradi.wizard.noproject.NoProjectWizardStep;

public class ProjectListPanel extends JPanel
{
	public ProjectListPanel(MainWindow mainWindow, NoProjectWizardStep handlerToUse) throws Exception
	{
		super(new BorderLayout());
		projectList = new TreeBasedProjectList(mainWindow, handlerToUse);
		
		intro = new WizardHtmlViewer(mainWindow, handlerToUse);
		intro.setText(TEXT);
		
		add(intro, BorderLayout.BEFORE_FIRST_LINE);
		add(projectList, BorderLayout.CENTER);
		setBackground(Color.WHITE);
	}
	
	public void refresh()
	{
		projectList.refresh();
		intro.setText(TEXT);
	}
	
	static final String TEXT = EAM.text("Or, to <strong>continue work</strong> " +
		"on an existing project, or to <strong>browse an example</strong>, " +
		"double-click on the name in the list below:");
	
	TreeBasedProjectList projectList;
	WizardHtmlViewer intro;
}
