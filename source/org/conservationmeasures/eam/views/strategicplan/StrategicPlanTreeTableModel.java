/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import org.conservationmeasures.eam.objects.ConceptualModelIntervention;
import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.project.Project;

import com.java.sun.jtreetable.AbstractTreeTableModel;
import com.java.sun.jtreetable.TreeTableModel;

public class StrategicPlanTreeTableModel extends AbstractTreeTableModel
{
	public StrategicPlanTreeTableModel(Project project)
	{
		super(new StratPlanRoot(project));
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
		return intervention.getName();
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
		return intervention.getName();
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