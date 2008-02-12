/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.objectpools;

import org.miradi.ids.BaseId;
import org.miradi.ids.DiagramFactorId;
import org.miradi.ids.DiagramFactorLinkId;
import org.miradi.ids.IdAssigner;
import org.miradi.objecthelpers.CreateDiagramFactorLinkParameter;
import org.miradi.objecthelpers.CreateObjectParameter;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.DiagramLink;
import org.miradi.project.ObjectManager;

public class DiagramFactorLinkPool extends EAMNormalObjectPool
{
	public DiagramFactorLinkPool(IdAssigner idAssignerToUse)
	{
		super(idAssignerToUse, ObjectType.DIAGRAM_LINK);
	}
	
	public DiagramLink find(BaseId id)
	{
		return (DiagramLink)findObject(id);
	}

	BaseObject createRawObject(ObjectManager objectManager, BaseId actualId, CreateObjectParameter extraInfo) throws Exception
	{
		return new DiagramLink(objectManager, actualId, (CreateDiagramFactorLinkParameter)extraInfo);
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
			DiagramLink thisLinkage = getLinkage(i);
			DiagramFactorId fromId = thisLinkage.getFromDiagramFactorId();
			DiagramFactorId toId = thisLinkage.getToDiagramFactorId();
			if(fromId.equals(nodeId1) && toId.equals(nodeId2))
				return (DiagramFactorLinkId) thisLinkage.getId();
			if(fromId.equals(nodeId2) && toId.equals(nodeId1))
				return (DiagramFactorLinkId) thisLinkage.getId();
		}
		return null;
	}
	
	private DiagramLink getLinkage(int index)
	{
		return find(getIds()[index]);
	}
	
}
