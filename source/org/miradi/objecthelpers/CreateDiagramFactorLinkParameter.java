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

import org.miradi.ids.BaseId;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.FactorLink;

public class CreateDiagramFactorLinkParameter extends CreateObjectParameter
{
	public CreateDiagramFactorLinkParameter(ORef fromRefToUse, ORef toRefToUse)
	{
		this(ORef.createInvalidWithType(FactorLink.getObjectType()), fromRefToUse, toRefToUse);
	}
		
	public CreateDiagramFactorLinkParameter(ORef factorLinkRefToUse, ORef fromRefToUse, ORef toRefToUse)
	{
		factorLinkRef = factorLinkRefToUse;
		fromRef = fromRefToUse;
		toRef = toRefToUse;
	}
	
	public CreateDiagramFactorLinkParameter(BaseId factorLinkIdToUse, BaseId fromIdToUse, BaseId toIdToUse)
	{
		this(new ORef(FactorLink.getObjectType(), factorLinkIdToUse), new ORef(DiagramFactor.getObjectType(), fromIdToUse), new ORef(DiagramFactor.getObjectType(), toIdToUse));
	}
	
	public ORef getFactorLinkRef()
	{
		return factorLinkRef;
	}
	
	public ORef getFromDiagramFactorRef()
	{
		return fromRef;
	}
	
	public ORef getToDiagramFactorRef()
	{
		return toRef;
	}
	
	@Override
	public String getFormatedDataString()
	{
		HashMap<String, Comparable> dataPairs = new HashMap<String, Comparable>();
		dataPairs.put("FactorLinkId", factorLinkRef.getObjectId());
		dataPairs.put("FactorFromId", fromRef.getObjectId());
		dataPairs.put("FactorToID", toRef.getObjectId());
		return formatDataString(dataPairs);
	}
	
	private ORef factorLinkRef;
	private ORef fromRef;
	private ORef toRef;
}
