/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.objectdata.BooleanData;
import org.conservationmeasures.eam.objectdata.ORefData;
import org.conservationmeasures.eam.objectdata.StringData;
import org.conservationmeasures.eam.objecthelpers.CreateFactorLinkParameter;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.ObjectManager;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

public class FactorLink extends BaseObject
{
	public FactorLink(ObjectManager objectManager, FactorLinkId id, ORef fromFactorRef, ORef toFactorRef)
	{
		super(objectManager, id);
		clear();
		setFromRef(fromFactorRef);
		setToRef(toFactorRef);
	}

	public FactorLink(FactorLinkId id, ORef fromFactorRef, ORef toFactorRef)
	{
		super(id);
		clear();
		setFromRef(fromFactorRef);
		setToRef(toFactorRef);
	}
	
	public FactorLink(ObjectManager objectManager, int idAsInt, EnhancedJsonObject jsonObject) throws Exception 
	{
		super(objectManager, new FactorLinkId(idAsInt), jsonObject);
	}
	
	public void setFromRef(ORef fromFactorRef)
	{
		fromRef.set(fromFactorRef);
	}
	
	public void setToRef(ORef toFactorRef)
	{
		toRef.set(toFactorRef);
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
				Factor toFactor = (Factor) objectManager.findObject(getToFactorRef());
				if (toFactor.getType() == objectType)
					list.add(toFactor.getRef());
				
				Factor fromFactor = (Factor) objectManager.findObject(getFromFactorRef());
				if (fromFactor.getType() == objectType)
					list.add(fromFactor.getRef());
			}
		}
		return list;
	}
	
	public ORef getFromFactorRef()
	{
		return fromRef.getRawRef();
	}
	
	public ORef getToFactorRef()
	{
		return toRef.getRawRef();
	}
	
	public FactorLinkId getFactorLinkId()
	{
		return new FactorLinkId(getId().asInt());
	}
	
	public String getStressLabel()
	{
		return stressLabel.get();
	}
	
	public boolean isBidirectional()
	{
		return bidirectionalLink.get().equals(BIDIRECTIONAL_LINK);
	}
	
	public boolean isTargetLink()
	{
		 if (((Factor) objectManager.findObject(getToFactorRef())).isTarget())
			 return true;
		 
		 if (!isBidirectional())
			 return false;
			 
		 return (((Factor) objectManager.findObject(getFromFactorRef())).isTarget());
	}
	
	public CreateObjectParameter getCreationExtraInfo()
	{
		Factor fromFactor = (Factor) objectManager.findObject(getFromFactorRef());
		Factor toFactor = (Factor) objectManager.findObject(getToFactorRef());
		return new CreateFactorLinkParameter(fromFactor.getRef(), toFactor.getRef());
	}


	public ORef getFactorRef(int direction)
	{
		if(direction == FROM)
			return getFromFactorRef();
		if(direction == TO)
			return getToFactorRef();
		throw new RuntimeException("Link: Unknown direction " + direction);
	}
	
	public ORef getOppositeFactorRef(int direction)
	{
		if(direction == FROM)
			return getFactorRef(TO);
		if(direction == TO)
			return getFactorRef(FROM);
		throw new RuntimeException("Link: Unknown direction " + direction);
	}
	

	void clear()
	{
		super.clear();
		fromRef = new ORefData();
		toRef = new ORefData();
		stressLabel = new StringData();
		bidirectionalLink = new BooleanData();
		
		addNoClearField(TAG_FROM_REF, fromRef);
		addNoClearField(TAG_TO_REF, toRef);
		addField(TAG_STRESS_LABEL, stressLabel);
		addField(TAG_BIDIRECTIONAL_LINK, bidirectionalLink);
	}
	
	
	public static final String TAG_FROM_REF = "FromRef";
	public static final String TAG_TO_REF = "ToRef";
	public static final String TAG_STRESS_LABEL = "StressLabel";
	public static final String TAG_BIDIRECTIONAL_LINK = "BidirectionalLink";

	public static final String OBJECT_NAME = "Link";
	
	public static final int FROM = 1;
	public static final int TO = 2;
	public static final String BIDIRECTIONAL_LINK = BooleanData.BOOLEAN_TRUE;
	
	private ORefData fromRef;
	private ORefData toRef;
	private StringData stressLabel;
	private BooleanData bidirectionalLink;
}
