/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
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

	public String getTypeName()
	{
		return OBJECT_NAME;
	}

	public static int getObjectType()
	{
		return ObjectType.RESULTS_CHAIN_DIAGRAM;
	}
	
	public static final String OBJECT_NAME = "ResultsChainDiagram";


}
