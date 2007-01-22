/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.noproject.wizard;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;

import org.conservationmeasures.eam.views.umbrella.WizardHtmlViewer;
import org.martus.swing.HyperlinkHandler;

public class LeftSideTextPanel extends JPanel
{
	public LeftSideTextPanel(String html, HyperlinkHandler wizardToUse)
	{
		super(new BorderLayout());
		
		setBackground(Color.WHITE);
		WizardHtmlViewer viewer = new WizardHtmlViewer(wizardToUse);
		viewer.setText(html);

		add(viewer, BorderLayout.BEFORE_FIRST_LINE);
	}
}
