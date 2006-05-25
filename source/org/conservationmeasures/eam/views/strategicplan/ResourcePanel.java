/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import org.conservationmeasures.eam.annotations.ResourcePool;
import org.conservationmeasures.eam.project.Project;
import org.martus.swing.UiScrollPane;
import org.martus.swing.UiTable;
import org.martus.swing.UiTableModel;

public class ResourcePanel extends JPanel
{
	public ResourcePanel(Project project)
	{
		super(new BorderLayout());
		ResourceTableModel model = new ResourceTableModel(project.getResourcePool());
		UiTable table = new UiTable(model);
		add(new UiScrollPane(table));
	}
	
	static class ResourceTableModel extends UiTableModel
	{
		public ResourceTableModel(ResourcePool resourcePool)
		{
			resources = resourcePool;
		}
		
		public boolean isEnabled(int row)
		{
			return false;
		}

		public int getColumnCount()
		{
			return 3;
		}

		public int getRowCount()
		{
			return resources.size();
		}

		public Object getValueAt(int rowIndex, int columnIndex)
		{
			return "Hello";
		}

		public String getColumnName(int column)
		{
			switch(column)
			{
				case 0: return "Name";
				case 1: return "Description";
				case 2: return "Rate";
				default: throw new RuntimeException("Unknown column " + column);
			}
		}
		
		ResourcePool resources;
	}
}
