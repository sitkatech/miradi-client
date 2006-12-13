/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.umbrella;

import javax.swing.JScrollPane;

import org.martus.swing.HtmlViewer;


public class WizardStep extends SkeletonWizardStep
{
	public WizardStep(WizardPanel wizardToUse)
	{
		super(wizardToUse);

		htmlViewer = new WizardHtmlViewer(this);
		JScrollPane scrollPane = new JScrollPane(htmlViewer);
		add(scrollPane);
	}

	public void refresh() throws Exception
	{
		String htmlText = getText();
		htmlViewer.setText(htmlText);
		invalidate();
		validate();
	}

	private HtmlViewer htmlViewer;
}
