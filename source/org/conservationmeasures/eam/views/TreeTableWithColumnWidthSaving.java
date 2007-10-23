package org.conservationmeasures.eam.views;

import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.ColumnWidthSaver;

abstract public class TreeTableWithColumnWidthSaving extends TreeTableWithStateSaving
{
	public TreeTableWithColumnWidthSaving(Project projectToUse, GenericTreeTableModel treeTableModel)
	{
		super(projectToUse, treeTableModel);
		getTableHeader().addMouseListener(new ColumnWidthSaver(this));
	}
	
	abstract public String getUniqueTableIdentifier();
}
