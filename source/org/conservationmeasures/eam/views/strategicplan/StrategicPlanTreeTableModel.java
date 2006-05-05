/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import org.conservationmeasures.eam.objects.ActivityInsertionPoint;
import org.conservationmeasures.eam.objects.ConceptualModelIntervention;
import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.project.Project;

import com.java.sun.jtreetable.AbstractTreeTableModel;
import com.java.sun.jtreetable.TreeTableModel;

public class StrategicPlanTreeTableModel extends AbstractTreeTableModel
{
	static public StrategicPlanTreeTableModel createForProject(Project project)
	{
		return new StrategicPlanTreeTableModel(new StratPlanRoot(project));
	}
	
	static public StrategicPlanTreeTableModel createForStrategy(ConceptualModelIntervention strategy)
	{
		return new StrategicPlanTreeTableModel(new StratPlanStrategy(strategy));
	}
	
	
	private StrategicPlanTreeTableModel(StratPlanObject root)
	{
		super(root);
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



}

abstract class StratPlanObject
{
	abstract public Object getValueAt(int column);
	abstract public int getChildCount();
	abstract public Object getChild(int index);
	
	abstract public String toString();
	
	public boolean canInsertActivityHere()
	{
		return getActivityInsertionPoint().isValid();
	}

	public ActivityInsertionPoint getActivityInsertionPoint()
	{
		return new ActivityInsertionPoint();
	}

}

class StratPlanRoot extends StratPlanObject
{
	public StratPlanRoot(Project projectToUse)
	{
		project = projectToUse;
		ConceptualModelNode[] interventionObjects = project.getNodePool().getInterventions();
		strategies = new StratPlanStrategy[interventionObjects.length];
		for(int i = 0; i < strategies.length; ++i)
			strategies[i] = new StratPlanStrategy((ConceptualModelIntervention)interventionObjects[i]);
	}
	
	public Object getValueAt(int column)
	{
		return "";
	}

	public int getChildCount()
	{
		return strategies.length;
	}

	public Object getChild(int index)
	{
		return strategies[index];
	}
	
	public String toString()
	{
		return project.getName();
	}

	Project project;
	StratPlanStrategy[] strategies;
}

class StratPlanStrategy extends StratPlanObject
{
	public StratPlanStrategy(ConceptualModelIntervention interventionToUse)
	{
		intervention = interventionToUse;
	}
	
	public Object getValueAt(int column)
	{
		return intervention.getLabel();
	}

	public int getChildCount()
	{
		return 0;
	}

	public Object getChild(int index)
	{
		return null;
	}
	
	public String toString()
	{
		return intervention.getLabel();
	}

	public ActivityInsertionPoint getActivityInsertionPoint()
	{
		return new ActivityInsertionPoint(intervention.getId(), intervention.getActivityIds().size());
	}

	ConceptualModelIntervention intervention;
}

class StratPlanActivity extends StratPlanObject
{

	public Object getValueAt(int column)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public int getChildCount()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public Object getChild(int index)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	public String toString()
	{
		return "";
	}

}
