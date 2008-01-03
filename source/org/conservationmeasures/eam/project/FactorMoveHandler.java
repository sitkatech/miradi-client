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
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.GroupBox;
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
			
			ORefList diagramFactorRefs = new ORefList(DiagramFactor.getObjectType(), new IdList(DiagramFactor.getObjectType(), ids));
			Vector commandsToExecute = new Vector();
			for(int i = 0 ; i < diagramFactorRefs.size(); ++i)
			{
				FactorCell factorCell = model.getFactorCellById((DiagramFactorId) diagramFactorRefs.get(i).getObjectId());
				if(factorCell.hasMoved())
				{
					commandsToExecute.add(buildMoveCommand(factorCell));
					commandsToExecute.addAll(buildGroupBoxRelatedMoveCommands(diagramFactorRefs, factorCell));
				}

				if(factorCell.sizeHasChanged())
					commandsToExecute.add(buildResizeCommand(factorCell));
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

	private Vector<Command> buildGroupBoxRelatedMoveCommands(ORefList diagramFactorRefs, FactorCell factorCell)
	{
		int deltaX = factorCell.getLocation().x - factorCell.getDiagramFactor().getLocation().x;
		int deltaY = factorCell.getLocation().y - factorCell.getDiagramFactor().getLocation().y;
		Vector<Command> commandsToMove = new Vector();
		if (factorCell.getWrappedType() != GroupBox.getObjectType())
			return new Vector();
		
		ORefList groupChildRefs = factorCell.getDiagramFactor().getGroupBoxChildrenRefs();
		for (int i = 0; i < groupChildRefs.size(); ++i)
		{
			ORef groupChildRef = groupChildRefs.get(i);
			if (diagramFactorRefs.contains(groupChildRef))
				continue;
			
			DiagramFactor groupChild = DiagramFactor.find(getProject(), groupChildRef);
			Point currentLocation = (Point) groupChild.getLocation().clone();
			currentLocation.translate(deltaX, deltaY);
			Point snappedLocation = getProject().getSnapped(currentLocation);
			String snappedLocationAsJson = EnhancedJsonObject.convertFromPoint(snappedLocation);
			//FIXME DO not use new DiagramFactorId(ref.asInt);
			commandsToMove.add(new CommandSetObjectData(DiagramFactor.getObjectType(), new DiagramFactorId(groupChildRef.getObjectId().asInt()), DiagramFactor.TAG_LOCATION, snappedLocationAsJson));
		}
				
		return commandsToMove;
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
