/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views;

import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.EAMTreeTableModelAdapter;
import org.conservationmeasures.eam.utils.TreeTableStateSaver;

public class TreeTableWithStateSaving extends TreeTableWithIcons
{
	public TreeTableWithStateSaving(Project projectToUse, GenericTreeTableModel treeTableModel)
	{
		super(treeTableModel);
		treeTableStateSaver = new TreeTableStateSaver(projectToUse, tree);
		treeTableModelAdapter = new EAMTreeTableModelAdapter(projectToUse, treeTableStateSaver, treeTableModel, tree);
	}
	
	public void restoreTreeState() throws Exception
	{
		treeTableStateSaver.restoreTreeState();
	}
	
	protected EAMTreeTableModelAdapter treeTableModelAdapter;
	protected TreeTableStateSaver treeTableStateSaver; 
}
