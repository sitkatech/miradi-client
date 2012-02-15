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
import org.miradi.utils.CodeList;
import org.miradi.utils.EnhancedJsonObject;


public class Target extends AbstractTarget
{
	public Target(ObjectManager objectManager, FactorId idToUse)
	{
		super(objectManager, idToUse);
		clear();
	}
	
	public Target(ObjectManager objectManager, FactorId idToUse, EnhancedJsonObject json) throws Exception
	{
		super(objectManager, idToUse, json);
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
			appendedChoiceValues.append(choiceItem);
		}
		
		return appendedChoiceValues.toString();
	}
	
	public ORefList getStressRefs()
	{
		return getRefListData(TAG_STRESS_REFS);
	}
	
	@Override
	public boolean isRefList(String tag)
	{
		if (tag.equals(TAG_STRESS_REFS))
			return true;
		
		return super.isRefList(tag);
	}

	@Override
	public int getAnnotationType(String tag)
	{
		if (tag.equals(TAG_STRESS_REFS))
			return Stress.getObjectType();
	
		return super.getAnnotationType(tag);
	}
	
	@Override
	public ORefList getAllObjectsToDeepCopy(ORefList deepCopiedFactorRefs)
	{
		ORefList deepObjectRefsToCopy = super.getAllObjectsToDeepCopy(deepCopiedFactorRefs);
		deepObjectRefsToCopy.addAll(getStressRefs());
		
		return deepObjectRefsToCopy;
	}
	
	private ChoiceQuestion getHabitatAssociationQuestion()
	{
		return getQuestion(HabitatAssociationQuestion.class);
	}
	
	@Override
	public int getType()
	{
		return getObjectType();
	}
	
	@Override
	public String getTypeName()
	{
		return OBJECT_NAME;
	}

	public static int getObjectType()
	{
		return ObjectType.TARGET;
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
		return objectType == getObjectType();
	}
		
	public static boolean is(ORef ref)
	{
		return is(ref.getObjectType());
	}
	
	@Override
	protected void clear()
	{
		super.clear();
		createRefListField(TAG_STRESS_REFS);
		createSingleLineUserTextField(TAG_SPECIES_LATIN_NAME);
		createCodeListField(TAG_HABITAT_ASSOCIATION, getHabitatAssociationQuestion());
		
		createPseudoQuestionField(PSEUDO_TAG_HABITAT_ASSOCIATION_VALUE);
	}

	public static final String OBJECT_NAME = "Target";
	
	public static final String TAG_STRESS_REFS = "StressRefs";
	public static final String TAG_HABITAT_ASSOCIATION = "HabitatAssociation";
	public static final String TAG_SPECIES_LATIN_NAME = "SpeciesLatinName";
	
	public static final String PSEUDO_TAG_HABITAT_ASSOCIATION_VALUE = "HabitatAssociationValue";
}
