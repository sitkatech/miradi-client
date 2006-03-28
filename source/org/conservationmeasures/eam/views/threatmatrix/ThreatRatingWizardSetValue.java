/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import javax.swing.JScrollPane;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ThreatRatingBundle;
import org.conservationmeasures.eam.objects.ThreatRatingCriterion;
import org.conservationmeasures.eam.objects.ThreatRatingValueOption;
import org.conservationmeasures.eam.project.ThreatRatingFramework;
import org.conservationmeasures.eam.utils.HtmlViewer;
import org.conservationmeasures.eam.utils.HyperlinkHandler;

abstract public class ThreatRatingWizardSetValue extends ThreatRatingWizardStep implements HyperlinkHandler
{
	
	public abstract String getHtmlText();
	
	public ThreatRatingWizardSetValue(ThreatRatingWizardPanel wizardToUse, int criterionId) throws Exception
	{
		super(wizardToUse);

		criterion = getFramework().getCriterion(criterionId);

		htmlViewer = new HtmlViewer("", this);
		JScrollPane scrollPane = new JScrollPane(htmlViewer);
		add(scrollPane);
	}

	public void refresh() throws Exception
	{
		ThreatRatingBundle bundle = getThreatRatingWizard().getSelectedBundle();
		if(bundle == null)
		{
			EAM.logDebug("ThreatRatingWizardSetValue ignoring refresh of null bundle");
			return;
		}
		int valueId = bundle.getValueId(criterion.getId());
		value = getFramework().getValueOption(valueId);
		String htmlText = getHtmlText();
		htmlViewer.setText(htmlText);
	}

	protected String[] getValueOptionLabels()
	{
		ThreatRatingValueOption[] options = getFramework().getValueOptions();
		String[] optionLabels = new String[options.length];
		for(int i = 0; i < optionLabels.length; ++i)
			optionLabels[i] = options[i].getLabel();
		return optionLabels;
	}

	private ThreatRatingFramework getFramework()
	{
		return getThreatRatingWizard().getFramework();
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

	public boolean save() throws Exception
	{
		getThreatRatingWizard().getView().setBundleValue(criterion, value);
		return true;
	}

	ThreatRatingCriterion criterion;
	protected ThreatRatingValueOption value;
	HtmlViewer htmlViewer;
}
