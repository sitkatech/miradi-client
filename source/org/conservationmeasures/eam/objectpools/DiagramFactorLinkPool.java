/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objectpools;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramFactorLinkId;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.objecthelpers.CreateDiagramFactorLinkParameter;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.DiagramFactorLink;

public class DiagramFactorLinkPool extends EAMNormalObjectPool
{
	public DiagramFactorLinkPool(IdAssigner idAssignerToUse)
	{
		super(idAssignerToUse, ObjectType.DIAGRAM_LINK);
	}
	
	public DiagramFactorLink find(BaseId id)
	{
		return (DiagramFactorLink)findObject(id);
	}

	BaseObject createRawObject(BaseId actualId, CreateObjectParameter extraInfo) throws Exception
	{
		return new DiagramFactorLink(actualId, (CreateDiagramFactorLinkParameter)extraInfo);
	}
	
	public DiagramFactorLinkId[] getallDiagramFactorLinkIds()
	{
		BaseId[] allBaseIds = getIds();
		DiagramFactorLinkId[] allDiagramFactorLinkIds = new DiagramFactorLinkId[allBaseIds.length];
		for (int i = 0; i < allBaseIds.length; i++)
		{
			allDiagramFactorLinkIds[i] = new DiagramFactorLinkId(allBaseIds[i].asInt());
		}
		
		return allDiagramFactorLinkIds; 
	}
}
