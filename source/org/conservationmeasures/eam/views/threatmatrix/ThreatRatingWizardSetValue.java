/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ThreatRatingCriterion;
import org.conservationmeasures.eam.objects.ThreatRatingValueOption;
import org.conservationmeasures.eam.project.ThreatRatingFramework;
import org.conservationmeasures.eam.utils.HtmlViewer;
import org.conservationmeasures.eam.utils.HyperlinkHandler;

public class ThreatRatingWizardSetValue extends JPanel implements HyperlinkHandler
{
	public ThreatRatingWizardSetValue(ThreatRatingWizardPanel wizardToUse) throws Exception
	{
		super(new BorderLayout());
		wizard = wizardToUse;

		criterion = getFramework().findCriterionByLabel("Scope");

		String htmlText = new ThreatRatingWizardSetValueText(getValueOptionLabels()).getText();
		HtmlViewer contents = new HtmlViewer(htmlText, this);
		JScrollPane scrollPane = new JScrollPane(contents);
		add(scrollPane);

	}

	String[] getValueOptionLabels()
	{
		ThreatRatingValueOption[] options = getFramework().getValueOptions();
		String[] optionLabels = new String[options.length];
		for(int i = 0; i < optionLabels.length; ++i)
			optionLabels[i] = options[i].getLabel();
		return optionLabels;
	}

	private ThreatRatingFramework getFramework()
	{
		ThreatRatingFramework framework = wizard.getView().getProject().getThreatRatingFramework();
		return framework;
	}
	
	public void linkClicked(String linkDescription)
	{
	}

	public void valueChanged(String widget, String newValue)
	{
		setValue(newValue);
	}
	
	public void setValue(String newValue)
	{
		value = findValueOptionByName(newValue);
	}
	
	public ThreatRatingValueOption findValueOptionByName(String label)
	{
		ThreatRatingValueOption[] options = getFramework().getValueOptions();
		for(int i = 0; i < options.length; ++i)
			if(label.equals(options[i].getLabel()))
				return options[i];
			
		throw new RuntimeException("Unknown value option: " + label);
	}

	public void buttonPressed(String buttonName)
	{
		try
		{
			wizard.getView().setBundleValue(criterion, value);
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}

	ThreatRatingWizardPanel wizard;
	ThreatRatingCriterion criterion;
	ThreatRatingValueOption value;
}
