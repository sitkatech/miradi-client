/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.planning.upperPanel;

import org.miradi.dialogs.tablerenderers.RowColumnBaseObjectProvider;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;
import org.miradi.utils.CodeList;
import org.miradi.utils.ExportableTableInterface;

public class ExportablePlanningTreeTableModel extends PlanningTreeTableModel implements ExportableTableInterface, RowColumnBaseObjectProvider
{
	public ExportablePlanningTreeTableModel(Project projectToUse, CodeList visibleRowCodesToUse, CodeList visibleColumnCodesToUse) throws Exception
	{
		super(projectToUse, visibleRowCodesToUse, visibleColumnCodesToUse);
		rowObjectRefs = getFullyExpandedRefList();
	}
	
	public BaseObject getBaseObjectForRowColumn(int row, int column)
	{
		return getBaseObjectForRow(row);
	}

	public BaseObject getBaseObjectForRow(int row)
	{
		ORef rowObjectRef = rowObjectRefs.get(row);
		if (rowObjectRef.isInvalid())
			return null;
		return getProject().findObject(rowObjectRef);
	}

	public int getDepth(int row)
	{
		return 0;
	}

	public String getHeaderFor(int column)
	{
		return getColumnName(column);
	}

	public int getMaxDepthCount()
	{
		return 0;
	}

	public int getRowCount()
	{
		return rowObjectRefs.size();
	}

	public Object getValueAt(int row, int column)
	{
		return null;
	}
	
	private ORefList rowObjectRefs;
}
