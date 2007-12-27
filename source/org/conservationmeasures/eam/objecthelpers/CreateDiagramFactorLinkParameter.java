/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objecthelpers;

import java.util.HashMap;

import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.FactorLinkId;

public class CreateDiagramFactorLinkParameter extends CreateObjectParameter
{
	
	public CreateDiagramFactorLinkParameter(ORef factorLinkRef, DiagramFactorId fromIdToUse, DiagramFactorId toIdToUse)
	{
		this((FactorLinkId) factorLinkRef.getObjectId(), fromIdToUse, toIdToUse);
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
