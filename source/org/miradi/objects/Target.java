/* 
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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
package org.miradi.objects;


import org.miradi.ids.FactorId;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.HabitatAssociationQuestion;
import org.miradi.schemas.StressSchema;
import org.miradi.schemas.TargetSchema;
import org.miradi.utils.CodeList;


public class Target extends AbstractTarget
{
	public Target(ObjectManager objectManager, FactorId idToUse)
	{
		super(objectManager, idToUse, createSchema(objectManager));
	}

	public static TargetSchema createSchema(Project projectToUse)
	{
		return createSchema(projectToUse.getObjectManager());
	}

	public static TargetSchema createSchema(ObjectManager objectManager)
	{
		return (TargetSchema) objectManager.getSchemas().get(ObjectType.TARGET);
	}

	@Override
	public boolean isTarget()
	{
		return true;
	}
	
	@Override
	public String getPseudoData(String fieldTag)
	{
		if(fieldTag.equals(PSEUDO_TAG_HABITAT_ASSOCIATION_VALUE))
			return getHabitatAssociationValue();
		
		return super.getPseudoData(fieldTag);
	}

	private String getHabitatAssociationValue()
	{
		StringBuffer appendedChoiceValues = new StringBuffer();
		ChoiceQuestion question = getHabitatAssociationQuestion();
		CodeList habitatCodes = getCodeListData(TAG_HABITAT_ASSOCIATION);
		for(int index = 0; index < habitatCodes.size(); ++index)
		{
			if (index > 0)
				appendedChoiceValues.append(";");
			
			ChoiceItem choiceItem = question.findChoiceByCode(habitatCodes.get(index));
			appendedChoiceValues.append(choiceItem.getLabel());
		}
		
		return appendedChoiceValues.toString();
	}
	
	public ORefList getStressRefs()
	{
		return getSafeRefListData(TAG_STRESS_REFS);
	}
	
	@Override
	public int getAnnotationType(String tag)
	{
		if (tag.equals(TAG_STRESS_REFS))
			return StressSchema.getObjectType();
	
		return super.getAnnotationType(tag);
	}
	
	private ChoiceQuestion getHabitatAssociationQuestion()
	{
		return getQuestion(HabitatAssociationQuestion.class);
	}
	
	public static Target find(ObjectManager objectManager, ORef targetRef)
	{
		return (Target) objectManager.findObject(targetRef);
	}
	
	public static Target find(Project project, ORef targetRef)
	{
		return find(project.getObjectManager(), targetRef);
	}

	public static boolean is(BaseObject baseObject)
	{
		return is(baseObject.getType());
	}
	
	public static boolean is(int objectType)
	{
		return objectType == TargetSchema.getObjectType();
	}
		
	public static boolean is(ORef ref)
	{
		return is(ref.getObjectType());
	}
	

	public static final String TAG_STRESS_REFS = "StressRefs";
	public static final String TAG_HABITAT_ASSOCIATION = "HabitatAssociation";
	public static final String TAG_SPECIES_LATIN_NAME = "SpeciesLatinName";
	
	public static final String PSEUDO_TAG_HABITAT_ASSOCIATION_VALUE = "HabitatAssociationValue";
}
