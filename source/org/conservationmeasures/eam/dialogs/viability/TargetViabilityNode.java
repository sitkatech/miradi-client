/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.viability;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Vector;

import org.conservationmeasures.eam.dialogs.treetables.TreeTableNode;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.KeyEcologicalAttribute;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.ViabilityModeQuestion;

public class TargetViabilityNode extends TreeTableNode
{
	public TargetViabilityNode(Project projectToUse, FactorId targetId) throws Exception
	{
		project = projectToUse;
		target = (Target)project.findNode(targetId);
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
		String tag = COLUMN_TAGS[column];
		String rawValue = target.getData(tag);

		if(tag.equals(Target.TAG_VIABILITY_MODE))
			return new ViabilityModeQuestion(tag).findChoiceByCode(rawValue);
		
		if (tag.equals(Target.PSEUDO_TAG_TARGET_VIABILITY_VALUE))
			return target.getPseudoTargetViabilityChoiceItem();
		
		if(tag.equals(Target.TAG_EMPTY))
			return null;
		
		return rawValue;
	}

	public String toString()
	{
		return target.getLabel();
	}
	
	public BaseId getId()
	{
		return target.getId();
	}
	
	public void rebuild() throws Exception
	{
		children = getKeaNodes(target);
	}

	static public KeyEcologicalAttributeNode[] getKeaNodes(Target target) throws Exception
	{
		Project project = target.getProject();
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
	
	public static final String[] COLUMN_TAGS = {
		Target.TAG_EMPTY, 
		Target.TAG_VIABILITY_MODE, 
		Target.PSEUDO_TAG_TARGET_VIABILITY_VALUE,
		Target.TAG_EMPTY,
		Target.TAG_EMPTY,
		Target.TAG_EMPTY,
		Target.TAG_EMPTY,
		Target.TAG_EMPTY,
		Target.TAG_EMPTY,
		Target.TAG_EMPTY,
		};
	
	private Project project;
	private Target target;
	private KeyEcologicalAttributeNode[] children;
}
