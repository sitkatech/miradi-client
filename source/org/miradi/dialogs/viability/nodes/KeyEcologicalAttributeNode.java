/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.dialogs.viability.nodes;

import java.util.Vector;

import org.miradi.dialogs.treetables.TreeTableNode;
import org.miradi.ids.BaseId;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Indicator;
import org.miradi.objects.KeyEcologicalAttribute;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.EmptyChoiceItem;
import org.miradi.questions.KeyEcologicalAttributeTypeQuestion;
import org.miradi.questions.StatusQuestion;
import org.miradi.questions.TaglessChoiceItem;

public class KeyEcologicalAttributeNode extends TreeTableNode
{
	public KeyEcologicalAttributeNode(Project projectToUse, KeyEcologicalAttribute keaToUse) throws Exception
	{
		project = projectToUse;
		kea = keaToUse;
		rebuild();
	}
	
	public BaseObject getObject()
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
		String tag = COLUMN_TAGS[column];
		String rawValue = kea.getData(tag);
		if (tag.equals(KeyEcologicalAttribute.PSEUDO_TAG_VIABILITY_STATUS))
			return new StatusQuestion().findChoiceByCode(rawValue);
		
		if (tag.equals(KeyEcologicalAttribute.TAG_KEY_ECOLOGICAL_ATTRIBUTE_TYPE))
			return new KeyEcologicalAttributeTypeQuestion().findChoiceByCode(rawValue);
		
		if(tag.equals(KeyEcologicalAttribute.TAG_EMPTY))
			return new EmptyChoiceItem();
		
		return new TaglessChoiceItem(kea.getData(tag));
	}


	@Override
	public String toRawString()
	{
		return ((ChoiceItem)getValueAt(0)).getLabel();
	}
	
	public BaseId getId()
	{
		return kea.getId();
	}

	public void rebuild() throws Exception
	{
		int childCount = kea.getIndicatorIds().size();
		Vector indicatorVector = new Vector();
		for(int i = 0; i < childCount; ++i)
		{
			BaseId indicatorId = kea.getIndicatorIds().get(i);
			Indicator indicator = project.getIndicatorPool().find(indicatorId);
			indicatorVector.add(new ViabilityIndicatorNode(project, this, indicator));
		}
		indicators = (ViabilityIndicatorNode[])indicatorVector.toArray(new ViabilityIndicatorNode[0]);
	}
	
	public static final String[] COLUMN_TAGS = {
		KeyEcologicalAttribute.TAG_LABEL, 
		KeyEcologicalAttribute.TAG_EMPTY,
		KeyEcologicalAttribute.PSEUDO_TAG_VIABILITY_STATUS, 
		KeyEcologicalAttribute.TAG_KEY_ECOLOGICAL_ATTRIBUTE_TYPE,
		KeyEcologicalAttribute.TAG_EMPTY,
		KeyEcologicalAttribute.TAG_EMPTY,
		KeyEcologicalAttribute.TAG_EMPTY,
		KeyEcologicalAttribute.TAG_EMPTY,
		KeyEcologicalAttribute.TAG_EMPTY,
		KeyEcologicalAttribute.TAG_EMPTY,
		};
	
	private Project project;
	private KeyEcologicalAttribute kea;
	private ViabilityIndicatorNode[] indicators;
}

