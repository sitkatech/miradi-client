/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.conservationmeasures.eam.icons.InterventionIcon;
import org.conservationmeasures.eam.objects.ObjectType;

import com.java.sun.jtreetable.JTreeTable;
import com.java.sun.jtreetable.TreeTableModel;

public class StrategicPlanTreeTable extends JTreeTable
{
	public StrategicPlanTreeTable(TreeTableModel treeTableModel)
	{
		super(treeTableModel);
		DefaultTreeCellRenderer renderer = new Renderer();
		tree.setCellRenderer(renderer);
	}

	static class Renderer extends DefaultTreeCellRenderer
	{
		public Renderer()
		{
			setClosedIcon(new InterventionIcon());
			setOpenIcon(new InterventionIcon());
			setLeafIcon(new InterventionIcon());
			defaultRenderer = new DefaultTreeCellRenderer();
		}

		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocusToUse)
		{
			StratPlanObject object = (StratPlanObject)value;
			System.out.println(object.getType());
			if(object.getType() == ObjectType.MODEL_NODE)
				return super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
			
			return defaultRenderer.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		}
		
		DefaultTreeCellRenderer defaultRenderer;
	}
}
