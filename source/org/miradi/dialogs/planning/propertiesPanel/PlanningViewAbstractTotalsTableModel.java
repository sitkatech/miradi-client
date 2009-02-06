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
package org.miradi.dialogs.planning.propertiesPanel;

import org.miradi.project.Project;
import org.miradi.utils.ColumnTagProvider;

abstract public class PlanningViewAbstractTotalsTableModel extends PlanningViewAbstractAssignmentTableModel implements ColumnTagProvider
{
	public PlanningViewAbstractTotalsTableModel(Project projectToUse)
	{
		super(projectToUse);
	}
	
	public int getColumnCount()
	{
		return 1;
	}
	
	public String getColumnTag(int column)
	{
		return getColumnName(column);
	}
}
