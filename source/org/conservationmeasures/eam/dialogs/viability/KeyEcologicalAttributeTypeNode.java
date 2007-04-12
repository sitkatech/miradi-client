/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.viability;


import java.util.Vector;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.KeyEcologicalAttribute;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.KeyEcologicalAttributeTypeQuestion;
import org.conservationmeasures.eam.questions.StatusQuestion;
import org.conservationmeasures.eam.views.TreeTableNode;

public class KeyEcologicalAttributeTypeNode extends TreeTableNode
{
	public KeyEcologicalAttributeTypeNode(Project projectToUse, Target targetToUse, String typeCodeToUse)
	{
		project = projectToUse;
		target = targetToUse;
		typeCode = typeCodeToUse;
		question = new KeyEcologicalAttributeTypeQuestion(KeyEcologicalAttribute.TAG_KEY_ECOLOGICAL_ATTRIBUTE_TYPE);
		label = question.findChoiceByCode(typeCode).getLabel();
		statusQuestion = new StatusQuestion(Target.TAG_TARGET_STATUS);
		rebuild();
	}
	
	public BaseObject getObject()
	{
		return target;
	}

	public TreeTableNode getChild(int index)
	{
		return keaNodes[index];
	}

	public int getChildCount()
	{
		return keaNodes.length;
	}

	// TODO: Is it ok to have multiple nodes in the tree with the same ref?
	public ORef getObjectReference()
	{
		return target.getRef();
	}
	
	public int getType()
	{
		return target.getObjectType();
	}

	public Object getValueAt(int column)
	{
		if (ViabilityTreeModel.columnTags[column].equals("Status"))
		{
			String code = target.computeTNCViabilityOfKEAType(typeCode);
			return statusQuestion.findChoiceByCode(code);
		}
		return "";
	}

	public String toString()
	{
		 return label;
	}
	
	public BaseId getId()
	{
		return target.getId();
	}
	
	
	public String getTypeCode()
	{
		return typeCode;
	}
	
	public void rebuild()
	{
		KeyEcologicalAttribute[] keas = target.getKeasForType(typeCode);
		
		Vector KeyEcologicalAttributesVector = new Vector();
		for(int i = 0; i < keas.length; ++i)
			KeyEcologicalAttributesVector.add(new KeyEcologicalAttributeNode(project, keas[i]));
		
		keaNodes = (KeyEcologicalAttributeNode[])KeyEcologicalAttributesVector.toArray(new KeyEcologicalAttributeNode[0]);
	}
	
	private Project project;
	private Target target;
	private String typeCode;
	private KeyEcologicalAttributeNode[] keaNodes;
	private String label;
	
	private StatusQuestion statusQuestion;
	private KeyEcologicalAttributeTypeQuestion question;
}
