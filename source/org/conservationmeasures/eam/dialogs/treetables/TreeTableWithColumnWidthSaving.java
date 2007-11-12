/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/
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
