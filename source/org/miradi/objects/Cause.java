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

import org.miradi.diagram.factortypes.FactorTypeCause;
import org.miradi.ids.FactorId;
import org.miradi.objectdata.BooleanData;
import org.miradi.objectdata.ChoiceData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.questions.StrategyClassificationQuestion;
import org.miradi.questions.ThreatClassificationQuestion;
import org.miradi.utils.EnhancedJsonObject;

public class Cause extends Factor
{
	public Cause(ObjectManager objectManager, FactorId idToUse)
	{
		super(objectManager, idToUse, new FactorTypeCause());
		clear();
	}
	
	public Cause(ObjectManager objectManager, FactorId idToUse, EnhancedJsonObject json) throws Exception
	{
		super(objectManager, idToUse, Factor.TYPE_CAUSE, json);
	}
	
	public static boolean canOwnThisType(int type)
	{
		if (Factor.canOwnThisType(type))
			return true;
		
		switch(type)
		{
			case ObjectType.OBJECTIVE: 
				return true;
			default:
				return false;
		}
	}
	
	public ORefList getOwnedObjects(int objectType)
	{
		ORefList list = super.getOwnedObjects(objectType);
		
		switch(objectType)
		{
			case ObjectType.OBJECTIVE: 
				list.addAll(new ORefList(objectType, getObjectiveIds()));
				break;
		}
		return list;
	}

	public boolean isCause()
	{
		return true;
	}
	
	public boolean isContributingFactor()
	{
		return !isDirectThreat();
	}
		
	public boolean isDirectThreat()
	{
		return isDirectThreat.asBoolean();
	}

	public boolean canHaveObjectives()
	{
		return true;
	}
	
	public int getType()
	{
		return getObjectType();
	}
	
	public String getTypeName()
	{
		if(isDirectThreat())
			return OBJECT_NAME_THREAT;

		return OBJECT_NAME_CONTRIBUTING_FACTOR;
	}

	public String getPseudoData(String fieldTag)
	{
		if (fieldTag.equals(PSEUDO_TAG_TAXONOMY_CODE_VALUE))
			return new ThreatClassificationQuestion().findChoiceByCode(taxonomyCode.get()).getLabel();
		
		return super.getPseudoData(fieldTag);
	}

	public static int getObjectType()
	{
		return ObjectType.CAUSE;
	}
	
	public static boolean is(ORef ref)
	{
		return is(ref.getObjectType());
	}
	
	public static boolean is(int objectType)
	{
		return objectType == getObjectType();
	}
	
	public static Cause find(ObjectManager objectManager, ORef causeRef)
	{
		return (Cause) objectManager.findObject(causeRef);
	}
	
	public static Cause find(Project project, ORef causeRef)
	{
		return find(project.getObjectManager(), causeRef);
	}
	
	void clear()
	{
		super.clear();
		taxonomyCode = new ChoiceData(TAG_TAXONOMY_CODE, new ThreatClassificationQuestion());
		isDirectThreat = new BooleanData(TAG_IS_DIRECT_THREAT);
		taxonomyCodeLabel = new PseudoQuestionData(PSEUDO_TAG_TAXONOMY_CODE_VALUE, new StrategyClassificationQuestion());
		
		addField(TAG_TAXONOMY_CODE, taxonomyCode);
		addField(TAG_IS_DIRECT_THREAT, isDirectThreat);
		addField(PSEUDO_TAG_TAXONOMY_CODE_VALUE, taxonomyCodeLabel);
	}	
	
	public static final String TAG_TAXONOMY_CODE = "TaxonomyCode";
	public static final String TAG_IS_DIRECT_THREAT = "IsDirectThreat";
	
	public static final String OBJECT_NAME = "Cause";
	
	private ChoiceData taxonomyCode;
	private BooleanData isDirectThreat;
	private PseudoQuestionData taxonomyCodeLabel;
	
	public static final String OBJECT_NAME_THREAT = "DirectThreat";
	public static final String OBJECT_NAME_CONTRIBUTING_FACTOR = "ContributingFactor";
}
