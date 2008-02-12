/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.diagram;

import javax.swing.Icon;

import org.miradi.dialogs.base.ModelessDialogPanel;
import org.miradi.main.MainWindow;
import org.miradi.objects.BaseObject;
import org.miradi.objects.DiagramFactor;
import org.miradi.utils.MiradiScrollPane;

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
