/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.workplan;

import java.util.Vector;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.TreeTableNode;

public class WorkPlanRoot extends WorkPlanTreeTableNode
{
	public WorkPlanRoot(Project projectToUse)
	{
		project = projectToUse;
		rebuild();
	}
	
	public EAMObject getObject()
	{
		return null;
	}

	public TreeTableNode getChild(int index)
	{
		return (TreeTableNode)children.get(index);
	}

	public int getChildCount()
	{
		return children.size();
	}

	public ORef getObjectReference()
	{
		return null;
	}
	
	public int getType()
	{
		return ObjectType.FACTOR;
	}

	public Object getValueAt(int column)
	{
		return "";
	}

	public String toString()
	{
		return null;
	}
	
	public boolean canInsertActivityHere()
	{
		return false;
	}
	
	public BaseId getId()
	{
		return null;
	}
	public void rebuild()
	{
		Vector vector = new Vector();
		vector.add(new WorkPlanStrategyRoot(project));
		vector.add(new WorkPlanMonitoringRoot(project));
		children = vector;
	}
	
	Vector children;
	Project project;

}
