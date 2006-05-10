/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import javax.swing.tree.TreePath;

import org.conservationmeasures.eam.objects.ConceptualModelIntervention;
import org.conservationmeasures.eam.project.Project;

import com.java.sun.jtreetable.AbstractTreeTableModel;
import com.java.sun.jtreetable.TreeTableModel;

public class StrategicPlanTreeTableModel extends AbstractTreeTableModel
{
	static public StrategicPlanTreeTableModel createForProject(Project project)
	{
		return new StrategicPlanTreeTableModel(project, new StratPlanRoot(project));
	}
	
	static public StrategicPlanTreeTableModel createForStrategy(Project project, ConceptualModelIntervention strategy)
	{
		return new StrategicPlanTreeTableModel(project, new StratPlanStrategy(project, strategy));
	}
	
	
	private StrategicPlanTreeTableModel(Project projectToUse, StratPlanObject root)
	{
		super(root);
		project = projectToUse;
	}
	
	public int getColumnCount()
	{
		return 1;
	}

	public String getColumnName(int column)
	{
		return "Item";
	}

	public Object getValueAt(Object node, int column)
	{
		return ((StratPlanObject)node).getValueAt(column);
	}

	public int getChildCount(Object parent)
	{
		return ((StratPlanObject)parent).getChildCount();
	}

	public Object getChild(Object parent, int index)
	{
		return ((StratPlanObject)parent).getChild(index);
	}

	public Class getColumnClass(int column)
	{
		if(column == 0)
			return TreeTableModel.class;
		return String.class;
	}

	public void idListWasChanged(int objectType, int objectId, String newIdListAsString)
	{
		TreePath found = findObject(new TreePath(getRootStratPlanObject()), objectType, objectId);
		if(found == null)
			return;
		
		StratPlanObject parent = (StratPlanObject)found.getLastPathComponent();
		parent.rebuild();

		int[] childIndices = new int[parent.getChildCount()];
		Object[] children = new Object[parent.getChildCount()];
		for(int i = 0; i < childIndices.length; ++i)
		{
			childIndices[i] = i;
			children[i] = parent.getChild(i);
		}
		fireTreeStructureChanged(parent, found.getPath(), childIndices, children);
	}
	
	TreePath findObject(TreePath pathToStartSearch, int objectType, int objectId)
	{
		StratPlanObject nodeToSearch = (StratPlanObject)pathToStartSearch.getLastPathComponent();
		if(nodeToSearch.getType() == objectType && nodeToSearch.getId() == objectId)
			return pathToStartSearch;
		
		for(int i = 0; i < nodeToSearch.getChildCount(); ++i)
		{
			StratPlanObject thisChild = (StratPlanObject)nodeToSearch.getChild(i);
			TreePath childPath = pathToStartSearch.pathByAddingChild(thisChild);
			TreePath found = findObject(childPath, objectType, objectId);
			if(found != null)
				return childPath;
		}
		
		return null;
	}
	
	StratPlanObject getRootStratPlanObject()
	{
		return (StratPlanObject)getRoot();
	}

	Project project;
}

