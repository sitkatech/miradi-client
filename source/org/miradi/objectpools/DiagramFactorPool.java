/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.objectpools;

import org.miradi.ids.BaseId;
import org.miradi.ids.DiagramFactorId;
import org.miradi.ids.IdAssigner;
import org.miradi.objecthelpers.CreateDiagramFactorParameter;
import org.miradi.objecthelpers.CreateObjectParameter;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.DiagramFactor;
import org.miradi.project.ObjectManager;

public class DiagramFactorPool extends EAMNormalObjectPool
{
	public DiagramFactorPool(IdAssigner idAssignerToUse)
	{
		super(idAssignerToUse, ObjectType.DIAGRAM_FACTOR);
	}

	public void put(DiagramFactor diagramFactor)
	{
		put(diagramFactor.getDiagramFactorId(), diagramFactor);
	}
	
	public DiagramFactor find(BaseId id)
	{
		return (DiagramFactor)findObject(id);
	}
	
	BaseObject createRawObject(ObjectManager objectManager, BaseId actualId, CreateObjectParameter extraInfo)	throws Exception
	{
		DiagramFactorId diagramFactorId = new DiagramFactorId(actualId.asInt());
		
		return new DiagramFactor(objectManager, diagramFactorId, (CreateDiagramFactorParameter)extraInfo);
	}
	
	public DiagramFactorId[] getDiagramFactorIds()
	{
		BaseId[] baseIds = getIds();
		DiagramFactorId[] diagramFactorIds = new DiagramFactorId[baseIds.length];
		
		for (int i = 0; i < baseIds.length; i++)
		{
			diagramFactorIds[i] = new DiagramFactorId(baseIds[i].asInt());
		}
		
		return diagramFactorIds;
	}

}
