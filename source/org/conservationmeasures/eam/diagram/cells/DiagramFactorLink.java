/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.cells;

import org.conservationmeasures.eam.ids.DiagramFactorLinkId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.objects.FactorLink;

public class DiagramFactorLink
{
	public DiagramFactorLink(FactorLink linkToWrap, DiagramFactor fromToUse, DiagramFactor toToUse)
	{
		underlyingObject = linkToWrap;
		from = fromToUse;
		to = toToUse;
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
		dataMap.setFromId(from.getDiagramFactorId());
		dataMap.setToId(to.getDiagramFactorId());
		return dataMap;
	}
	
	private FactorLink underlyingObject;
	private DiagramFactor from;
	private DiagramFactor to;
}
