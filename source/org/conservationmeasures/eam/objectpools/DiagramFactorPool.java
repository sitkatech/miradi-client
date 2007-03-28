/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objectpools;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.objecthelpers.CreateDiagramFactorParameter;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.DiagramFactorLink;
import org.conservationmeasures.eam.objects.BaseObject;

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
	
	public DiagramFactorLink find(BaseId id)
	{
		return (DiagramFactorLink)findObject(id);
	}
	
	BaseObject createRawObject(BaseId actualId, CreateObjectParameter extraInfo)	throws Exception
	{
		DiagramFactorId diagramFactorId = new DiagramFactorId(actualId.asInt());
		
		return new DiagramFactor(diagramFactorId, (CreateDiagramFactorParameter)extraInfo);
	}

}
