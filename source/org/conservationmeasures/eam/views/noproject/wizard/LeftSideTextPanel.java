/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.noproject.wizard;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;

import org.conservationmeasures.eam.wizard.WizardHtmlViewer;
import org.martus.swing.HyperlinkHandler;

public class LeftSideTextPanel extends JPanel
{
	public LeftSideTextPanel(String htmlToUse, HyperlinkHandler wizardToUse)
	{
		super(new BorderLayout());
		String html = htmlToUse;
		setBackground(Color.WHITE);
		viewer = new WizardHtmlViewer(wizardToUse);
		viewer.setText(html);

		add(viewer, BorderLayout.BEFORE_FIRST_LINE);
	}
	
	public void refresh() throws Exception
	{
		viewer.setText(viewer.getText());
	}
	
	WizardHtmlViewer viewer;
}
