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
import org.miradi.diagram.DiagramChainObject;
import org.miradi.diagram.DiagramModel;
import org.miradi.diagram.cells.FactorCell;
import org.miradi.dialogs.diagram.DiagramPanel;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.ids.DiagramFactorId;
import org.miradi.ids.FactorLinkId;
import org.miradi.ids.IdList;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.CreateDiagramFactorLinkParameter;
import org.miradi.objecthelpers.CreateDiagramFactorParameter;
import org.miradi.objecthelpers.CreateObjectParameter;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.Factor;
import org.miradi.objects.FactorLink;
import org.miradi.objects.ResultsChainDiagram;
import org.miradi.objects.Strategy;
import org.miradi.objects.Stress;
import org.miradi.objects.Task;
import org.miradi.objects.ThreatReductionResult;
import org.miradi.utils.PointList;
import org.miradi.views.diagram.LinkCreator;

public class ResultsChainCreatorHelper
{
	public ResultsChainCreatorHelper(Project projectToUse, DiagramPanel diagramPanelToUse)
	{
		project = projectToUse;
		diagramPanel = diagramPanelToUse;
		model = diagramPanel.getDiagramModel();
	}
		
	public ORef createResultsChain() throws Exception
	{
			HashSet<DiagramFactor> diagramFactors = getSelectedAndRelatedDiagramFactors();
			DiagramLink[] diagramLinks = getDiagramLinksInChain();
			CommandCreateObject createResultsChain = new CommandCreateObject(ObjectType.RESULTS_CHAIN_DIAGRAM);
			project.executeCommand(createResultsChain);
			
			ORef newResultsChainRef = createResultsChain.getObjectRef();
			ResultsChainDiagram resultsChain = (ResultsChainDiagram) project.findObject(newResultsChainRef);
			
			HashMap clonedDiagramFactors = cloneDiagramFactors(diagramFactors);
			ORefList clonedDiagramFactorRefs = extractClonedObjectRefs(clonedDiagramFactors);
			IdList idList = clonedDiagramFactorRefs.convertToIdList(DiagramFactor.getObjectType());
			CommandSetObjectData addFactorsToChain = CommandSetObjectData.createAppendListCommand(resultsChain, ResultsChainDiagram.TAG_DIAGRAM_FACTOR_IDS, idList);
			project.executeCommand(addFactorsToChain);
			
			updateAllGroupBoxChildren(clonedDiagramFactors);
			
			HashMap clonedDiagramLinks = cloneDiagramLinks(diagramLinks, clonedDiagramFactors);
			ORefList clonedDiagramLinkRefs = extractClonedObjectRefs(clonedDiagramLinks);
			IdList diagramLinkList = clonedDiagramLinkRefs.convertToIdList(DiagramLink.getObjectType());
			CommandSetObjectData addLinksToChain = CommandSetObjectData.createAppendListCommand(resultsChain, ResultsChainDiagram.TAG_DIAGRAM_FACTOR_LINK_IDS, diagramLinkList);
			project.executeCommand(addLinksToChain);
			
			updateAllGroupBoxLinkChildren(clonedDiagramLinks);
			
			String label = getFirstStrategyShortLabel(diagramFactors); 
			CommandSetObjectData setLabelCommand = new CommandSetObjectData(newResultsChainRef, DiagramObject.TAG_LABEL, label);
			project.executeCommand(setLabelCommand);
			
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
		ORefList groupBoxChildrenRefs = originalDiagramLink.getSelfOrChildren();
		for (int childIndex = 0; childIndex < groupBoxChildrenRefs.size(); ++childIndex)
		{
			ORef childRef = groupBoxChildrenRefs.get(childIndex);
			DiagramLink child = DiagramLink.find(project, childRef);
			DiagramLink clonedDiagramLink = clonedDiagramLinks.get(child);
			newlyClonedChildren.add(clonedDiagramLink.getRef());
		}
		
		CommandSetObjectData setGroupBoxChildren = new CommandSetObjectData(clonedGroupBoxDiagramLink.getRef(), DiagramLink.TAG_GROUPED_DIAGRAM_LINK_REFS, newlyClonedChildren.toString());
		project.executeCommand(setGroupBoxChildren);
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
			DiagramFactor child = DiagramFactor.find(project, childRef);
			DiagramFactor clonedDiagramFactor = clonedDiagramFactors.get(child);
			newlyClonedChildren.add(clonedDiagramFactor.getRef());
		}
		
		CommandSetObjectData setGroupBoxChildren = new CommandSetObjectData(clonedGroupBoxDiagramFactor.getRef(), DiagramFactor.TAG_GROUP_BOX_CHILDREN_REFS, newlyClonedChildren.toString());
		project.executeCommand(setGroupBoxChildren);
	}

	private String getFirstStrategyShortLabel(HashSet<DiagramFactor> diagramFactors) throws Exception
	{
		for(DiagramFactor diagramFactor : diagramFactors)
		{
			if (isNonDraftStrategy(diagramFactor))
			{
				Strategy strategy = (Strategy) project.findObject(diagramFactor.getWrappedORef());
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
		
		Strategy strategy = (Strategy) project.findObject(diagramFactor.getWrappedORef());
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

	private ORefList extractClonedObjectRefs(HashMap clonedBaseObjects)
	{
		ORefList clonedBaseObjectRefs = new ORefList();
		Vector baseObjects = new Vector(clonedBaseObjects.values());
		for (int i = 0; i < baseObjects.size(); i ++)
		{
			BaseObject baseObject = ((BaseObject) baseObjects.get(i));
			clonedBaseObjectRefs.add(baseObject.getRef());
		}
		
		return clonedBaseObjectRefs;
	}

	private HashMap cloneDiagramFactors(HashSet<DiagramFactor> diagramFactors) throws Exception
	{
		HashMap originalAndClonedDiagramFactors = new HashMap();
		for(DiagramFactor diagramFactorToBeCloned : diagramFactors)
		{	
			if (diagramFactorToBeCloned.isGroupBoxFactor())
				originalAndClonedDiagramFactors.putAll(cloneGroupBoxDiagramFactor(diagramFactors, diagramFactorToBeCloned));
			else
				originalAndClonedDiagramFactors.putAll(cloneDiagramFactor(diagramFactors, diagramFactorToBeCloned));
		}
		
		return originalAndClonedDiagramFactors;
	}

	private HashMap<DiagramFactor, DiagramFactor> cloneGroupBoxDiagramFactor(HashSet<DiagramFactor> diagramFactors, DiagramFactor groupBox) throws Exception
	{
		HashMap originalAndClonedDiagramFactors = new HashMap();
		ORefList childrenRefs = groupBox.getGroupBoxChildrenRefs();
		for (int childIndex = 0; childIndex < childrenRefs.size(); ++childIndex)
		{
			DiagramFactor child = DiagramFactor.find(project, childrenRefs.get(childIndex));
			originalAndClonedDiagramFactors.putAll(cloneDiagramFactor(diagramFactors, child));
		}
		
		originalAndClonedDiagramFactors.putAll(cloneDiagramFactor(diagramFactors, groupBox));
		
		return originalAndClonedDiagramFactors;
	}

	private HashMap<DiagramFactor, DiagramFactor> cloneDiagramFactor(HashSet<DiagramFactor> diagramFactors, DiagramFactor diagramFactorToBeCloned) throws Exception, CommandFailedException
	{
		
		HashMap originalAndClonedDiagramFactors = new HashMap();
		if (ignoreCloning(diagramFactors, diagramFactorToBeCloned))
			return originalAndClonedDiagramFactors;
		
		ORef factorRef = createOrReuseWrappedObject(diagramFactorToBeCloned);
		
		CreateDiagramFactorParameter extraDiagramFactorInfo = new CreateDiagramFactorParameter(factorRef);
		CommandCreateObject createDiagramFactor = new CommandCreateObject(ObjectType.DIAGRAM_FACTOR, extraDiagramFactorInfo);
		project.executeCommand(createDiagramFactor);
		
		DiagramFactorId newlyCreatedId = (DiagramFactorId) createDiagramFactor.getCreatedId();
		Command[] commandsToClone = diagramFactorToBeCloned.createCommandsToMirror(newlyCreatedId);
		project.executeCommandsWithoutTransaction(commandsToClone);
		
		DiagramFactor clonedDiagramFactor = (DiagramFactor) project.findObject(new ORef(ObjectType.DIAGRAM_FACTOR, newlyCreatedId));
		originalAndClonedDiagramFactors.put(diagramFactorToBeCloned, clonedDiagramFactor);
		
		return originalAndClonedDiagramFactors;
	}
	
	private boolean ignoreCloning(HashSet<DiagramFactor> diagramFactors, DiagramFactor diagramFactor)
	{
		if (Stress.is(diagramFactor.getWrappedType()))
			return true;
		
		if (Task.is(diagramFactor.getWrappedType()))
			return !containsRelatedStrategy(diagramFactors, (Task)diagramFactor.getWrappedFactor());
		
		return false;
	}

	private boolean containsRelatedStrategy(HashSet<DiagramFactor> diagramFactors, Task activity)
	{
		ORefList strategyReferrerRefs = activity.findObjectsThatReferToUs(Strategy.getObjectType());
		for (int index = 0; index < strategyReferrerRefs.size(); ++index)
		{
			ORef strategyRef = strategyReferrerRefs.get(index);
			DiagramFactor strategyDiagramFactor = model.getDiagramFactor(strategyRef);
			if (diagramFactors.contains(strategyDiagramFactor))
				return true;
		}
		
		return false;
	}

	private ORef createOrReuseWrappedObject(DiagramFactor diagramFactor) throws Exception
	{
		if (diagramFactor.getWrappedType() == ObjectType.TARGET)
			return diagramFactor.getWrappedORef();
		
		if (diagramFactor.getWrappedType() == ObjectType.STRATEGY)
			return diagramFactor.getWrappedORef();
		
		if (diagramFactor.getWrappedType() == ObjectType.INTERMEDIATE_RESULT)
			return diagramFactor.getWrappedORef();
		
		if (diagramFactor.getWrappedType() == ObjectType.THREAT_REDUCTION_RESULT)
			return diagramFactor.getWrappedORef();

		if (diagramFactor.getWrappedType() == ObjectType.TASK)
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
		Factor factor = (Factor) project.findObject(diagramFactor.getWrappedORef());
		CommandCreateObject createCommand = createNewFactorCommand(factor);
		project.executeCommand(createCommand);
		
		ORef newlyCreatedRef = createCommand.getObjectRef();
		String clonedLabel = new String("[ " + factor.getLabel() + " ]");
		CommandSetObjectData setLabelCommand = new CommandSetObjectData(newlyCreatedRef, Factor.TAG_LABEL, clonedLabel);
		project.executeCommand(setLabelCommand);
		
		possiblySetThreatReductionResultsDirectThreat(factor, newlyCreatedRef);
		
		return newlyCreatedRef;
	}

	private void possiblySetThreatReductionResultsDirectThreat(Factor factor, ORef newlyCreatedRef) throws CommandFailedException
	{
		if (! factor.isDirectThreat())
			return;
		
		CommandSetObjectData setDirectThreat = new CommandSetObjectData(newlyCreatedRef, ThreatReductionResult.TAG_RELATED_DIRECT_THREAT_REF, factor.getRef().toString());
		project.executeCommand(setDirectThreat);
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
		if (diagramPanel.getCurrentDiagramComponent() == null)
			return new HashSet<DiagramFactor>();
		
		FactorCell[] selectedFactorCells = getSelectedCells();
		if (containsOnlyStrategies(selectedFactorCells))
			return getRelatedDiagramFactors(selectedFactorCells);
			
		return extractDiagramFactors(selectedFactorCells);
	}
	
	private HashSet<DiagramFactor> extractDiagramFactors(FactorCell[] selectedFactorCells)
	{
		HashSet<DiagramFactor> diagramFactors = new HashSet();
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
			DiagramChainObject chainObject = diagramFactor.getDiagramChainBuilder();
			Factor[] factorsArray = chainObject.buildNormalChainAndGetFactors(model, diagramFactor).toFactorArray();
			
			Vector diagramFactors = convertToDiagramFactors(factorsArray);
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
		if (diagramPanel.getCurrentDiagramComponent() == null)
			return new FactorCell[0];
		
		return diagramPanel.getOnlySelectedFactorCells();
	}
	
	private DiagramLink[] getDiagramLinksAndChildrenInSelection()
	{
		ORefSet diagramFactorAndChildrenRefSet = extractDiagramFactorRefs();
		DiagramLink[] allDiagramLinks = model.getAllDiagramLinksAsArray();
		Vector containedDiagramLinks = new Vector();
		for (int i = 0; i < allDiagramLinks.length; ++i)
		{
			ORef fromRef = allDiagramLinks[i].getFromDiagramFactorRef();
			ORef toRef = allDiagramLinks[i].getToDiagramFactorRef();
			if (diagramFactorAndChildrenRefSet.contains(fromRef) && diagramFactorAndChildrenRefSet.contains(toRef))
				containedDiagramLinks.add(allDiagramLinks[i]);
		}
		
		return (DiagramLink[]) containedDiagramLinks.toArray(new DiagramLink[0]);
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
			DiagramChainObject chainObject = diagramFactor.getDiagramChainBuilder();  
			allDiagramLinks.addAll(chainObject.buildNormalChainAndGetDiagramLinks(model, diagramFactor));
		}
		
		return allDiagramLinks.toArray(new DiagramLink[0]);
	}
	
	private HashMap cloneDiagramLinks(DiagramLink[] diagramLinks, HashMap diagramFactors) throws Exception
	{
		HashMap originalAndClonedDiagramLinks = new HashMap();
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
		DiagramFactor fromDiagramFactor = DiagramFactor.find(project, link.getFromDiagramFactorRef());
		DiagramFactor toDiagramFactor = DiagramFactor.find(project, link.getToDiagramFactorRef());
		
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
		project.executeCommand(createDiagramLink);

		DiagramLink newlyCreated = (DiagramLink) project.findObject(createDiagramLink.getObjectRef());
		PointList bendPoints = diagramLink.getBendPoints();
		CommandSetObjectData setBendPoints = CommandSetObjectData.createNewPointList(newlyCreated, DiagramLink.TAG_BEND_POINTS, bendPoints);
		project.executeCommand(setBendPoints);

		return newlyCreated;
	}
	
	
	private CreateObjectParameter createDiagramLinkExtraInfo(DiagramLink diagramLink, DiagramFactor from, DiagramFactor fromCloned, DiagramFactor to, DiagramFactor toCloned) throws Exception
	{
		if (areSharingTheSameFactor(from, fromCloned, to, toCloned))
			return new CreateDiagramFactorLinkParameter(diagramLink.getWrappedId(), fromCloned.getDiagramFactorId(), toCloned.getDiagramFactorId());

		ORef factorLinkRef = new ORef(FactorLink.getObjectType(), new FactorLinkId(FactorLinkId.INVALID.asInt()));
		if(!diagramLink.isGroupBoxLink())
			factorLinkRef = new LinkCreator(project).createFactorLink(fromCloned, toCloned);

		return new CreateDiagramFactorLinkParameter(factorLinkRef, fromCloned.getRef(), toCloned.getRef());
	}

	private boolean areSharingTheSameFactor(DiagramFactor from, DiagramFactor fromCloned, DiagramFactor to, DiagramFactor toCloned)
	{
		return from.getWrappedORef().equals(fromCloned.getWrappedORef()) && to.getWrappedORef().equals(toCloned.getWrappedORef());
	}
	
	private Vector convertToDiagramFactors(Factor[] factors)
	{
		Vector vector = new Vector();
		for (int i = 0; i < factors.length; i++)
		{
			ORef factorRef = factors[i].getRef();
			DiagramFactor diagramFactor = model.getDiagramFactor(factorRef);
			if (canAddTypeToResultsChain(diagramFactor))
				vector.add(diagramFactor);
		}
		
		return vector;
	}

	private DiagramModel model;
	private DiagramPanel diagramPanel;
	private Project project;
}
 