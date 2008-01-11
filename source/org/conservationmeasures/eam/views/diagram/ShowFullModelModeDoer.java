/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import java.text.ParseException;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.dialogs.diagram.DiagramPanel;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.ViewDoer;
import org.jgraph.graph.GraphLayoutCache;

public class ShowFullModelModeDoer extends ViewDoer
{
	public boolean isAvailable()
	{
		try
		{
			if(!isInDiagram())
				return false;
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

		getMainWindow().preventActionUpdates();
		try
		{
			ORefList factorsToMakeSelected = getFactorsToMakeSelected();
			ORef viewDataRef = getProject().getCurrentViewData().getRef();
			CommandSetObjectData clearBrainsStormNodeList = new CommandSetObjectData(viewDataRef, ViewData.TAG_CHAIN_MODE_FACTOR_REFS, "");
			getProject().executeCommand(clearBrainsStormNodeList);
						
			CommandSetObjectData changeToDefaultMode = new CommandSetObjectData(viewDataRef, ViewData.TAG_CURRENT_MODE, ViewData.MODE_DEFAULT);
			getProject().executeCommand(changeToDefaultMode);
			
			selectFactors(factorsToMakeSelected);
			
			final int CONCEPTUAL_MODEL_INDEX = 0;
			 //TODO this should be handled more cleanly
			((DiagramPanel) getDiagramView().getTabContents(CONCEPTUAL_MODEL_INDEX)).getdiagramComponent().setToDefaultBackgroundColor();
			
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
		finally
		{
			getMainWindow().allowActionUpdates();
		}
	}

	private ORefList getFactorsToMakeSelected() throws Exception, ParseException
	{
		Project project = getMainWindow().getProject();
		String listOfORefs = project.getCurrentViewData().getData(ViewData.TAG_CHAIN_MODE_FACTOR_REFS);
		ORefList factorsToMakeVisible = new ORefList(listOfORefs);
		return factorsToMakeVisible;
	}

	private void selectFactors(ORefList factorORefs) throws Exception
	{
		DiagramComponent diagramComponent  = ((DiagramView)getView()).getDiagramComponent();
		GraphLayoutCache glc  = diagramComponent.getGraphLayoutCache();
		DiagramModel diagramModel = getDiagramView().getDiagramModel();
		
		for(int i = 0; i < factorORefs.size(); ++i)
		{
			FactorId nodeId = new FactorId(factorORefs.get(i).getObjectId().asInt());
			FactorCell diagramFactor = diagramModel.getFactorCellByWrappedId(nodeId);
			if (glc.isVisible(diagramFactor))
				diagramComponent.addSelectionCell(diagramFactor);
		}
	}
}
