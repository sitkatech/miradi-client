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
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

public class FactorLink extends BaseObject
{
	public FactorLink(FactorLinkId id, FactorId fromNodeId, FactorId toNodeId)
	{
		super(id);
		clear();
		setFromId(fromNodeId);
		setToId(toNodeId);
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
		return ObjectType.FACTOR_LINK;
	}
	
	public FactorId getFromFactorId()
	{
		return new FactorId(fromId.getId().asInt());
	}
	
	public FactorId getToFactorId()
	{
		return new FactorId(toId.getId().asInt());
	}
	
	public String getStressLabel()
	{
		return stressLabel.get();
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
		
		addNoClearField(TAG_FROM_ID, fromId);
		addNoClearField(TAG_TO_ID, toId);
		addField(TAG_STRESS_LABEL, stressLabel);
	}
	
	
	private static String TAG_FROM_ID = "FromId";
	private static String TAG_TO_ID = "ToId";
	public static String TAG_STRESS_LABEL = "StressLabel";
	public static final String OBJECT_NAME = "Link";
	
	public static final int FROM = 1;
	public static final int TO = 2;
	
	private BaseIdData fromId;
	private BaseIdData toId;
	private StringData stressLabel;
}
