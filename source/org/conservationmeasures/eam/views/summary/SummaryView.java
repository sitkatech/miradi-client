/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.summary;

import java.awt.BorderLayout;

import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;

public class SummaryView extends UmbrellaView
{
	public SummaryView(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
		setToolBar(new SummaryToolBar(mainWindowToUse.getActions()));
		
		lastKnownSplitLocation = -1;

		bigSplitter = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		add(bigSplitter, BorderLayout.CENTER);
	}

	public String cardName() 
	{
		return getViewName();
	}
	
	static public String getViewName()
	{
		return "Summary ";
	}

	public void becomeActive() throws Exception
	{
		wizardPanel = new SummaryWizardPanel();
		summaryPanel = new SummaryPanel(getProject());
		
		JScrollPane bottomHalf = new JScrollPane(summaryPanel);
		
		bigSplitter.setTopComponent(wizardPanel);
		bigSplitter.setBottomComponent(bottomHalf);
		bigSplitter.validate();
		if(lastKnownSplitLocation > 0)
			bigSplitter.setDividerLocation(lastKnownSplitLocation);
		else
			bigSplitter.setDividerLocation(.5);
	}

	public void becomeInactive() throws Exception
	{
		lastKnownSplitLocation = bigSplitter.getDividerLocation();
		bigSplitter.removeAll();
		wizardPanel = null;
		summaryPanel = null;
	}

	int lastKnownSplitLocation;
	JSplitPane bigSplitter;
	SummaryWizardPanel wizardPanel;
	SummaryPanel summaryPanel;
}
