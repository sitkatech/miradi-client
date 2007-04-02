/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objectpools;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramContentsId;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.DiagramContentsObject;
import org.conservationmeasures.eam.project.ObjectManager;

public class DiagramContentsPool extends EAMNormalObjectPool
{
	public DiagramContentsPool(IdAssigner idAssignerToUse)
	{
		super(idAssignerToUse, ObjectType.DIAGRAM_CONTENTS);
	}
	
	public void put(DiagramContentsObject diagramContents)
	{
		put(diagramContents.getId(), diagramContents);
	}
	
	public DiagramContentsObject find(BaseId id)
	{
		return (DiagramContentsObject)getRawObject(id);
	}

	BaseObject createRawObject(ObjectManager objectManager, BaseId actualId, CreateObjectParameter extraInfo)
	{
		return new DiagramContentsObject(objectManager, new DiagramContentsId(actualId.asInt()));
	}	
}
