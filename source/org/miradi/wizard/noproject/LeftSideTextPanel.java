/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.wizard.noproject;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import org.martus.swing.HyperlinkHandler;
import org.miradi.main.AppPreferences;
import org.miradi.main.MainWindow;
import org.miradi.wizard.WizardHtmlViewer;

public class LeftSideTextPanel extends JPanel
{
	public LeftSideTextPanel(MainWindow mainWindow, String htmlToUse, HyperlinkHandler wizardToUse)
	{
		super(new BorderLayout());
		String html = htmlToUse;
		setBackground(AppPreferences.getWizardBackgroundColor());
		viewer = new WizardHtmlViewer(mainWindow, wizardToUse);
		viewer.setText(html);

		add(viewer, BorderLayout.BEFORE_FIRST_LINE);
	}
	
	public void refresh() throws Exception
	{
		viewer.setText(viewer.getText());
	}
	
	WizardHtmlViewer viewer;
}
