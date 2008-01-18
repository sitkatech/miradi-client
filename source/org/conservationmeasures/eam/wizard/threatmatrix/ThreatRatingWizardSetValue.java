/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.wizard.threatmatrix;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.RatingCriterion;
import org.conservationmeasures.eam.objects.ValueOption;
import org.conservationmeasures.eam.project.ThreatRatingBundle;
import org.conservationmeasures.eam.project.SimpleThreatRatingFramework;
import org.conservationmeasures.eam.views.threatmatrix.ThreatMatrixView;
import org.conservationmeasures.eam.wizard.ThreatRatingWizardStep;
import org.conservationmeasures.eam.wizard.WizardPanel;

abstract public class ThreatRatingWizardSetValue extends ThreatRatingWizardStep
{
	public ThreatRatingWizardSetValue(WizardPanel wizardToUse, String critertionNameToUse) throws Exception
	{
		super(wizardToUse);
		critertionName = critertionNameToUse;
	}

	public void refresh() throws Exception
	{
		SimpleThreatRatingFramework framework = getFramework();
		BaseId criterionId = framework.findCriterionByLabel(critertionName).getId();
		ThreatRatingBundle bundle = getThreatView().getBundle();
		criterion = getFramework().getCriterion(criterionId);
		if(bundle != null)
		{
			BaseId valueId = bundle.getValueId(criterion.getId());
			value = getFramework().getValueOption(valueId);
		}
		super.refresh();

	}

	private ThreatMatrixView getThreatView()
	{
		return getMainWindow().getThreatView();
	}

	protected String[] getValueOptionLabels()
	{
		ValueOption[] options = getFramework().getValueOptions();
		String[] optionLabels = new String[options.length];
		for(int i = 0; i < optionLabels.length; ++i)
			optionLabels[i] = options[i].getLabel();
		return optionLabels;
	}

	private SimpleThreatRatingFramework getFramework()
	{
		return getMainWindow().getProject().getSimpleThreatRatingFramework();
	}
	
	public void valueChanged(String widget, String newValue)
	{
	}
	
	public void setValue(String newValue)
	{
		value = findValueOptionByName(newValue);
	}
	
	public ValueOption findValueOptionByName(String label)
	{
		ValueOption[] options = getFramework().getValueOptions();
		for(int i = 0; i < options.length; ++i)
			if(label.equals(options[i].getLabel()))
				return options[i];
			
		throw new RuntimeException("Unknown value option: " + label);
	}

	public void setComponent(String name, JComponent component)
	{
		try
		{
			ThreatRatingBundle bundle = getThreatView().getBundle();
			if (name.equals("value"))
			{
				valueBox = (JComboBox)component;
				DefaultComboBoxModel cbm = new DefaultComboBoxModel(getValueOptionLabels());
				valueBox.setModel(cbm);
				if (bundle!=null) 
					valueBox.setSelectedItem(value.getLabel());
			}
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
				
		((JComboBox)component).addItemListener(new ValueItemListener());
	}
	
	
	class ValueItemListener implements ItemListener
	{
		public void itemStateChanged(ItemEvent arg0)
		{
			try
			{
				setValue(valueBox.getSelectedItem().toString());
				getThreatView().setBundleValue(criterion, value);
			}
			catch(Exception e)
			{
				EAM.logException(e);
			}

		}
		ThreatRatingWizardChooseBundle wizard;
	}
	
	
	JComboBox valueBox;
	String critertionName;
	RatingCriterion criterion;
	protected ValueOption value;
}
