/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import java.awt.Point;

import org.conservationmeasures.eam.diagram.cells.EAMGraphCell;
import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.dialogs.FactorLinkPropertiesDialog;
import org.conservationmeasures.eam.dialogs.FactorLinkPropertiesPanel;
import org.conservationmeasures.eam.dialogs.FactorPropertiesPanel;
import org.conservationmeasures.eam.dialogs.ModelessDialogWithClose;
import org.conservationmeasures.eam.dialogs.ProjectScopePanel;
import org.conservationmeasures.eam.dialogs.TextBoxPropertiesPanel;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.DiagramFactorLink;

public class PropertiesDoer extends LocationDoer
{
	public boolean isAvailable()
	{
		if(!getProject().isOpen())
			return false;
		
		if (! isDiagramView())
			return false;
		
		EAMGraphCell[] selected = getDiagramView().getDiagramPanel().getOnlySelectedCells();
		if(selected.length != 1)
			return false;
		
		if(selected[0].isFactor() || selected[0].isProjectScope())
			return true;
		
		if(selected[0].isFactorLink())
			return true;

		return false;
	}
	

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;

		EAMGraphCell selected = getDiagramView().getDiagramPanel().getOnlySelectedCells()[0];
		
		if(selected.isFactor())
			doFactorProperties((FactorCell)selected, getLocation());

		else if(selected.isProjectScope())
			doProjectScopeProperties();
		
		else if(selected.isFactorLink())
			doFactorLinkProperties(selected.getDiagramFactorLink());
	}
	
	private boolean isTextBoxFactor(DiagramFactor selected)
	{
		if (selected.getWrappedType() == ObjectType.TEXT_BOX)
			return true;
		
		return false;
	}

	void doProjectScopeProperties() throws CommandFailedException
	{
		ProjectScopePanel projectScopePanel = new ProjectScopePanel(getProject(), getProject().getMetadata());
		ModelessDialogWithClose dlg = new ModelessDialogWithClose(getMainWindow(), projectScopePanel, projectScopePanel.getPanelDescription()); 
		getView().showFloatingPropertiesDialog(dlg);
	}
	
	void doFactorLinkProperties(DiagramFactorLink linkage) throws CommandFailedException
	{
		FactorLinkPropertiesPanel panel = new FactorLinkPropertiesPanel(getProject(), linkage);
		FactorLinkPropertiesDialog dlg = new FactorLinkPropertiesDialog(getMainWindow(), panel, panel.getPanelDescription()); 
		getView().showFloatingPropertiesDialog(dlg);
	}
	
	void doFactorProperties(FactorCell selectedFactorCell, Point at) throws CommandFailedException
	{
		int tabToStartOn = getTabToStartOn(selectedFactorCell, at);
		DiagramFactor diagramFactor = selectedFactorCell.getDiagramFactor();
		doFactorProperties(diagramFactor, tabToStartOn);
	}

	void doFactorProperties(DiagramFactor diagramFactor, int tabToStartOn)
	{
		DiagramView view = (DiagramView)getView();
		if (isTextBoxFactor(diagramFactor))
			doTextBoxProperties(diagramFactor);
		else
			view.showNodeProperties(diagramFactor, tabToStartOn);
	}
	
	private void doTextBoxProperties(DiagramFactor diagramFactor)
	{
		TextBoxPropertiesPanel panel = new TextBoxPropertiesPanel(getProject(), diagramFactor);
		ModelessDialogWithClose propertiesDialog = new ModelessDialogWithClose(getMainWindow(), panel, panel.getPanelDescription()); 
		getView().showFloatingPropertiesDialog(propertiesDialog);
	}

	// TODO: The tab should probably be computed elsewhere?
	private int getTabToStartOn(FactorCell factorCell, Point at)
	{
		DiagramFactor factor = factorCell.getDiagramFactor();
		if(at == null)
			return FactorPropertiesPanel.TAB_DETAILS;
		
		Point cellOrigin = factor.getLocation();
		at.translate(-cellOrigin.x, -cellOrigin.y);
		EAM.logDebug(at.toString());
		if(factorCell.isPointInObjective(at))
		{
			EAM.logDebug("Objective");
			return FactorPropertiesPanel.TAB_OBJECTIVES;
		}
		if (factorCell.isPointInViability(at))
		{
			EAM.logDebug("ViabilityModeTNC");
			return FactorPropertiesPanel.TAB_VIABILITY;
		}
		if(factorCell.isPointInIndicator(at))
		{
			EAM.logDebug("Indicator");
			return FactorPropertiesPanel.TAB_INDICATORS;
		}
		if(factorCell.isPointInGoal(at))
		{
			EAM.logDebug("Goal");
			return FactorPropertiesPanel.TAB_GOALS;
		}
		
		return FactorPropertiesPanel.TAB_DETAILS;
	}
}
