/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.diagram;

import org.miradi.actions.ActionCreateResultsChain;
import org.miradi.actions.ActionDeleteResultsChain;
import org.miradi.actions.ActionDiagramProperties;
import org.miradi.dialogs.base.ObjectPoolTableModel;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.ResultsChainDiagram;

public class ResultsChainPageList extends DiagramPageList
{
	public ResultsChainPageList(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, new ObjectPoolTableModel(mainWindowToUse.getProject(), ObjectType.RESULTS_CHAIN_DIAGRAM, getTags()));
	}
	
	private static String[] getTags()
	{
		return new String[] {ResultsChainDiagram.PSEUDO_COMBINED_LABEL};
	}

	public boolean isConceptualModelPageList()
	{
		return false;
	}

	public boolean isResultsChainPageList()
	{
		return true;
	}

	public int getManagedDiagramType()
	{
		return ObjectType.RESULTS_CHAIN_DIAGRAM;
	}

	public Class[] getPopUpMenuActions()
	{
		return  new Class[] {
				ActionCreateResultsChain.class,
				ActionDeleteResultsChain.class,
				ActionDiagramProperties.class,
		};
	}
}
