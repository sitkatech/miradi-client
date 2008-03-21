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
package org.miradi.project;

import java.awt.Dimension;
import java.awt.Point;

import org.miradi.commands.Command;
import org.miradi.commands.CommandCreateObject;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.diagram.DiagramModel;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.ids.DiagramFactorId;
import org.miradi.ids.FactorId;
import org.miradi.objecthelpers.CreateDiagramFactorParameter;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.Factor;
import org.miradi.utils.EnhancedJsonObject;

public class FactorCommandHelper
{
	public FactorCommandHelper(Project projectToUse, DiagramModel modelToUse)
	{
		project = projectToUse;
		currentModel = modelToUse;
	}
	
	public CommandCreateObject createFactorAndDiagramFactor(int objectType, Point insertionLocation, Dimension size, String label) throws Exception
	{
		CommandCreateObject createObjectCommand = createFactorAndDiagramFactor(objectType);
		DiagramFactorId diagramFactorId = (DiagramFactorId) createObjectCommand.getCreatedId();
		DiagramFactor diagramFactor = (DiagramFactor) project.findObject(new ORef(ObjectType.DIAGRAM_FACTOR, diagramFactorId));
		setLocationSizeLabel(diagramFactor, insertionLocation, size, label);
		
		return createObjectCommand;
	}

	public CommandCreateObject createFactorAndDiagramFactor(int objectType) throws Exception
	{
		FactorId factorId = createFactor(objectType);
		return createDiagramFactor(currentModel.getDiagramObject(), objectType, factorId);
	}
	
	public CommandCreateObject createDiagramFactor(DiagramObject diagramObject, int objectType, FactorId factorId) throws Exception
	{
		CreateDiagramFactorParameter extraDiagramFactorInfo = new CreateDiagramFactorParameter(new ORef(objectType, factorId));
		CommandCreateObject createDiagramFactor = new CommandCreateObject(ObjectType.DIAGRAM_FACTOR, extraDiagramFactorInfo);
		executeCommand(createDiagramFactor);
		
		DiagramFactorId diagramFactorId = (DiagramFactorId) createDiagramFactor.getCreatedId();
		CommandSetObjectData addDiagramFactor = CommandSetObjectData.createAppendIdCommand(diagramObject, DiagramObject.TAG_DIAGRAM_FACTOR_IDS, diagramFactorId);
		executeCommand(addDiagramFactor);
		
		Factor factor = project.findNode(factorId);
		Command[] commandsToAddToView = getProject().getCurrentViewData().buildCommandsToAddNode(factor.getRef());
		for(int i = 0; i < commandsToAddToView.length; ++i)
			executeCommand(commandsToAddToView[i]);
		
		return createDiagramFactor;
	}

	private FactorId createFactor(int objectType) throws CommandFailedException
	{
		CommandCreateObject createFactorCommand = new CommandCreateObject(objectType);
		executeCommand(createFactorCommand);
		
		return (FactorId) createFactorCommand.getCreatedId();
	}

	private void setLocationSizeLabel(DiagramFactor diagramFactor, Point insertionLocation, Dimension size, String label) throws CommandFailedException, Exception
	{
		setDiagramFactorSize(diagramFactor.getDiagramFactorId(), size);
		setDiagramFactorLocation(diagramFactor.getDiagramFactorId(), insertionLocation);
		setDiagramFactorLabel(diagramFactor.getWrappedId(), label);
	}
	
	private void setDiagramFactorSize(DiagramFactorId diagramFactorId, Dimension originalSize) throws CommandFailedException
	{
		String currentSize = EnhancedJsonObject.convertFromDimension(originalSize);
		CommandSetObjectData setSizeCommand = new CommandSetObjectData(ObjectType.DIAGRAM_FACTOR, diagramFactorId, DiagramFactor.TAG_SIZE, currentSize);
		executeCommand(setSizeCommand);
	}

	private void setDiagramFactorLabel(FactorId factorId, String label) throws CommandFailedException
	{
		CommandSetObjectData setLabel = new CommandSetObjectData(ObjectType.FACTOR, factorId, Factor.TAG_LABEL, label); 
		executeCommand(setLabel);
	}
	
	private void setDiagramFactorLocation(DiagramFactorId diagramFactorId, Point newNodeLocation) throws Exception
	{
		String newMoveLocation = EnhancedJsonObject.convertFromPoint(new Point(newNodeLocation.x, newNodeLocation.y));
		CommandSetObjectData moveCommand = new CommandSetObjectData(ObjectType.DIAGRAM_FACTOR, diagramFactorId, DiagramFactor.TAG_LOCATION, newMoveLocation);
		executeCommand(moveCommand);
	}

	public static CommandSetObjectData createSetLabelCommand(ORef ref, String newLabel)
	{
		return new CommandSetObjectData(ref, Factor.TAG_LABEL, newLabel);
	}

	private Project getProject()
	{
		return project;
	}
	
	private void executeCommand(Command cmd) throws CommandFailedException
	{
		getProject().executeCommand(cmd);
	}
			
	Project project;
	DiagramModel currentModel;
}
