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
import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.CreateDiagramFactorParameter;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.ResultsChainDiagram;
import org.conservationmeasures.eam.views.ViewDoer;

public class CreateResultsChainDoer extends ViewDoer
{
	public boolean isAvailable()
	{
		Factor[] selectedFactors = getProject().getOnlySelectedFactors();
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
			createResultsChain();
		}
		catch (Exception e) 
		{
			EAM.logException(e);
		}
	}
	
	private void createResultsChain() throws Exception
	{
		DiagramFactor[] diagramFactors = createChainBasedOnFactorSelection();
		
		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			CommandCreateObject createResultsChain = new CommandCreateObject(ObjectType.RESULTS_CHAIN_DIAGRAM);
			getProject().executeCommand(createResultsChain);
			
			BaseId createId = createResultsChain.getCreatedId();
			ResultsChainDiagram resultsChain = (ResultsChainDiagram) getProject().findObject(ObjectType.RESULTS_CHAIN_DIAGRAM, createId);
				
			DiagramFactorId[] clonedDiagramFactorIds = cloneDiagramFactors(diagramFactors);
			IdList idList = new IdList(clonedDiagramFactorIds);
			CommandSetObjectData addFactorsToChain = CommandSetObjectData.createAppendListCommand(resultsChain, ResultsChainDiagram.TAG_DIAGRAM_FACTOR_IDS, idList);
			getProject().executeCommand(addFactorsToChain);
		}
		finally
		{
			getProject().executeCommand(new CommandEndTransaction());
		}
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
			//FIXME size and location have to be set for new diagramFactor
//			DiagramFactorId newlyCreatedId = (DiagramFactorId) createDiagramFactor.getCreatedId();
//			String size = EnhancedJsonObject.convertFromDimension(diagramFactor.getSize());
//			CommandSetObjectData setSizeCommand = new CommandSetObjectData(ObjectType.DIAGRAM_FACTOR, newlyCreatedId, DiagramFactor.TAG_SIZE, size);
//			getProject().executeCommand(setSizeCommand);
//			
//			String location = EnhancedJsonObject.convertFromPoint(diagramFactor.getLocation());
//			CommandSetObjectData setLocationCommand = new CommandSetObjectData(ObjectType.DIAGRAM_FACTOR, newlyCreatedId, DiagramFactor.TAG_SIZE, location);
//			getProject().executeCommand(setLocationCommand);
//			
			createdDiagramFactorIds.add(new DiagramFactorId(createDiagramFactor.getCreatedId().asInt()));
		}
		
		DiagramFactorId[] ids = (DiagramFactorId[]) createdDiagramFactorIds.toArray(new DiagramFactorId[0]);
		return ids;
	}
	
	//TODO use chain instead of just selected
	private DiagramFactor[] createChainBasedOnFactorSelection() throws Exception
	{
		FactorCell[] selectedFactorCells = getDiagramView().getDiagramPanel().getOnlySelectedFactorCells();
		Vector selectedFactorIds = new Vector();
		for (int i = 0; i < selectedFactorCells.length; i++)
			selectedFactorIds.add(selectedFactorCells[i].getDiagramFactor());
	
		return (DiagramFactor[]) selectedFactorIds.toArray(new DiagramFactor[0]);
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
