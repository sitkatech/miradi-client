/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objectpools;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramContentsId;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.ConceptualModelDiagram;
import org.conservationmeasures.eam.project.ObjectManager;

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
