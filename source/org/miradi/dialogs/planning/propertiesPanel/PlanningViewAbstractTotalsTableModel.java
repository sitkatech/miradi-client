/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.planning.propertiesPanel;

import org.miradi.project.Project;
import org.miradi.utils.ColumnTagProvider;

abstract public class PlanningViewAbstractTotalsTableModel extends PlanningViewAbstractAssignmentTabelModel implements ColumnTagProvider
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
