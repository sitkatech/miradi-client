/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views;

import javax.swing.tree.DefaultTreeCellRenderer;

import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.project.Project;

public class BoldedStrategyIndicatorTableTree extends TreeTableWithStateSaving
{
	public BoldedStrategyIndicatorTableTree(Project projectToUse, GenericTreeTableModel treeTableModel)
	{
		super(projectToUse, treeTableModel);
		getTree().setCellRenderer(new BoldedStrategyIndicatorRenderer());
	}
	
	protected static class BoldedStrategyIndicatorRenderer extends Renderer
	{		
		public BoldedStrategyIndicatorRenderer()
		{
			indicatorRenderer.setFont(getBoldFont());
		}
		
		protected DefaultTreeCellRenderer getStrategyRenderer(Factor factor)
		{
			DefaultTreeCellRenderer renderer = super.getStrategyRenderer(factor);

			renderer.setFont(getBoldFont());
			return renderer;
		}
	}
}
