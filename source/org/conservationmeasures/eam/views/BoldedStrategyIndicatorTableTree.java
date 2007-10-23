/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views;

import javax.swing.tree.DefaultTreeCellRenderer;

import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.project.Project;

public class BoldedStrategyIndicatorTableTree extends TreeTableWithColumnWidthSaving
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
