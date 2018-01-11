/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

import org.miradi.dialogs.diagram.ResultsChainPoolTableModel;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ObjectType;

public class ResultsChainPageList extends DiagramPageList
{
	public ResultsChainPageList(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, new ResultsChainPoolTableModel(mainWindowToUse.getProject()));
	}
	
	@Override
	public boolean isConceptualModelPageList()
	{
		return false;
	}

	@Override
	public boolean isResultsChainPageList()
	{
		return true;
	}

	@Override
	public int getManagedDiagramType()
	{
		return ObjectType.RESULTS_CHAIN_DIAGRAM;
	}
}
