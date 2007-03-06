/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs;

import java.util.Vector;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.objects.KeyEcologicalAttribute;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.TreeTableNode;

public class TargetViabilityRoot extends TreeTableNode
{
	public TargetViabilityRoot(Project projectToUse, FactorId targetId)
	{
		project = projectToUse;
		target = (Target)project.findNode(targetId);
		rebuild();
	}
	
	public EAMObject getObject()
	{
		return null;
	}

	public TreeTableNode getChild(int index)
	{
		return keyEcologicalAttributes[index];
	}

	public int getChildCount()
	{
		return keyEcologicalAttributes.length;
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
		int childCount = target.getKeyEcologicalAttributes().size();
		Vector KeyEcologicalAttributesVector = new Vector();
		for(int i = 0; i < childCount; ++i)
		{
			BaseId keaId = target.getKeyEcologicalAttributes().get(i);
			KeyEcologicalAttribute kea = project.getKeyEcologicalAttributePool().find(keaId);
			KeyEcologicalAttributesVector.add(new KeyEcologicalAttributesNode(project, kea));
		}
		
		keyEcologicalAttributes = (KeyEcologicalAttributesNode[])KeyEcologicalAttributesVector.toArray(new KeyEcologicalAttributesNode[0]);

	}
	
	Project project;
	Target target;
	KeyEcologicalAttributesNode[] keyEcologicalAttributes;

}
