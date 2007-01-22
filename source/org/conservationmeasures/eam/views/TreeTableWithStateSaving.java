/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views;

import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.EAMTreeTableModelAdapter;
import org.conservationmeasures.eam.utils.TreeTableStateSaver;

public class TreeTableWithStateSaving extends TreeTableWithIcons
{
	public TreeTableWithStateSaving(Project projectToUse, GenericTreeTableModel treeTableModel)
	{
		super(projectToUse, treeTableModel);
		treeTableStateSaver = new TreeTableStateSaver(projectToUse, tree);
		treeTableModelAdapter = new EAMTreeTableModelAdapter(projectToUse, treeTableStateSaver, treeTableModel, tree);
	}
	
	public void dispose()
	{
		treeTableStateSaver.dispose();
	}
	
	public void restoreTreeState() throws Exception
	{
		treeTableStateSaver.restoreTreeState();
	}
	
	protected EAMTreeTableModelAdapter treeTableModelAdapter;
	protected TreeTableStateSaver treeTableStateSaver; 
}
