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
import org.miradi.objects.ResultsChainDiagram;
import org.miradi.project.ObjectManager;

public class ResultsChainDiagramPool extends EAMNormalObjectPool
{
	public ResultsChainDiagramPool(IdAssigner idAssignerToUse)
	{
		super(idAssignerToUse, ObjectType.RESULTS_CHAIN_DIAGRAM);
	}
	
	public void put(ResultsChainDiagram resultsChainDiagram)
	{
		put(resultsChainDiagram.getId(), resultsChainDiagram);
	}
	
	public ResultsChainDiagram find(BaseId id)
	{
		return (ResultsChainDiagram)getRawObject(id);
	}
	
	BaseObject createRawObject(ObjectManager objectManager, BaseId actualId, CreateObjectParameter extraInfo) throws Exception
	{
		return new ResultsChainDiagram(objectManager ,new DiagramContentsId(actualId.asInt()));
	}
}
