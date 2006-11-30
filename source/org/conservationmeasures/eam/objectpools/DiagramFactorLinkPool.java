/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objectpools;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.objecthelpers.CreateDiagramFactorLinkParameter;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.DiagramFactorLink;
import org.conservationmeasures.eam.objects.EAMObject;

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

	EAMObject createRawObject(BaseId actualId, CreateObjectParameter extraInfo) throws Exception
	{
		return new DiagramFactorLink(actualId, (CreateDiagramFactorLinkParameter)extraInfo);
	}


}
