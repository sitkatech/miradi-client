/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import java.awt.Point;

import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.diagram.cells.EAMGraphCell;
import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.diagram.cells.LinkCell;
import org.conservationmeasures.eam.dialogs.base.ModelessDialogWithClose;
import org.conservationmeasures.eam.dialogs.diagram.FactorLinkPropertiesDialog;
import org.conservationmeasures.eam.dialogs.diagram.FactorLinkPropertiesPanel;
import org.conservationmeasures.eam.dialogs.diagram.FactorPropertiesPanel;
import org.conservationmeasures.eam.dialogs.diagram.GroupBoxPropertiesPanel;
import org.conservationmeasures.eam.dialogs.diagram.ProjectScopePanel;
import org.conservationmeasures.eam.dialogs.diagram.TextBoxPropertiesPanel;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.DiagramLink;
import org.conservationmeasures.eam.views.umbrella.StaticPicker;

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
		{
			LinkCell linkCell = (LinkCell)selected[0];
			if(linkCell.getDiagramLink().isGroupBoxLink())
				return false;
			
			return true;
		}

		return false;
	}
	

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;

		try
		{
			EAMGraphCell selected = getDiagramView().getDiagramPanel().getOnlySelectedCells()[0];

			if(selected.isFactor())
				doFactorProperties((FactorCell)selected, getLocation());

			else if(selected.isProjectScope())
				doProjectScopeProperties();

			else if(selected.isFactorLink())
				doFactorLinkProperties(selected.getDiagramLink());
		}
		catch (Exception e)
		{
			throw new CommandFailedException(e);
		}
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
	
	void doFactorLinkProperties(DiagramLink diagramLink) throws Exception
	{
		FactorLinkPropertiesPanel panel = getFactorLinkPropertiesPanel(diagramLink);
		FactorLinkPropertiesDialog dlg = new FactorLinkPropertiesDialog(getMainWindow(), panel, panel.getPanelDescription()); 
		getView().showFloatingPropertiesDialog(dlg);
	}
	
	private FactorLinkPropertiesPanel getFactorLinkPropertiesPanel(DiagramLink diagramLink) throws Exception
	{
		if (!diagramLink.isTargetLink())
			return new FactorLinkPropertiesPanel(getProject(), diagramLink);
		
		ORef targetRef = diagramLink.getUnderlyingLink().getLinkTarget().getRef();
		StaticPicker picker = new StaticPicker(targetRef);
		
		return new FactorLinkPropertiesPanel(getMainWindow(), diagramLink, picker);
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
		else if (diagramFactor.getWrappedType() == ObjectType.GROUP_BOX)
			doGroupBoxProperties(diagramFactor);
		else
			view.showNodeProperties(diagramFactor, tabToStartOn);
	}
	
	private void doTextBoxProperties(DiagramFactor diagramFactor)
	{
		TextBoxPropertiesPanel panel = new TextBoxPropertiesPanel(getProject(), diagramFactor);
		ModelessDialogWithClose propertiesDialog = new ModelessDialogWithClose(getMainWindow(), panel, panel.getPanelDescription()); 
		getView().showFloatingPropertiesDialog(propertiesDialog);
	}

	private void doGroupBoxProperties(DiagramFactor diagramFactor)
	{
		GroupBoxPropertiesPanel panel = new GroupBoxPropertiesPanel(getProject(), diagramFactor);
		ModelessDialogWithClose propertiesDialog = new ModelessDialogWithClose(getMainWindow(), panel, panel.getPanelDescription()); 
		getView().showFloatingPropertiesDialog(propertiesDialog);
	}
	
	// TODO: The tab should probably be computed elsewhere?
	private int getTabToStartOn(FactorCell factorCell, Point screenPoint)
	{
		if(screenPoint == null)
			return FactorPropertiesPanel.TAB_DETAILS;
		
		DiagramComponent diagramComponent = getDiagramView().getDiagramComponent();
		Point pointRelativeToCellOrigin = diagramComponent.convertScreenPointToCellRelativePoint(screenPoint, factorCell);

		EAM.logDebug(screenPoint.toString() + "->" + pointRelativeToCellOrigin.toString());
		if(factorCell.isPointInObjective(pointRelativeToCellOrigin))
		{
			EAM.logDebug("Objective");
			return FactorPropertiesPanel.TAB_OBJECTIVES;
		}
		if (factorCell.isPointInViability(pointRelativeToCellOrigin))
		{
			EAM.logDebug("ViabilityModeTNC");
			return FactorPropertiesPanel.TAB_VIABILITY;
		}
		if(factorCell.isPointInIndicator(pointRelativeToCellOrigin))
		{
			EAM.logDebug("Indicator");
			return FactorPropertiesPanel.TAB_INDICATORS;
		}
		if(factorCell.isPointInGoal(pointRelativeToCellOrigin))
		{
			EAM.logDebug("Goal");
			return FactorPropertiesPanel.TAB_GOALS;
		}
		
		return FactorPropertiesPanel.TAB_DETAILS;
	}
}
