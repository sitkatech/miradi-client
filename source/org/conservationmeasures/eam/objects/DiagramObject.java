/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramContentsId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.objectdata.IdListData;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.ObjectManager;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

abstract public class DiagramObject extends BaseObject
{
	public DiagramObject(ObjectManager objectManager, BaseId idToUse)
	{
		super(objectManager,idToUse);
	}
	
	public DiagramObject(BaseId idToUse)
	{
		super(idToUse);
	}
	
	public DiagramObject(ObjectManager objectManager, int idToUse, EnhancedJsonObject json) throws Exception
	{
		super(objectManager, new DiagramContentsId(idToUse), json);
	}
	
	
	public DiagramObject(int idToUse, EnhancedJsonObject json) throws Exception
	{
		super(new DiagramContentsId(idToUse), json);
	}
	
	public DiagramFactor getDiagramFactor(FactorId factorId)
	{
		IdList diagramFactorIds = getAllDiagramFactorIds();
		for (int i = 0; i < diagramFactorIds.size(); i++)
		{
			DiagramFactor diagramFactor = (DiagramFactor) getObjectManager().findObject(new ORef(ObjectType.DIAGRAM_FACTOR, diagramFactorIds.get(i)));
			if (diagramFactor.getWrappedId().equals(factorId))
				return diagramFactor;
		}
		
		return null;
	}
	public boolean isResultsChain()
	{
		return (getType() == ObjectType.RESULTS_CHAIN_DIAGRAM);
	}
	
	public IdList getAllDiagramFactorIds()
	{
		return allDiagramFactorIds.getIdList();
	}
	
	public IdList getAllDiagramFactorLinkIds()
	{
		return allDiagramFactorLinkIds.getIdList();
	}
	
	public static boolean canOwnThisType(int type)
	{
		return false;
	}
	
	public static boolean canReferToThisType(int type)
	{
		return false;
	}
	
	public void clear()
	{
		super.clear();
		
		allDiagramFactorIds = new IdListData();
		allDiagramFactorLinkIds = new IdListData();
		
		addField(TAG_DIAGRAM_FACTOR_IDS, allDiagramFactorIds);
		addField(TAG_DIAGRAM_FACTOR_LINK_IDS, allDiagramFactorLinkIds);
	}
	
	public static final String TAG_DIAGRAM_FACTOR_IDS = "DiagramFactorIds";
	public static final String TAG_DIAGRAM_FACTOR_LINK_IDS = "DiagramFactorLinkIds";
	
	IdListData allDiagramFactorIds;
	IdListData allDiagramFactorLinkIds;
}
