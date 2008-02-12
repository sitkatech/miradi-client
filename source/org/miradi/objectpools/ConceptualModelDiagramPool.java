/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.objectpools;

import org.miradi.ids.BaseId;
import org.miradi.ids.DiagramContentsId;
import org.miradi.ids.IdAssigner;
import org.miradi.objecthelpers.CreateObjectParameter;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ConceptualModelDiagram;
import org.miradi.project.ObjectManager;

public class ConceptualModelDiagramPool extends EAMNormalObjectPool
{
	public ConceptualModelDiagramPool(IdAssigner idAssignerToUse)
	{
		super(idAssignerToUse, ObjectType.CONCEPTUAL_MODEL_DIAGRAM);
	}
	
	public void put(ConceptualModelDiagram conceptualModelDiagram)
	{
		put(conceptualModelDiagram.getId(), conceptualModelDiagram);
	}
	
	public ConceptualModelDiagramPool find(BaseId id)
	{
		return (ConceptualModelDiagramPool)getRawObject(id);
	}
	
	BaseObject createRawObject(ObjectManager objectManager, BaseId actualId, CreateObjectParameter extraInfo) throws Exception
	{
		return new ConceptualModelDiagram(objectManager ,new DiagramContentsId(actualId.asInt()));
	}
}
