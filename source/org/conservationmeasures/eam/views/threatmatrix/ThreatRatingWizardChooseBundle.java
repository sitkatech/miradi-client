/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import javax.swing.JScrollPane;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ThreatRatingBundle;
import org.conservationmeasures.eam.utils.HtmlViewer;
import org.conservationmeasures.eam.utils.HyperlinkHandler;

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
		return getView().getModel().getTargetNames();
	}

	private String[] getThreatNames()
	{
		return getView().getModel().getThreatNames();
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

			wizard.bundleWasClicked(getSelectedBundle());
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
		int threatId = model.findThreatByName(selectedThreatName);
		int targetId = model.findTargetByName(selectedTargetName);
		ThreatRatingBundle bundle = model.getBundle(threatId, targetId);
		return bundle;
	}
	
	ThreatMatrixView getView()
	{
		return wizard.getView();
	}
	
	void refresh() throws Exception
	{
		ThreatRatingBundle bundle = wizard.getSelectedBundle();

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

	private String getName(int nodeId)
	{
		return getView().getProject().getNodePool().find(nodeId).getName();
	}

	HtmlViewer htmlViewer;
	String selectedThreatName;
	String selectedTargetName;
}