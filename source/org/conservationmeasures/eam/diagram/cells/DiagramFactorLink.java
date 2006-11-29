/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.cells;

import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.ids.DiagramFactorLinkId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.objects.FactorLink;

public class DiagramFactorLink
{
	public DiagramFactorLink(DiagramModel model, FactorLink linkToWrap) throws Exception
	{
		underlyingObject = linkToWrap;
		from = model.getDiagramFactorByWrappedId(linkToWrap.getFromFactorId());
		to = model.getDiagramFactorByWrappedId(linkToWrap.getToFactorId());
		cell = new LinkCell(this);
	}
	
	public LinkCell getCell()
	{
		return cell;
	}
	
	public FactorLinkId getWrappedId()
	{
		return (FactorLinkId)underlyingObject.getId();
	}
	
	public DiagramFactor getFromNode()
	{
		return from;
	}
	
	public DiagramFactor getToNode()
	{
		return to;
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
	
	private LinkCell cell;
	private FactorLink underlyingObject;
	private DiagramFactor from;
	private DiagramFactor to;
}
