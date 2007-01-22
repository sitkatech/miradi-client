/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.threatmatrix.wizard;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.ThreatRatingBundle;
import org.conservationmeasures.eam.project.ThreatRatingFramework;

public class ThreatRatingWizardScopeStep extends ThreatRatingWizardSetValue
{
	public static ThreatRatingWizardScopeStep create(ThreatRatingWizardPanel wizardToUse) throws Exception
	{
		ThreatRatingFramework framework = wizardToUse.getView().getProject().getThreatRatingFramework();
		BaseId criterionId = framework.findCriterionByLabel("Scope").getId();
		ThreatRatingWizardScopeStep step = new ThreatRatingWizardScopeStep(wizardToUse, criterionId);
		return step;
	}
	
	private ThreatRatingWizardScopeStep(ThreatRatingWizardPanel wizardToUse, BaseId criterionIdToUse) throws Exception
	{
		super(wizardToUse, criterionIdToUse);
	}


	public void setComponent(String name, JComponent component)
	{
		try
		{
			ThreatRatingBundle bundle = getThreatRatingWizard().getSelectedBundle();
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
			e.printStackTrace();
		}
				
		((JComboBox)component).addItemListener(new ValueItemListener());
	}
	
	
	public String getResourceFileName()
	{
		return HTML_FILENAME;
	}

	class ValueItemListener implements ItemListener
	{
		public void itemStateChanged(ItemEvent arg0)
		{
			try
			{
//TODO: Make hot value change here
			}
			catch(Exception e)
			{
				EAM.logException(e);
			}

		}
		ThreatRatingWizardChooseBundle wizard;
	}
	
	String HTML_FILENAME = "ThreatRatingScope.html";
	
	JComboBox valueBox;

}
