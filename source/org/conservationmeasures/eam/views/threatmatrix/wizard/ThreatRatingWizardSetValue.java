/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix.wizard;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objects.ThreatRatingCriterion;
import org.conservationmeasures.eam.objects.ThreatRatingValueOption;
import org.conservationmeasures.eam.project.ThreatRatingBundle;
import org.conservationmeasures.eam.project.ThreatRatingFramework;

abstract public class ThreatRatingWizardSetValue extends ThreatRatingWizardStep
{
	public ThreatRatingWizardSetValue(ThreatRatingWizardPanel wizardToUse, BaseId criterionId) throws Exception
	{
		super(wizardToUse);
		criterion = getFramework().getCriterion(criterionId);
	}

	public void refresh() throws Exception
	{
		ThreatRatingBundle bundle = getThreatRatingWizard().getSelectedBundle();
		if(bundle == null)
		{
			return;
		}
		BaseId valueId = bundle.getValueId(criterion.getId());
		value = getFramework().getValueOption(valueId);
		
		super.refresh();
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
}
