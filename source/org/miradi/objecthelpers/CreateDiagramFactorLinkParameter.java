/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.objecthelpers;

import java.util.HashMap;

import org.miradi.ids.DiagramFactorId;
import org.miradi.ids.FactorLinkId;
import org.miradi.objects.DiagramFactor;

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
	
	public ORef getFromDiagramFactorRef()
	{
		return new ORef(DiagramFactor.getObjectType(), getFromFactorId());
	}
	
	public ORef getToDiagramFactorRef()
	{
		return new ORef(DiagramFactor.getObjectType(), getToFactorId());
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
