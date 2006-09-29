/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import java.util.Vector;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objectpools.DesirePool;
import org.conservationmeasures.eam.objects.Desire;
import org.conservationmeasures.eam.project.Project;

public class StratPlanRoot extends StratPlanObject
{
	public StratPlanRoot(Project projectToUse)
	{
		project = projectToUse;
		rebuild();
	}
	
	public Object getValueAt(int column)
	{
		return "";
	}

	public int getChildCount()
	{
		return desireNodes.length;
	}

	public Object getChild(int index)
	{
		return desireNodes[index];
	}
	
	public String toString()
	{
		return project.getName();
	}

	public int getType()
	{
		return -1;
	}
	
	public BaseId getId()
	{
		return BaseId.INVALID;
	}
	
	public boolean canInsertActivityHere()
	{
		return false;
	}
	
	public void rebuild()
	{
		Vector desireVector = new Vector();
		desireVector.addAll(getAllDesires(project.getGoalPool()));
		desireVector.addAll(getAllDesires(project.getObjectivePool()));
		desireNodes = (StratPlanDesire[])desireVector.toArray(new StratPlanDesire[0]);
	}

	private Vector getAllDesires(DesirePool pool)
	{
		BaseId[] desireIds = pool.getIds();
		Vector desires = new Vector();
		for(int i = 0; i < desireIds.length; ++i)
		{
			Desire desire = pool.findDesire(desireIds[i]);
			desires.add(new StratPlanDesire(project, desire));
		}
		return desires;
	}
	
	Project project;
	StratPlanDesire[] desireNodes;
}

