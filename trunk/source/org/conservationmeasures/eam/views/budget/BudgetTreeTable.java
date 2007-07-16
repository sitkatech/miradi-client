/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.budget;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.BoldedStrategyIndicatorTableTree;
import org.conservationmeasures.eam.views.TreeTableNode;

public class BudgetTreeTable extends BoldedStrategyIndicatorTableTree
{
	public BudgetTreeTable(Project projectToUse, BudgetTreeTableModel treeTableModel)
	{
		super(projectToUse, treeTableModel);
		setModel(treeTableModelAdapter);
		setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		getTree().setShowsRootHandles(true);
		
		setTableColumnRenderer();	
	}

	private void setTableColumnRenderer()
	{
		final int STARTING_TABLE_COLUMN = 1;
		int columnCount = getColumnModel().getColumnCount();
		
		for (int i  = STARTING_TABLE_COLUMN; i < columnCount; i++)
		{	
			TableColumn tableColumn = getColumnModel().getColumn(i);
			tableColumn.setCellRenderer(new FontRenderer(getTree()));
			tableColumn.setHeaderRenderer(new CenterAlignRender());
		}
	}
	
	public class CenterAlignRender extends DefaultTableCellRenderer 
	{
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		{
			Component component =  table.getTableHeader().getDefaultRenderer().getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			((JLabel) component).setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
			
			return component;
		}
	}

	protected static class FontRenderer extends DefaultTableCellRenderer
	{
		public FontRenderer(JTree treeToUse)
		{
			tree = treeToUse;
		
			defaultFont = getFont();
			boldFont = defaultFont.deriveFont(Font.BOLD);
			italicFont = defaultFont.deriveFont(Font.ITALIC);
			customFont = createFristLevelFont(defaultFont);
		    
			setHorizontalAlignment(SwingConstants.RIGHT);
		}
		
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		{
			Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			TreePath path = tree.getPathForRow(row);
			TreeTableNode node = (TreeTableNode)path.getLastPathComponent();
			component.setFont(getNodeFont(node));
			
			return component;
		}

		private Font getNodeFont(TreeTableNode node)
		{
			if (node.getType() == ObjectType.FAKE)
				return customFont;
			
			if(node.getType() == ObjectType.INDICATOR)
				return boldFont;
			
			if(Factor.isFactor(node.getType()))
				return getFactorFont((Factor)node.getObject());
			
			if(node.getType() == ObjectType.TASK)
				return getTaskFont((Task)node.getObject());

			return defaultFont;
		}

		private Font getFactorFont(Factor factor)
		{
			if (factor.isStrategy())
				return boldFont;
			
			return defaultFont;
		}

		private Font getTaskFont(Task task)
		{
			if (task.isActivity())
				return defaultFont;
			
			if (task.isMethod())
				return defaultFont;
			
			return italicFont;
		}
		
		Font defaultFont;
		Font boldFont;
		Font italicFont;
		Font customFont;
		JTree tree;
	}
}
