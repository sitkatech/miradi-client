/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.viability;


import java.util.Vector;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.KeyEcologicalAttribute;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.KeyEcologicalAttributeTypeQuestion;
import org.conservationmeasures.eam.views.TreeTableNode;

public class KeyEcologicalAttributeTypeNode extends TreeTableNode
{
	public KeyEcologicalAttributeTypeNode(Project projectToUse, String typeCodeToUse, Target targetToUse)
	{
		project = projectToUse;
		target = targetToUse;
		typeCode = typeCodeToUse;
		question = new KeyEcologicalAttributeTypeQuestion(KeyEcologicalAttribute.TAG_KEY_ECOLOGICAL_ATTRIBUTE_TYPE);
		label = question.findChoiceByCode(typeCode).getLabel();
		rebuild();
	}
	
	public BaseObject getObject()
	{
		return null;
	}

	public TreeTableNode getChild(int index)
	{
		return keaNodes[index];
	}

	public int getChildCount()
	{
		return keaNodes.length;
	}

	public ORef getObjectReference()
	{
		final int index = question.findIndexByCode(typeCode);
		return new ORef(ObjectType.FAKE, new BaseId(index));
	}
	
	public int getType()
	{
		return getObjectReference().getObjectType();
	}

	public Object getValueAt(int column)
	{
		return "";
	}

	public String toString()
	{
		 return label;
	}
	
	public BaseId getId()
	{
		return null;
	}
	
	public void rebuild()
	{
		IdList keyEcologicalAttributes = target.getKeyEcologicalAttributes();
		int childCount = keyEcologicalAttributes.size();
		Vector KeyEcologicalAttributesVector = new Vector();
		for(int i = 0; i < childCount; ++i)
		{
			BaseId keaId = keyEcologicalAttributes.get(i);
			KeyEcologicalAttribute kea = project.getKeyEcologicalAttributePool().find(keaId);
			if (kea.getKeyEcologicalAttributeType().equals(typeCode))
				KeyEcologicalAttributesVector.add(new KeyEcologicalAttributeNode(project, kea));
		}
		
		keaNodes = (KeyEcologicalAttributeNode[])KeyEcologicalAttributesVector.toArray(new KeyEcologicalAttributeNode[0]);
	}
	
	Project project;
	Target target;
	String typeCode;
	KeyEcologicalAttributeNode[] keaNodes;
	String label;
	
	private KeyEcologicalAttributeTypeQuestion question;
}
