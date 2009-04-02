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
package org.miradi.dialogs.planning.upperPanel;

import org.miradi.main.EAM;
import org.miradi.project.Project;
import org.miradi.utils.CodeList;
import org.miradi.views.planning.ColumnManager;
import org.miradi.views.planning.RowManager;

public class ObjectsOnlyPlanningTreeTableModel extends PlanningTreeTableModel
{
	public ObjectsOnlyPlanningTreeTableModel(Project project) throws Exception
	{
		super(project, getVisibleRowCodes(project), getVisibleColumnCodes(project));
	}

	private static CodeList getVisibleColumnCodes(Project projectToUse) throws Exception
	{
		return ColumnManager.getVisibleColumnsForSingleType(projectToUse.getCurrentViewData());
	}
	
	public static CodeList getVisibleRowCodes(Project projectToUse) throws Exception
	{
		return RowManager.getVisibleRowsForSingleType(projectToUse.getCurrentViewData());
	}

	@Override
	public void updateColumnsToShow() throws Exception
	{
	}
	
	@Override
	public CodeList getRowCodesToShow()
	{
		try
		{
			return getVisibleRowCodes(getProject());
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return new CodeList();
		}
	}
}
