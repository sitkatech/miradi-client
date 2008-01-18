/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.diagram;

import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.diagram.DiagramSplitPane;
import org.conservationmeasures.eam.views.diagram.ResultsChainDiagramSplitPane;

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
}
