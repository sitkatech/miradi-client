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
package org.miradi.views.diagram.doers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Vector;

import org.miradi.diagram.DiagramChainObject;
import org.miradi.diagram.DiagramComponent;
import org.miradi.diagram.DiagramModel;
import org.miradi.diagram.cells.FactorCell;
import org.miradi.diagram.cells.LinkCell;
import org.miradi.dialogs.diagram.DiagramPanel;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.Factor;
import org.miradi.project.Project;
import org.miradi.views.ViewDoer;
import org.miradi.views.diagram.DiagramView;

public class SelectChainDoer extends ViewDoer
{
	public boolean isAvailable()
	{
		if (! isInDiagram())
			return false;
		
		FactorCell[] selectedFactors = getDiagramView().getDiagramPanel().getOnlySelectedFactorCells();
		DiagramLink[] selectedLinks = getDiagramView().getDiagramPanel().getOnlySelectedLinks();
		int combinedLengths = selectedLinks.length + selectedFactors.length;
		
		if (combinedLengths == 0)
			return false;
	    
		return true;
	}
	
	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
		try
		{
			DiagramView view = (DiagramView)getView();
			DiagramPanel diagram = view.getDiagramPanel();
			selectChainsRelatedToSelectedFactorsAndLinks(diagram);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog("Unknown error");
		}
	}

	public static void selectChainsRelatedToSelectedFactorsAndLinks(DiagramPanel diagramPanel) throws Exception
	{
		HashSet<FactorCell> onlySelectedFactorAndGroupChildCells = diagramPanel.getOnlySelectedFactorAndGroupChildCells();
		FactorCell[] selectedFactors = onlySelectedFactorAndGroupChildCells.toArray(new FactorCell[0]);
		DiagramLink[] selectedLinks = diagramPanel.getOnlySelectedLinks();
		ORefList realDiagramLinkRefs = new ORefList();
		for(int i = 0; i < selectedLinks.length; ++i)
		{
			realDiagramLinkRefs.addAll(selectedLinks[i].getSelfOrChildren());
		}
		
		selectChainsBasedOnFactorsAndLinks(diagramPanel.getCurrentDiagramComponent(), diagramPanel.getDiagramModel(), selectedFactors, realDiagramLinkRefs);
	}
	
	private static void selectChainsBasedOnFactorsAndLinks(DiagramComponent diagramComponent, DiagramModel model, FactorCell[] factorCells, ORefList diagramLinkRefs) throws Exception
	{
		Factor[] factorReleatedFactors = getChainsBasedOnFactors(model, factorCells);
		Factor[] linkRelatedFactors = getChainsBasedOnLinks(model, diagramLinkRefs);
		Vector nodes = new Vector();
		nodes.addAll(Arrays.asList(factorReleatedFactors));
		nodes.addAll(Arrays.asList(linkRelatedFactors));
		selectFactors(diagramComponent, model, (Factor[])nodes.toArray(new Factor[0]));
	}

	private static Factor[] getChainsBasedOnFactors(DiagramModel diagramModel, FactorCell[] factors) throws Exception
	{
		Vector nodes = new Vector();
		for(int i = 0; i < factors.length; ++i)
		{
			FactorCell selectedFactor = factors[i];
			DiagramChainObject chainObject = new DiagramChainObject();
			Factor[] chainNodes = chainObject.buildNormalChainAndGetFactors(diagramModel, selectedFactor.getDiagramFactor()).toFactorArray();
			nodes.addAll(Arrays.asList(chainNodes));
		}
		return (Factor[])nodes.toArray(new Factor[0]);
	}
	
	private static Factor[] getChainsBasedOnLinks(DiagramModel diagramModel, ORefList diagramLinkRefs) throws Exception
	{
		Project project = diagramModel.getProject();
		Vector nodes = new Vector();
		for(int i = 0; i < diagramLinkRefs.size(); ++i)
		{
			DiagramLink selectedLinkage = DiagramLink.find(project, diagramLinkRefs.get(i));
			LinkCell cell = diagramModel.findLinkCell(selectedLinkage);
			
			DiagramChainObject upstreamChain = new DiagramChainObject();
			DiagramFactor from = cell.getFrom().getDiagramFactor();
			Factor[] upstreamFactors = upstreamChain.buildUpstreamChainAndGetFactors(diagramModel, from).toFactorArray();
			nodes.addAll(Arrays.asList(upstreamFactors));
			
			DiagramChainObject downstreamChain = new DiagramChainObject();
			DiagramFactor to = cell.getTo().getDiagramFactor();
			Factor[] downstreamFactors = downstreamChain.buildDownstreamChainAndGetFactors(diagramModel, to).toFactorArray();
			nodes.addAll(Arrays.asList(downstreamFactors));
		}
		return (Factor[])nodes.toArray(new Factor[0]);
	}

	private static void selectFactors(DiagramComponent diagramComponent, DiagramModel model, Factor[] chainNodes) throws Exception
	{
		for(int i = 0; i < chainNodes.length; ++i)
		{
			// convert CMNode to DiagramNode
			FactorCell nodeToSelect = model.getFactorCellByWrappedRef(chainNodes[i].getRef());
			diagramComponent.addSelectionCell(nodeToSelect);
		}
	}
}
