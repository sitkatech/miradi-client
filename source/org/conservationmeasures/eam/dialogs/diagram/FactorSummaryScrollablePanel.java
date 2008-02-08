/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.diagram;

import javax.swing.Icon;

import org.conservationmeasures.eam.dialogs.base.ModelessDialogPanel;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.utils.MiradiScrollPane;

public class FactorSummaryScrollablePanel extends ModelessDialogPanel
{
	public FactorSummaryScrollablePanel(MainWindow mainWindowToUse, DiagramFactor diagramFactorToUse) throws Exception
	{
		summaryPanel = new FactorSummaryPanel(mainWindowToUse, diagramFactorToUse);
		MiradiScrollPane summaryScrollPane = new MiradiScrollPane(summaryPanel);
		add(summaryScrollPane);
	}
	
	public void dispose()
	{
		super.dispose();
		summaryPanel.dispose();
	}
	
	public String getPanelDescription()
	{
		return summaryPanel.getPanelDescription();
	}
	
	public Icon getIcon()
	{
		return summaryPanel.getIcon();
	}

	public BaseObject getObject()
	{
		return null;
	}

	private FactorSummaryPanel summaryPanel;
}
