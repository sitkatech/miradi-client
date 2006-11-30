/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.cells;

import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.DiagramFactorLinkId;
import org.conservationmeasures.eam.ids.FactorLinkId;

public class DiagramFactorLink
{
	public DiagramFactorLink(FactorLinkId factorLinkId, DiagramFactorId fromIdToUse, DiagramFactorId toIdToUse)
	{
		id = new DiagramFactorLinkId(factorLinkId.asInt());
		underlyingObjectId = factorLinkId;
		fromId = fromIdToUse;
		toId = toIdToUse;
	}
	
	public DiagramFactorLinkId getDiagramLinkageId()
	{
		return id; 
	}
	
	public FactorLinkId getWrappedId()
	{
		return underlyingObjectId;
	}
	
	public FactorLinkDataMap createLinkageDataMap() throws Exception
	{
		FactorLinkDataMap dataMap = new FactorLinkDataMap();
		dataMap.setId(getDiagramLinkageId());
		dataMap.setFromId(fromId);
		dataMap.setToId(toId);
		return dataMap;
	}

	private DiagramFactorLinkId id;
	private FactorLinkId underlyingObjectId;
	private DiagramFactorId fromId;
	private DiagramFactorId toId;
}
