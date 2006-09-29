/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.monitoring;

import java.util.Vector;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objectpools.DesirePool;
import org.conservationmeasures.eam.objects.Desire;
import org.conservationmeasures.eam.project.Project;

public class MonitoringRootNode extends MonitoringNode
{
	public MonitoringRootNode(Project projectToUse)
	{
		project = projectToUse;
		rebuild();
	}
	
	public int getType()
	{
		return -1;
	}

	public String toString()
	{
		return getClass().toString();
	}
	
	public int getChildCount()
	{
		return children.size();
	}

	public MonitoringNode getChild(int index)
	{
		return (MonitoringNode)children.get(index);
	}

	public Object getValueAt(int column)
	{
		return "";
	}

	private void rebuild()
	{
		Vector desireVector = new Vector();
		desireVector.addAll(getAllDesires(project.getGoalPool()));
		desireVector.addAll(getAllDesires(project.getObjectivePool()));
		children = desireVector;
	}

	private Vector getAllDesires(DesirePool pool)
	{
		BaseId[] desireIds = pool.getIds();
		Vector desires = new Vector();
		for(int i = 0; i < desireIds.length; ++i)
		{
			Desire desire = pool.findDesire(desireIds[i]);
			desires.add(new MonitoringDesireNode(project, desire));
		}
		return desires;
	}
	
	Project project;
	Vector children;
}
