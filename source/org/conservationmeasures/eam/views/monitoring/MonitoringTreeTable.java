/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.monitoring;

import javax.swing.tree.TreePath;

import org.conservationmeasures.eam.views.GenericTreeTableModel;
import org.conservationmeasures.eam.views.TreeTableNode;
import org.conservationmeasures.eam.views.TreeTableWithIcons;

public class MonitoringTreeTable extends TreeTableWithIcons
{
	public MonitoringTreeTable(GenericTreeTableModel monitoringModelToUse)
	{
		super(monitoringModelToUse);
		monitoringModel = monitoringModelToUse;
	}

	public void expandEverything()
	{
		MonitoringNode root = (MonitoringNode)getTreeTableModel().getRoot();
		TreePath rootPath = new TreePath(root);
		expandNode(rootPath);
	}
	
	public GenericTreeTableModel getTreeTableModel()
	{
		return monitoringModel;
	}

	public void expandNode(TreePath thisPath)
	{
		MonitoringNode topLevelObject = (MonitoringNode)thisPath.getLastPathComponent();
		getTree().expandPath(thisPath);
		for(int childIndex = 0; childIndex < topLevelObject.getChildCount(); ++childIndex)
		{
			TreeTableNode secondLevelObject = topLevelObject.getChild(childIndex);
			TreePath secondLevelPath = thisPath.pathByAddingChild(secondLevelObject);
			expandNode(secondLevelPath);
		}
	}
	
	GenericTreeTableModel monitoringModel;
}
