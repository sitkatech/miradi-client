/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.diagram;

import org.miradi.actions.ActionCreateResultsChain;
import org.miradi.actions.ActionDeleteResultsChain;
import org.miradi.actions.ActionDiagramProperties;
import org.miradi.actions.ActionRenameResultsChain;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ObjectType;

public class ResultsChainDiagramSplitPane extends DiagramSplitPane
{
	public ResultsChainDiagramSplitPane(MainWindow mainWindow) throws Exception
	{
		super(mainWindow, ObjectType.RESULTS_CHAIN_DIAGRAM, "ResultsChainDiagramSplitPane");
	}

	public DiagramLegendPanel createLegendPanel(MainWindow mainWindow)
	{
		return new ResultsChainDiagramLegendPanel(mainWindow);
	}
	
	public DiagramPageList createPageList(MainWindow mainWindowToUse)
	{
		return new ResultsChainPageList(mainWindowToUse);
	}
	
	public Class[] getPopUpMenuActions()
	{
		return  new Class[] {
				ActionDiagramProperties.class,
				null,
				ActionCreateResultsChain.class,
				ActionRenameResultsChain.class,
				ActionDeleteResultsChain.class,
		};
	}
}
