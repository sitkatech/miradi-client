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
import org.conservationmeasures.eam.objects.ThreatRatingBundle;
import org.conservationmeasures.eam.utils.HtmlViewer;
import org.conservationmeasures.eam.utils.HyperlinkHandler;

public class ThreatRatingWizardChooseBundle extends JPanel implements HyperlinkHandler
{
	public ThreatRatingWizardChooseBundle(ThreatRatingWizardPanel wizardToUse)
	{
		super(new BorderLayout());
		wizard = wizardToUse;
		ThreatMatrixTableModel model = getView().getModel();
		
		String[] threatNames = model.getThreatNames();
		String[] targetNames = model.getTargetNames();
		String htmlText = new ThreatRatingWizardWelcomeText(threatNames, targetNames).getText();
		HtmlViewer contents = new HtmlViewer(htmlText, this);
		
		if(threatNames.length > 0)
			selectedThreatName = threatNames[0];
		else
			selectedThreatName = "";
		
		if(targetNames.length > 0)
			selectedTargetName = targetNames[0];
		else
			selectedTargetName = "";
		
		JScrollPane scrollPane = new JScrollPane(contents);
		add(scrollPane);
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

			getView().selectBundle(getSelectedBundle());
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}
	
	public void buttonPressed(String buttonName)
	{
		ThreatRatingBundle selectedBundle = null;
		try
		{
			selectedBundle = getSelectedBundle();
			wizard.next();
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
		
		if(selectedBundle == null)
		{
			EAM.errorDialog(EAM.text("The selected Target is not affected by the selected Threat"));
			return;
		}
		
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
	
	ThreatRatingWizardPanel wizard;
	String selectedThreatName;
	String selectedTargetName;
}