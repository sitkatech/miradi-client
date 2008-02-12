/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.wizard.threatmatrix;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;

import org.miradi.actions.jump.ActionJumpThreatMatrixOverviewStep;
import org.miradi.ids.FactorId;
import org.miradi.main.EAM;
import org.miradi.main.menu.ProcessSteps;
import org.miradi.project.ThreatRatingBundle;
import org.miradi.views.threatmatrix.ThreatMatrixTableModel;
import org.miradi.views.threatmatrix.ThreatMatrixView;
import org.miradi.wizard.ThreatRatingWizardStep;
import org.miradi.wizard.WizardPanel;

public class ThreatRatingWizardChooseBundle extends ThreatRatingWizardStep
{
	public ThreatRatingWizardChooseBundle(WizardPanel wizardToUse)
	{
		super(wizardToUse);
	}

	
	public String getProcessStepTitle()
	{
		return ProcessSteps.PROCESS_STEP_1C;
	}

	public Class getAssociatedActionClass()
	{
		return ActionJumpThreatMatrixOverviewStep.class;
	}
	
	public ThreatRatingBundle getSelectedBundle() throws Exception
	{
		ThreatMatrixTableModel model = getThreatView().getModel();
		FactorId threatId = model.findThreatByName(threatBox.getSelectedItem().toString());
		FactorId targetId = model.findTargetByName(targetBox.getSelectedItem().toString());
		ThreatRatingBundle bundle = model.getBundle(threatId, targetId);
		return bundle;
	}

	private ThreatMatrixView getThreatView()
	{
		return getMainWindow().getThreatView();
	}
	

	private String getName(FactorId nodeId)
	{
		return getMainWindow().getProject().findNode(nodeId).getLabel();
	}
	
	
	public void setComponent(String name, JComponent component)
	{
		try
		{
			ThreatRatingBundle bundle = getThreatView().getBundle();
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
			EAM.logException(e);
		}
				
		((JComboBox)component).addItemListener(new RatingItemListener());
	}
	
	
	private String[] getTargetNames()
	{
		String[] targetNames = getThreatView().getModel().getTargetNames();
		String[] choices = new String[targetNames.length+ 1];
		System.arraycopy(targetNames, 0, choices, 1, targetNames.length);
		choices[0] = SELECT_A_TARGET;
		return choices;
	}

	private String[] getThreatNames()
	{
		String[] threatNames = getThreatView().getModel().getThreatNames();
		String[] choices = new String[threatNames.length + 1];
		System.arraycopy(threatNames, 0, choices, 1, threatNames.length);
		choices[0] = SELECT_A_THREAT;
		return choices;
	}

	public void buttonPressed(String buttonName)
	{
		if (buttonName.equals("Next"))
			try
			{
				if(getSelectedBundle() == null)
				{
					if ((threatBox.getSelectedIndex())==0 || (targetBox.getSelectedIndex()==0))
						EAM.errorDialog(EAM.text("Please select a threat and target"));
					else
						EAM.errorDialog(EAM.text("This threat is not currently linked to the selected target.  " +
						"To create a link, click in the associated gray box in the threat table"));
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
					getThreatView().selectBundle(bundle);
			}
			catch(Exception e)
			{
				EAM.logException(e);
			}

		}

		ThreatRatingWizardChooseBundle wizard;
	}
	
	public String getSubHeading()
	{
		return EAM.text("Page 1");
	}

	static final String SELECT_A_TARGET = "--Select a Target";
	static final String SELECT_A_THREAT = "--Select a Threat";


	JComboBox threatBox;
	JComboBox targetBox;
	
}