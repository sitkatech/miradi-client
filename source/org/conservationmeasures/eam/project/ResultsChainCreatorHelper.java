/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.project;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.DiagramChainObject;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.dialogs.diagram.DiagramPanel;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.DiagramFactorLinkId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.CreateDiagramFactorLinkParameter;
import org.conservationmeasures.eam.objecthelpers.CreateDiagramFactorParameter;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.DiagramLink;
import org.conservationmeasures.eam.objects.DiagramObject;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.objects.ResultsChainDiagram;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.ThreatReductionResult;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.utils.PointList;
import org.conservationmeasures.eam.views.diagram.LinkCreator;

public class ResultsChainCreatorHelper
{
	public ResultsChainCreatorHelper(Project projectToUse, DiagramPanel diagramPanelToUse)
	{
		project = projectToUse;
		diagramPanel = diagramPanelToUse;
		model = diagramPanel.getDiagramModel();
	}
		
	public BaseId createResultsChain() throws Exception
	{
			DiagramFactor[] diagramFactors = getSelectedAndRelatedDiagramFactors();
			DiagramLink[] diagramLinks = getDiagramLinksInChain();
			CommandCreateObject createResultsChain = new CommandCreateObject(ObjectType.RESULTS_CHAIN_DIAGRAM);
			project.executeCommand(createResultsChain);
			
			BaseId newResultsChainId = createResultsChain.getCreatedId();
			ResultsChainDiagram resultsChain = (ResultsChainDiagram) project.findObject(ObjectType.RESULTS_CHAIN_DIAGRAM, newResultsChainId);
			
			HashMap clonedDiagramFactors = cloneDiagramFactors(diagramFactors);
			DiagramFactorId[] clonedDiagramFactorIds = extractClonedDiagramFactors(clonedDiagramFactors);
			IdList idList = new IdList(DiagramFactor.getObjectType(), clonedDiagramFactorIds);
			CommandSetObjectData addFactorsToChain = CommandSetObjectData.createAppendListCommand(resultsChain, ResultsChainDiagram.TAG_DIAGRAM_FACTOR_IDS, idList);
			project.executeCommand(addFactorsToChain);

			DiagramFactorLinkId[] clonedDiagramLinkIds = cloneDiagramLinks(diagramLinks, clonedDiagramFactors);
			IdList diagramLinkList = new IdList(DiagramLink.getObjectType(), clonedDiagramLinkIds);
			CommandSetObjectData addLinksToChain = CommandSetObjectData.createAppendListCommand(resultsChain, ResultsChainDiagram.TAG_DIAGRAM_FACTOR_LINK_IDS, diagramLinkList);
			project.executeCommand(addLinksToChain);
			
			String label = getFirstStrategyShortLabel(diagramFactors); 
			CommandSetObjectData setLabelCommand = new CommandSetObjectData(ObjectType.RESULTS_CHAIN_DIAGRAM, newResultsChainId, DiagramObject.TAG_LABEL, label);
			project.executeCommand(setLabelCommand);

			ViewData viewData = project.getCurrentViewData();
			CommandSetObjectData setCurrentDiagram = new CommandSetObjectData(viewData.getRef(), ViewData.TAG_CURRENT_RESULTS_CHAIN_REF, resultsChain.getRef());
			project.executeCommand(setCurrentDiagram);
			
			
			return newResultsChainId;
	}
	
	private String getFirstStrategyShortLabel(DiagramFactor[] diagramFactors) throws Exception
	{
		for (int i = 0; i < diagramFactors.length; ++i)
		{
			DiagramFactor diagramFactor = diagramFactors[i];
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

	private DiagramFactorId[] extractClonedDiagramFactors(HashMap clonedDiagramFactors)
	{
		Vector diagramFactorIds = new Vector();
		Vector diagramFactors = new Vector(clonedDiagramFactors.values());
		
		for (int i = 0; i < diagramFactors.size(); i ++)
		{
			DiagramFactor diagramFactor = ((DiagramFactor) diagramFactors.get(i));
			diagramFactorIds.add(diagramFactor.getDiagramFactorId());
		}
		
		return (DiagramFactorId[]) diagramFactorIds.toArray(new DiagramFactorId[0]);
	}

	private HashMap cloneDiagramFactors(DiagramFactor[] diagramFactors) throws Exception
	{
		HashMap originalAndClonedDiagramFactors = new HashMap();
		for (int i  = 0; i < diagramFactors.length; i++)
		{
			DiagramFactor diagramFactorToBeCloned = diagramFactors[i];
			ORef factorRef = createOrReuseWrappedObject(diagramFactorToBeCloned);
			
			CreateDiagramFactorParameter extraDiagramFactorInfo = new CreateDiagramFactorParameter(factorRef);
			CommandCreateObject createDiagramFactor = new CommandCreateObject(ObjectType.DIAGRAM_FACTOR, extraDiagramFactorInfo);
			project.executeCommand(createDiagramFactor);
			
			DiagramFactorId newlyCreatedId = (DiagramFactorId) createDiagramFactor.getCreatedId();
			Command[] commandsToClone = diagramFactorToBeCloned.createCommandsToMirror(newlyCreatedId);
			project.executeCommandsWithoutTransaction(commandsToClone);
			
			DiagramFactor clonedDiagramFactor = (DiagramFactor) project.findObject(new ORef(ObjectType.DIAGRAM_FACTOR, newlyCreatedId));
			originalAndClonedDiagramFactors.put(diagramFactorToBeCloned, clonedDiagramFactor);
		}
		 
		return originalAndClonedDiagramFactors;
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
		
		throw new Exception("cannot create object for type " + factor.getType());
	}

	private DiagramFactor[] getSelectedAndRelatedDiagramFactors()
	{
		if (diagramPanel.getdiagramComponent() == null)
			return new DiagramFactor[0];
		
		FactorCell[] selectedFactorCells = getSelectedCells();
		if (containsOnlyStrategies(selectedFactorCells))
			return getRelatedDiagramFactors(selectedFactorCells);
			
		return extractDiagramFactors(selectedFactorCells);
	}
	
	private DiagramFactor[] extractDiagramFactors(FactorCell[] selectedFactorCells)
	{
		DiagramFactor diagramFactors[] = new DiagramFactor[selectedFactorCells.length];
		for (int i = 0; i < selectedFactorCells.length; ++i)
		{
			diagramFactors[i] = selectedFactorCells[i].getDiagramFactor();
		}
		
		return diagramFactors;
	}

	private DiagramFactor[] getRelatedDiagramFactors(FactorCell[] selectedFactorCells)
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
	
		return allDiagramFactors.toArray(new DiagramFactor[0]);	
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

		return getDiagramLinksInSelection();
	}

	private FactorCell[] getSelectedCells()
	{
		if (diagramPanel.getdiagramComponent() == null)
			return new FactorCell[0];
		
		return diagramPanel.getOnlySelectedFactorCells();
	}
	
	private DiagramLink[] getDiagramLinksInSelection()
	{
		HashSet diagramFactorIdSet = extractDiagramFactorIds();
		DiagramLink[] allDiagramLinks = model.getAllDiagramLinksAsArray();
		Vector containedDiagramLinks = new Vector();
		for (int i = 0; i < allDiagramLinks.length; ++i)
		{
			DiagramFactorId fromId = allDiagramLinks[i].getFromDiagramFactorId();
			DiagramFactorId toId = allDiagramLinks[i].getToDiagramFactorId();
			if (diagramFactorIdSet.contains(fromId) && diagramFactorIdSet.contains(toId))
				containedDiagramLinks.add(allDiagramLinks[i]);
		}
		
		return (DiagramLink[]) containedDiagramLinks.toArray(new DiagramLink[0]);
	}
	
	private HashSet extractDiagramFactorIds()
	{
		HashSet selectedDiagramFactorSet = new HashSet();
		FactorCell[] selectedFactorCells = getSelectedCells();
		for (int i = 0; i < selectedFactorCells.length; ++i)
		{
			selectedDiagramFactorSet.add(selectedFactorCells[i].getDiagramFactorId());
		}
		
		return selectedDiagramFactorSet;
	}

	private DiagramLink[] getLinksInRelatedFactors(FactorCell[] selectedFactorCells) throws Exception
	{
		HashSet<DiagramLink> allDiagramLinks = new HashSet<DiagramLink>();
		for (int i = 0; i < selectedFactorCells.length; i++)
		{
			DiagramFactor diagramFactor = selectedFactorCells[i].getDiagramFactor();
			DiagramChainObject chainObject = diagramFactor.getDiagramChainBuilder();
			Vector diagramLinks = convertToDiagramLinks(chainObject.buildNormalChainAndGetFactorLinks(model, diagramFactor));
			allDiagramLinks.addAll(diagramLinks);
		}
		
		return allDiagramLinks.toArray(new DiagramLink[0]);
	}

	private Vector convertToDiagramLinks(FactorLink[] links) throws Exception
	{
		 Vector vector = new Vector();
		 for (int i  = 0; i < links.length; i++)
		 {
			 FactorLinkId id = links[i].getFactorLinkId();
			 DiagramLink link = model.getDiagramFactorLinkbyWrappedId(id);
			 if (canAddLinkToResultsChain((link)))
				 vector.add(link);
		 }
		 
		 return vector;
	}
	
	private DiagramFactorLinkId[] cloneDiagramLinks(DiagramLink[] diagramLinks, HashMap diagramFactors) throws Exception
	{
		Vector createdDiagramLinkIds = new Vector();
		
		for (int i = 0; i < diagramLinks.length; i++)
		{
			DiagramLink diagramLink = diagramLinks[i];
			DiagramFactorLinkId newlyCreatedLinkId = cloneDiagramFactorLink(diagramFactors, diagramLink);
			if (canAddLinkToResultsChain(diagramLink))
				createdDiagramLinkIds.add(newlyCreatedLinkId);
		}
		
		return (DiagramFactorLinkId[]) createdDiagramLinkIds.toArray(new DiagramFactorLinkId[0]);
	}
	
	private boolean canAddTypeToResultsChain(DiagramFactor diagramFactor)
	{
		if (diagramFactor.getWrappedType() == ObjectType.TARGET)
			return true;
		
		if (diagramFactor.getWrappedType() == ObjectType.CAUSE)
			return true;
		
		if (isNonDraftStrategy(diagramFactor))
			return true;
		
		return false;
	}
	
	private boolean canAddLinkToResultsChain(DiagramLink link)
	{
		DiagramFactor fromDiagramFactor = (DiagramFactor) project.findObject(new ORef(ObjectType.DIAGRAM_FACTOR, link.getFromDiagramFactorId()));
		DiagramFactor toDiatramFactor = (DiagramFactor) project.findObject(new ORef(ObjectType.DIAGRAM_FACTOR, link.getToDiagramFactorId()));
		
		return (canAddTypeToResultsChain(fromDiagramFactor) && canAddTypeToResultsChain(toDiatramFactor));
	}


	private DiagramFactorLinkId cloneDiagramFactorLink(HashMap diagramFactors, DiagramLink diagramLink) throws Exception
	{
		DiagramFactorId fromDiagramFactorId = diagramLink.getFromDiagramFactorId();
		DiagramFactor fromDiagramFactor = (DiagramFactor) project.findObject(new ORef(ObjectType.DIAGRAM_FACTOR, fromDiagramFactorId));
		DiagramFactor fromClonedDiagramFactor = (DiagramFactor) diagramFactors.get(fromDiagramFactor);
		 
		DiagramFactorId toDiagramFactorId = diagramLink.getToDiagramFactorId();
		DiagramFactor toDiagramFactor = (DiagramFactor) project.findObject(new ORef(ObjectType.DIAGRAM_FACTOR, toDiagramFactorId));
		DiagramFactor toClonedDiagramFactor = (DiagramFactor) diagramFactors.get(toDiagramFactor);
		
		CreateObjectParameter extraInfo = createDiagramLinkExtraInfo(diagramLink, fromDiagramFactor, fromClonedDiagramFactor, toDiagramFactor, toClonedDiagramFactor);
		CommandCreateObject createDiagramLink = new CommandCreateObject(ObjectType.DIAGRAM_LINK, extraInfo);
		project.executeCommand(createDiagramLink);

		DiagramFactorLinkId newlyCreatedLinkId = (DiagramFactorLinkId) createDiagramLink.getCreatedId();
		DiagramLink newlyCreated = (DiagramLink) project.findObject(new ORef(ObjectType.DIAGRAM_LINK, newlyCreatedLinkId));
		PointList bendPoints = diagramLink.getBendPoints();
		CommandSetObjectData setBendPoints = CommandSetObjectData.createNewPointList(newlyCreated, DiagramLink.TAG_BEND_POINTS, bendPoints);
		project.executeCommand(setBendPoints);

		return newlyCreatedLinkId;
	}
	
	
	private CreateObjectParameter createDiagramLinkExtraInfo(DiagramLink diagramLink, DiagramFactor from, DiagramFactor fromCloned, DiagramFactor to, DiagramFactor toCloned) throws Exception
	{
		if (areSharingTheSameFactor(from, fromCloned, to, toCloned))
			return new CreateDiagramFactorLinkParameter(diagramLink.getWrappedId(), fromCloned.getDiagramFactorId(), toCloned.getDiagramFactorId());
	
		ORef factorLinkRef = new LinkCreator(project).createFactorLink(fromCloned, toCloned);

		return new CreateDiagramFactorLinkParameter(factorLinkRef, fromCloned.getRef(), toCloned.getRef());
	}

	private boolean areSharingTheSameFactor(DiagramFactor from, DiagramFactor fromCloned, DiagramFactor to, DiagramFactor toCloned)
	{
		return from.getWrappedId().equals(fromCloned.getWrappedId()) && to.getWrappedId().equals(toCloned.getWrappedId());
	}
	
	private Vector convertToDiagramFactors(Factor[] factors)
	{
		Vector vector = new Vector();
		for (int i = 0; i < factors.length; i++)
		{
			FactorId id = factors[i].getFactorId();
			DiagramFactor diagramFactor = model.getDiagramFactor(id);
			if (canAddTypeToResultsChain(diagramFactor))
				vector.add(diagramFactor);
		}
		
		return vector;
	}

	private DiagramModel model;
	private DiagramPanel diagramPanel;
	private Project project;
}
 