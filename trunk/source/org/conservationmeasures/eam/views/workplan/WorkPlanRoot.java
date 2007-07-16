/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.workplan;

import java.util.Vector;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.TreeTableNode;

public class WorkPlanRoot extends TreeTableNode
{
	public WorkPlanRoot(Project projectToUse)
	{
		project = projectToUse;
		rebuild();
	}
	
	public BaseObject getObject()
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
		return "";
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
