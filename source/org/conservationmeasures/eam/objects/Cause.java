/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.diagram.factortypes.FactorTypeCause;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.objectdata.StringData;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objectpools.FactorLinkPool;
import org.conservationmeasures.eam.project.ObjectManager;
import org.conservationmeasures.eam.questions.ThreatClassificationQuestion;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

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
				list.addAll(new ORefList(objectType, getObjectives()));
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
		// NOTE: This was optimized for speed because doing it "the right way"
		// was causing significant slowness for users
		FactorLinkPool factorLinkPool = objectManager.getLinkagePool();
		BaseId[] ids = factorLinkPool.getIds();
		for(int i = 0; i < ids.length; ++i)
		{
			FactorLink link = (FactorLink)factorLinkPool.getRawObject(ids[i]);
			if (doesLinkPointToTargetFactor(link)) 
				return true;
		}
		return false;
	}

	private boolean doesLinkPointToTargetFactor(FactorLink link)
	{
		if(link.getFromFactorRef().equals(getRef()))
		{
			Factor toFactor = (Factor) objectManager.findObject(link.getToFactorRef());
			if (toFactor.getType() == ObjectType.TARGET)
				return true;
		}
		
		if (!link.isBidirectional())
			return false;
		
		if(link.getToFactorRef().equals(getRef()))
		{
			Factor fromFactor = (Factor) objectManager.findObject(link.getFromFactorRef());
			if (fromFactor.getType() == ObjectType.TARGET)
				return true;
		}
		
		return false;
	}
	
	public boolean canHaveObjectives()
	{
		return true;
	}
	
	public int getType()
	{
		return getObjectType();
	}
	
	public static int getObjectType()
	{
		return ObjectType.CAUSE;
	}
	
	void clear()
	{
		super.clear();
		taxonomyCode = new StringData();
		taxonomyCodeLabel = new PseudoQuestionData(new ThreatClassificationQuestion(TAG_TAXONOMY_CODE));
		
		addField(TAG_TAXONOMY_CODE, taxonomyCode);
		addField(PSEUDO_TAG_TAXONOMY_CODE_VALUE, taxonomyCodeLabel);
	}	
	
	public static final String TAG_TAXONOMY_CODE = "TaxonomyCode";
	public static final String PSEUDO_TAG_TAXONOMY_CODE_VALUE = "TaxonomyCodeValue";
	
	public static final String OBJECT_NAME = "Cause";
	
	StringData taxonomyCode; 
	PseudoQuestionData taxonomyCodeLabel;
	
	public static final String OBJECT_NAME_THREAT = "DirectThreat";
	public static final String OBJECT_NAME_CONTRIBUTING_FACTOR = "ContributingFactor";
}
