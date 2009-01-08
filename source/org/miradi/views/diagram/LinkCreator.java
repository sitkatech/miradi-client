/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.views.diagram;

import java.text.ParseException;
import java.util.HashSet;

import org.miradi.commands.CommandCreateObject;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.diagram.DiagramModel;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.ids.BaseId;
import org.miradi.ids.DiagramFactorId;
import org.miradi.ids.DiagramLinkId;
import org.miradi.ids.FactorId;
import org.miradi.main.EAM;
import org.miradi.objectdata.BooleanData;
import org.miradi.objecthelpers.CreateDiagramFactorLinkParameter;
import org.miradi.objecthelpers.CreateFactorLinkParameter;
import org.miradi.objecthelpers.CreateThreatStressRatingParameter;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Cause;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.Factor;
import org.miradi.objects.FactorLink;
import org.miradi.objects.GroupBox;
import org.miradi.objects.IntermediateResult;
import org.miradi.objects.Strategy;
import org.miradi.objects.Target;
import org.miradi.objects.ThreatReductionResult;
import org.miradi.objects.ThreatStressRating;
import org.miradi.project.Project;


//FIXME Examine all the methods and try to make it more uniform, simpler, etc....
//seems like a ton of very similar methods, among other things
public class LinkCreator
{
	public LinkCreator(Project projectToUse)
	{
		project = projectToUse;
	}
	
	public boolean linkWasRejected(DiagramModel model, ORef fromFactorRef, ORef toFactorRef) throws Exception
	{
		if (fromFactorRef.equals(toFactorRef))
			return true;
		
		if (! model.doesFactorExist(fromFactorRef) || ! model.doesFactorExist(toFactorRef))
			return true;

		if (model.areLinked(fromFactorRef, toFactorRef))
			return true;
		
		return false;
		
	}
	
	private boolean isGroupThatContains(ORef potentialGroupBoxDiagramFactorRef, ORef potentialChildDiagramFactorRef)
	{
		DiagramFactor from = DiagramFactor.find(project, potentialGroupBoxDiagramFactorRef);
		if(!from.isGroupBoxFactor())
			return false;
		
		return(from.getGroupBoxChildrenRefs().contains(potentialChildDiagramFactorRef));
	}

	public boolean linkToBePastedWasRejected(DiagramModel model, DiagramFactorId fromDiagramFactorId, DiagramFactorId toDiagramFactorId) throws Exception
	{
		DiagramFactor fromDiagramFactor = (DiagramFactor) project.findObject(new ORef(ObjectType.DIAGRAM_FACTOR, fromDiagramFactorId));
		DiagramFactor toDiagramFactor = (DiagramFactor) project.findObject(new ORef(ObjectType.DIAGRAM_FACTOR, toDiagramFactorId));
		
		return linkWasRejected(model, fromDiagramFactor, toDiagramFactor);
	}
	
	public boolean linkToBeCreatedWasRejected(DiagramModel model, DiagramFactor fromDiagramFactor, DiagramFactor toDiagramFactor) throws Exception
	{
		boolean linkWasRejected = linkWasRejected(model, fromDiagramFactor, toDiagramFactor);
		if (linkWasRejected)
			return true;
		
		return !canBeLinked(model.getDiagramObject(), fromDiagramFactor, toDiagramFactor);  
	}
	
	private boolean linkWasRejected(DiagramModel model, DiagramFactor fromDiagramFactor, DiagramFactor toDiagramFactor) throws Exception
	{
		if (fromDiagramFactor == null || toDiagramFactor == null)
			return true;
		
		if(fromDiagramFactor.getDiagramFactorId().equals(toDiagramFactor.getDiagramFactorId()))
		{
			String[] body = {EAM.text("Can't link an item to itself"), };
			EAM.okDialog(EAM.text("Can't Create Link"), body);
			return true;
		}
		
		ORef fromRef = fromDiagramFactor.getRef();
		ORef toRef = toDiagramFactor.getRef();
		if (isGroupThatContains(fromRef, toRef) || isGroupThatContains(toRef, fromRef))
		{
			String[] body = {EAM.text("Can't link a group to an item it contains"), };
			EAM.okDialog(EAM.text("Can't Create Link"), body);
			return true;
		}
		
		if(fromDiagramFactor.getDiagramFactorId().isInvalid() || toDiagramFactor.getDiagramFactorId().isInvalid())
		{
			EAM.logWarning("Unable to Paste Link : from " + fromDiagramFactor.getDiagramFactorId() + " to OriginalId:" + toDiagramFactor.getDiagramFactorId()+" node deleted?");	
			return true;
		}

		if (! model.containsDiagramFactor(fromDiagramFactor.getRef()) || ! model.containsDiagramFactor(toDiagramFactor.getRef()))
			return true;

		//TODO this method is called twice when inserting a link (that is linking a GB).  Since we are in frozen
		//state we dont want to change this.  This class in general needs cleaning up after frozen.
		if (areDiagramFactorsLinked(fromDiagramFactor, toDiagramFactor))
			return true;
			
		return false;		
	}
	
	public boolean canBeLinked(DiagramObject diagramObject, DiagramFactor fromDiagramFactor, DiagramFactor toDiagramFactor) throws Exception
	{
		if (areDiagramFactorsLinked(fromDiagramFactor, toDiagramFactor))
			return false;
	
		if (areWrappedFactorsLinkedInDiagram(diagramObject, fromDiagramFactor, toDiagramFactor))
			return false;
		
		boolean isFromGroupBox = fromDiagramFactor.isGroupBoxFactor();
		boolean isToGroupBoxChild = toDiagramFactor.isCoveredByGroupBox();
		if (isFromGroupBox && isToGroupBoxChild)
			return canLinkBetweenGroupAndChild(fromDiagramFactor, toDiagramFactor);
		
		boolean isFromGroupBoxChild = fromDiagramFactor.isCoveredByGroupBox();
		boolean isToGroupBox = toDiagramFactor.isGroupBoxFactor();
		if (isFromGroupBoxChild && isToGroupBox)
			return canLinkBetweenGroupAndChild(toDiagramFactor, fromDiagramFactor);

		return true;
	}

	private boolean canLinkBetweenGroupAndChild(DiagramFactor groupBox, DiagramFactor childDiagramFactor) throws Exception
	{
		ORef owningGroupBoxRef = childDiagramFactor.getOwningGroupBoxRef();
		DiagramFactor owningGroupBox = DiagramFactor.find(getProject(), owningGroupBoxRef);
		if (isLinkedToAnyGroupBoxChildren(owningGroupBox, groupBox))
			return false;

		boolean isOwningAlreadyLinkedToGroupBox = getProject().areDiagramFactorsLinked(owningGroupBoxRef, groupBox.getRef());
		if (isOwningAlreadyLinkedToGroupBox)
			return false;
		
		return true;
	}

	private boolean areDiagramFactorsLinked(DiagramFactor fromDiagramFactor, DiagramFactor toDiagramFactor) throws Exception
	{
		return getProject().areDiagramFactorsLinked(fromDiagramFactor.getRef(), toDiagramFactor.getRef());
	}

	private boolean areWrappedFactorsLinkedInDiagram(DiagramObject diagramObject, DiagramFactor fromDiagramFactor, DiagramFactor toDiagramFactor)
	{
		ORef factorLinkRef = getProject().getFactorLinkPool().getLinkedRef(fromDiagramFactor.getWrappedFactor(), toDiagramFactor.getWrappedFactor());
		if (factorLinkRef.isInvalid())
			return false;
		
		DiagramLink diagramLink = diagramObject.getDiagramFactorLink(factorLinkRef);
		return diagramLink != null;
	}
	
	private boolean isLinkedToAnyGroupBoxChildren(DiagramFactor from, DiagramFactor toGroupBox) throws Exception
	{
		ORefList childrenRefs = toGroupBox.getGroupBoxChildrenRefs();
		for (int index = 0; index < childrenRefs.size(); ++index)
		{
			if (getProject().areDiagramFactorsLinked(from.getRef(), childrenRefs.get(index)))
				return true;
		}
		
		return false;
	}
	
	public boolean areGroupBoxOwnedFactorsLinked(DiagramModel diagramModel, DiagramFactor from, DiagramFactor to) throws Exception
	{
		ORefList fromOwningGroupBoxAndChildren = getOwningGroupBoxAndChildren(from);
		ORefList toOwningGroupBoxAndChildren = getOwningGroupBoxAndChildren(to);		
		for (int fromIndex = 0; fromIndex < fromOwningGroupBoxAndChildren.size(); ++fromIndex)
		{
			for (int toIndex = 0; toIndex < toOwningGroupBoxAndChildren.size(); ++toIndex)
			{
				DiagramFactor thisFrom = DiagramFactor.find(getProject(), fromOwningGroupBoxAndChildren.get(fromIndex));
				DiagramFactor thisTo = DiagramFactor.find(getProject(), toOwningGroupBoxAndChildren.get(toIndex));
				if (diagramModel.areLinked(thisFrom.getWrappedORef(), thisTo.getWrappedORef()))
					return true;
			}
		}
		
		return false;
	}

	private ORefList getOwningGroupBoxAndChildren(DiagramFactor diagramFactor)
	{
		if (diagramFactor.isCoveredByGroupBox())
		{
			DiagramFactor owningGroupBox = DiagramFactor.find(getProject(), diagramFactor.getOwningGroupBoxRef());
			return owningGroupBox.getSelfAndChildren();
		}

		return diagramFactor.getSelfAndChildren();
	}

	public void createFactorLinkAndAddToDiagramUsingCommands(DiagramObject diagramObject, FactorId fromThreatId , FactorId toTargetId ) throws Exception
	{
		DiagramFactor fromDiagramFactor = diagramObject.getDiagramFactor(fromThreatId);
		DiagramFactor toDiagramFactor = diagramObject.getDiagramFactor(toTargetId);

		createFactorLinkAndAddToDiagramUsingCommands(diagramObject, fromDiagramFactor, toDiagramFactor);
	}
	
	public ORef createFactorLinkAndAddToDiagramUsingCommands(DiagramModel diagramModel, DiagramFactor diagramFactorFrom, DiagramFactor diagramFactorTo) throws Exception
	{
		DiagramObject diagramObject = diagramModel.getDiagramObject();
		return createFactorLinkAndAddToDiagramUsingCommands(diagramObject, diagramFactorFrom, diagramFactorTo);
	}
	
	public ORef createFactorLinkAndAddToDiagramUsingCommands(DiagramObject diagramObject, DiagramFactor fromDiagramFactor, DiagramFactor toDiagramFactor) throws Exception
	{
		Factor fromFactor = Factor.findFactor(getProject(), fromDiagramFactor.getWrappedORef());
		Factor toFactor = Factor.findFactor(getProject(), toDiagramFactor.getWrappedORef());
		ORef factorLinkRef = project.getFactorLinkPool().getLinkedRef(fromFactor, toFactor);
		
		if(!factorLinkRef.isInvalid())
			ensureLinkGoesOurWay(factorLinkRef, fromFactor.getFactorId());
		else
			factorLinkRef = createFactorLink(fromDiagramFactor, toDiagramFactor);

		createDiagramLink(diagramObject, createDiagramFactorLinkParameter(fromDiagramFactor.getRef(), toDiagramFactor.getRef(), factorLinkRef));
		return factorLinkRef; 
	}

	private void ensureLinkGoesOurWay(ORef factorLinkRef, FactorId fromFactorId) throws CommandFailedException
	{
		FactorLink link = (FactorLink)project.findObject(factorLinkRef);
		if (link.isBidirectional())
			return;
		
		if(link.getFromFactorRef().getObjectId().equals(fromFactorId))
			return;
		
		enableBidirectional(link.getRef());
	}

	private void enableBidirectional(ORef factorLinkRef) throws CommandFailedException
	{
		CommandSetObjectData command = new CommandSetObjectData(factorLinkRef, FactorLink.TAG_BIDIRECTIONAL_LINK, BooleanData.BOOLEAN_TRUE);
		project.executeCommand(command);
	}

	private void enableBidirectional(DiagramLink diagramLink) throws CommandFailedException
	{
		enableBidirectional(diagramLink.getWrappedRef());
	}
	
	public ORef createFactorLink(DiagramFactor fromDiagramFactor, DiagramFactor toDiagramFactor) throws Exception
	{
		ORef factorLinkRef = createFactorLinkWithPossibleThreatStressRatings(fromDiagramFactor.getWrappedORef(), toDiagramFactor.getWrappedORef());
		return factorLinkRef;
	}

	public ORef createFactorLinkWithPossibleThreatStressRatings(ORef fromFactorRef, ORef toFactorRef) throws Exception
	{
		ORef factorLinkRef = createFactorLink(fromFactorRef, toFactorRef);
		FactorLink factorLink = (FactorLink) project.findObject(factorLinkRef);
		if (factorLink.isThreatTargetLink())
			createAndAddThreatStressRatingsFromTarget(factorLinkRef, factorLink.getDownstreamTargetRef());
		
		return factorLinkRef;
	}
	
	public ORef createFactorLinkWithoutThreatStressRatings(ORef fromRef, ORef toRef) throws Exception
	{
		return createFactorLink(fromRef, toRef);
	}
	
	private ORef createFactorLink(ORef fromFactorRef, ORef toFactorRef) throws CommandFailedException
	{
		CreateFactorLinkParameter extraInfo = new CreateFactorLinkParameter(fromFactorRef, toFactorRef);
		CommandCreateObject createFactorLink = new CommandCreateObject(ObjectType.FACTOR_LINK, extraInfo);
		project.executeCommand(createFactorLink);
		
		return createFactorLink.getObjectRef();
	}
	
	public void createAndAddThreatStressRatingsFromTarget(ORef FactorLinkRef, ORef targetRef) throws Exception
	{
		ORefList threatStressRatingRefs = new ORefList();
		Target target = (Target) project.findObject(targetRef);
		ORefList stressRefs = target.getStressRefs();
		for (int i = 0; i < stressRefs.size(); ++i)
		{			
			ORef stressRef = stressRefs.get(i);
			ORef threatStressRatingRef = createThreatStressRating(stressRef);
			threatStressRatingRefs.add(threatStressRatingRef);
		}
		
		CommandSetObjectData setThreatStressRatingRefs = new CommandSetObjectData(FactorLinkRef, FactorLink.TAG_THREAT_STRESS_RATING_REFS, threatStressRatingRefs.toString());
		project.executeCommand(setThreatStressRatingRefs);
	}

	public ORef createThreatStressRating(ORef stressRef) throws CommandFailedException
	{
		CreateThreatStressRatingParameter extraInfo = new CreateThreatStressRatingParameter(stressRef);
		CommandCreateObject createThreatStressRating = new CommandCreateObject(ThreatStressRating.getObjectType(), extraInfo);
		project.executeCommand(createThreatStressRating);
		
		return createThreatStressRating.getObjectRef();
	}

	private void createDiagramLinkWithChildren(DiagramObject diagramObject, ORefList allLinkRefs, ORef fromDiagramFactorRef, ORef toDiagramFactorRef) throws Exception
	{
		CreateDiagramFactorLinkParameter extraInfoWithNoFactorLink = new CreateDiagramFactorLinkParameter(fromDiagramFactorRef, toDiagramFactorRef);
		ORef newGroupBoxDiagramLinkRef = createDiagramLink(diagramObject, extraInfoWithNoFactorLink);
	
		updateGroupBoxChildrenRefs(allLinkRefs, newGroupBoxDiagramLinkRef);
	}

	public void updateGroupBoxChildrenRefs(ORefList allLinkRefs, ORef newGroupBoxDiagramLinkRef) throws CommandFailedException
	{
		CommandSetObjectData setChildrenRefs = new CommandSetObjectData(newGroupBoxDiagramLinkRef, DiagramLink.TAG_GROUPED_DIAGRAM_LINK_REFS, allLinkRefs.toString());
		getProject().executeCommand(setChildrenRefs);
	}
	
	public ORef createDiagramLink(DiagramObject diagramObject, CreateDiagramFactorLinkParameter diagramLinkExtraInfo) throws CommandFailedException, ParseException
	{
		CommandCreateObject createDiagramLinkCommand =  new CommandCreateObject(ObjectType.DIAGRAM_LINK, diagramLinkExtraInfo);
		project.executeCommand(createDiagramLinkCommand);
    	
    	BaseId rawId = createDiagramLinkCommand.getCreatedId();
		DiagramLinkId createdDiagramLinkId = new DiagramLinkId(rawId.asInt());
		
		CommandSetObjectData addDiagramLink = CommandSetObjectData.createAppendIdCommand(diagramObject, DiagramObject.TAG_DIAGRAM_FACTOR_LINK_IDS, createdDiagramLinkId);
		project.executeCommand(addDiagramLink);
		
		return createDiagramLinkCommand.getObjectRef();
	}

	private CreateDiagramFactorLinkParameter createDiagramFactorLinkParameter(ORef fromDiagramFactorRef, ORef toDiagramFactorRef, ORef factorlLinkRef)
	{
		CreateDiagramFactorLinkParameter diagramLinkExtraInfo = new CreateDiagramFactorLinkParameter(factorlLinkRef, fromDiagramFactorRef, toDiagramFactorRef);
		
		return diagramLinkExtraInfo;
	}
	
	public ORefList createGroupBoxChildrenDiagramLinks(DiagramModel model, DiagramFactor fromDiagramFactorToUse, DiagramFactor toDiagramFactorToUse) throws Exception
	{
		if (fromDiagramFactorToUse.isGroupBoxFactor() && toDiagramFactorToUse.isGroupBoxFactor())
		{
			deleteRelatedGroupBoxLinks(model, fromDiagramFactorToUse, toDiagramFactorToUse.getGroupBoxChildrenRefs());
			deleteRelatedGroupBoxLinks(model, toDiagramFactorToUse, fromDiagramFactorToUse.getGroupBoxChildrenRefs());
		}
		
		ORefList allNonGroupBoxDiagramLinkRefs = new ORefList();
		ORefList fromDiagramFactorRefs = fromDiagramFactorToUse.getSelfOrChildren();
		ORefList toDiagramFactorRefs = toDiagramFactorToUse.getSelfOrChildren();
		DiagramObject diagramObject = model.getDiagramObject();
		for (int from = 0; from < fromDiagramFactorRefs.size(); ++from)
		{
			for (int to = 0; to < toDiagramFactorRefs.size(); ++to)
			{
				DiagramFactor fromDiagramFactor = DiagramFactor.find(getProject(), fromDiagramFactorRefs.get(from));
				DiagramFactor toDiagramFactor = DiagramFactor.find(getProject(), toDiagramFactorRefs.get(to));
				if (model.areLinked(fromDiagramFactor.getWrappedORef(), toDiagramFactor.getWrappedORef()))
				{
					DiagramLink diagramLink = model.getDiagramLink(fromDiagramFactor.getWrappedORef(), toDiagramFactor.getWrappedORef());
					allNonGroupBoxDiagramLinkRefs.add(diagramLink.getRef());
					continue;
				}
				
				ORef factorLinkRef = createFactorLinkAndAddToDiagramUsingCommands(diagramObject, fromDiagramFactor, toDiagramFactor);
				DiagramLink diagramLink = model.getDiagramLinkByWrappedRef(factorLinkRef);
				allNonGroupBoxDiagramLinkRefs.add(diagramLink.getRef());
			}
		}
		
		if (model.areLinked(fromDiagramFactorToUse.getWrappedORef(), toDiagramFactorToUse.getWrappedORef()))
		{
			DiagramLink groupBoxDiagramLink = model.getDiagramLink(fromDiagramFactorToUse.getWrappedORef(), toDiagramFactorToUse.getWrappedORef());
			updateGroupBoxChildrenRefs(allNonGroupBoxDiagramLinkRefs, groupBoxDiagramLink.getRef());
		}
		else
		{
			createDiagramLinkWithChildren(diagramObject, allNonGroupBoxDiagramLinkRefs, fromDiagramFactorToUse.getRef(), toDiagramFactorToUse.getRef());
		}
		
		if (anyOppositeLinks(allNonGroupBoxDiagramLinkRefs, fromDiagramFactorRefs, toDiagramFactorRefs))
			enableBidirectional(allNonGroupBoxDiagramLinkRefs);
		
		return allNonGroupBoxDiagramLinkRefs;
	}
	
	public void enableBidirectional(ORefList createdDiagramLinkRefs) throws Exception
	{
		for (int i = 0; i < createdDiagramLinkRefs.size(); ++i)
		{
			DiagramLink diagramLink = DiagramLink.find(getProject(), createdDiagramLinkRefs.get(i));
			enableBidirectional(diagramLink);
		}
	}
	
	public void enableBidirectionalityForFactorLinks(ORefList factorLinkRefs) throws Exception
	{
		for (int index = 0; index < factorLinkRefs.size(); ++index)
		{
			enableBidirectional(factorLinkRefs.get(index));		
		}
	}

	private boolean anyOppositeLinks(ORefList createdDiagramLinkRefs, ORefList fromDiagramFactorRefs, ORefList toDiagramFactorRefs)
	{
		for (int i = 0; i < createdDiagramLinkRefs.size(); ++i)
		{
			DiagramLink diagramLink = DiagramLink.find(getProject(), createdDiagramLinkRefs.get(i));
			if (diagramLink.isBidirectional())
				return true;
			
			ORef toDiagramFactorRef = diagramLink.getToDiagramFactorRef();
			if (fromDiagramFactorRefs.contains(toDiagramFactorRef))
				return true;
			
			ORef fromDiagramFactorRef = diagramLink.getFromDiagramFactorRef();
			if (toDiagramFactorRefs.contains(fromDiagramFactorRef))
				return true;
		}
		
		return false;
	}
	
	private void deleteRelatedGroupBoxLinks(DiagramModel model, DiagramFactor groupBoxDiagramFactor, ORefList groupBoxChildren) throws Exception
	{
		LinkDeletor linkDeletor = new LinkDeletor(getProject());
		for (int childRef = 0; childRef < groupBoxChildren.size(); ++childRef)
		{
			ORefList diagramLinkRefs = model.getDiagramLinkFromDiagramFactors(groupBoxDiagramFactor.getRef(), groupBoxChildren.get(childRef));
			for (int refIndex = 0; refIndex < diagramLinkRefs.size(); ++refIndex)
			{
				DiagramLink diagramLink = DiagramLink.find(getProject(), diagramLinkRefs.get(refIndex));
				linkDeletor.deleteDiagramLink(diagramLink);
			}
		}
	}
	
	public static boolean isValidLinkableType(int wrappedType)
	{
		return getLinkableTypes().contains(wrappedType);
	}

	private static HashSet getLinkableTypes()
	{
		int[] linkableTypesArray = {Strategy.getObjectType(), 
							   Cause.getObjectType(), 
							   IntermediateResult.getObjectType(), 
							   ThreatReductionResult.getObjectType(), 
							   Target.getObjectType(),
							   GroupBox.getObjectType(), };  
		
		HashSet linkableTypes = new HashSet();
		for (int i = 0; i < linkableTypesArray.length; ++i)
		{
			linkableTypes.add(linkableTypesArray[i]);
		}
		
		return linkableTypes;
	}
	
	public void splitSelectedLinkToIncludeFactor(DiagramModel diagramModel, DiagramLink diagramLink, DiagramFactor diagramFactor) throws Exception
	{
		DiagramFactor fromDiagramFactor = diagramLink.getFromDiagramFactor();
		DiagramFactor toDiagramFactor = diagramLink.getToDiagramFactor();
		boolean isBidirectional = diagramLink.isBidirectional();
		
		LinkDeletor linkDeletor = new LinkDeletor(getProject());
		linkDeletor.deleteDiagramLinkAndOrphandFactorLink(diagramLink);
		
		if (!fromDiagramFactor.isGroupBoxFactor() && !toDiagramFactor.isGroupBoxFactor())
		{
			ORef factorLinkRef1 = createFactorLinkAndAddToDiagramUsingCommands(diagramModel, fromDiagramFactor, diagramFactor);
			ensureFactorLinkBidirectionality(factorLinkRef1, isBidirectional);
			
			ORef factorLinkRef2 = createFactorLinkAndAddToDiagramUsingCommands(diagramModel, diagramFactor, toDiagramFactor);
			ensureFactorLinkBidirectionality(factorLinkRef2, isBidirectional);
		}
		else if (fromDiagramFactor.isGroupBoxFactor() && toDiagramFactor.isGroupBoxFactor())
		{
			ORefList diagramLinkRefsToUse1 = createGroupBoxChildrenDiagramLinks(diagramModel, fromDiagramFactor, diagramFactor);
			ensureDiagramLinkBidirectionality(diagramLinkRefsToUse1, isBidirectional);
			
			ORefList diagramLinkRefsToUse2 = createGroupBoxChildrenDiagramLinks(diagramModel, diagramFactor, toDiagramFactor);
			ensureDiagramLinkBidirectionality(diagramLinkRefsToUse2, isBidirectional);
		}
		else
		{
			ORefList factorLinkRefs1 = createDiagramLinks(diagramModel, diagramFactor, fromDiagramFactor, toDiagramFactor);
			ensureFactorLinkBidirectionality(factorLinkRefs1, isBidirectional);
			
			ORefList factorLinkRefs2 = createDiagramLinks(diagramModel, diagramFactor, toDiagramFactor, fromDiagramFactor);
			ensureFactorLinkBidirectionality(factorLinkRefs2, isBidirectional);
		}
	}
	
	private void ensureDiagramLinkBidirectionality(ORefList diagramLinkRefs, boolean isBidirectional) throws Exception
	{
		if (!isBidirectional)
			return;
		
		for (int index = 0; index < diagramLinkRefs.size(); ++index)
		{
			DiagramLink diagramLink = DiagramLink.find(getProject(), diagramLinkRefs.get(index));
			enableBidirectional(diagramLink);
		}
	}
	
	private void ensureFactorLinkBidirectionality(ORefList factorLinkRefs, boolean isBidirectional) throws Exception
	{
		if (!isBidirectional)
			return;
		
		for (int index = 0; index < factorLinkRefs.size(); ++index)
		{
			enableBidirectional(factorLinkRefs.get(index));
		}
	}

	private void ensureFactorLinkBidirectionality(ORef factorLinkRef, boolean isBidirectional) throws Exception
	{
		ensureFactorLinkBidirectionality(new ORefList(factorLinkRef), isBidirectional);
	}

	private ORefList createDiagramLinks(DiagramModel diagramModel, DiagramFactor diagramFactor, DiagramFactor diagramFactor1, DiagramFactor diagramFactor2) throws Exception
	{
		ORefList factorLinkRefs = new ORefList();
		if (diagramFactor1.isGroupBoxFactor() && !diagramFactor2.isGroupBoxFactor())
		{
			ORefList diagramLinkRefs = createGroupBoxChildrenDiagramLinks(diagramModel, diagramFactor1, diagramFactor);
			factorLinkRefs.addAll(convertToFactorLinks(diagramLinkRefs));
		
			ORef factorLinkRef = createFactorLinkAndAddToDiagramUsingCommands(diagramModel, diagramFactor, diagramFactor2);
			factorLinkRefs.add(factorLinkRef);
		}
		
		return factorLinkRefs;
	}
	
	private ORefList convertToFactorLinks(ORefList diagramLinkRefs)
	{
		ORefList factorLinkRefs = new ORefList();
		for (int index = 0; index < diagramLinkRefs.size(); ++index)
		{
			DiagramLink diagramLink = DiagramLink.find(getProject(), diagramLinkRefs.get(index));
			factorLinkRefs.add(diagramLink.getWrappedRef());
		}
		
		return factorLinkRefs;
	}
	
	public void createFactorLinkAndDiagramLink(DiagramModel model, DiagramFactor from, DiagramFactor to) throws Exception
	{
		if (!from.isGroupBoxFactor() && !to.isGroupBoxFactor())
			createFactorLinkAndAddToDiagramUsingCommands(model, from, to);
		else
			createGroupBoxChildrenDiagramLinks(model, from, to);
	}
	
	private Project getProject()
	{
		return project;
	}
	
	private Project project;
}
