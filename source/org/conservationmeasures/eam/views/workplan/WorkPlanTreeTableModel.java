/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.workplan;

import javax.swing.tree.TreePath;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.GenericTreeTableModel;
import org.conservationmeasures.eam.views.TreeTableNode;

public class WorkPlanTreeTableModel extends GenericTreeTableModel
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
		return EAM.fieldLabel(ObjectType.TASK, columnTags[column]);
	}
	
	public Strategy getParentIntervention(Task activity)
	{
		TreePath interventionPath = getPathOfParent(activity.getType(), activity.getId());
		WorkPlanStrategy workPlanStrategy = (WorkPlanStrategy)interventionPath.getLastPathComponent();
		return workPlanStrategy.getIntervention();
	}
	
	public TreePath getPathOfParent(int objectType, BaseId objectId)
	{
		TreePath path = getPathOfNode(objectType, objectId);
		if(path == null)
			return null;
		return path.getParentPath();
	}
	
	public TreePath getPathOfNode(int objectType, BaseId objectId)
	{
		return findObject(getPathToRoot(), objectType, objectId);
	}

	public TreePath getPathToRoot()
	{
		return new TreePath(getRootWorkPlanObject());
	}
	
	TreeTableNode getRootWorkPlanObject()
	{
		return (TreeTableNode)getRoot();
	}
	
	public void objectiveWasModified()
	{
EAM.logDebug("objectiveWasModified");
		getRootStratPlanObject().rebuild();
		fireTreeStructureChanged(getRoot(), new Object[] {getRoot()}, null, null);
	}
	
	public void dataWasChanged(int objectType, BaseId objectId)
	{
		getRootStratPlanObject().rebuild();
		fireTreeStructureChanged(getRoot(), new Object[] {getRoot()}, null, null);
		
EAM.logDebug("dataWasChanged");
	}
	
	WorkPlanTreeTableNode getRootStratPlanObject()
	{
		return (WorkPlanTreeTableNode)getRoot();
	}

	public void idListWasChanged(int objectType, BaseId objectId, String newIdListAsString)
	{
		getRootStratPlanObject().rebuild();
		fireTreeStructureChanged(getRoot(), new Object[] {getRoot()}, null, null);

EAM.logDebug("idListWasChanged");
	}
	
	
	TreePath findObject(TreePath pathToStartSearch, int objectType, BaseId objectId)
	{
		WorkPlanTreeTableNode nodeToSearch = (WorkPlanTreeTableNode)pathToStartSearch.getLastPathComponent();
		if(nodeToSearch.getType() == objectType && nodeToSearch.getId().equals(objectId))
			return pathToStartSearch;
		
		for(int i = 0; i < nodeToSearch.getChildCount(); ++i)
		{
			TreeTableNode thisChild = nodeToSearch.getChild(i);
			TreePath childPath = pathToStartSearch.pathByAddingChild(thisChild);
			TreePath found = findObject(childPath, objectType, objectId);
			if(found != null)
				return found;
		}
		return null;
	}

	public static String[] columnTags = {"Item", "Resources", };
	Project project;
}
