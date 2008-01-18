/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objectpools;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.objecthelpers.CreateDiagramFactorParameter;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.project.ObjectManager;

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
