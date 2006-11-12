/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.EAMTreeTableModelAdapter;
import org.conservationmeasures.eam.views.TreeTableWithIcons;

import com.java.sun.jtreetable.TreeTableModel;

public class StrategicPlanTreeTable extends TreeTableWithIcons
{
	public StrategicPlanTreeTable(Project projectToUse, TreeTableModel treeTableModel)
	{
		super(treeTableModel);
		setModel(new EAMTreeTableModelAdapter(projectToUse, treeTableModel, tree));
		DefaultTreeCellRenderer renderer = new Renderer();
		tree.setCellRenderer(renderer);
		tree.setRootVisible(false);
		TableCellEditor ce = new TreeTableCellEditor()
		{
			public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int r, int c)
			{
			    ((JTextField)getComponent()).setEditable(false);
				return super.getTableCellEditorComponent(table, value, isSelected, r, c);
			}
		};
		setDefaultEditor(TreeTableModel.class, ce);

	}
}
