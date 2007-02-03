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

import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.ThreatRatingBundle;
import org.conservationmeasures.eam.views.threatmatrix.NonEditableThreatMatrixTableModel;
import org.conservationmeasures.eam.views.threatmatrix.ThreatMatrixView;

public class ThreatRatingWizardChooseBundle extends ThreatRatingWizardStep
{
	public ThreatRatingWizardChooseBundle(ThreatRatingWizardPanel wizardToUse)
	{
		super(wizardToUse);
	}


	public ThreatRatingBundle getSelectedBundle() throws Exception
	{
		NonEditableThreatMatrixTableModel model = getView().getModel();
		FactorId threatId = model.findThreatByName(threatBox.getSelectedItem().toString());
		FactorId targetId = model.findTargetByName(targetBox.getSelectedItem().toString());
		ThreatRatingBundle bundle = model.getBundle(threatId, targetId);
		return bundle;
	}
	
	
	ThreatMatrixView getView()
	{
		return getThreatRatingWizard().getView();
	}
	

	private String getName(FactorId nodeId)
	{
		return getView().getProject().getFactorPool().find(nodeId).getLabel();
	}
	
	
	public void setComponent(String name, JComponent component)
	{
		try
		{
			ThreatRatingBundle bundle = getThreatRatingWizard().getSelectedBundle();
			if (name.equals("Threat"))
			{
				threatBox = (JComboBox)component;
				DefaultComboBoxModel cbm = new DefaultComboBoxModel(getThreatNames());
				threatBox.setModel(cbm);
				if (bundle!=null) 
					threatBox.setSelectedItem(getName(bundle.getThreatId()));
			}

			if (name.equals("Target")) 
			{
				targetBox = (JComboBox)component;
				DefaultComboBoxModel cbm = new DefaultComboBoxModel(getTargetNames());
				targetBox.setModel(cbm);
				if (bundle!=null) 
					targetBox.setSelectedItem(getName(bundle.getTargetId()));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
				
		((JComboBox)component).addItemListener(new RatingItemListener());
	}
	
	
	private String[] getTargetNames()
	{
		String[] targetNames = getView().getModel().getTargetNames();
		String[] choices = new String[targetNames.length+ 1];
		System.arraycopy(targetNames, 0, choices, 1, targetNames.length);
		choices[0] = SELECT_A_TARGET;
		return choices;
	}

	private String[] getThreatNames()
	{
		String[] threatNames = getView().getModel().getThreatNames();
		String[] choices = new String[threatNames.length + 1];
		System.arraycopy(threatNames, 0, choices, 1, threatNames.length);
		choices[0] = SELECT_A_THREAT;
		return choices;
	}
	
	public String getResourceFileName()
	{
		return HTML_FILENAME;
	}

	
	public void buttonPressed(String buttonName)
	{
		if (buttonName.equals("Next"))
			try
			{
				if(getSelectedBundle() == null)
				{
					EAM.errorDialog(EAM.text("Please select a threat and target"));
					return;
				}
			}
			catch (Exception e)
			{
				EAM.errorDialog("Internal Error bundle not found");
				return;
			}
		
		super.buttonPressed(buttonName);
		
	}
	
	class RatingItemListener implements ItemListener
	{
		public void itemStateChanged(ItemEvent arg0)
		{
			try
			{
				 ThreatRatingBundle bundle = getSelectedBundle();
				if (bundle!=null)
					getThreatRatingWizard().bundleWasClicked(bundle);
			}
			catch(Exception e)
			{
				EAM.logException(e);
			}

		}
		ThreatRatingWizardChooseBundle wizard;
	}
	

	
	String HTML_FILENAME = "ThreatRatingSelectTarget.html";


	static final String SELECT_A_TARGET = "--Select a Target";
	static final String SELECT_A_THREAT = "--Select a Threat";


	JComboBox threatBox;
	JComboBox targetBox;
	
}