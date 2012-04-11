/* 
Copyright 2005-2012, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.objects;

import java.awt.Rectangle;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import org.miradi.ids.BaseId;
import org.miradi.ids.DiagramContentsId;
import org.miradi.ids.DiagramFactorId;
import org.miradi.ids.IdList;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.schemas.ConceptualModelDiagramSchema;
import org.miradi.schemas.DiagramFactorSchema;
import org.miradi.schemas.DiagramLinkSchema;
import org.miradi.schemas.FactorLinkSchema;
import org.miradi.schemas.HumanWelfareTargetSchema;
import org.miradi.schemas.ResultsChainDiagramSchema;
import org.miradi.schemas.TargetSchema;
import org.miradi.utils.CodeList;
import org.miradi.utils.EnhancedJsonObject;

abstract public class DiagramObject extends BaseObject
{
	public DiagramObject(ObjectManager objectManager, BaseId idToUse, final BaseObjectSchema schemaToUse)
	{
		super(objectManager,idToUse, schemaToUse);
	}
	
	public DiagramObject(ObjectManager objectManager, int idToUse, EnhancedJsonObject json, final BaseObjectSchema schemaToUse) throws Exception
	{
		super(objectManager, new DiagramContentsId(idToUse), json, schemaToUse);
	}

	public HashSet<Factor> getAbstractTargets()
	{
		HashSet<Factor> diagramAbstractTargets = getFactorsOfType(TargetSchema.getObjectType());
		diagramAbstractTargets.addAll(getFactorsOfType(HumanWelfareTargetSchema.getObjectType()));
		
		return diagramAbstractTargets;
	}

	public HashSet<Factor> getFactorsOfType(int wrappedType)
	{
		HashSet<Factor> filteredFactorsByType = new HashSet<Factor>();
		HashSet<DiagramFactor> filteredDiagramFactors = getFactorsFromDiagram(wrappedType);
		for(DiagramFactor diagramFactor : filteredDiagramFactors)
		{
			filteredFactorsByType.add(diagramFactor.getWrappedFactor());
		}
		
		return filteredFactorsByType;
	}
	
	public HashSet<DiagramFactor> getFactorsFromDiagram(int wrappedType)
	{
		HashSet<DiagramFactor> filteredFactorsByType = new HashSet<DiagramFactor>();
		ORefList allDiagramFactorRefs = getAllDiagramFactorRefs();
		for (int index = 0; index < allDiagramFactorRefs.size(); ++index)
		{
			DiagramFactor diagramFactor = DiagramFactor.find(getProject(), allDiagramFactorRefs.get(index));
			if (diagramFactor.getWrappedType() == wrappedType)
				filteredFactorsByType.add(diagramFactor);
		}
		
		return filteredFactorsByType;
	}
	
	public DiagramLink getDiagramFactorLink(ORef factorLinkRef)
	{
		ORefList diagramLinkRefs = getAllDiagramLinkRefs();
		for (int index = 0; index < diagramLinkRefs.size(); index++)
		{
			DiagramLink diagramFactorLink = DiagramLink.find(getProject(), diagramLinkRefs.get(index));
			if (diagramFactorLink.getWrappedRef().equals(factorLinkRef))
				return diagramFactorLink;
		}
		
		return null;
	}
	
	public DiagramLink getDiagramLinkByWrappedRef(ORef factorLinkRef)
	{
		if (!FactorLink.is(factorLinkRef.getObjectType()))
			throw new RuntimeException(factorLinkRef + " is not a factor link ref");
		
		FactorLink link = FactorLink.find(getProject(), factorLinkRef);
		ORefList wrappingDiagramLinkRefs = link.findObjectsThatReferToUs(DiagramLinkSchema.getObjectType());
		ORefList diagramLinkOnThisDiagram = wrappingDiagramLinkRefs.getOverlappingRefs(getAllDiagramLinkRefs());
		if(diagramLinkOnThisDiagram.size() == 1)
			return DiagramLink.find(getProject(), diagramLinkOnThisDiagram.get(0));
		if(diagramLinkOnThisDiagram.size() > 1)
			throw new RuntimeException("On " + getRef() + " FL " + factorLinkRef + " has multiple DL: " + diagramLinkOnThisDiagram);
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
	
	public ORefList getSelectedTaggedObjectSetRefs()
	{
		return getSafeRefListData(TAG_SELECTED_TAGGED_OBJECT_SET_REFS);
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
			Factor factor = Factor.findFactor(objectManager, factorRef);
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
	
	@Override
	public boolean isRefList(String tag)
	{
		if (tag.equals(TAG_SELECTED_TAGGED_OBJECT_SET_REFS))
			return true;
		
		return super.isRefList(tag);
	}
	
	@Override
	public boolean isIdListTag(String tag)
	{
		if (tag.equals(TAG_DIAGRAM_FACTOR_IDS))
			return true;
		
		if (tag.equals(TAG_DIAGRAM_FACTOR_LINK_IDS))
			return true;

		return super.isIdListTag(tag);
	}
	
	public boolean isResultsChain()
	{
		return (getType() == ObjectType.RESULTS_CHAIN_DIAGRAM);
	}
	
	public boolean isConceptualModelDiagram()
	{
		return ConceptualModelDiagram.is(getType());
	}
	
	public boolean areDiagramFactorsLinkedFromToNonBidirectional(ORef fromDiagramFactorRef, ORef toDiagramFactorRef)
	{
		return areDiagramFactorsLinkedFromToNonBidirectional(fromDiagramFactorRef, toDiagramFactorRef, DiagramLink.FROM);
	}
	
	public boolean areDiagramFactorsLinkedFromToNonBidirectional(ORef fromDiagramFactorRef, ORef toDiagramFactorRef, int direction)
	{
		ORefList diagramLinkRefs = getAllDiagramLinkRefs();
		for (int i  = 0; i < diagramLinkRefs.size(); ++i)
		{
			DiagramLink diagramLink = (DiagramLink) getObjectManager().findObject(diagramLinkRefs.get(i));
			if (!diagramLink.isBidirectional()  && 
					diagramLink.getDiagramFactorRef(direction).equals(fromDiagramFactorRef) && 
					diagramLink.getOppositeDiagramFactorRef(direction).equals(toDiagramFactorRef))
				return true;
			
		}
		
		return false;
	}
	
	public boolean areLinkedEitherDirection(ORef fromFactorRef, ORef toFactorRef)
	{
		Factor.ensureFactor(fromFactorRef);
		Factor.ensureFactor(toFactorRef);
		return (getDiagramLink(fromFactorRef, toFactorRef) != null);
	}

	// TODO: This code can be much simpler and faster using new low-level methods
	public DiagramLink getDiagramLink(ORef factorRef1, ORef factorRef2)
	{
		Factor.ensureFactor(factorRef1);
		Factor.ensureFactor(factorRef2);
		
		ORefList diagramLinkRefs = getAllDiagramLinkRefs();
		for(int i = 0; i < diagramLinkRefs.size(); ++i)
		{
			DiagramLink diagramLink = DiagramLink.find(getProject(), diagramLinkRefs.get(i));
			ORef fromDiagramFactorRef = diagramLink.getFromDiagramFactorRef();
			DiagramFactor diagramFactor1 = DiagramFactor.find(getProject(), fromDiagramFactorRef);
			ORef toDiagramFactorRef = diagramLink.getToDiagramFactorRef();
			DiagramFactor diagramFactor2 = DiagramFactor.find(getProject(), toDiagramFactorRef);
			ORef foundFactorRef1 = diagramFactor1.getWrappedORef();
			ORef foundFactorRef2 = diagramFactor2.getWrappedORef();
			if(foundFactorRef1.equals(factorRef1) && foundFactorRef2.equals(factorRef2))
				return diagramLink;
			
			if(foundFactorRef1.equals(factorRef2) && foundFactorRef2.equals(factorRef1))
				return diagramLink;
		}
		
		return null;
	}

	public ORefList getDiagramLinkFromDiagramFactors(ORef diagramFactorRef1, ORef diagramFactorRef2)
	{
		if (!DiagramFactor.is(diagramFactorRef1) || !DiagramFactor.is(diagramFactorRef2))
			throw new RuntimeException("Trying to find link for wrong type.");
		
		DiagramFactor diagramFactor1 = DiagramFactor.find(getProject(), diagramFactorRef1);
		DiagramFactor diagramFactor2 = DiagramFactor.find(getProject(), diagramFactorRef2);
		ORefList diagramFactor1LinkReferrers = diagramFactor1.findObjectsThatReferToUs(DiagramLinkSchema.getObjectType());
		ORefList diagramFactor2LinkReferrers = diagramFactor2.findObjectsThatReferToUs(DiagramLinkSchema.getObjectType());

		ORefList sharedLinks = diagramFactor1LinkReferrers.getOverlappingRefs(diagramFactor2LinkReferrers);
		if(sharedLinks.size() == 0)
			return new ORefList();
		
		if(sharedLinks.size() > 1)
			EAM.logWarning("Found two factors linked more than once");
		
		return sharedLinks;
	}

	
	public IdList getAllDiagramFactorIds()
	{
		return getSafeIdListData(TAG_DIAGRAM_FACTOR_IDS);
	}
	
	public ORefList getAllDiagramFactorRefs()
	{
		return new ORefList(DiagramFactorSchema.getObjectType(), getAllDiagramFactorIds());
	}
	
	public Set<DiagramFactor> getDiagramFactorsThatWrap(int objectType)
	{
		HashSet<DiagramFactor> matches = new HashSet<DiagramFactor>();
		ORefList allDiagramFactorRefs = getAllDiagramFactorRefs();
		for(int i = 0; i < allDiagramFactorRefs.size(); ++i)
		{
			DiagramFactor diagramFactor = DiagramFactor.find(getObjectManager(), allDiagramFactorRefs.get(i));
			if(diagramFactor.getWrappedType() == objectType)
				matches.add(diagramFactor);
		}
		
		return matches;
	}

	public ORefList getAllDiagramLinkRefs()
	{
		return new ORefList(DiagramLinkSchema.getObjectType(), getAllDiagramFactorLinkIds());
	}
	
	public IdList getAllDiagramFactorLinkIds()
	{
		return getSafeIdListData(TAG_DIAGRAM_FACTOR_LINK_IDS);
	}
	
	public CodeList getHiddenTypes() throws Exception
	{
		return getCodeList(TAG_HIDDEN_TYPES);
	}
	
	public Factor[] getAllWrappedFactors()
	{
		return getFactorsExcludingTypes(new Vector<Integer>());
	}
	
	public ORefSet getAllWrappedFactorRefSet()
	{
		return new ORefSet(getAllWrappedFactors());
	}
	
	public Factor[] getFactorsExcludingTypes(Vector<Integer> typesToFilterBy)
	{
		ORefList diagramFactorRefs = getAllDiagramFactorRefs();
		Vector<Factor> factors = new Vector<Factor>();
		for(int i = 0; i < diagramFactorRefs.size(); ++i)
		{
			DiagramFactor diagramFactor = (DiagramFactor) getProject().findObject(diagramFactorRefs.get(i));
			ORef wrappedRef = diagramFactor.getWrappedORef();
			if (typesToFilterBy.contains(wrappedRef.getObjectType()))
				continue;
			
			factors.add(Factor.findFactor(getProject(), wrappedRef));
		}
		
		return factors.toArray(new Factor[0]); 
	}
	
	public Rectangle getBoundsOfFactorsAndBendPoints()
	{
		ORefList allDiagramFactorRefs = getAllDiagramFactorRefs();
		Rectangle bounds = null;
		for (int index = 0; index < allDiagramFactorRefs.size(); ++index)
		{
			DiagramFactor diagramFactor = DiagramFactor.find(getProject(), allDiagramFactorRefs.get(index));
			Rectangle diagramFactorBounds = diagramFactor.getBounds();
			if (bounds == null)
				bounds = new Rectangle(diagramFactorBounds);
			
			bounds = bounds.union(diagramFactorBounds);			
		}
		
		ORefList allDiagramLinkRefs = getAllDiagramLinkRefs();
		for (int index = 0; index < allDiagramLinkRefs.size(); ++index)
		{
			DiagramLink diagramLink = DiagramLink.find(getProject(), allDiagramLinkRefs.get(index));
			if (diagramLink.isCoveredByGroupBoxLink())
				continue;
			
			Rectangle bendPointBounds = diagramLink.getBendPointBounds();
			if (bendPointBounds == null)
				continue;
			
			if (bounds == null)
				bounds = new Rectangle(bendPointBounds);
			
			bounds.add(bendPointBounds);
		}
		
		return bounds;
	}
	
	//TODO write test for this method
	public boolean containsDiagramFactor(DiagramFactorId diagramFactorId)
	{
		IdList ids = getAllDiagramFactorIds();
		return ids.contains(diagramFactorId);
	}
	
	@Override
	public ORefList getOwnedObjects(int objectType)
	{
		ORefList list = super.getOwnedObjects(objectType);
		switch(objectType)
		{
			case ObjectType.DIAGRAM_FACTOR: 
				list.addAll(new ORefList(DiagramFactorSchema.getObjectType(), getAllDiagramFactorIds()));
				break;
			case ObjectType.DIAGRAM_LINK: 
				list.addAll(new ORefList(DiagramLinkSchema.getObjectType(), getAllDiagramFactorLinkIds()));
				break;
		}
		return list;
	}
	
	public static ORefList getDiagramRefsContainingFactor(Project projectToUse, int parentDiagramType, ORef factorRef)
	{
		return findOwnersOfObject(projectToUse, parentDiagramType, factorRef, DiagramFactorSchema.getObjectType());
	}
		
	//TODO not sure this the right place for this method
	public static ORefList getDiagramRefsContainingFactor(Project projectToUse, ORef factorRef)
	{
		if (! Factor.isFactor(factorRef))
			return new ORefList();
		
		return findOwnersOfObject(projectToUse, factorRef, DiagramFactorSchema.getObjectType());
	}
	
	public static ORefList getDiagramRefsContainingLink(Project projectToUse, ORef factorLinkRef)
	{
		if (factorLinkRef.getObjectType() != FactorLinkSchema.getObjectType())
			return new ORefList();

		return findOwnersOfObject(projectToUse, factorLinkRef, DiagramLinkSchema.getObjectType());
	}
	
	private static ORefList findOwnersOfObject(Project projectToUse, ORef ref, int objectType)
	{
		ORefSet diagrams = new ORefSet();
		diagrams.addAllRefs(findOwnersOfObject(projectToUse, ConceptualModelDiagramSchema.getObjectType(), ref, objectType));
		diagrams.addAllRefs(findOwnersOfObject(projectToUse, ResultsChainDiagramSchema.getObjectType(), ref, objectType));
		
		return diagrams.toRefList();
	}
	
	private static ORefList findOwnersOfObject(Project projectToUse, int diagramTypeToFilterBy, ORef ref, int objectType)
	{
		ORefList referrers = new ORefList();
		BaseObject underlyingFactorOrLink = projectToUse.findObject(ref);
		ORefList referrerRefs = underlyingFactorOrLink.findObjectsThatReferToUs(objectType);
		for(int i = 0; i < referrerRefs.size(); ++i)
		{
			BaseObject object = projectToUse.findObject(referrerRefs.get(i));
			ORef ownerRef = object.getOwnerRef();
			if (diagramTypeToFilterBy == ownerRef.getObjectType())
				referrers.add(ownerRef);
		}
		
		return referrers;
	}
	
	public ORefList findReferrersOnSameDiagram(ORef referredObjectRef, int referrerType)
	{
		ORefList referrerRefs = new ORefList();
		BaseObject referredObject = getProject().findObject(referredObjectRef);
		ORefList targetReferrerRefs = referredObject.findObjectsThatReferToUs(referrerType);
		for (int index = 0; index < targetReferrerRefs.size(); ++index)
		{
			if (containsWrappedFactorRef(targetReferrerRefs.get(index)))
				referrerRefs.add(targetReferrerRefs.get(index));
		}
		
		return referrerRefs;	
	}
	
	public double getZoomScale()
	{
		double scale = getSafeNumberData(TAG_ZOOM_SCALE);
		if (scale <  .0001 || scale > 1000000)
			return 1.0;
		
		return scale;
	}
	
	@Override
	public String getPseudoData(String fieldTag)
	{
		if (fieldTag.equals(PSEUDO_COMBINED_LABEL))
			return toString();
		
		return super.getPseudoData(fieldTag);
	}
	
	@Override
	public String getShortLabel()
	{
		return getStringData(TAG_SHORT_LABEL);
	}
	
	@Override
	public String toString()
	{
		return combineShortLabelAndLabel();
	}
	
	public static DiagramObject findDiagramObject(ObjectManager objectManager, ORef diagramObjectRef)
	{
		return (DiagramObject) objectManager.findObject(diagramObjectRef);
	}
	
	public static DiagramObject findDiagramObject(Project project, ORef diagramObjectRef)
	{
		return findDiagramObject(project.getObjectManager(), diagramObjectRef);
	}
	
	public static boolean isDiagramObject(ORef ref)
	{
		return isDiagramObject(ref.getObjectType());
	}
	
	public static boolean isDiagramObject(int objectType)
	{
		if (ConceptualModelDiagram.is(objectType))
			return true;
		
		if (ResultsChainDiagram.is(objectType))
			return true;
		
		return false;
	}
	
	public boolean canContainFactorType(int objectType)
	{
		if(Strategy.is(objectType))
			return true;
		if(Target.is(objectType))
			return true;
		if(HumanWelfareTarget.is(objectType))
			return true;
		if(TextBox.is(objectType))
			return true;
		if(GroupBox.is(objectType))
			return true;
		
		return false;
	}

	public static final String TAG_DIAGRAM_FACTOR_IDS = "DiagramFactorIds";
	public static final String TAG_DIAGRAM_FACTOR_LINK_IDS = "DiagramFactorLinkIds";
	public static final String TAG_SHORT_LABEL = "ShortLabel";
	public static final String TAG_DETAIL = "Detail";
	public static final String TAG_HIDDEN_TYPES = "HiddenTypes";
	public static final String TAG_SELECTED_TAGGED_OBJECT_SET_REFS = "SelectedTaggedObjectSetRefs";
	public static final String TAG_ZOOM_SCALE = "ZoomScale";
	
	public static final String PSEUDO_COMBINED_LABEL = "PseudoCombinedLabel";
}
