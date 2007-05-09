/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objectpools;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.DiagramFactorLinkId;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.objecthelpers.CreateDiagramFactorLinkParameter;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.DiagramFactorLink;
import org.conservationmeasures.eam.project.ObjectManager;

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

	BaseObject createRawObject(ObjectManager objectManager, BaseId actualId, CreateObjectParameter extraInfo) throws Exception
	{
		return new DiagramFactorLink(objectManager, actualId, (CreateDiagramFactorLinkParameter)extraInfo);
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
	
	
	public DiagramFactorLinkId getLinkedId(DiagramFactorId nodeId1, DiagramFactorId nodeId2)
	{
		for(int i = 0; i < getIds().length; ++i)
		{
			DiagramFactorLink thisLinkage = getLinkage(i);
			DiagramFactorId fromId = thisLinkage.getFromDiagramFactorId();
			DiagramFactorId toId = thisLinkage.getToDiagramFactorId();
			if(fromId.equals(nodeId1) && toId.equals(nodeId2))
				return (DiagramFactorLinkId) thisLinkage.getId();
			if(fromId.equals(nodeId2) && toId.equals(nodeId1))
				return (DiagramFactorLinkId) thisLinkage.getId();
		}
		return null;
	}
	
	private DiagramFactorLink getLinkage(int index)
	{
		return find(getIds()[index]);
	}
	
}
