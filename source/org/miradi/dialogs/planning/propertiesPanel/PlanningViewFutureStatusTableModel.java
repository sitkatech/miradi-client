/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.planning.propertiesPanel;

import org.miradi.dialogs.treetables.TreeTableNode;
import org.miradi.main.EAM;
import org.miradi.objects.Indicator;
import org.miradi.project.Project;

import com.java.sun.jtreetable.TreeTableModelAdapter;

public class PlanningViewFutureStatusTableModel extends PlanningViewAbstractTreeTableSyncedTableModel
{
	public PlanningViewFutureStatusTableModel(Project projectToUse, TreeTableModelAdapter adapterToUse) throws Exception
	{
		super(projectToUse, adapterToUse);
	}

	public int getColumnCount()
	{
		return columnTags.length;
	}
	
	public String getColumnName(int column)
	{
		return EAM.fieldLabel(Indicator.getObjectType(), columnTags[column]);
	}
	
	public Object getValueAt(int row, int column)
	{
		TreeTableNode node = getNodeForRow(row);
		if (node.getType() != Indicator.getObjectType())
			return "";
		
		return node.getObject().getData(columnTags[column]);
	}
	
	public final static String[] columnTags = {Indicator.TAG_FUTURE_STATUS_DATE, Indicator.TAG_FUTURE_STATUS_SUMMARY};
}
