/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs;

import java.util.Vector;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.KeyEcologicalAttribute;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.TreeTableNode;

public class KeyEcologicalAttributesNode extends TreeTableNode
{
	public KeyEcologicalAttributesNode(Project projectToUse, KeyEcologicalAttribute keaToUse)
	{
		project = projectToUse;
		kea = keaToUse;
		rebuild();
	}
	
	public EAMObject getObject()
	{
		return kea;
	}

	public int getChildCount()
	{
		return indicators.length;
	}

	public TreeTableNode getChild(int index)
	{
		return indicators[index];
	}

	public ORef getObjectReference()
	{
		return kea.getRef();
	}
	
	public int getType()
	{
		return ObjectType.KEY_ECOLOGICAL_ATTRIBUTE;
	}

	public Object getValueAt(int column)
	{
		if(column == 0)
			return kea.getLabel();
		return "";
	}

	public String toString()
	{
		return (String)getValueAt(0);
	}
	
	public BaseId getId()
	{
		return kea.getId();
	}

	public void rebuild()
	{
		int childCount = kea.getIndicatorIds().size();
		Vector indicatorVector = new Vector();
		for(int i = 0; i < childCount; ++i)
		{
			BaseId indicatorId = kea.getIndicatorIds().get(i);
			Indicator indicator = project.getIndicatorPool().find(indicatorId);
			indicatorVector.add(new KeyEcologicalAttributeIndicatorNode(project, indicator));
		}
		indicators = (KeyEcologicalAttributeIndicatorNode[])indicatorVector.toArray(new KeyEcologicalAttributeIndicatorNode[0]);
	}
	

	Vector children;
	Project project;
	KeyEcologicalAttribute kea;
	KeyEcologicalAttributeIndicatorNode[] indicators;

}

