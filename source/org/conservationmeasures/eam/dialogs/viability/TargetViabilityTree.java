/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.viability;

import javax.swing.JTable;
import javax.swing.tree.TreeSelectionModel;

import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.GenericTreeTableModel;
import org.conservationmeasures.eam.views.TreeTableWithColumnWidthSaving;

public class TargetViabilityTree extends TreeTableWithColumnWidthSaving 
{
	public TargetViabilityTree(Project projectToUse, GenericTreeTableModel targetViabilityModelToUse)
	{
		super(projectToUse, targetViabilityModelToUse);
		setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		getTree().setShowsRootHandles(true);
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		setColumnWidths();
	}

	// NOTE: This code is duplicated in PlanningTreeTable...should they be combined?
	private void setColumnWidths()
	{
		ViabilityTreeModel model = (ViabilityTreeModel) getTree().getModel();
		int columnCount = getColumnModel().getColumnCount();
		for (int i = 0; i < columnCount; ++i)
		{
			int realColumn = convertColumnIndexToModel(i);
			String columnTag = model.getColumnTag(realColumn);
			int columnWidth = getColumnWidth(realColumn, columnTag);
			setColumnWidth(realColumn, columnWidth);
		}
	}
	
	private int getColumnWidth(int columnIndex, String columnTag)
	{
		if (columnTag.equals("Item"))		
			return 400;
			
		return 200;
	}


}
