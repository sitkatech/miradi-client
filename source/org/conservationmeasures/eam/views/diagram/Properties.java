/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import javax.swing.JDialog;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.diagram.EAMGraphCell;
import org.conservationmeasures.eam.diagram.ProjectScopeBox;
import org.conservationmeasures.eam.diagram.nodes.DiagramLinkage;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.dialogs.LinkagePropertiesDialog;
import org.conservationmeasures.eam.dialogs.ModelessDialogWithClose;
import org.conservationmeasures.eam.dialogs.NodePropertiesPanel;
import org.conservationmeasures.eam.dialogs.ProjectScopePanel;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.ConceptualModelLinkage;
import org.martus.swing.Utilities;

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
			DiagramLinkage linkage = (DiagramLinkage)selected[0];
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
			doLinkageProperties((DiagramLinkage)selected);
	}
	
	void doProjectScopeProperties(ProjectScopeBox scope) throws CommandFailedException
	{
		ProjectScopePanel projectScopePanel = new ProjectScopePanel(getProject(), getProject().getMetadata());
		ModelessDialogWithClose dlg = new ModelessDialogWithClose(getMainWindow(), projectScopePanel, projectScopePanel.getPanelDescriptionText()); 
		setDialogLocation(dlg, scope.getBounds());
		dlg.pack();
		dlg.setVisible(true);
	}
	
	void doLinkageProperties(DiagramLinkage linkage) throws CommandFailedException
	{
		LinkagePropertiesDialog dlg = new LinkagePropertiesDialog(getMainWindow(), getProject(), linkage);
		dlg.setText(linkage.getStressLabel());
		Utilities.centerDlg(dlg);
		dlg.setVisible(true);
		if(!dlg.getResult())
			return;

		Command cmd = new CommandSetObjectData(ObjectType.MODEL_LINKAGE, linkage.getDiagramLinkageId(), 
				ConceptualModelLinkage.TAG_STRESS_LABEL, dlg.getText());
		getProject().executeCommand(cmd);
	}
	
	void doNodeProperties(DiagramNode selectedNode, Point at) throws CommandFailedException
	{
		diagram.showNodeProperties(selectedNode, getTabToStartOn(selectedNode, at));
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

	private void setDialogLocation(JDialog dlg, Rectangle2D rect2D)
	{
		Rectangle rect = rect2D.getBounds();
		int scaledX = rect.x;
		int scaledY = rect.y + rect.height;
		Point scaledLowerLeftCorner = new Point(scaledX, scaledY);
		Point actualLowerLeftcorner = diagram.toWindowCoordinates(scaledLowerLeftCorner);
		dlg.setLocation(actualLowerLeftcorner);
		Utilities.fitInScreen(dlg);
	}

	DiagramComponent diagram;
}
