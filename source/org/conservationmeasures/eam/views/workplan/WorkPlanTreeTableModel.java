/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.workplan;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.Project;

import com.java.sun.jtreetable.AbstractTreeTableModel;
import com.java.sun.jtreetable.TreeTableModel;

public class WorkPlanTreeTableModel extends AbstractTreeTableModel
{
	public WorkPlanTreeTableModel(Project projectToUse)
	{
		super(new WorkPlanRoot(projectToUse));
		project = projectToUse;
	}

	public int getColumnCount()
	{
		return columnTags.length;
	}

	public String getColumnName(int column)
	{
		return EAM.fieldLabel(ObjectType.INDICATOR, columnTags[column]);
	}

	public Class getColumnClass(int column)
	{
		if(column == 0)
			return TreeTableModel.class;
		return String.class;
	}

	public Object getValueAt(Object rawNode, int column)
	{
		WorkPlanNode node = (WorkPlanNode)rawNode;
		return node.getValueAt(column);
	}

	public int getChildCount(Object rawNode)
	{
		WorkPlanNode node = (WorkPlanNode)rawNode;
		return node.getChildCount();
	}

	public Object getChild(Object rawNode, int index)
	{
		WorkPlanNode node = (WorkPlanNode)rawNode;
		return node.getChild(index);
	}

	public static String[] columnTags = {"Item", "Resources", };
	Project project;
}
