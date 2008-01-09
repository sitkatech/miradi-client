/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.wizard.noproject.projectlist;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.wizard.WizardHtmlViewer;
import org.conservationmeasures.eam.wizard.noproject.NoProjectWizardStep;

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
	
	static final String TEXT = EAM.text("<p>To <strong>continue working on an existing project</strong>, " +
	"double-click on the name in the list below, or select it and click the Open button.");
	
	TreeBasedProjectList projectList;
	WizardHtmlViewer intro;
}
