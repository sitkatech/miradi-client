/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.objectdata.BaseIdData;
import org.conservationmeasures.eam.objectdata.StringData;
import org.conservationmeasures.eam.objecthelpers.CreateFactorLinkParameter;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.ObjectManager;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

public class FactorLink extends BaseObject
{
	public FactorLink(ObjectManager objectManager, FactorLinkId id, FactorId fromNodeId, FactorId toNodeId)
	{
		super(objectManager, id);
		clear();
		setFromId(fromNodeId);
		setToId(toNodeId);
	}

	public FactorLink(FactorLinkId id, FactorId fromNodeId, FactorId toNodeId)
	{
		super(id);
		clear();
		setFromId(fromNodeId);
		setToId(toNodeId);
	}
	
	public FactorLink(ObjectManager objectManager, int idAsInt, EnhancedJsonObject jsonObject) throws Exception 
	{
		super(objectManager, new FactorLinkId(idAsInt), jsonObject);
	}
	
	public FactorLink(int idAsInt, EnhancedJsonObject jsonObject) throws Exception 
	{
		super(new FactorLinkId(idAsInt), jsonObject);
	}
	
	
	public void setFromId(FactorId fromNodeId)
	{
		fromId.setId(fromNodeId);
	}
	
	public void setToId(FactorId toNodeId)
	{
		toId.setId(toNodeId);
	}

	public int getType()
	{
		return getObjectType();
	}

	public static int getObjectType()
	{
		return ObjectType.FACTOR_LINK;
	}
	
	
	public static boolean canOwnThisType(int type)
	{
		return false;
	}
	
	
	public static boolean canReferToThisType(int type)
	{
		switch(type)
		{
			case ObjectType.CAUSE:
			case ObjectType.STRATEGY:
			case ObjectType.TARGET:
				return true;
			default:
				return false;
		}
	}
	
	public ORefList getReferencedObjects(int objectType)
	{
		ORefList list = super.getReferencedObjects(objectType);
		
		switch(objectType)
		{
			case ObjectType.CAUSE:
			case ObjectType.STRATEGY:
			case ObjectType.TARGET:
			{
				Factor toFactor = objectManager.findNode(getToFactorId());
				if (toFactor.getType() == objectType)
					list.add(toFactor.getRef());
				
				Factor fromFactor = objectManager.findNode(getFromFactorId());
				if (fromFactor.getType() == objectType)
					list.add(fromFactor.getRef());
			}
		}
		return list;
	}
	
	public FactorId getFromFactorId()
	{
		return new FactorId(fromId.getId().asInt());
	}
	
	public FactorId getToFactorId()
	{
		return new FactorId(toId.getId().asInt());
	}
	
	public FactorLinkId getFactorLinkId()
	{
		return new FactorLinkId(getId().asInt());
	}
	
	public String getStressLabel()
	{
		return stressLabel.get();
	}
	
	public boolean isBiDirectional()
	{
		return biDirectionalLink.get().equals(BI_DIRECTIONAL_LINK);
	}
	
	public CreateObjectParameter getCreationExtraInfo()
	{
		return new CreateFactorLinkParameter(getFromFactorId(), getToFactorId());
	}


	public FactorId getNodeId(int direction)
	{
		if(direction == FROM)
			return getFromFactorId();
		if(direction == TO)
			return getToFactorId();
		throw new RuntimeException("Link: Unknown direction " + direction);
	}
	
	public FactorId getOppositeNodeId(int direction)
	{
		if(direction == FROM)
			return getNodeId(TO);
		if(direction == TO)
			return getNodeId(FROM);
		throw new RuntimeException("Link: Unknown direction " + direction);
	}
	

	void clear()
	{
		super.clear();
		fromId = new BaseIdData();
		toId = new BaseIdData();
		stressLabel = new StringData();
		biDirectionalLink = new StringData();
		
		addNoClearField(TAG_FROM_ID, fromId);
		addNoClearField(TAG_TO_ID, toId);
		addField(TAG_STRESS_LABEL, stressLabel);
		addField(TAG_BI_DRECTIONAL_LINK, biDirectionalLink);
	}
	
	
	private static String TAG_FROM_ID = "FromId";
	private static String TAG_TO_ID = "ToId";
	public static String TAG_STRESS_LABEL = "StressLabel";
	public static String TAG_BI_DRECTIONAL_LINK = "BiDirectionalLink";

	public static final String OBJECT_NAME = "Link";
	
	public static final int FROM = 1;
	public static final int TO = 2;
	public static final String BI_DIRECTIONAL_LINK = "Y";
	
	private BaseIdData fromId;
	private BaseIdData toId;
	private StringData stressLabel;
	private StringData biDirectionalLink;
}
