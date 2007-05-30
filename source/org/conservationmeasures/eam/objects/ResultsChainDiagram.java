/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.ObjectManager;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

public class ResultsChainDiagram extends DiagramObject
{
	public ResultsChainDiagram(ObjectManager objectManager, BaseId idToUse)
	{
		super(objectManager, idToUse);
	}

	public ResultsChainDiagram(BaseId idToUse)
	{
		super(idToUse);
	}

	public ResultsChainDiagram(ObjectManager objectManager, int idToUse, EnhancedJsonObject json) throws Exception
	{
		super(objectManager, idToUse, json);
	}

	public ResultsChainDiagram(int idToUse, EnhancedJsonObject json) throws Exception
	{
		super(idToUse, json);
	}
	
	public int getType()
	{
		return getObjectType();
	}

	public static int getObjectType()
	{
		return ObjectType.RESULTS_CHAIN_DIAGRAM;
	}

	//FIXME: Richard: need to verify if this should be can be owner rather then can be referer
	public static boolean canReferToThisType(int type)
	{
		switch(type)
		{
			case ObjectType.DIAGRAM_FACTOR:
			case ObjectType.DIAGRAM_LINK:
				return true;
		}
		
		return false;
	}

	public ORefList getReferencedObjects(int objectType)
	{
		ORefList results = new ORefList();
		results.addAll(new ORefList(DiagramFactor.getObjectType(), getAllDiagramFactorIds()));
		results.addAll(new ORefList(DiagramFactorLink.getObjectType(), getAllDiagramFactorLinkIds()));
		return results;
	}
	
}
