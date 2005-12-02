/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import java.awt.Point;
import java.awt.geom.Point2D;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetFactorType;
import org.conservationmeasures.eam.commands.CommandSetIndicator;
import org.conservationmeasures.eam.commands.CommandSetNodeObjectives;
import org.conservationmeasures.eam.commands.CommandSetNodePriority;
import org.conservationmeasures.eam.commands.CommandSetNodeText;
import org.conservationmeasures.eam.commands.CommandSetProjectVision;
import org.conservationmeasures.eam.commands.CommandSetTargetGoal;
import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.diagram.ProjectScopeBox;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.diagram.nodes.EAMGraphCell;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.NodePropertiesDialog;
import org.conservationmeasures.eam.main.ProjectScopePropertiesDialog;
import org.conservationmeasures.eam.views.ProjectDoer;
import org.martus.swing.Utilities;

public class Properties extends ProjectDoer
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
		return false;
	}

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
		
		EAMGraphCell selected = getProject().getOnlySelectedCells()[0];
		if(selected.isNode())
			doNodeProperties((DiagramNode)selected);
		else if(selected.isProjectScope())
			doProjectScopeProperties((ProjectScopeBox)selected);
	}
	
	void doProjectScopeProperties(ProjectScopeBox scope) throws CommandFailedException
	{
		ProjectScopePropertiesDialog dlg = new ProjectScopePropertiesDialog(EAM.mainWindow, getProject(), scope);
		dlg.setText(scope.getVision());
		
		dlg.setVisible(true);
		if(!dlg.getResult())
			return;

		getProject().executeCommand(new CommandSetProjectVision(dlg.getText()));
	}
	
	void doNodeProperties(DiagramNode selectedNode) throws CommandFailedException
	{
		String title = EAM.text("Title|Node Properties");
		Point scaledLocation = selectedNode.getLocation();
		Point2D screenLocation2D = diagram.toScreen(scaledLocation);
		Point scaledPoint = Utilities.createPointFromPoint2D(screenLocation2D);
		NodePropertiesDialog dlg = new NodePropertiesDialog(EAM.mainWindow, title, scaledPoint, selectedNode);
		dlg.setVisible(true);
		if(!dlg.getResult())
			return;

		int id = selectedNode.getId();
		getProject().executeCommand(new CommandBeginTransaction());
		getProject().executeCommand(new CommandSetNodeText(id, dlg.getText()));
		getProject().executeCommand(new CommandSetIndicator(id, dlg.getIndicator()));
		if(selectedNode.canHavePriority())
			getProject().executeCommand(new CommandSetNodePriority(id, dlg.getPriority()));
		if(selectedNode.canHaveObjectives())
			getProject().executeCommand(new CommandSetNodeObjectives(id, dlg.getObjectives()));
		if(selectedNode.canHaveGoal())
			getProject().executeCommand(new CommandSetTargetGoal(id, dlg.getGoals()));
		if(selectedNode.isFactor())
			getProject().executeCommand(new CommandSetFactorType(id, dlg.getType()));

		getProject().executeCommand(new CommandEndTransaction());
	}

	DiagramComponent diagram;
}
