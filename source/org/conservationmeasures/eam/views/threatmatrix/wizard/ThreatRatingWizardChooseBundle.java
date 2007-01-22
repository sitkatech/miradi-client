/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.threatmatrix.wizard;

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
	
	public String getText()
	{
		String htmlText = new ThreatRatingWizardChooseBundleText(getThreatNames(), selectedThreatName, 
				getTargetNames(), selectedTargetName).getText();
		return htmlText;
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

	public void valueChanged(String widget, String newValue)
	{
		try
		{
			if(widget.equals("Threat"))
				selectedThreatName = newValue;
			else if(widget.equals("Target"))
				selectedTargetName = newValue;

			getThreatRatingWizard().bundleWasClicked(getSelectedBundle());
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}
	
	public boolean save() throws Exception
	{
		ThreatRatingBundle selectedBundle = getSelectedBundle();

		if(selectedBundle == null)
		{
			EAM.errorDialog(EAM.text("This threat is not currently linked to the selected target.  If you would like to link this threat to this target, please do so in the diagram view."));
			return false;
		}
		
		return true;
	}

	public ThreatRatingBundle getSelectedBundle() throws Exception
	{
		NonEditableThreatMatrixTableModel model = getView().getModel();
		FactorId threatId = model.findThreatByName(selectedThreatName);
		FactorId targetId = model.findTargetByName(selectedTargetName);
		ThreatRatingBundle bundle = model.getBundle(threatId, targetId);
		return bundle;
	}
	
	ThreatMatrixView getView()
	{
		return getThreatRatingWizard().getView();
	}
	
	public void buttonPressed(String buttonName)
	{
		if(buttonName.equals("Done"))
		{
			try
			{
				getThreatRatingWizard().jump(ThreatRatingWizardCheckTotalsStep.class);
			}
			catch (Exception e)
			{
				EAM.logException(e);
			}
			return;
		}
		super.buttonPressed(buttonName);
	}

	public void refresh() throws Exception
	{
		ThreatRatingBundle bundle = getThreatRatingWizard().getSelectedBundle();

		if(bundle != null)
		{
			selectedThreatName = getName(bundle.getThreatId());
			selectedTargetName = getName(bundle.getTargetId());
		}
		
		super.refresh();
	}

	private String getName(FactorId nodeId)
	{
		return getView().getProject().getFactorPool().find(nodeId).getLabel();
	}
	
	static final String SELECT_A_TARGET = "--Select a Target";
	static final String SELECT_A_THREAT = "--Select a Threat";

	String selectedThreatName;
	String selectedTargetName;
}