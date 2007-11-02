package org.conservationmeasures.eam.dialogs.treetables;

import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.ColumnWidthSaver;

abstract public class TreeTableWithColumnWidthSaving extends TreeTableWithStateSaving
{
	public TreeTableWithColumnWidthSaving(Project projectToUse, GenericTreeTableModel treeTableModel)
	{
		super(projectToUse, treeTableModel);
		columnWidthSaver = new ColumnWidthSaver(this, treeTableModel, getUniqueTableIdentifier());
		getTableHeader().addMouseListener(columnWidthSaver);
	}
	
	public void rebuildTableCompletely()
	{
		super.rebuildTableCompletely();
		columnWidthSaver.restoreColumnWidths();
	}

	abstract public String getUniqueTableIdentifier();
	
	private ColumnWidthSaver columnWidthSaver;
}
