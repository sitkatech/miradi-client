/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.diagram.cells.FactorLinkDataMap;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.DiagramFactorLinkId;
import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.objectdata.BaseIdData;
import org.conservationmeasures.eam.objecthelpers.CreateDiagramFactorLinkParameter;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;

public class DiagramFactorLink extends EAMBaseObject
{
	public DiagramFactorLink(DiagramFactorLinkId idToUse, CreateDiagramFactorLinkParameter extraInfo) throws Exception
	{
		super(idToUse);
		clear();
		
		underlyingObjectId.setId(extraInfo.getFactorLinkId());
		fromId.setId(extraInfo.getFromFactorId());
		toId.setId(extraInfo.getToFactorId());
	}
	
	public int getType()
	{
		return ObjectType.DIAGRAM_LINK;
	}

	public DiagramFactorLinkId getDiagramLinkageId()
	{
		return (DiagramFactorLinkId)getId(); 
	}
	
	public FactorLinkId getWrappedId()
	{
		return new FactorLinkId(underlyingObjectId.getId().asInt());
	}
	
	public FactorLinkDataMap createLinkageDataMap() throws Exception
	{
		FactorLinkDataMap dataMap = new FactorLinkDataMap();
		dataMap.setId(getDiagramLinkageId());
		dataMap.setFromId(new DiagramFactorId(fromId.getId().asInt()));
		dataMap.setToId(new DiagramFactorId(toId.getId().asInt()));
		return dataMap;
	}

	public CreateObjectParameter getCreationExtraInfo()
	{
		return new CreateDiagramFactorLinkParameter(
				new FactorLinkId(underlyingObjectId.getId().asInt()), 
				new DiagramFactorId(fromId.getId().asInt()), 
				new DiagramFactorId(toId.getId().asInt()));
	}

	void clear()
	{
		super.clear();
		underlyingObjectId = new BaseIdData();
		fromId = new BaseIdData();
		toId = new BaseIdData();
		
		addField(TAG_WRAPPED_ID, underlyingObjectId);
		addField(TAG_FROM_DIAGRAM_FACTOR_ID, fromId);
		addField(TAG_TO_DIAGRAM_FACTOR_ID, toId);
	}
	
	public static final String TAG_WRAPPED_ID = "WrappedLinkId";
	public static final String TAG_FROM_DIAGRAM_FACTOR_ID = "FromDiagramFactorId";
	public static final String TAG_TO_DIAGRAM_FACTOR_ID = "ToDiagramFactorId";
	
	private BaseIdData underlyingObjectId;
	private BaseIdData fromId;
	private BaseIdData toId;
}
