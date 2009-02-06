/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.views.diagram;

import org.miradi.dialogs.base.ObjectPoolTableModel;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.ResultsChainDiagram;

public class ResultsChainPageList extends DiagramPageList
{
	public ResultsChainPageList(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, new ObjectPoolTableModel(mainWindowToUse.getProject(), ObjectType.RESULTS_CHAIN_DIAGRAM, getTags()), UNIQUE_IDENTIFIER);
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
	
	public static final String UNIQUE_IDENTIFIER = "ResultsChainPageList";
}
