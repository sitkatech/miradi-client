/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.project;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import org.miradi.commands.Command;
import org.miradi.commands.CommandCreateObject;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.diagram.ChainWalker;
import org.miradi.diagram.DiagramModel;
import org.miradi.diagram.cells.FactorCell;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.ids.DiagramFactorId;
import org.miradi.ids.FactorLinkId;
import org.miradi.ids.IdList;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.CreateDiagramFactorLinkParameter;
import org.miradi.objecthelpers.CreateDiagramFactorParameter;
import org.miradi.objecthelpers.CreateObjectParameter;
import org.miradi.objecthelpers.FactorSet;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objecthelpers.OldToNewDiagramFactorMap;
import org.miradi.objects.BaseObject;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.Factor;
import org.miradi.objects.FactorLink;
import org.miradi.objects.HumanWelfareTarget;
import org.miradi.objects.ResultsChainDiagram;
import org.miradi.objects.Strategy;
import org.miradi.objects.Stress;
import org.miradi.objects.Task;
import org.miradi.objects.ThreatReductionResult;
import org.miradi.utils.PointList;
import org.miradi.views.diagram.LinkCreator;

public class ResultsChainCreatorHelper
{
	public ResultsChainCreatorHelper(Project projectToUse, DiagramModel diagramModelToUse, FactorCell[] selectedCellsToUse)
	{
		project = projectToUse;
		model = diagramModelToUse;
		selectedCells = selectedCellsToUse;
	}
		
	public ORef createResultsChain() throws Exception
	{
			HashSet<DiagramFactor> selectedDiagramFactors = getSelectedAndRelatedDiagramFactors();
			DiagramLink[] diagramLinks = getDiagramLinksInChain();
			CommandCreateObject createResultsChain = new CommandCreateObject(ObjectType.RESULTS_CHAIN_DIAGRAM);
			getProject().executeCommand(createResultsChain);
			
			ORef newResultsChainRef = createResultsChain.getObjectRef();
			ResultsChainDiagram resultsChain = (ResultsChainDiagram) getProject().findObject(newResultsChainRef);
			
			HashMap<DiagramFactor, DiagramFactor> clonedDiagramFactors = cloneDiagramFactors(selectedDiagramFactors);
			ORefList clonedDiagramFactorRefs = extractClonedObjectRefs(clonedDiagramFactors);
			IdList idList = clonedDiagramFactorRefs.convertToIdList(DiagramFactor.getObjectType());
			CommandSetObjectData addFactorsToChain = CommandSetObjectData.createAppendListCommand(resultsChain, ResultsChainDiagram.TAG_DIAGRAM_FACTOR_IDS, idList);
			getProject().executeCommand(addFactorsToChain);
			
			updateAllGroupBoxChildren(clonedDiagramFactors);
			
			HashMap<DiagramLink, DiagramLink> clonedDiagramLinks = cloneDiagramLinks(diagramLinks, clonedDiagramFactors);
			ORefList clonedDiagramLinkRefs = extractClonedObjectRefs(clonedDiagramLinks);
			IdList diagramLinkList = clonedDiagramLinkRefs.convertToIdList(DiagramLink.getObjectType());
			CommandSetObjectData addLinksToChain = CommandSetObjectData.createAppendListCommand(resultsChain, ResultsChainDiagram.TAG_DIAGRAM_FACTOR_LINK_IDS, diagramLinkList);
			getProject().executeCommand(addLinksToChain);
			
			updateAllGroupBoxLinkChildren(clonedDiagramLinks);
			
			String label = getFirstStrategyShortLabel(selectedDiagramFactors); 
			CommandSetObjectData setLabelCommand = new CommandSetObjectData(newResultsChainRef, DiagramObject.TAG_LABEL, label);
			getProject().executeCommand(setLabelCommand);
			
			return newResultsChainRef;
	}
	
	private void updateAllGroupBoxLinkChildren(HashMap<DiagramLink, DiagramLink> clonedDiagramLinks) throws Exception
	{
		Set<DiagramLink> keys = clonedDiagramLinks.keySet();
		for(DiagramLink diagramLink : keys)
		{
			if (diagramLink.isGroupBoxLink())
			{
				updateGroupBoxLinkChildren(diagramLink, clonedDiagramLinks);
			}
		}
	}
	
	private void updateGroupBoxLinkChildren(DiagramLink originalDiagramLink, HashMap<DiagramLink, DiagramLink> clonedDiagramLinks) throws Exception
	{
		ORefList newlyClonedChildren = new ORefList();
		DiagramLink clonedGroupBoxDiagramLink = clonedDiagramLinks.get(originalDiagramLink);
		ORefList groupedDiagramLinkRefs = originalDiagramLink.getSelfOrChildren();
		for (int childIndex = 0; childIndex < groupedDiagramLinkRefs.size(); ++childIndex)
		{
			ORef diagramLinkRef = groupedDiagramLinkRefs.get(childIndex);
			DiagramLink child = DiagramLink.find(getProject(), diagramLinkRef);
			DiagramLink clonedDiagramLink = clonedDiagramLinks.get(child);
			newlyClonedChildren.add(clonedDiagramLink.getRef());
		}
		
		CommandSetObjectData setGroupBoxChildren = new CommandSetObjectData(clonedGroupBoxDiagramLink.getRef(), DiagramLink.TAG_GROUPED_DIAGRAM_LINK_REFS, newlyClonedChildren.toString());
		getProject().executeCommand(setGroupBoxChildren);
	}
	
	private void updateAllGroupBoxChildren(HashMap<DiagramFactor, DiagramFactor> clonedDiagramFactors) throws Exception
	{
		Set<DiagramFactor> keys = clonedDiagramFactors.keySet();
		for(DiagramFactor diagramFactor : keys)
		{
			if (diagramFactor.isGroupBoxFactor())
			{
				updateGroupBoxChildren(diagramFactor, clonedDiagramFactors);
			}
		}
	}
	
	private void updateGroupBoxChildren(DiagramFactor originalDiagramFactor, HashMap<DiagramFactor, DiagramFactor> clonedDiagramFactors) throws Exception
	{
		ORefList newlyClonedChildren = new ORefList();
		DiagramFactor clonedGroupBoxDiagramFactor = clonedDiagramFactors.get(originalDiagramFactor);
		ORefList groupBoxRefs = originalDiagramFactor.getSelfOrChildren();
		for (int childIndex = 0; childIndex < groupBoxRefs.size(); ++childIndex)
		{
			ORef childRef = groupBoxRefs.get(childIndex);
			DiagramFactor child = DiagramFactor.find(getProject(), childRef);
			DiagramFactor clonedDiagramFactor = clonedDiagramFactors.get(child);
			newlyClonedChildren.add(clonedDiagramFactor.getRef());
		}
		
		CommandSetObjectData setGroupBoxChildren = new CommandSetObjectData(clonedGroupBoxDiagramFactor.getRef(), DiagramFactor.TAG_GROUP_BOX_CHILDREN_REFS, newlyClonedChildren.toString());
		getProject().executeCommand(setGroupBoxChildren);
	}

	private String getFirstStrategyShortLabel(HashSet<DiagramFactor> diagramFactors) throws Exception
	{
		for(DiagramFactor diagramFactor : diagramFactors)
		{
			if (isNonDraftStrategy(diagramFactor))
			{
				Strategy strategy = (Strategy) getProject().findObject(diagramFactor.getWrappedORef());
				return getLabel(strategy);
			}
		}
		
		return EAM.text("[New Results Chain]");
	}
	
	//TODO RC this test should go away once Draft Strategies are excluded from list
	private boolean isNonDraftStrategy(DiagramFactor diagramFactor)
	{
		if (diagramFactor.getWrappedType() != ObjectType.STRATEGY)
			return false;
		
		Strategy strategy = (Strategy) getProject().findObject(diagramFactor.getWrappedORef());
		if (strategy.isStatusDraft())
			return false;
		
		return true;
	}

	private String getLabel(Strategy strategy)
	{
		String shortLabel = strategy.getLabel();
		if (shortLabel.trim().length() > 0)
			return shortLabel;
		
		return EAM.text("Results Chain");
	}

	private ORefList extractClonedObjectRefs(HashMap<? extends BaseObject, ? extends BaseObject> clonedBaseObjects)
	{
		ORefList clonedBaseObjectRefs = new ORefList();
		Vector<BaseObject> baseObjects = new Vector<BaseObject>(clonedBaseObjects.values());
		for (int i = 0; i < baseObjects.size(); i ++)
		{
			BaseObject baseObject = baseObjects.get(i);
			clonedBaseObjectRefs.add(baseObject.getRef());
		}
		
		return clonedBaseObjectRefs;
	}

	public OldToNewDiagramFactorMap cloneDiagramFactors(HashSet<DiagramFactor> selectedDiagramFactors) throws Exception
	{
		OldToNewDiagramFactorMap originalAndClonedDiagramFactors = new OldToNewDiagramFactorMap();
		for(DiagramFactor diagramFactorToBeCloned : selectedDiagramFactors)
		{	
			if (diagramFactorToBeCloned.isGroupBoxFactor())
				originalAndClonedDiagramFactors.putAll(cloneGroupBoxDiagramFactor(selectedDiagramFactors, diagramFactorToBeCloned));
			else
				originalAndClonedDiagramFactors.putAll(cloneDiagramFactor(selectedDiagramFactors, diagramFactorToBeCloned));
		}
		
		return originalAndClonedDiagramFactors;
	}

	private OldToNewDiagramFactorMap cloneGroupBoxDiagramFactor(HashSet<DiagramFactor> selectedDiagramFactors, DiagramFactor groupBox) throws Exception
	{
		OldToNewDiagramFactorMap originalAndClonedDiagramFactors = new OldToNewDiagramFactorMap();
		ORefList childrenRefs = groupBox.getGroupBoxChildrenRefs();
		for (int childIndex = 0; childIndex < childrenRefs.size(); ++childIndex)
		{
			DiagramFactor child = DiagramFactor.find(getProject(), childrenRefs.get(childIndex));
			if (!selectedDiagramFactors.contains(child))
				originalAndClonedDiagramFactors.putAll(cloneDiagramFactor(selectedDiagramFactors, child));
		}
		
		originalAndClonedDiagramFactors.putAll(cloneDiagramFactor(selectedDiagramFactors, groupBox));
		
		return originalAndClonedDiagramFactors;
	}

	private OldToNewDiagramFactorMap cloneDiagramFactor(HashSet<DiagramFactor> selectedDiagramFactors, DiagramFactor diagramFactorToBeCloned) throws Exception, CommandFailedException
	{
		OldToNewDiagramFactorMap originalAndClonedDiagramFactors = new OldToNewDiagramFactorMap();
		if (ignoreCloning(selectedDiagramFactors, diagramFactorToBeCloned))
			return originalAndClonedDiagramFactors;
		
		ORef factorRef = createOrReuseWrappedObject(diagramFactorToBeCloned);
		
		CreateDiagramFactorParameter extraDiagramFactorInfo = new CreateDiagramFactorParameter(factorRef);
		CommandCreateObject createDiagramFactor = new CommandCreateObject(ObjectType.DIAGRAM_FACTOR, extraDiagramFactorInfo);
		getProject().executeCommand(createDiagramFactor);
		
		DiagramFactorId newlyCreatedId = (DiagramFactorId) createDiagramFactor.getCreatedId();
		Command[] commandsToClone = diagramFactorToBeCloned.createCommandsToMirror(newlyCreatedId);
		getProject().executeCommandsWithoutTransaction(commandsToClone);
		
		DiagramFactor clonedDiagramFactor = (DiagramFactor) getProject().findObject(new ORef(ObjectType.DIAGRAM_FACTOR, newlyCreatedId));
		originalAndClonedDiagramFactors.put(diagramFactorToBeCloned, clonedDiagramFactor);
		
		return originalAndClonedDiagramFactors;
	}
	
	private boolean ignoreCloning(HashSet<DiagramFactor> selectedDiagramFactors, DiagramFactor diagramFactor)
	{
		if (Stress.is(diagramFactor.getWrappedType()))
			return true;
		
		if (Task.is(diagramFactor.getWrappedType()))
			return !containsRelatedStrategy(selectedDiagramFactors, (Task)diagramFactor.getWrappedFactor());
		
		return false;
	}

	private boolean containsRelatedStrategy(HashSet<DiagramFactor> selectedDiagramFactors, Task activity)
	{
		ORefList strategyReferrerRefs = activity.findObjectsThatReferToUs(Strategy.getObjectType());
		for (int index = 0; index < strategyReferrerRefs.size(); ++index)
		{
			ORef strategyRef = strategyReferrerRefs.get(index);
			DiagramFactor strategyDiagramFactor = model.getDiagramFactor(strategyRef);
			if (selectedDiagramFactors.contains(strategyDiagramFactor))
				return true;
		}
		
		return false;
	}

	private ORef createOrReuseWrappedObject(DiagramFactor diagramFactor) throws Exception
	{
		if (diagramFactor.getWrappedType() == ObjectType.TARGET)
			return diagramFactor.getWrappedORef();
		
		if (diagramFactor.getWrappedType() == ObjectType.HUMAN_WELFARE_TARGET)
			return diagramFactor.getWrappedORef();
		
		if (diagramFactor.getWrappedType() == ObjectType.STRATEGY)
			return diagramFactor.getWrappedORef();
		
		if (diagramFactor.getWrappedType() == ObjectType.INTERMEDIATE_RESULT)
			return diagramFactor.getWrappedORef();
		
		if (diagramFactor.getWrappedType() == ObjectType.THREAT_REDUCTION_RESULT)
			return diagramFactor.getWrappedORef();

		if (diagramFactor.getWrappedType() == ObjectType.TASK)
			return diagramFactor.getWrappedORef();
		
		if (diagramFactor.getWrappedType() == ObjectType.SCOPE_BOX)
			return diagramFactor.getWrappedORef();
		
		
		if (diagramFactor.getWrappedType() == ObjectType.CAUSE)
			return createNewFactorAndSetLabel(diagramFactor);
		
		if (diagramFactor.getWrappedType() == ObjectType.TEXT_BOX)
			return createNewFactorAndSetLabel(diagramFactor);
		
		if (diagramFactor.getWrappedType() == ObjectType.GROUP_BOX)
			return createNewFactorAndSetLabel(diagramFactor);
		
		throw new Exception("wrapped type not found "+diagramFactor.getWrappedType());
	}

	private ORef createNewFactorAndSetLabel(DiagramFactor diagramFactor) throws Exception, CommandFailedException
	{
		Factor factor = (Factor) getProject().findObject(diagramFactor.getWrappedORef());
		CommandCreateObject createCommand = createNewFactorCommand(factor);
		getProject().executeCommand(createCommand);
		
		ORef newlyCreatedRef = createCommand.getObjectRef();
		transferAnnotationsToNewFactor(factor.getRef(), newlyCreatedRef, Factor.TAG_INDICATOR_IDS);
		transferAnnotationsToNewFactor(factor.getRef(), newlyCreatedRef, Factor.TAG_OBJECTIVE_IDS);
		
		String clonedLabel = new String("[ " + factor.getLabel() + " ]");
		CommandSetObjectData setLabelCommand = new CommandSetObjectData(newlyCreatedRef, Factor.TAG_LABEL, clonedLabel);
		getProject().executeCommand(setLabelCommand);
		
		possiblySetThreatReductionResultsDirectThreat(factor, newlyCreatedRef);
		
		return newlyCreatedRef;
	}

	public void transferAnnotationsToNewFactor(ORef tranferringFromFactorRef, ORef transferringToFactorRef, String tag) throws Exception
	{
		String idsToMoveToTransfer = getProject().getObjectData(tranferringFromFactorRef, tag);
		CommandSetObjectData setNewlyCreatedToPointToIds = new CommandSetObjectData(transferringToFactorRef, tag, idsToMoveToTransfer);
		getProject().executeCommand(setNewlyCreatedToPointToIds);
		
		CommandSetObjectData clearOriginalIds = new CommandSetObjectData(tranferringFromFactorRef, tag, "");
		getProject().executeCommand(clearOriginalIds);
	}

	private void possiblySetThreatReductionResultsDirectThreat(Factor factor, ORef newlyCreatedRef) throws CommandFailedException
	{
		if (! factor.isDirectThreat())
			return;
		
		CommandSetObjectData setDirectThreat = new CommandSetObjectData(newlyCreatedRef, ThreatReductionResult.TAG_RELATED_DIRECT_THREAT_REF, factor.getRef().toString());
		getProject().executeCommand(setDirectThreat);
	}

	private CommandCreateObject createNewFactorCommand(Factor factor) throws Exception
	{
		if (factor.isDirectThreat())
			return new CommandCreateObject(ObjectType.THREAT_REDUCTION_RESULT);
		
		if (factor.isContributingFactor())
			return new CommandCreateObject(ObjectType.INTERMEDIATE_RESULT);
		
		if (factor.isTextBox())
			return new CommandCreateObject(ObjectType.TEXT_BOX);
		
		if (factor.isGroupBox())
			return new CommandCreateObject(ObjectType.GROUP_BOX);
		
		throw new Exception("cannot create object for type " + factor.getType());
	}

	private HashSet<DiagramFactor> getSelectedAndRelatedDiagramFactors()
	{
		FactorCell[] selectedFactorCells = getSelectedCells();
		if (containsOnlyStrategies(selectedFactorCells))
			return getRelatedDiagramFactors(selectedFactorCells);
			
		return extractDiagramFactors(selectedFactorCells);
	}
	
	private HashSet<DiagramFactor> extractDiagramFactors(FactorCell[] selectedFactorCells)
	{
		HashSet<DiagramFactor> diagramFactors = new HashSet<DiagramFactor>();
		for (int i = 0; i < selectedFactorCells.length; ++i)
		{
			diagramFactors.add(selectedFactorCells[i].getDiagramFactor());
		}
		
		return diagramFactors;
	}

	private HashSet<DiagramFactor> getRelatedDiagramFactors(FactorCell[] selectedFactorCells)
	{
		HashSet<DiagramFactor> allDiagramFactors = new HashSet<DiagramFactor>();
		for (int i = 0; i < selectedFactorCells.length; i++)
		{
			DiagramFactor diagramFactor = selectedFactorCells[i].getDiagramFactor();
			ChainWalker chainObject = diagramFactor.getDiagramChainWalker();
			FactorSet chainFactors = chainObject.buildNormalChainAndGetFactors(diagramFactor);
			Factor[] factorsArray = chainFactors.toFactorArray();
			
			Vector<DiagramFactor> diagramFactors = convertToDiagramFactors(factorsArray);
			allDiagramFactors.addAll(diagramFactors);
		}
	
		return allDiagramFactors;	
	}

	private boolean containsOnlyStrategies(FactorCell[] selectedFactorCells)
	{
		for (int i = 0; i < selectedFactorCells.length; ++i)
		{
			if (!isNonDraftStrategy(selectedFactorCells[i].getDiagramFactor()))
				return false;
		}
		
		return true;
	}

	private DiagramLink[] getDiagramLinksInChain() throws Exception
	{
		FactorCell[] selectedFactorCells = getSelectedCells();
		if (containsOnlyStrategies(selectedFactorCells))
			return getLinksInRelatedFactors(selectedFactorCells);

		return getDiagramLinksAndChildrenInSelection();
	}

	private FactorCell[] getSelectedCells()
	{
		return selectedCells;
	}
	
	private DiagramLink[] getDiagramLinksAndChildrenInSelection()
	{
		ORefSet diagramFactorAndChildrenRefSet = extractDiagramFactorRefs();
		DiagramLink[] allDiagramLinks = model.getAllDiagramLinksAsArray();
		Vector<DiagramLink> containedDiagramLinks = new Vector<DiagramLink>();
		for (int i = 0; i < allDiagramLinks.length; ++i)
		{
			ORef fromRef = allDiagramLinks[i].getFromDiagramFactorRef();
			ORef toRef = allDiagramLinks[i].getToDiagramFactorRef();
			if (diagramFactorAndChildrenRefSet.contains(fromRef) && diagramFactorAndChildrenRefSet.contains(toRef))
				containedDiagramLinks.add(allDiagramLinks[i]);
		}
		
		return containedDiagramLinks.toArray(new DiagramLink[0]);
	}
	
	private ORefSet extractDiagramFactorRefs()
	{
		ORefSet diagramFactorAndChildrenRefSet = new ORefSet();
		FactorCell[] selectedFactorCells = getSelectedCells();
		for (int i = 0; i < selectedFactorCells.length; ++i)
		{
			DiagramFactor diagramFactor = selectedFactorCells[i].getDiagramFactor();
			diagramFactorAndChildrenRefSet.addAllRefs(diagramFactor.getSelfAndChildren());
		}
		
		return diagramFactorAndChildrenRefSet;
	}

	private DiagramLink[] getLinksInRelatedFactors(FactorCell[] selectedFactorCells) throws Exception
	{
		HashSet<DiagramLink> allDiagramLinks = new HashSet<DiagramLink>();
		for (int i = 0; i < selectedFactorCells.length; i++)
		{
			DiagramFactor diagramFactor = selectedFactorCells[i].getDiagramFactor();
			ChainWalker chainObject = diagramFactor.getDiagramChainWalker();  
			allDiagramLinks.addAll(chainObject.buildNormalChainAndGetDiagramLinks(diagramFactor));
		}
		
		return allDiagramLinks.toArray(new DiagramLink[0]);
	}
	
	private HashMap<DiagramLink, DiagramLink> cloneDiagramLinks(DiagramLink[] diagramLinks, HashMap diagramFactors) throws Exception
	{
		HashMap<DiagramLink, DiagramLink> originalAndClonedDiagramLinks = new HashMap<DiagramLink, DiagramLink>();
		for (int i = 0; i < diagramLinks.length; i++)
		{
			DiagramLink diagramLink = diagramLinks[i];
			if (canAddLinkToResultsChain(diagramLink))
			{
				DiagramLink clonedDiagramLink  = cloneDiagramFactorLink(diagramFactors, diagramLink);
				originalAndClonedDiagramLinks.put(diagramLink, clonedDiagramLink);
			}
		}
		
		return originalAndClonedDiagramLinks;
	}
	
	private boolean canAddTypeToResultsChain(DiagramFactor diagramFactor)
	{
		if (diagramFactor.getWrappedType() == ObjectType.TARGET)
			return true;
		
		if (HumanWelfareTarget.is(diagramFactor.getWrappedType()))
			return true;
		
		if (diagramFactor.getWrappedType() == ObjectType.CAUSE)
			return true;
		
		if (isNonDraftStrategy(diagramFactor))
			return true;

		if (diagramFactor.getWrappedType() == ObjectType.INTERMEDIATE_RESULT)
			return true;
		
		if (diagramFactor.getWrappedType() == ObjectType.THREAT_REDUCTION_RESULT)
			return true;
		
		if (diagramFactor.getWrappedType() == ObjectType.GROUP_BOX)
			return true;
		
		return false;
	}
	
	private boolean canAddLinkToResultsChain(DiagramLink link)
	{
		DiagramFactor fromDiagramFactor = DiagramFactor.find(getProject(), link.getFromDiagramFactorRef());
		DiagramFactor toDiagramFactor = DiagramFactor.find(getProject(), link.getToDiagramFactorRef());
		
		return (canAddTypeToResultsChain(fromDiagramFactor) && canAddTypeToResultsChain(toDiagramFactor));
	}

	private DiagramLink cloneDiagramFactorLink(HashMap diagramFactors, DiagramLink diagramLink) throws Exception
	{
		DiagramFactor fromDiagramFactor = diagramLink.getFromDiagramFactor();
		DiagramFactor fromClonedDiagramFactor = (DiagramFactor) diagramFactors.get(fromDiagramFactor);
		 
		DiagramFactor toDiagramFactor = diagramLink.getToDiagramFactor();
		DiagramFactor toClonedDiagramFactor = (DiagramFactor) diagramFactors.get(toDiagramFactor);
		
		CreateObjectParameter extraInfo = createDiagramLinkExtraInfo(diagramLink, fromDiagramFactor, fromClonedDiagramFactor, toDiagramFactor, toClonedDiagramFactor);
		CommandCreateObject createDiagramLink = new CommandCreateObject(ObjectType.DIAGRAM_LINK, extraInfo);
		getProject().executeCommand(createDiagramLink);

		DiagramLink newlyCreated = (DiagramLink) getProject().findObject(createDiagramLink.getObjectRef());
		PointList bendPoints = diagramLink.getBendPoints();
		CommandSetObjectData setBendPoints = CommandSetObjectData.createNewPointList(newlyCreated, DiagramLink.TAG_BEND_POINTS, bendPoints);
		getProject().executeCommand(setBendPoints);

		return newlyCreated;
	}
	
	
	private CreateObjectParameter createDiagramLinkExtraInfo(DiagramLink diagramLink, DiagramFactor from, DiagramFactor fromCloned, DiagramFactor to, DiagramFactor toCloned) throws Exception
	{
		if (areSharingTheSameFactor(from, fromCloned, to, toCloned))
			return new CreateDiagramFactorLinkParameter(diagramLink.getWrappedId(), fromCloned.getDiagramFactorId(), toCloned.getDiagramFactorId());

		ORef factorLinkRef = new ORef(FactorLink.getObjectType(), new FactorLinkId(FactorLinkId.INVALID.asInt()));
		if(!diagramLink.isGroupBoxLink())
			factorLinkRef = new LinkCreator(getProject()).createFactorLink(fromCloned, toCloned);

		return new CreateDiagramFactorLinkParameter(factorLinkRef, fromCloned.getRef(), toCloned.getRef());
	}

	private boolean areSharingTheSameFactor(DiagramFactor from, DiagramFactor fromCloned, DiagramFactor to, DiagramFactor toCloned)
	{
		return from.getWrappedORef().equals(fromCloned.getWrappedORef()) && to.getWrappedORef().equals(toCloned.getWrappedORef());
	}
	
	private Vector<DiagramFactor> convertToDiagramFactors(Factor[] factors)
	{
		Vector<DiagramFactor> vector = new Vector<DiagramFactor>();
		for (int i = 0; i < factors.length; i++)
		{
			ORef factorRef = factors[i].getRef();
			DiagramFactor diagramFactor = model.getDiagramFactor(factorRef);
			if (canAddTypeToResultsChain(diagramFactor))
				vector.add(diagramFactor);
		}
		
		return vector;
	}
	
	private Project getProject()
	{
		return project;
	}

	private DiagramModel model;
	private FactorCell[] selectedCells;
	private Project project;
}
 