/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.dialogs.viability;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Vector;

import org.miradi.dialogs.treetables.TreeTableNode;
import org.miradi.ids.BaseId;
import org.miradi.ids.FactorId;
import org.miradi.ids.IdList;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.objects.KeyEcologicalAttribute;
import org.miradi.objects.Target;
import org.miradi.project.Project;
import org.miradi.questions.StatusQuestion;
import org.miradi.questions.ViabilityModeQuestion;

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
			return new ViabilityModeQuestion().findChoiceByCode(rawValue);
		
		if (tag.equals(Target.PSEUDO_TAG_TARGET_VIABILITY))
			return new StatusQuestion().findChoiceByCode(rawValue);
		
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
		Target.PSEUDO_TAG_TARGET_VIABILITY,
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
