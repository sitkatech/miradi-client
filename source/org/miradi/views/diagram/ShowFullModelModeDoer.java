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

import org.jgraph.graph.GraphLayoutCache;
import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.diagram.DiagramComponent;
import org.miradi.diagram.DiagramModel;
import org.miradi.diagram.cells.FactorCell;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.ViewData;
import org.miradi.project.Project;
import org.miradi.views.ViewDoer;

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

		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			showFullModelMode(getProject(), getDiagramView().getDiagramComponent());
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
		finally
		{
			getProject().executeCommand(new CommandEndTransaction());
			getMainWindow().updateActionsAndStatusBar();
		}
	}

	public static void showFullModelMode(Project project, DiagramComponent diagramComponent) throws Exception, ParseException, CommandFailedException
	{
		ORefList factorsToMakeSelected = getFactorsToMakeSelected(project);
		ORef viewDataRef = project.getCurrentViewData().getRef();
					
		CommandSetObjectData changeToDefaultMode = new CommandSetObjectData(viewDataRef, ViewData.TAG_CURRENT_MODE, ViewData.MODE_DEFAULT);
		project.executeCommand(changeToDefaultMode);
		
		CommandSetObjectData clearBrainsStormNodeList = new CommandSetObjectData(viewDataRef, ViewData.TAG_CHAIN_MODE_FACTOR_REFS, "");
		project.executeCommand(clearBrainsStormNodeList);

		selectFactors(diagramComponent, factorsToMakeSelected);
	}

	private static ORefList getFactorsToMakeSelected(Project project) throws Exception, ParseException
	{
		String listOfORefs = project.getCurrentViewData().getData(ViewData.TAG_CHAIN_MODE_FACTOR_REFS);
		ORefList factorsToMakeVisible = new ORefList(listOfORefs);
		return factorsToMakeVisible;
	}

	private static void selectFactors(DiagramComponent diagramComponent, ORefList factorORefs) throws Exception
	{
		GraphLayoutCache glc  = diagramComponent.getGraphLayoutCache();
		DiagramModel diagramModel = diagramComponent.getDiagramModel();
		
		for(int i = 0; i < factorORefs.size(); ++i)
		{
			FactorCell diagramFactor = diagramModel.getFactorCellByWrappedRef(factorORefs.get(i));
			if (glc.isVisible(diagramFactor))
				diagramComponent.addSelectionCell(diagramFactor);
		}
	}
}
