/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramContentsId;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.objectdata.IdListData;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
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
	
	public DiagramLink getDiagramFactorLink(FactorLinkId factorLinkId)
	{
		IdList diagramFactorLinkIds = getAllDiagramFactorLinkIds();
		for (int i = 0; i < diagramFactorLinkIds.size(); i++)
		{
			DiagramLink diagramFactorLink = (DiagramLink) getObjectManager().findObject(new ORef(ObjectType.DIAGRAM_LINK, diagramFactorLinkIds.get(i)));
			if (diagramFactorLink.getWrappedId().equals(factorLinkId))
				return diagramFactorLink;
		}
		
		return null;
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
	
	public boolean containsWrappedFactor(FactorId factorId)
	{
		if (getDiagramFactor(factorId) != null)
			return true;
		
		return false;
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
	
	//TODO nima write test for this method
	public boolean containsDiagramFactor(DiagramFactorId diagramFactorId)
	{
		return allDiagramFactorIds.getIdList().contains(diagramFactorId);
	}
	
	public static boolean canOwnThisType(int type)
	{
		switch(type)
		{
			case ObjectType.DIAGRAM_FACTOR:
			case ObjectType.DIAGRAM_LINK:
				return true;
		}
		
		return false;
	}
	
	
	public static boolean canReferToThisType(int type)
	{
		return false;
	}
	

	public ORefList getOwnedObjects(int objectType)
	{
		ORefList list = super.getOwnedObjects(objectType);
		switch(objectType)
		{
			case ObjectType.DIAGRAM_FACTOR: 
				list.addAll(new ORefList(DiagramFactor.getObjectType(), getAllDiagramFactorIds()));
				break;
			case ObjectType.DIAGRAM_LINK: 
				list.addAll(new ORefList(DiagramLink.getObjectType(), getAllDiagramFactorLinkIds()));
				break;
		}
		return list;
	}
	
	public void clear()
	{
		super.clear();
		
		allDiagramFactorIds = new IdListData();
		allDiagramFactorLinkIds = new IdListData();
		
		addField(TAG_DIAGRAM_FACTOR_IDS, allDiagramFactorIds);
		addField(TAG_DIAGRAM_FACTOR_LINK_IDS, allDiagramFactorLinkIds);
	}
	
	public String toString()
	{
		return getLabel();
	}
	
	public static final String TAG_DIAGRAM_FACTOR_IDS = "DiagramFactorIds";
	public static final String TAG_DIAGRAM_FACTOR_LINK_IDS = "DiagramFactorLinkIds";
	
	IdListData allDiagramFactorIds;
	IdListData allDiagramFactorLinkIds;
}
