/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import java.awt.Point;
import java.awt.Rectangle;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.project.FactorCommandHelper;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

abstract public class InsertFactorDoer extends LocationDoer
{
	abstract public int getTypeToInsert();
	abstract public String getInitialText();
	abstract public void forceVisibleInLayerManager();

	public boolean isAvailable()
	{
		return getProject().isOpen();
	}

	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;
		
		try
		{
			FactorCell[] selectedFactors = getDiagramView().getDiagramPanel().getOnlySelectedFactorCells();
			DiagramFactor diagramFactor = insertFactorItself();
			FactorId id = diagramFactor.getWrappedId();
			if((selectedFactors.length > 0) && (getTypeToInsert()!= ObjectType.TARGET))
				linkToPreviouslySelectedFactors(diagramFactor, selectedFactors);
			else
				notLinkingToAnyFactors();
			
			selectNewFactor(id);
			launchPropertiesEditor(diagramFactor);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
	}
	
    protected void selectNewFactor(FactorId idToUse)
	{
		getDiagramView().getDiagramPanel().selectFactor(idToUse);
	}
	
	void launchPropertiesEditor(DiagramFactor diagramFactor) throws Exception, CommandFailedException
	{
		getDiagramView().getPropertiesDoer().doFactorProperties(diagramFactor, 0);
	}

	private DiagramFactor insertFactorItself() throws Exception
	{
		Point createAt = getLocation();
		Project project = getProject();
		FactorCell[] selectedNodes = getDiagramView().getDiagramPanel().getOnlySelectedFactorCells();

		project.executeCommand(new CommandBeginTransaction());
		int factorType = getTypeToInsert();
		FactorCommandHelper factorCommandHelper = new FactorCommandHelper(project, getDiagramView().getDiagramModel());
		DiagramFactorId id = (DiagramFactorId) factorCommandHelper.createFactorAndDiagramFactor(factorType).getCreatedId();
		
		DiagramFactor addedFactor = (DiagramFactor)getProject().findObject(DiagramFactor.getObjectType(), id);

		CommandSetObjectData setNameCommand = FactorCommandHelper.createSetLabelCommand(addedFactor.getWrappedId(), getInitialText());
		project.executeCommand(setNameCommand);

		Point deltaPoint = getDeltaPoint(createAt, selectedNodes, factorType, addedFactor);
		Point snappedPoint  = project.getSnapped(deltaPoint);
		DiagramFactorId diagramFactorId = addedFactor.getDiagramFactorId();
		String newLocation = EnhancedJsonObject.convertFromPoint(snappedPoint);
		CommandSetObjectData moveCommand = new CommandSetObjectData(ObjectType.DIAGRAM_FACTOR, diagramFactorId, DiagramFactor.TAG_LOCATION, newLocation);
		project.executeCommand(moveCommand);
		DiagramFactor diagramFactor = (DiagramFactor) project.findObject(ObjectType.DIAGRAM_FACTOR, id);
		FactorId factorId = diagramFactor.getWrappedId();
		doExtraSetup(factorId);
		project.executeCommand(new CommandEndTransaction());

		forceVisibleInLayerManager();
		getDiagramView().updateVisibilityOfFactors();
		
		return diagramFactor;
	}
	
	private Point getDeltaPoint(Point createAt, FactorCell[] selectedFactors, int factorType, DiagramFactor newFactor) throws Exception
	{
		if (createAt != null)
			return createAt;
		
		if (factorType == ObjectType.TARGET)
			return getTargetLocation(getDiagramView().getDiagramModel(), newFactor, getDiagramVisibleRect());
		
		return getNonTargetDeltaPoint(selectedFactors, factorType, newFactor);
	}
	
	private Point getNonTargetDeltaPoint(FactorCell[] selectedFactors, int factorType, DiagramFactor newFactor)
	{
		if (selectedFactors.length > 0 && !(factorType == ObjectType.TARGET))
			return getLocationSelectedNonTargetNode(selectedFactors, (int)newFactor.getSize().getWidth());
		
		return getCenterLocation(getDiagramVisibleRect());
	}
	
	private Rectangle getDiagramVisibleRect()
	{
		DiagramComponent diagramComponent = getDiagramView().getDiagramComponent();
		Rectangle visibleRectangle = diagramComponent.getVisibleRect();
		return visibleRectangle;
	}
	
	public Point getCenterLocation(Rectangle visibleRectangle)
	{
		Point deltaPoint = new Point();
		int centeredWidth = visibleRectangle.width / 2;
		int centeredHeight = visibleRectangle.height / 2;
		
		deltaPoint.x = visibleRectangle.x + centeredWidth;
		deltaPoint.y = visibleRectangle.y + centeredHeight;
		
		return deltaPoint;
	}
	
	public Point getTargetLocation(DiagramModel diagramModel, DiagramFactor addedNode, Rectangle visibleRectangle) throws Exception
	{
		Point deltaPoint = new Point();
		FactorCell[] allTargets = diagramModel.getAllDiagramTargets();

		if (allTargets.length == 1)
		{
			int nodeWidth = addedNode.getSize().width;
			deltaPoint.x = visibleRectangle.width - TARGET_RIGHT_SPACING - nodeWidth;
			deltaPoint.y = TARGET_TOP_LOCATION;
		}
		else
		{
			int highestYIndex = 0;
			int highestY = 0;
			
			for (int i = 0; i < allTargets.length; i++)
			{
				double y = allTargets[i].getBounds().getY();
				if (highestY < y)
				{
					highestY = (int) y;
					highestYIndex = i;
				}
			}
			
			deltaPoint.x = (int)allTargets[highestYIndex].getBounds().getX();
			deltaPoint.y = highestY + (int)allTargets[highestYIndex].getBounds().getHeight() + TARGET_BETWEEN_SPACING;
		}
		
		return deltaPoint;
	}
	
	public Point getLocationSelectedNonTargetNode(FactorCell[] selectedNodes, int nodeWidth)
	{
		Point nodeLocation = selectedNodes[0].getLocation();
		int x = Math.max(0, nodeLocation.x - DEFAULT_MOVE - nodeWidth);
		
		return new Point(x, nodeLocation.y);
	}
	
	void linkToPreviouslySelectedFactors(DiagramFactor newlyInserted, FactorCell[] nodesToLinkTo) throws Exception
	{
		getProject().executeCommand(new CommandBeginTransaction());
		for(int i = 0; i < nodesToLinkTo.length; ++i)
		{
			DiagramFactor toDiagramFactor = nodesToLinkTo[i].getDiagramFactor();
			InsertFactorLinkDoer.createModelLinkageAndAddToDiagramUsingCommands(getDiagramView().getDiagramModel(), newlyInserted, toDiagramFactor);
		}
		getProject().executeCommand(new CommandEndTransaction());
	}

	void notLinkingToAnyFactors() throws CommandFailedException
	{

	}

	void doExtraSetup(FactorId id) throws CommandFailedException
	{

	}
	
	public static final int TARGET_TOP_LOCATION = 150;
	public static final int TARGET_BETWEEN_SPACING = 20;
	public static final int TARGET_RIGHT_SPACING = 10;
	public static final int DEFAULT_MOVE = 150;
}
