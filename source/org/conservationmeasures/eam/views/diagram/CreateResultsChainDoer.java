/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import java.util.Vector;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.cells.FactorCell;
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
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.DiagramFactorLink;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.objects.ResultsChainDiagram;
import org.conservationmeasures.eam.project.ProjectChainObject;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;
import org.conservationmeasures.eam.utils.PointList;
import org.conservationmeasures.eam.views.ViewDoer;

public class CreateResultsChainDoer extends ViewDoer
{
	public boolean isAvailable()
	{
		if (! isDiagramView())
			return false;
		
		Factor[] selectedFactors = getDiagramView().getDiagramPanel().getOnlySelectedFactors();
		if (selectedFactors.length == 0)
			return false;
		
		if (! areAllStrategies(selectedFactors))
			return false;
		
		return true;
	}

	public void doIt() throws CommandFailedException
	{
		if (! isAvailable())
			return;

		try
		{
			DiagramModel model = getDiagramView().getDiagramModel();
			createResultsChain(model);
		}
		catch (Exception e) 
		{
			EAM.logException(e);
		}
	}
	
	private void createResultsChain(DiagramModel model) throws Exception
	{
		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			CommandCreateObject createResultsChain = new CommandCreateObject(ObjectType.RESULTS_CHAIN_DIAGRAM);
			getProject().executeCommand(createResultsChain);
			
			BaseId createId = createResultsChain.getCreatedId();
			ResultsChainDiagram resultsChain = (ResultsChainDiagram) getProject().findObject(ObjectType.RESULTS_CHAIN_DIAGRAM, createId);

			DiagramFactor[] diagramFactors = getDiagramFactorsInChain(model);
			DiagramFactorId[] clonedDiagramFactorIds = cloneDiagramFactors(diagramFactors);
			IdList idList = new IdList(clonedDiagramFactorIds);
			CommandSetObjectData addFactorsToChain = CommandSetObjectData.createAppendListCommand(resultsChain, ResultsChainDiagram.TAG_DIAGRAM_FACTOR_IDS, idList);
			getProject().executeCommand(addFactorsToChain);

			DiagramFactorLink[] diagramLinks = getDiagramLinksInChain(model);
			DiagramFactorLinkId[] clonedDiagramLinkIds = cloneDiagramLinks(model, diagramLinks, clonedDiagramFactorIds);
			IdList diagramLinkList = new IdList(clonedDiagramLinkIds);
			CommandSetObjectData addLinksToChain = CommandSetObjectData.createAppendListCommand(resultsChain, ResultsChainDiagram.TAG_DIAGRAM_FACTOR_LINK_IDS, diagramLinkList);
			getProject().executeCommand(addLinksToChain);

		}
		finally
		{
			getProject().executeCommand(new CommandEndTransaction());
		}
	}
	
	private DiagramFactorLinkId[] cloneDiagramLinks(DiagramModel model, DiagramFactorLink[] diagramLinks, DiagramFactorId[] clonedDiagramFactorIds) throws Exception
	{
		Vector createdDiagramLinkIds = new Vector();
		
		for (int i = 0; i < diagramLinks.length; i++)
		{
			DiagramFactorLink diagramLink = diagramLinks[i];
			//TODO RC needs refactoring
			DiagramFactorId fromDiagramFactorId = diagramLink.getFromDiagramFactorId();
			DiagramFactor fromDiagramFactor = (DiagramFactor) getProject().findObject(new ORef(ObjectType.DIAGRAM_FACTOR, fromDiagramFactorId));
			FactorId fromFactorId = fromDiagramFactor.getWrappedId();
			DiagramFactorId clonedFromId = findDiagramFactor(fromFactorId, clonedDiagramFactorIds);
			
			DiagramFactorId toDiagramFactorId = diagramLink.getToDiagramFactorId();
			DiagramFactor toDiagramFactor = (DiagramFactor) getProject().findObject(new ORef(ObjectType.DIAGRAM_FACTOR, toDiagramFactorId));
			FactorId toFactorId = toDiagramFactor.getWrappedId();
			DiagramFactorId clonedToId = findDiagramFactor(toFactorId, clonedDiagramFactorIds);
			
			CreateObjectParameter extraInfo = new CreateDiagramFactorLinkParameter(diagramLink.getWrappedId(), clonedFromId, clonedToId);
			CommandCreateObject createDiagramLink = new CommandCreateObject(ObjectType.DIAGRAM_LINK, extraInfo);
			getProject().executeCommand(createDiagramLink);

			DiagramFactorLinkId newlyCreatedLinkId = (DiagramFactorLinkId) createDiagramLink.getCreatedId();
			DiagramFactorLink newlyCreated = (DiagramFactorLink) getProject().findObject(new ORef(ObjectType.DIAGRAM_LINK, newlyCreatedLinkId));
			PointList bendPoints = diagramLink.getBendPoints();
			CommandSetObjectData setBendPoints = CommandSetObjectData.createNewPointList(newlyCreated, DiagramFactorLink.TAG_BEND_POINTS, bendPoints);
			getProject().executeCommand(setBendPoints);

			createdDiagramLinkIds.add(newlyCreatedLinkId);
		}
		
		return (DiagramFactorLinkId[]) createdDiagramLinkIds.toArray(new DiagramFactorLinkId[0]);
	}

	private DiagramFactorId findDiagramFactor(FactorId factorId, DiagramFactorId[] clonedDiagramFactorIds) throws Exception
	{
		for (int i = 0; i < clonedDiagramFactorIds.length; i++)
		{
			DiagramFactorId diagramFactorId = clonedDiagramFactorIds[i];
			DiagramFactor diagramFactor = (DiagramFactor) getProject().findObject(new ORef(ObjectType.DIAGRAM_FACTOR, diagramFactorId));
			if (factorId.equals(diagramFactor.getWrappedId()))
				return diagramFactor.getDiagramFactorId();
		}
		
		throw new Exception("Cloned DiagramFactor not found");
	}

	private DiagramFactorId[] cloneDiagramFactors(DiagramFactor[] diagramFactors) throws CommandFailedException
	{
		Vector createdDiagramFactorIds = new Vector();
		for (int i  = 0; i < diagramFactors.length; i++)
		{
			DiagramFactor diagramFactor = diagramFactors[i];
			FactorId factorId = diagramFactor.getWrappedId();
			CreateDiagramFactorParameter extraDiagramFactorInfo = new CreateDiagramFactorParameter(factorId);
			CommandCreateObject createDiagramFactor = new CommandCreateObject(ObjectType.DIAGRAM_FACTOR, extraDiagramFactorInfo);
			getProject().executeCommand(createDiagramFactor);
			
			DiagramFactorId newlyCreatedId = (DiagramFactorId) createDiagramFactor.getCreatedId();
			String size = EnhancedJsonObject.convertFromDimension(diagramFactor.getSize());
			CommandSetObjectData setSizeCommand = new CommandSetObjectData(ObjectType.DIAGRAM_FACTOR, newlyCreatedId, DiagramFactor.TAG_SIZE, size);
			getProject().executeCommand(setSizeCommand);
			
			String location = EnhancedJsonObject.convertFromPoint(diagramFactor.getLocation());
			CommandSetObjectData setLocationCommand = new CommandSetObjectData(ObjectType.DIAGRAM_FACTOR, newlyCreatedId, DiagramFactor.TAG_LOCATION, location);
			getProject().executeCommand(setLocationCommand);
			
			createdDiagramFactorIds.add(new DiagramFactorId(createDiagramFactor.getCreatedId().asInt()));
		}
		 
		return (DiagramFactorId[]) createdDiagramFactorIds.toArray(new DiagramFactorId[0]);
	}
	
	private DiagramFactor[] getDiagramFactorsInChain(DiagramModel model)
	{
		FactorCell[] selectedFactorCells = getDiagramView().getDiagramPanel().getOnlySelectedFactorCells();
		Vector allDiagramFactors = new Vector();
		for (int i = 0; i < selectedFactorCells.length; i++)
		{
			ProjectChainObject chainObject = new ProjectChainObject();
			Factor factor = selectedFactorCells[i].getUnderlyingObject();
			chainObject.buildNormalChain(factor);
			Factor[] factorsArray = chainObject.getFactorsArray();
			
			Vector diagramFactors = convertToDiagramFactors(model, factorsArray);
			allDiagramFactors.addAll(diagramFactors);
		}
		
		return (DiagramFactor[]) allDiagramFactors.toArray(new DiagramFactor[0]);
	}
	
	private DiagramFactorLink[] getDiagramLinksInChain(DiagramModel model) throws Exception
	{
		FactorCell[] selectedFactorCells = getDiagramView().getDiagramPanel().getOnlySelectedFactorCells();
		Vector allDiagramLinks = new Vector();
		for (int i = 0; i < selectedFactorCells.length; i++)
		{
			ProjectChainObject chainObject = new ProjectChainObject();
			Factor factor = selectedFactorCells[i].getUnderlyingObject();
			chainObject.buildNormalChain(factor);
			Vector diagramLinks = convertToDiagramLinks(model, chainObject.getFactorLinksArray());
			allDiagramLinks.addAll(diagramLinks);
		}
		
		return (DiagramFactorLink[]) allDiagramLinks.toArray(new DiagramFactorLink[0]);
	}

	private Vector convertToDiagramLinks(DiagramModel model, FactorLink[] links) throws Exception
	{
		 Vector vector = new Vector();
		 for (int i  = 0; i < links.length; i++)
		 {
			 FactorLinkId id = links[i].getFactorLinkId();
			 DiagramFactorLink link = model.getDiagramFactorLinkbyWrappedId(id);
			 vector.add(link);
		 }
		 
		 return vector;
	}
	
	private Vector convertToDiagramFactors(DiagramModel model, Factor[] factors)
	{
		Vector vector = new Vector();
		for (int i = 0; i < factors.length; i++)
		{
			FactorId id = factors[i].getFactorId();
			DiagramFactor diagramFactor = model.getDiagramFactor(id);
			vector.add(diagramFactor);
		}
		
		return vector;
	}
	
	private boolean areAllStrategies(BaseObject[] selectedObjects)
	{
		for (int i = 0; i < selectedObjects.length; i++)
		{
			ORef ref = selectedObjects[i].getRef();
			if (ref.getObjectType() != ObjectType.STRATEGY)
				return false;
		}
		
		return true;
	}
}
