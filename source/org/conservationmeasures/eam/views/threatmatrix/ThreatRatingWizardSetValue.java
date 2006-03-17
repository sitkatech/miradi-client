/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.conservationmeasures.eam.objects.ThreatRatingBundle;
import org.conservationmeasures.eam.project.ThreatRatingFramework;
import org.conservationmeasures.eam.utils.HtmlViewer;
import org.conservationmeasures.eam.utils.HyperlinkHandler;

public class ThreatRatingWizardSetValue extends JPanel implements HyperlinkHandler
{
	public ThreatRatingWizardSetValue(ThreatRatingWizardPanel wizardToUse) throws Exception
	{
		super(new BorderLayout());
		wizard = wizardToUse;

		ThreatRatingBundle bundle = wizard.getSelectedBundle();
		ThreatRatingFramework framework = wizard.getView().getProject().getThreatRatingFramework();
		int criterionId = framework.getCriteria()[0].getId();
		String htmlText = new ThreatRatingWizardSetValueText(framework, bundle, criterionId).getText();
		HtmlViewer contents = new HtmlViewer(htmlText, this);
		JScrollPane scrollPane = new JScrollPane(contents);
		add(scrollPane);

	}
	
	public void linkClicked(String linkDescription)
	{
	}

	public void valueChanged(String widget, String newValue)
	{
	}

	public void buttonPressed(String buttonName)
	{
	}

	ThreatRatingWizardPanel wizard;

}
