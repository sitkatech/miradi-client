/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix.wizard;

import javax.swing.JScrollPane;

import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.ThreatRatingBundle;
import org.conservationmeasures.eam.views.threatmatrix.ThreatMatrixTableModel;
import org.conservationmeasures.eam.views.threatmatrix.ThreatMatrixView;
import org.martus.swing.HtmlViewer;
import org.martus.swing.HyperlinkHandler;

public class ThreatRatingWizardChooseBundle extends ThreatRatingWizardStep implements HyperlinkHandler
{
	public ThreatRatingWizardChooseBundle(ThreatRatingWizardPanel wizardToUse)
	{
		super(wizardToUse);
		htmlViewer = new HtmlViewer("", this);
		JScrollPane scrollPane = new JScrollPane(htmlViewer);
		add(scrollPane);
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

	public void linkClicked(String linkDescription)
	{
		// TODO Auto-generated method stub
		
	}

	public void valueChanged(String widget, String newValue)
	{
		try
		{
			System.out.println("valueChanged for " + widget + " to " + newValue);
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
		ThreatMatrixTableModel model = getView().getModel();
		ModelNodeId threatId = model.findThreatByName(selectedThreatName);
		ModelNodeId targetId = model.findTargetByName(selectedTargetName);
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
				getThreatRatingWizard().setStep(ThreatRatingWizardPanel.CHECK_TOTALS);
			}
			catch (Exception e)
			{
				EAM.logException(e);
			}
			return;
		}
		super.buttonPressed(buttonName);
	}

	void refresh() throws Exception
	{
		ThreatRatingBundle bundle = getThreatRatingWizard().getSelectedBundle();

		if(bundle != null)
		{
			selectedThreatName = getName(bundle.getThreatId());
			selectedTargetName = getName(bundle.getTargetId());
		}
		
		String htmlText = new ThreatRatingWizardChooseBundleText(getThreatNames(), selectedThreatName, 
				getTargetNames(), selectedTargetName).getText();
		htmlViewer.setText(htmlText);
		validate();
	}

	private String getName(ModelNodeId nodeId)
	{
		return getView().getProject().getNodePool().find(nodeId).getLabel();
	}
	
	static final String SELECT_A_TARGET = "--Select a Target";
	static final String SELECT_A_THREAT = "--Select a Threat";

	HtmlViewer htmlViewer;
	String selectedThreatName;
	String selectedTargetName;
}