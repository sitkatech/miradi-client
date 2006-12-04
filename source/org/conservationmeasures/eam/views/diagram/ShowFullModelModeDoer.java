/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import java.text.ParseException;
import java.util.Vector;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.cells.DiagramFactor;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.ViewDoer;

public class ShowFullModelModeDoer extends ViewDoer
{
	public boolean isAvailable()
	{
		try
		{
			ViewData viewData = getProject().getViewData(getView().cardName());
			String currentViewMode = viewData.getData(ViewData.TAG_CURRENT_MODE);
			if(ViewData.MODE_DEFAULT.equals(currentViewMode))
				return false;
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return false;
		}

		return true;
	}

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;

		try
		{
			IdList factorsToMakeSelected = getFactorsToMakeSelected();
			
			BaseId viewId = getCurrentViewId();
			getProject().executeCommand(new CommandSetObjectData(ObjectType.VIEW_DATA, viewId, 
					ViewData.TAG_CURRENT_MODE, ViewData.MODE_DEFAULT));
			
			selectFactors(factorsToMakeSelected);
			
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
	}

	private IdList getFactorsToMakeSelected() throws Exception, ParseException
	{
		Project project = getMainWindow().getProject();
		String listOfIds = project.getCurrentViewData().getData(ViewData.TAG_BRAINSTORM_NODE_IDS);
		IdList factorsToMakeVisible = new IdList(listOfIds);
		return factorsToMakeVisible;
	}

	private void selectFactors(IdList factorIds) throws Exception
	{

		DiagramView diagramView = (DiagramView)getView();
		DiagramModel diagramModel = getProject().getDiagramModel();
		Vector allFactors = diagramModel.getAllDiagramFactors();
		
		for(int i = 0; i < factorIds.size(); ++i)
		{
			FactorId nodeId = new FactorId(factorIds.get(i).asInt());
			DiagramFactor diagramFactor = diagramModel.getDiagramFactorByWrappedId(nodeId);
			if (! (allFactors.indexOf(diagramFactor)<0)) 
				diagramView.getDiagramComponent().addSelectionCell(diagramFactor);
		}
	}
	
	
	private BaseId getCurrentViewId() throws Exception
	{
		ViewData viewData = getProject().getCurrentViewData();
		return viewData.getId();
	}
}
