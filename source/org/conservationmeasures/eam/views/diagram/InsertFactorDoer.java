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
import org.conservationmeasures.eam.diagram.factortypes.FactorType;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.project.FactorCommandHelper;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

abstract public class InsertFactorDoer extends LocationDoer
{
	abstract public FactorType getTypeToInsert();
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
			FactorCell[] selectedFactors = getProject().getOnlySelectedFactors();
			FactorId id = insertFactorItself();
			if((selectedFactors.length > 0) && (getTypeToInsert()!= Factor.TYPE_TARGET))
				linkToPreviouslySelectedFactors(id, selectedFactors);
			else
				notLinkingToAnyFactors();
			
			selectNewFactor(id);
			launchPropertiesEditor(id);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
	}
	
    protected void selectNewFactor(FactorId idToUse)
	{
		getProject().selectFactor(idToUse);
	}
	
	void launchPropertiesEditor(FactorId id) throws Exception, CommandFailedException
	{
		FactorCell newFactor = getProject().getDiagramModel().getFactorCellByWrappedId(id);
		getDiagramView().getPropertiesDoer().doFactorProperties(newFactor, null);
	}

	private FactorId insertFactorItself() throws Exception
	{
		Point createAt = getLocation();
		Project project = getProject();
		FactorCell[] selectedNodes = project.getOnlySelectedFactors();

		project.executeCommand(new CommandBeginTransaction());
		FactorType factorType = getTypeToInsert();
		FactorCommandHelper factorCommandHelper = new FactorCommandHelper(project);
		DiagramFactorId id = (DiagramFactorId) factorCommandHelper.createFactorAndDiagramFactor(factorType).getCreatedId();
		
		FactorCell factorCell = project.getDiagramModel().getFactorCellById(id);
		DiagramFactor addedFactor = factorCell.getDiagramFactor();

		CommandSetObjectData setNameCommand = FactorCommandHelper.createSetLabelCommand(addedFactor.getWrappedId(), getInitialText());
		project.executeCommand(setNameCommand);

		Point deltaPoint = getDeltaPoint(createAt, selectedNodes, factorType, addedFactor);
		Point snappedPoint  = project.getSnapped(deltaPoint);
		//Command moveCommand = new CommandDiagramMove(snappedPoint.x, snappedPoint.y, new DiagramFactorId[] {addedFactor.getDiagramFactorId()});
		DiagramFactorId diagramFactorId = addedFactor.getDiagramFactorId();
		String newLocation = EnhancedJsonObject.convertFromPoint(snappedPoint);
		CommandSetObjectData moveCommand = new CommandSetObjectData(ObjectType.DIAGRAM_FACTOR, diagramFactorId, DiagramFactor.TAG_LOCATION, newLocation);
		project.executeCommand(moveCommand);
		DiagramFactor diagramFactor = (DiagramFactor) project.findObject(ObjectType.DIAGRAM_FACTOR, id);
		FactorId factorId = diagramFactor.getWrappedId();
		doExtraSetup(factorId);
		project.executeCommand(new CommandEndTransaction());

		forceVisibleInLayerManager();
		project.updateVisibilityOfFactors();
		
		return factorId;
	}
	
	private Point getDeltaPoint(Point createAt, FactorCell[] selectedFactors, FactorType factorType, DiagramFactor newFactor)
	{
		if (createAt != null)
			return createAt;
		
		if (factorType.isTarget())
			return getTargetLocation(newFactor, getDiagramVisibleRect());
		
		return getNonTargetDeltaPoint(selectedFactors, factorType, newFactor);
	}
	
	private Point getNonTargetDeltaPoint(FactorCell[] selectedFactors, FactorType factorType, DiagramFactor newFactor)
	{
		if (selectedFactors.length > 0 && !factorType.isTarget())
			return getLocationSelectedNonTargetNode(selectedFactors, (int)newFactor.getSize().getWidth());
		
		return getCenterLocation(getDiagramVisibleRect());
	}
	
	private Rectangle getDiagramVisibleRect()
	{
		DiagramComponent diagramComponent = getMainWindow().getDiagramComponent();
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
	
	public Point getTargetLocation(DiagramFactor addedNode, Rectangle visibleRectangle)
	{
		Point deltaPoint = new Point();
		DiagramModel diagramModel = getProject().getDiagramModel();
		FactorCell[] allTargets = diagramModel.getAllDiagramTargets();

		if (allTargets.length == 1)
		{
			int nodeWidth = addedNode.getSize().width;
			deltaPoint.x = visibleRectangle.width - TARGET_RIGHT_SPACING - nodeWidth;
			deltaPoint.y = TARGET_TOP_LOCATION;
		}
		else
		{
			int highestY = 0;
			double y;
			for (int i = 0; i < allTargets.length; i++)
			{
				y = allTargets[i].getBounds().getY();
				highestY = (int)Math.max(highestY, y);
			}
			
			deltaPoint.x = (int)allTargets[0].getBounds().getX();
			deltaPoint.y = highestY + (int)allTargets[0].getBounds().getHeight() + TARGET_BETWEEN_SPACING;
		}
		
		return deltaPoint;
	}
	
	public Point getLocationSelectedNonTargetNode(FactorCell[] selectedNodes, int nodeWidth)
	{
		Point nodeLocation = selectedNodes[0].getLocation();
		int x = Math.max(0, nodeLocation.x - DEFAULT_MOVE - nodeWidth);
		
		return new Point(x, nodeLocation.y);
	}
	
	void linkToPreviouslySelectedFactors(FactorId newlyInsertedId, FactorCell[] nodesToLinkTo) throws CommandFailedException
	{
		getProject().executeCommand(new CommandBeginTransaction());
		for(int i = 0; i < nodesToLinkTo.length; ++i)
		{
			FactorId toId = nodesToLinkTo[i].getWrappedId();
			InsertFactorLinkDoer.createModelLinkageAndAddToDiagramUsingCommands(getProject(), newlyInsertedId, toId);
		}
		getProject().executeCommand(new CommandEndTransaction());
	}

	void notLinkingToAnyFactors() throws CommandFailedException
	{

	}

	void doExtraSetup(FactorId id) throws CommandFailedException
	{

	}

	public DiagramView getDiagramView()
	{
		return (DiagramView)getView();
	}
	
	public static final int TARGET_TOP_LOCATION = 150;
	public static final int TARGET_BETWEEN_SPACING = 20;
	public static final int TARGET_RIGHT_SPACING = 10;
	public static final int DEFAULT_MOVE = 150;
}
