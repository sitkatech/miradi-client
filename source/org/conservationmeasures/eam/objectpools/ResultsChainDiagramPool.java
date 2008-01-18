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
import org.conservationmeasures.eam.objects.ResultsChainDiagram;
import org.conservationmeasures.eam.project.ObjectManager;

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
