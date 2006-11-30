/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.cells;

import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.DiagramFactorLinkId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.objects.FactorLink;

public class DiagramFactorLink
{
	public DiagramFactorLink(FactorLink linkToWrap, DiagramFactorId fromIdToUse, DiagramFactorId toIdToUse)
	{
		underlyingObject = linkToWrap;
		fromId = fromIdToUse;
		toId = toIdToUse;
	}
	
	public FactorLinkId getWrappedId()
	{
		return (FactorLinkId)underlyingObject.getId();
	}
	
	public String getStressLabel()
	{
		return underlyingObject.getStressLabel();
	}
	
	public DiagramFactorLinkId getDiagramLinkageId()
	{
		return new DiagramFactorLinkId(underlyingObject.getId().asInt());
	}
	
	public FactorId getFromFactorId()
	{
		return underlyingObject.getFromFactorId();
	}
	
	public FactorId getToFactorId()
	{
		return underlyingObject.getToFactorId();
	}
	
	public FactorLinkDataMap createLinkageDataMap() throws Exception
	{
		FactorLinkDataMap dataMap = new FactorLinkDataMap();
		dataMap.setId(getDiagramLinkageId());
		dataMap.setFromId(fromId);
		dataMap.setToId(toId);
		return dataMap;
	}
	
	private FactorLink underlyingObject;
	private DiagramFactorId fromId;
	private DiagramFactorId toId;
}
