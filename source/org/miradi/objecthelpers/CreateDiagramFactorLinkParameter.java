/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.objecthelpers;

import java.util.HashMap;

import org.miradi.ids.DiagramFactorId;
import org.miradi.ids.FactorLinkId;

public class CreateDiagramFactorLinkParameter extends CreateObjectParameter
{
	public CreateDiagramFactorLinkParameter(ORef fromRefToUse, ORef toRefToUse)
	{
		this(new FactorLinkId(FactorLinkId.INVALID.asInt()), (DiagramFactorId) fromRefToUse.getObjectId(), (DiagramFactorId) toRefToUse.getObjectId());
	}
		
	public CreateDiagramFactorLinkParameter(ORef factorLinkRef, ORef fromRefToUse, ORef toRefToUse)
	{
		this((FactorLinkId) factorLinkRef.getObjectId(), (DiagramFactorId)fromRefToUse.getObjectId(), (DiagramFactorId)toRefToUse.getObjectId());
	}
	
	public CreateDiagramFactorLinkParameter(DiagramFactorId fromIdToUse, DiagramFactorId toIdToUse)
	{
		this(new FactorLinkId(FactorLinkId.INVALID.asInt()), fromIdToUse, toIdToUse);
	}
	
	public CreateDiagramFactorLinkParameter(FactorLinkId factorLinkIdToUse, DiagramFactorId fromIdToUse, DiagramFactorId toIdToUse)
	{
		factorLinkId = factorLinkIdToUse;
		fromId = fromIdToUse;
		toId = toIdToUse;
	}
	
	public FactorLinkId getFactorLinkId()
	{
		return factorLinkId;
	}
	
	public DiagramFactorId getFromFactorId()
	{
		return fromId;
	}
	
	public DiagramFactorId getToFactorId()
	{
		return toId;
	}
	
	public String getFormatedDataString()
	{
		HashMap dataPairs = new HashMap();
		dataPairs.put(FactorLinkId.class.getSimpleName(), factorLinkId);
		dataPairs.put("FactorFromId", fromId);
		dataPairs.put("FactorToID", toId);
		return formatDataString(dataPairs);
	}
	
	private FactorLinkId factorLinkId;
	private DiagramFactorId fromId;
	private DiagramFactorId toId;
}
