/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.viability;

import java.util.Vector;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objectpools.EAMObjectPool;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.TreeTableNode;

public class ViabilityTargetsNode extends TreeTableNode
{
	public ViabilityTargetsNode(Project projectToUse)
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
		return new ORef(ObjectType.FAKE, new BaseId(Target.getObjectType()));
	}
	
	public int getType()
	{
		return ObjectType.FAKE;
	}

	public Object getValueAt(int column)
	{
		return "";
	}

	public String toString()
	{
		return "Targets";
	}
	
	public BaseId getId()
	{
		return null;
	}
	
	public void rebuild()
	{
		Vector vector = new Vector();
		EAMObjectPool pool  = project.getPool(Target.getObjectType());
		BaseId[] baseIds = pool.getIds();
		for (int i=0; i< baseIds.length; ++i)
		{
			vector.add(new TargetViabilityNode(project, (FactorId) baseIds[i]));
		}
		children = vector;
	}
	
	Project project;
	Vector children;
}

