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
	public ThreatRatingWizardChooseBundle(ThreatMatrixView viewToUse)
	{
		super(new BorderLayout());
		view = viewToUse;
		ThreatMatrixTableModel model = view.getModel();
		
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

			view.selectBundle(getSelectedBundle());
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}
	
	public void buttonPressed(String buttonName)
	{
		EAM.logDebug("Button pressed: " + buttonName);
	}

	public ThreatRatingBundle getSelectedBundle() throws Exception
	{
		ThreatMatrixTableModel model = view.getModel();
		int threatId = model.findThreatByName(selectedThreatName);
		int targetId = model.findTargetByName(selectedTargetName);
		ThreatRatingBundle bundle = model.getBundle(threatId, targetId);
		return bundle;
	}
	
	ThreatMatrixView view;
	String selectedThreatName;
	String selectedTargetName;
}