/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.viability;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Vector;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.KeyEcologicalAttribute;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.StatusQuestion;
import org.conservationmeasures.eam.views.TreeTableNode;

public class TargetViabilityNode extends TreeTableNode
{
	public TargetViabilityNode(Project projectToUse, FactorId targetId)
	{
		project = projectToUse;
		target = (Target)project.findNode(targetId);
		statusQuestion = new StatusQuestion(Target.TAG_TARGET_STATUS);
		rebuild();
	}
	
	public BaseObject getObject()
	{
		return target;
	}

	public TreeTableNode getChild(int index)
	{
		return children[index];
	}

	public int getChildCount()
	{
		return children.length;
	}

	public ORef getObjectReference()
	{
		return target.getRef();
	}
	
	public int getType()
	{
		return Target.getObjectType();
	}

	public Object getValueAt(int column)
	{
		if (ViabilityTreeModel.columnTags[column].equals("Status"))
		{
			String code = target.computeTNCViability();
			return statusQuestion.findChoiceByCode(code);
		}
		return "";
	}

	public String toString()
	{
		return target.getLabel();
	}
	
	public BaseId getId()
	{
		return target.getId();
	}
	
	public void rebuild()
	{
		children = getKeaNodes(target);
	}

	static public KeyEcologicalAttributeNode[] getKeaNodes(Target target)
	{
		Project project = target.getObjectManager().getProject();
		IdList keas = target.getKeyEcologicalAttributes();
		Vector keyEcologicalAttributesVector = new Vector();
		for(int i = 0; i < keas.size(); ++i)
		{
			KeyEcologicalAttribute kea = (KeyEcologicalAttribute)project.findObject(new ORef(KeyEcologicalAttribute.getObjectType(),keas.get(i)));
			keyEcologicalAttributesVector.add(new KeyEcologicalAttributeNode(project, kea));
		}
		
		KeyEcologicalAttributeNode[] keaNodes = (KeyEcologicalAttributeNode[])keyEcologicalAttributesVector.toArray(new KeyEcologicalAttributeNode[0]);
		sortObjectList(keaNodes, new KeaNodeComparator());
		return keaNodes;
	}
	
	static public void sortObjectList(KeyEcologicalAttributeNode[] objectList, Comparator comparator)
	{
		Arrays.sort(objectList, comparator);
	}
	
	
	private Project project;
	private Target target;
	private KeyEcologicalAttributeNode[] children;
	private StatusQuestion statusQuestion;
}
