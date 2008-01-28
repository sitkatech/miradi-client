/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import java.util.Set;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramContentsId;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.objectdata.IdListData;
import org.conservationmeasures.eam.objectdata.StringData;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.ObjectManager;
import org.conservationmeasures.eam.project.Project;
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
	
	public DiagramFactor getDiagramFactor(ORef factorRef)
	{
		ORefList diagramFactorRefs = getAllDiagramFactorRefs();
		for (int i = 0; i < diagramFactorRefs.size(); ++i)
		{
			ORef diagramFactorRef = diagramFactorRefs.get(i);
			DiagramFactor diagramFactor = (DiagramFactor) getObjectManager().findObject(diagramFactorRef);
			if (diagramFactor.getWrappedORef().equals(factorRef))
				return diagramFactor;
		}
		
		return null;
	}
	
	// TODO: This really should have a test
	public ORefList getAllGoalRefs()
	{
		ORefList allGoalIds = objectManager.getGoalPool().getORefList();
		return getAnnotationInThisDiagram(allGoalIds);
	}

	// TODO: This really should have a test
	public ORefList getAllObjectiveRefs()
	{
		ORefList allObjectiveIds = objectManager.getObjectivePool().getORefList();
		return getAnnotationInThisDiagram(allObjectiveIds);
	}

	// TODO: This really should have a test
	private ORefList getAnnotationInThisDiagram(ORefList allAnnotationIds)
	{
		ORefList ourAnnotations = new ORefList();
		for (int i = 0; i < allAnnotationIds.size(); ++i)
		{
			ORef goalRef = allAnnotationIds.get(i);
			if(isAnnotationInThisDiagram(goalRef))
				ourAnnotations.add(goalRef);
		}
		return ourAnnotations;
	}

	// TODO: This really should have a test
	private boolean isAnnotationInThisDiagram(ORef annotationRef)
	{
		ORefList diagramFactorRefs = getAllDiagramFactorRefs();
		for(int dfr = 0; dfr < diagramFactorRefs.size(); ++dfr)
		{
			DiagramFactor diagramFactor = (DiagramFactor) objectManager.findObject(diagramFactorRefs.get(dfr));
			ORef factorRef = diagramFactor.getWrappedORef();
			Factor factor = objectManager.findFactor(factorRef);
			if(factor.getAllOwnedObjects().contains(annotationRef))
				return true;
		}
		
		return false;
	}
	
	public boolean containsWrappedFactorRef(ORef factorRef)
	{
		if (! Factor.isFactor(factorRef))
			return false;
		
		if (getDiagramFactor(factorRef) == null)
			return false;
		
		return true;
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
	
	//TODO the majority of this method was copied form DiagramModel.  this also has a test, so everyone should start using this method.
	public boolean areDiagramFactorsLinked(DiagramFactorId fromDiagramFactorId, DiagramFactorId toDiagramFactorId) throws Exception
	{
		ORefList diagramLinkRefs = getAllDiagramLinkRefs();
		for (int i  = 0; i < diagramLinkRefs.size(); ++i)
		{
			DiagramLink diagramLink = (DiagramLink) getObjectManager().findObject(diagramLinkRefs.get(i));
			if (diagramLink.getFromDiagramFactorId().equals(fromDiagramFactorId) && diagramLink.getToDiagramFactorId().equals(toDiagramFactorId))
				return true;
			
			if (diagramLink.getFromDiagramFactorId().equals(toDiagramFactorId) && diagramLink.getToDiagramFactorId().equals(fromDiagramFactorId))
				return true;
		}
		
		return false;
	}
	
	public IdList getAllDiagramFactorIds()
	{
		return allDiagramFactorIds.getIdList();
	}
	
	public ORefList getAllDiagramFactorRefs()
	{
		return new ORefList(DiagramFactor.getObjectType(), getAllDiagramFactorIds());
	}
	
	public ORefList getAllDiagramLinkRefs()
	{
		return new ORefList(DiagramLink.getObjectType(), getAllDiagramFactorLinkIds());
	}
	
	public IdList getAllDiagramFactorLinkIds()
	{
		return allDiagramFactorLinkIds.getIdList();
	}
	
	public Factor[] getAllWrappedFactors()
	{
		ORefList diagramFactorRefs = getAllDiagramFactorRefs();
		Factor[] factors = new Factor[diagramFactorRefs.size()];
		for(int i = 0; i < diagramFactorRefs.size(); ++i)
		{
			DiagramFactor diagramFactor = (DiagramFactor) getProject().findObject(diagramFactorRefs.get(i));
			factors[i] = getProject().findFactor(diagramFactor.getWrappedORef());
		}
		
		return factors;
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
	
	public Set<String> getReferencedObjectTags()
	{
		Set<String> set = super.getReferencedObjectTags();
		set.add(TAG_DIAGRAM_FACTOR_IDS);
		set.add(TAG_DIAGRAM_FACTOR_LINK_IDS);
		return set;
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
	
	//TODO not sure this the right place for this method
	public static ORefList getDiagramRefsContainingFactor(Project projectToUse, ORef factorRef)
	{
		if (! Factor.isFactor(factorRef))
			return new ORefList();
		
		return findOwnersOfObject(projectToUse, factorRef, DiagramFactor.getObjectType());
	}
	
	public static ORefList getDiagramRefsContainingLink(Project projectToUse, ORef factorLinkRef)
	{
		if (factorLinkRef.getObjectType() != FactorLink.getObjectType())
			return new ORefList();

		return findOwnersOfObject(projectToUse, factorLinkRef, DiagramLink.getObjectType());
	}

	private static ORefList findOwnersOfObject(Project projectToUse, ORef ref, int objectType)
	{
		ORefList referrers = new ORefList();
		BaseObject underlyingFactorOrLink = projectToUse.findObject(ref);
		ORefList referrerRefs = underlyingFactorOrLink.findObjectsThatReferToUs(objectType);
		for(int i = 0; i < referrerRefs.size(); ++i)
		{
			BaseObject object = projectToUse.findObject(referrerRefs.get(i));
			referrers.add(object.getOwnerRef());
		}
		
		return referrers;
	}
	
	public String getPseudoData(String fieldTag)
	{
		if (fieldTag.equals(PSEUDO_COMBINED_LABEL))
			return toString();
		
		return super.getPseudoData(fieldTag);
	}
	
	public String getShortLabel()
	{
		return shortLabel.get();
	}
	
	public String toString()
	{
		return combineShortLabelAndLabel();
	}
	
	public void clear()
	{
		super.clear();
		
		allDiagramFactorIds = new IdListData(TAG_DIAGRAM_FACTOR_IDS, DiagramFactor.getObjectType());
		allDiagramFactorLinkIds = new IdListData(TAG_DIAGRAM_FACTOR_LINK_IDS, DiagramLink.getObjectType());
		shortLabel = new StringData();
		details = new StringData();
		combinedLabel = new PseudoStringData(PSEUDO_COMBINED_LABEL);	
		
		addField(TAG_DIAGRAM_FACTOR_IDS, allDiagramFactorIds);
		addField(TAG_DIAGRAM_FACTOR_LINK_IDS, allDiagramFactorLinkIds);
		addField(TAG_SHORT_LABEL, shortLabel);
		addField(TAG_DETAIL, details);
		addField(PSEUDO_COMBINED_LABEL, combinedLabel);
	}
	
	public static final String TAG_DIAGRAM_FACTOR_IDS = "DiagramFactorIds";
	public static final String TAG_DIAGRAM_FACTOR_LINK_IDS = "DiagramFactorLinkIds";
	public static final String TAG_SHORT_LABEL = "ShortLabel";
	public static final String TAG_DETAIL = "Detail";
	public static final String PSEUDO_COMBINED_LABEL = "PseudoCombinedLabel";
 	
	private IdListData allDiagramFactorIds;
	private IdListData allDiagramFactorLinkIds;
	private StringData shortLabel;
	private StringData details;
	private PseudoStringData combinedLabel;
}
