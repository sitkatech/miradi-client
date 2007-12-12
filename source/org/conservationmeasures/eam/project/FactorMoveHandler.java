/* 
 * Copyright 2005-2007, Wildlife Conservation Society, 
 * Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
 * Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
 */ 
package org.conservationmeasures.eam.project;

import java.awt.Point;
import java.util.Vector;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

public class FactorMoveHandler
{
	public FactorMoveHandler(Project projectToUse, DiagramModel modelToUse)
	{
		project = projectToUse;
		model = modelToUse;
	}

	public void factorsWereMovedOrResized(DiagramFactorId[] ids) throws CommandFailedException
	{
		try 
		{
			model.factorsWereMoved(ids);

			Vector commandsToExecute = new Vector();
			for(int i = 0 ; i < ids.length; ++i)
			{
				FactorCell node = model.getFactorCellById(ids[i]);
				if(node.hasMoved())
					commandsToExecute.add(buildMoveCommand(node));

				if(node.sizeHasChanged())
					commandsToExecute.add(buildResizeCommand(node));
			}

			if(commandsToExecute.size() > 0)
			{
				for(int i=0; i < commandsToExecute.size(); ++i)
				{
					getProject().executeCommand((Command)commandsToExecute.get(i));
				}
			}

			//TODO remove cluster related code below
			/*
			 * NOTE: The following chunk of code works around a weird bug deep in jgraph
			 * If you click on a cluster, then click on a member, then drag the member out,
			 * part of jgraph still thinks the cluster has a member that is selected.
			 * So when you drag the node back in, it doesn't become a member because jgraph 
			 * won't return the cluster, because it thinks the cluster has something selected.
			 * The workaround is to re-select what is selected, so the cached values inside 
			 * jgraph get reset to their proper values.
			 */
			MainWindow mainWindow = EAM.getMainWindow();
			if(mainWindow != null)
			{
				DiagramComponent diagram = mainWindow.getDiagramComponent();
				diagram.setSelectionCells(diagram.getSelectionCells());
			}

		} 
		catch (Exception e) 
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}

	}

	private Object buildMoveCommand(FactorCell node)
	{
		Point location = node.getLocation();
		Point snappedLocation = getProject().getSnapped(location);
		String currentLocation = EnhancedJsonObject.convertFromPoint(snappedLocation);
		return new CommandSetObjectData(ObjectType.DIAGRAM_FACTOR, node.getDiagramFactorId(), DiagramFactor.TAG_LOCATION, currentLocation);
	}

	private Command buildResizeCommand(FactorCell node)
	{
		String currentSize = EnhancedJsonObject.convertFromDimension(node.getSize());
		return new CommandSetObjectData(ObjectType.DIAGRAM_FACTOR, node.getDiagramFactorId(), DiagramFactor.TAG_SIZE, currentSize);
	}

	private Project getProject()
	{
		return project;
	}

	private Project project;
	private DiagramModel model;
}
