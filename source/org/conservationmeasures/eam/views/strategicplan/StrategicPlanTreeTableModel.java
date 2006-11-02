/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import org.conservationmeasures.eam.objects.ConceptualModelIntervention;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.GenericTreeTableModel;
import org.conservationmeasures.eam.views.TreeTableNode;

public class StrategicPlanTreeTableModel extends GenericTreeTableModel
{
	static public StrategicPlanTreeTableModel createForProject(Project project)
	{
		return new StrategicPlanTreeTableModel(project, new StratPlanRoot(project));
	}
	
	static public StrategicPlanTreeTableModel createForStrategy(Project project, ConceptualModelIntervention strategy)
	{
		return new StrategicPlanTreeTableModel(project, new StratPlanStrategy(project, strategy));
	}
	
	
	private StrategicPlanTreeTableModel(Project projectToUse, TreeTableNode root)
	{
		super(root);
		project = projectToUse;
	}
	
//	TODO remove when transition is over
	/*public ConceptualModelIntervention getParentIntervention(Task activity)
	{
		TreePath interventionPath = getPathOfParent(activity.getType(), activity.getId());
		StratPlanStrategy strategy = (StratPlanStrategy)interventionPath.getLastPathComponent();
		return strategy.getIntervention();
	}*/
	
	public int getColumnCount()
	{
		return columnNames.length;
	}

	public String getColumnName(int column)
	{
		return columnNames[column];
	}

//	TODO remove when transition is over
	/*public TreePath getPathOfNode(int objectType, BaseId objectId)
	{
		return findObject(getPathToRoot(), objectType, objectId);
	}

	public TreePath getPathToRoot()
	{
		return new TreePath(getRootStratPlanObject());
	}
	
	public TreePath getPathOfParent(int objectType, BaseId objectId)
	{
		TreePath path = getPathOfNode(objectType, objectId);
		if(path == null)
			return null;
		return path.getParentPath();
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

	public void idListWasChanged(int objectType, BaseId objectId, String newIdListAsString)
	{
		getRootStratPlanObject().rebuild();
		fireTreeStructureChanged(getRoot(), new Object[] {getRoot()}, null, null);

EAM.logDebug("idListWasChanged");
	}
	
	TreePath findObject(TreePath pathToStartSearch, int objectType, BaseId objectId)
	{
		StratPlanObject nodeToSearch = (StratPlanObject)pathToStartSearch.getLastPathComponent();
		if(nodeToSearch.getType() == objectType && nodeToSearch.getId().equals(objectId))
			return pathToStartSearch;
		
		for(int i = 0; i < nodeToSearch.getChildCount(); ++i)
		{
			StratPlanObject thisChild = (StratPlanObject)nodeToSearch.getChild(i);
			TreePath childPath = pathToStartSearch.pathByAddingChild(thisChild);
			TreePath found = findObject(childPath, objectType, objectId);
			if(found != null)
				return found;
		}
		
		return null;
	}
	
	StratPlanObject getRootStratPlanObject()
	{
		return (StratPlanObject)getRoot();
	}
	*/

	public static final int labelColumn = 0;
	public static final int resourcesColumn = 1;
	public static final int budgetColumn = 2;
	public static final int datesColumn = 3;
	
	static final String[] columnNames = {"Item", "Resources", "Budget", "Dates", };

	Project project;
}

