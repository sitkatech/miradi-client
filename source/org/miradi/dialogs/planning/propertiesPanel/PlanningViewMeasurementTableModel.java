/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.planning.propertiesPanel;

import org.miradi.dialogs.treetables.TreeTableNode;
import org.miradi.main.EAM;
import org.miradi.objects.Measurement;
import org.miradi.project.Project;

import com.java.sun.jtreetable.TreeTableModelAdapter;

public class PlanningViewMeasurementTableModel extends PlanningViewAbstractTreeTableSyncedTableModel
{
	public PlanningViewMeasurementTableModel(Project projectToUse, TreeTableModelAdapter adapterToUse) throws Exception
	{
		super(projectToUse, adapterToUse);
	}

	public int getColumnCount()
	{
		return columnTags.length;
	}
	
	public String getColumnName(int column)
	{
		return EAM.fieldLabel(Measurement.getObjectType(), columnTags[column]);
	}
	
	public Object getValueAt(int row, int column)
	{
		TreeTableNode node = getNodeForRow(row);
		if (node.getType() != Measurement.getObjectType())
			return "";
		
		return node.getObject().getData(columnTags[column]);
	}
	
	public final static String[] columnTags = {Measurement.TAG_DATE, Measurement.TAG_SUMMARY, Measurement.TAG_TREND, Measurement.TAG_STATUS_CONFIDENCE};
}
