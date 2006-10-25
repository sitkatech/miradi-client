/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

import org.conservationmeasures.eam.icons.GoalIcon;
import org.conservationmeasures.eam.icons.InterventionIcon;
import org.conservationmeasures.eam.icons.ObjectiveIcon;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.utils.EAMTreeTableModelAdapter;

import com.java.sun.jtreetable.JTreeTable;
import com.java.sun.jtreetable.TreeTableModel;

public class StrategicPlanTreeTable extends JTreeTable
{
	public StrategicPlanTreeTable(TreeTableModel treeTableModel)
	{
		super(treeTableModel);
		super.setModel(new EAMTreeTableModelAdapter(treeTableModel, tree));
		DefaultTreeCellRenderer renderer = new Renderer();
		tree.setCellRenderer(renderer);
		tree.setRootVisible(false);
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

			goalRenderer = new DefaultTreeCellRenderer();
			goalRenderer.setClosedIcon(new GoalIcon());
			goalRenderer.setOpenIcon(new GoalIcon());
			goalRenderer.setLeafIcon(new GoalIcon());

			defaultRenderer = new DefaultTreeCellRenderer();
		}

		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocusToUse)
		{
			TreeCellRenderer renderer = defaultRenderer;
			
			StratPlanObject object = (StratPlanObject)value;
			if(object.getType() == ObjectType.MODEL_NODE)
				renderer = interventionRenderer;
			else if(object.getType() == ObjectType.OBJECTIVE)
				renderer = objectiveRenderer;
			else if(object.getType() == ObjectType.GOAL)
				renderer = goalRenderer;
			
			return renderer.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		}
		
		DefaultTreeCellRenderer objectiveRenderer;
		DefaultTreeCellRenderer goalRenderer;
		DefaultTreeCellRenderer interventionRenderer;
		DefaultTreeCellRenderer defaultRenderer;
	}
}
