/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.diagram;

import javax.swing.Icon;

import org.miradi.icons.ResultsChainIcon;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.views.diagram.DiagramSplitPane;
import org.miradi.views.diagram.ResultsChainDiagramSplitPane;

public class ResultsChainDiagramPanel extends DiagramPanel
{
	public ResultsChainDiagramPanel(MainWindow mainWindowToUse) throws Exception
	{
		super(mainWindowToUse);
	}

	protected DiagramSplitPane createDiagramSplitter() throws Exception
	{
		return  new ResultsChainDiagramSplitPane(mainWindow);
	}

	public Icon getIcon()
	{
		return new ResultsChainIcon();
	}

	public String getTabName()
	{
		return EAM.text("Results Chains");
	}
}
