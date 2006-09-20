/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.monitoring;

import java.util.Vector;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.project.Project;

public class MonitoringRootNode extends MonitoringNode
{
	public MonitoringRootNode(Project projectToUse)
	{
		project = projectToUse;
		children = new Vector();
		BaseId[] objectiveIds = project.getObjectivePool().getIds();
		for(int i = 0; i < objectiveIds.length; ++i)
		{
			BaseId id = objectiveIds[i];
			Objective objective = (Objective)project.findObject(ObjectType.OBJECTIVE, id);
			MonitoringObjective node = new MonitoringObjective(objective);
			children.add(node);
		}
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

	Project project;
	Vector children;
}
