/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.workplan;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.conservationmeasures.eam.icons.GoalIcon;
import org.conservationmeasures.eam.icons.IndicatorIcon;
import org.conservationmeasures.eam.icons.InterventionIcon;
import org.conservationmeasures.eam.icons.ObjectiveIcon;
import org.conservationmeasures.eam.objecthelpers.ObjectType;

import com.java.sun.jtreetable.JTreeTable;

public class WorkPlanTreeTable extends JTreeTable
{
	public WorkPlanTreeTable(WorkPlanTreeTableModel monitoringModelToUse)
	{
		super(monitoringModelToUse);
		workPlanTreeTableModel = monitoringModelToUse;
		setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		getTree().setShowsRootHandles(true);
		getTree().setRootVisible(false);
		getTree().setCellRenderer(new Renderer());
		getTree().setEditable(false);
		getColumnModel().getColumn(0).setPreferredWidth(200);

	}

	void expandEverything()
	{
		WorkPlanNode root = (WorkPlanNode)getTreeTableModel().getRoot();
		TreePath rootPath = new TreePath(root);
		expandNode(rootPath);
	}
	
	public WorkPlanTreeTableModel getTreeTableModel()
	{
		return workPlanTreeTableModel;
	}

	private void expandNode(TreePath thisPath)
	{
		WorkPlanNode topLevelObject = (WorkPlanNode)thisPath.getLastPathComponent();
		getTree().expandPath(thisPath);
		for(int childIndex = 0; childIndex < topLevelObject.getChildCount(); ++childIndex)
		{
			WorkPlanNode secondLevelObject = (WorkPlanNode)topLevelObject.getChild(childIndex);
			TreePath secondLevelPath = thisPath.pathByAddingChild(secondLevelObject);
			expandNode(secondLevelPath);
		}
	}

	static class Renderer extends DefaultTreeCellRenderer
	{
		public Renderer()
		{
			
			interventionRenderer = new DefaultTreeCellRenderer();
			interventionRenderer.setClosedIcon(new InterventionIcon());
			interventionRenderer.setOpenIcon(new InterventionIcon());
			interventionRenderer.setLeafIcon(new InterventionIcon());
			
			objectiveRenderer = new DefaultTreeCellRenderer();
			objectiveRenderer.setClosedIcon(new ObjectiveIcon());
			objectiveRenderer.setOpenIcon(new ObjectiveIcon());
			objectiveRenderer.setLeafIcon(new ObjectiveIcon());

			indicatorRenderer = new DefaultTreeCellRenderer();
			indicatorRenderer.setClosedIcon(new IndicatorIcon());
			indicatorRenderer.setOpenIcon(new IndicatorIcon());
			indicatorRenderer.setLeafIcon(new IndicatorIcon());
			
			goalRenderer = new DefaultTreeCellRenderer();
			goalRenderer.setClosedIcon(new GoalIcon());
			goalRenderer.setOpenIcon(new GoalIcon());
			goalRenderer.setLeafIcon(new GoalIcon());

			defaultRenderer = new DefaultTreeCellRenderer();
		}

		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocusToUse)
		{
			TreeCellRenderer renderer = defaultRenderer;
			
			WorkPlanNode node = (WorkPlanNode)value;
			if(node.getType() == ObjectType.INDICATOR)
				renderer = indicatorRenderer;
			else if(node.getType() == ObjectType.MODEL_NODE)
				renderer = interventionRenderer;
			if(node.getType() == ObjectType.OBJECTIVE)
				renderer = objectiveRenderer;
			else if(node.getType() == ObjectType.GOAL)
				renderer = goalRenderer;
			
			return renderer.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		}
		
		DefaultTreeCellRenderer objectiveRenderer;
		DefaultTreeCellRenderer goalRenderer;
		DefaultTreeCellRenderer indicatorRenderer;
		DefaultTreeCellRenderer defaultRenderer;
		DefaultTreeCellRenderer interventionRenderer;
	}
	
	WorkPlanTreeTableModel workPlanTreeTableModel;
}
