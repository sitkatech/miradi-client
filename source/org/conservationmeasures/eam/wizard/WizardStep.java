/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.wizard;

import javax.swing.JScrollPane;

import org.conservationmeasures.eam.utils.FastScrollPane;
import org.conservationmeasures.eam.utils.HtmlFormViewer;


public class WizardStep extends SkeletonWizardStep
{
	public WizardStep(WizardPanel wizardToUse)
	{
		super(wizardToUse);

		htmlViewer = new WizardHtmlViewer(this);
		JScrollPane scrollPane = new FastScrollPane(htmlViewer);
		add(scrollPane);
	}

	public void refresh() throws Exception
	{
		String htmlText = getText();
		htmlViewer.setText(htmlText);
		invalidate();
		validate();
	}
	
	private HtmlFormViewer htmlViewer;
}
