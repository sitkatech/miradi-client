/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import java.awt.Point;

import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.diagram.cells.DiagramFactorLink;
import org.conservationmeasures.eam.diagram.cells.DiagramNode;
import org.conservationmeasures.eam.diagram.cells.EAMGraphCell;
import org.conservationmeasures.eam.diagram.cells.ProjectScopeBox;
import org.conservationmeasures.eam.dialogs.LinkagePropertiesPanel;
import org.conservationmeasures.eam.dialogs.ModelessDialogWithClose;
import org.conservationmeasures.eam.dialogs.NodePropertiesPanel;
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
		if(selected[0].isNode() || selected[0].isProjectScope())
			return true;
		if(selected[0].isLinkage())
		{
			DiagramFactorLink linkage = (DiagramFactorLink)selected[0];
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
		if(selected.isNode())
			doNodeProperties((DiagramNode)selected, getLocation());
		else if(selected.isProjectScope())
			doProjectScopeProperties((ProjectScopeBox)selected);
		else if(selected.isLinkage())
			doLinkageProperties((DiagramFactorLink)selected);
	}
	
	void doProjectScopeProperties(ProjectScopeBox scope) throws CommandFailedException
	{
		ProjectScopePanel projectScopePanel = new ProjectScopePanel(getProject(), getProject().getMetadata());
		ModelessDialogWithClose dlg = new ModelessDialogWithClose(getMainWindow(), projectScopePanel, projectScopePanel.getPanelDescription()); 
		getView().showFloatingPropertiesDialog(dlg);
	}
	
	void doLinkageProperties(DiagramFactorLink linkage) throws CommandFailedException
	{
		LinkagePropertiesPanel panel = new LinkagePropertiesPanel(getProject(), linkage.getWrappedId());
		ModelessDialogWithClose dlg = new ModelessDialogWithClose(getMainWindow(), panel, panel.getPanelDescription()); 
		getView().showFloatingPropertiesDialog(dlg);
	}
	
	void doNodeProperties(DiagramNode selectedNode, Point at) throws CommandFailedException
	{
		DiagramView view = (DiagramView)getView();
		view.showNodeProperties(selectedNode, getTabToStartOn(selectedNode, at));
	}

	private int getTabToStartOn(DiagramNode node, Point at)
	{
		if(at == null)
			return NodePropertiesPanel.TAB_DETAILS;
		
		Point cellOrigin = node.getLocation();
		at.translate(-cellOrigin.x, -cellOrigin.y);
		EAM.logDebug(at.toString());
		if(node.isPointInObjective(at))
		{
			EAM.logDebug("Objective");
			return NodePropertiesPanel.TAB_OBJECTIVES;
		}
		if(node.isPointInIndicator(at))
		{
			EAM.logDebug("Indicator");
			return NodePropertiesPanel.TAB_INDICATORS;
		}
		if(node.isPointInGoal(at))
		{
			EAM.logDebug("Goal");
			return NodePropertiesPanel.TAB_GOALS;
		}
		
		return NodePropertiesPanel.TAB_DETAILS;
	}

	DiagramComponent diagram;
}
