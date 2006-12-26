/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.budget;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.font.TextAttribute;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.conservationmeasures.eam.diagram.renderers.FactorRenderer;
import org.conservationmeasures.eam.main.AppPreferences;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.TreeTableNode;
import org.conservationmeasures.eam.views.TreeTableWithStateSaving;

public class BudgetTreeTable extends TreeTableWithStateSaving
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
			
			Map map = defaultFont.getAttributes();
		    map.put(TextAttribute.SIZE, new Float(15.0));
		    map.put(TextAttribute.FOREGROUND, Color.RED);
		    map.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_SEMIBOLD);
		    customFont = new Font(map);
		}
		
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		{
			Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			TreePath path = tree.getPathForRow(row);
			TreeTableNode node = (TreeTableNode)path.getLastPathComponent();
			component.setFont(getNodeFont(node));
			component.setForeground(getForegroundColor(node));
			
			return component;
		}

		private Color getForegroundColor(TreeTableNode node)
		{
			if(node.getType() == ObjectType.INDICATOR)
				return FactorRenderer.INDICATOR_COLOR;

			if (node.getType() == ObjectType.TASK)
				return getTaskColor((Task)node.getObject());

			if (node.getType() == ObjectType.FACTOR)
				return getFactorColor((Factor)node.getObject());
			
			return Color.BLACK;
		}
		
		private Color getTaskColor(Task task)
		{
			if (task.isActivity())
				return EAM.mainWindow.getColorPreference(AppPreferences.TAG_COLOR_ACTIVITIES).darker();
			if (task.isMethod())
				return FactorRenderer.INDICATOR_COLOR.darker();
			
			return Color.BLACK;
		}
		
		private Color getFactorColor(Factor factor)
		{
			if (factor.isStrategy())
				return EAM.mainWindow.getColorPreference(AppPreferences.TAG_COLOR_ACTIVITIES).darker();
			
			return Color.BLACK;
		}

		private Font getNodeFont(TreeTableNode node)
		{
			if (node.getType() == ObjectType.FAKE)
				return customFont;
			
			if(node.getType() == ObjectType.INDICATOR)
				return boldFont;
			
			if(node.getType() == ObjectType.FACTOR)
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
				return italicFont;
			if (task.isMethod())
				return italicFont;
			
			return defaultFont;
		}
		
		Font defaultFont;
		Font boldFont;
		Font italicFont;
		Font customFont;
		JTree tree;
	}
}
