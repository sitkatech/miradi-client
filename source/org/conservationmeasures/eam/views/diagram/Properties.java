/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import java.awt.Point;

import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.diagram.cells.DiagramFactorLink;
import org.conservationmeasures.eam.diagram.cells.DiagramFactor;
import org.conservationmeasures.eam.diagram.cells.EAMGraphCell;
import org.conservationmeasures.eam.diagram.cells.ProjectScopeBox;
import org.conservationmeasures.eam.dialogs.FactorLinkPropertiesPanel;
import org.conservationmeasures.eam.dialogs.ModelessDialogWithClose;
import org.conservationmeasures.eam.dialogs.FactorPropertiesPanel;
import org.conservationmeasures.eam.dialogs.ProjectScopePanel;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;

public class Properties extends LocationDoer
{
	public Properties(DiagramComponent diagramToUse)
	{
		diagram = diagramToUse;
	}
	
	public boolean isAvailable()
	{
		if(!getProject().isOpen())
			return false;
		
		EAMGraphCell[] selected = getProject().getOnlySelectedCells();
		if(selected.length != 1)
			return false;
		if(selected[0].isFactor() || selected[0].isProjectScope())
			return true;
		if(selected[0].isFactorLink())
		{
			DiagramFactorLink linkage = selected[0].getDiagramFactorLink();
			if(linkage.getToNode().isTarget())
				return true;
		}
		return false;
	}

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;

		EAMGraphCell selected = getProject().getOnlySelectedCells()[0];
		if(selected.isFactor())
			doFactorProperties((DiagramFactor)selected, getLocation());
		else if(selected.isProjectScope())
			doProjectScopeProperties((ProjectScopeBox)selected);
		else if(selected.isFactorLink())
			doFactorLinkProperties(selected.getDiagramFactorLink());
	}
	
	void doProjectScopeProperties(ProjectScopeBox scope) throws CommandFailedException
	{
		ProjectScopePanel projectScopePanel = new ProjectScopePanel(getProject(), getProject().getMetadata());
		ModelessDialogWithClose dlg = new ModelessDialogWithClose(getMainWindow(), projectScopePanel, projectScopePanel.getPanelDescription()); 
		getView().showFloatingPropertiesDialog(dlg);
	}
	
	void doFactorLinkProperties(DiagramFactorLink linkage) throws CommandFailedException
	{
		FactorLinkPropertiesPanel panel = new FactorLinkPropertiesPanel(getProject(), linkage.getWrappedId());
		ModelessDialogWithClose dlg = new ModelessDialogWithClose(getMainWindow(), panel, panel.getPanelDescription()); 
		getView().showFloatingPropertiesDialog(dlg);
	}
	
	void doFactorProperties(DiagramFactor selectedFactor, Point at) throws CommandFailedException
	{
		DiagramView view = (DiagramView)getView();
		view.showNodeProperties(selectedFactor, getTabToStartOn(selectedFactor, at));
	}

	private int getTabToStartOn(DiagramFactor factor, Point at)
	{
		if(at == null)
			return FactorPropertiesPanel.TAB_DETAILS;
		
		Point cellOrigin = factor.getLocation();
		at.translate(-cellOrigin.x, -cellOrigin.y);
		EAM.logDebug(at.toString());
		if(factor.isPointInObjective(at))
		{
			EAM.logDebug("Objective");
			return FactorPropertiesPanel.TAB_OBJECTIVES;
		}
		if(factor.isPointInIndicator(at))
		{
			EAM.logDebug("Indicator");
			return FactorPropertiesPanel.TAB_INDICATORS;
		}
		if(factor.isPointInGoal(at))
		{
			EAM.logDebug("Goal");
			return FactorPropertiesPanel.TAB_GOALS;
		}
		
		return FactorPropertiesPanel.TAB_DETAILS;
	}

	DiagramComponent diagram;
}
