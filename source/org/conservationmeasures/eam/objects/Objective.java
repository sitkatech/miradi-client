/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.ObjectiveId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objectdata.ORefListData;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.ObjectManager;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;


public class Objective extends Desire
{
	public Objective(ObjectManager objectManager, BaseId id)
	{
		super(objectManager, new ObjectiveId(id.asInt()));
	}
	
	
	public Objective(BaseId id)
	{
		super(new ObjectiveId(id.asInt()));
	}
	
	public Objective(ObjectManager objectManager, int idAsInt, EnhancedJsonObject json) throws Exception
	{
		super(objectManager, new ObjectiveId(idAsInt), json);
	}
	
	public Objective(int idAsInt, EnhancedJsonObject json) throws Exception
	{
		super(new ObjectiveId(idAsInt), json);
	}
	
	
	public int getType()
	{
		return getObjectType();
	}

	public static int getObjectType()
	{
		return ObjectType.OBJECTIVE;
	}
	
	
	public static boolean canOwnThisType(int type)
	{
		return false;
	}
	
	
	public static boolean canReferToThisType(int type)
	{
		return false;
	}
	
	public ORefList getRelatedStrategies()
	{
		try
		{
			//FIXME planning finish		
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
		return new ORefList();
	}
	
	public ORefList getRelatedIndicators()
	{
		//FIXME fix method
		return new ORefList();
	}

	public String getPseudoData(String fieldTag)
	{

		if (fieldTag.equals(PSEUDO_TAG_RELATED_STRATEGY_OREF_LIST))
			return getRelatedStrategies().toString(); 
		
		if (fieldTag.equals(PSEUDO_TAG_RELATED_INDICATOR_OREF_LIST))
			return getRelatedIndicators().toString();
		
		return super.getPseudoData(fieldTag);
	}
	
	public void clear()
	{
		super.clear();	
		strategyChildren = new ORefListData();
		indicatorChildren = new ORefListData();
		
		addField(PSEUDO_TAG_RELATED_STRATEGY_OREF_LIST, strategyChildren);
		addField(PSEUDO_TAG_RELATED_INDICATOR_OREF_LIST, indicatorChildren);
	}
	
	public static final String OBJECT_NAME = "Objective";
	
	public final static String PSEUDO_TAG_RELATED_STRATEGY_OREF_LIST = "PseudoTagRelatedStrategyORefList";
	public final static String PSEUDO_TAG_RELATED_INDICATOR_OREF_LIST = "PseudoTagRelatedIndicatorORefList";
	
	ORefListData strategyChildren;
	ORefListData indicatorChildren;
}
