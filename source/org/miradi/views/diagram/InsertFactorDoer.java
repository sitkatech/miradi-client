/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.diagram;

import java.awt.Point;
import java.awt.Rectangle;

import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandCreateObject;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.diagram.DiagramComponent;
import org.miradi.diagram.DiagramModel;
import org.miradi.diagram.cells.FactorCell;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.ids.FactorId;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.TextBox;
import org.miradi.project.FactorCommandHelper;
import org.miradi.project.Project;

abstract public class InsertFactorDoer extends LocationDoer
{
	abstract public int getTypeToInsert();
	abstract public String getInitialText();
	abstract public void forceVisibleInLayerManager();

	public boolean isAvailable()
	{
		if (! getProject().isOpen())
			return false;
		
		if (!isInDiagram())
			return false;
		
		if (getDiagramModel() == null)
			return false;
		
		return true;
	}

	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;
		
		Project project = getProject();
		FactorCell[] selectedFactors = getSelectedFactorCells();
		DiagramFactor diagramFactor = null;
		project.executeCommand(new CommandBeginTransaction());
		try
		{
			diagramFactor = insertFactorItself();
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
		finally 
		{
			project.executeCommand(new CommandEndTransaction());
		}

		try
		{
			FactorId id = diagramFactor.getWrappedId();
			if((selectedFactors.length > 0) && (getTypeToInsert()!= ObjectType.TARGET) && (getTypeToInsert()!= ObjectType.GROUP_BOX))
			{
				// NOTE: Set up a second transaction, so the link creation is independently undoable
				project.executeCommand(new CommandBeginTransaction());
				try
				{
					linkToPreviouslySelectedFactors(diagramFactor, selectedFactors);
				}
				finally
				{
					project.executeCommand(new CommandEndTransaction());
				}
			}
			else
			{
				notLinkingToAnyFactors();
			}

			selectNewFactor(id);
			ensureNewFactorIsVisible(diagramFactor);
			launchPropertiesEditor(diagramFactor);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
	}
	private void ensureNewFactorIsVisible(DiagramFactor diagramFactor) throws Exception
	{
		FactorCell newCell = getDiagramModel().getFactorCellById(diagramFactor.getDiagramFactorId());
		getDiagramView().getDiagramComponent().scrollCellToVisible(newCell);
	}
	
	protected FactorCell[] getSelectedFactorCells()
	{
		return getDiagramView().getDiagramPanel().getOnlySelectedFactorCells();
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
		int factorType = getTypeToInsert();
		FactorCell[] selectedNodes = getSelectedFactorCells();
		Point deltaPoint = getDeltaPoint(createAt, selectedNodes, factorType, DiagramFactor.getDefaultSize(factorType).width);
		Point snappedPoint  = project.getSnapped(deltaPoint);
		Point ensuredNonOverlappintPoint = getDiagramModel().recursivelyGetNonOverlappingFactorPoint(snappedPoint);
		
		FactorCommandHelper factorCommandHelper = new FactorCommandHelper(project, getDiagramModel());
		CommandCreateObject createCommand = factorCommandHelper.createFactorAndDiagramFactor(factorType, ensuredNonOverlappintPoint, DiagramFactor.getDefaultSize(factorType), getInitialText());
		ORef newDiagramFactorRef =  createCommand.getObjectRef();
				
		DiagramFactor diagramFactor = (DiagramFactor) project.findObject(newDiagramFactorRef);
		doExtraSetup(diagramFactor, selectedNodes);

		forceVisibleInLayerManager();
		getDiagramView().updateVisibilityOfFactorsAndClearSelectionModel();
		
		return diagramFactor;
	}
	
	private Point getDeltaPoint(Point createAt, FactorCell[] selectedFactors, int factorType, int factorWidth) throws Exception
	{
		if (createAt != null)
			return createAt;
		
		if (factorType == ObjectType.TARGET)
			return getTargetLocation(getDiagramModel(), getDiagramVisibleRect(), factorWidth);
		
		return getNonTargetDeltaPoint(selectedFactors, factorType, factorWidth);
	}
	
	private Point getNonTargetDeltaPoint(FactorCell[] selectedFactors, int factorType, int factorWidth)
	{
		if (selectedFactors.length > 0 && !(factorType == ObjectType.TARGET))
			return getLocationSelectedNonTargetNode(selectedFactors, factorWidth);
		
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
	
	public Point getTargetLocation(DiagramModel diagramModel, Rectangle visibleRectangle, int factorWidth) throws Exception
	{
		Point deltaPoint = new Point();
		FactorCell[] allTargets = diagramModel.getAllDiagramTargets();

		if (allTargets.length == 0)
		{
			deltaPoint.x = visibleRectangle.width - TARGET_RIGHT_SPACING - factorWidth;
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
			
			FactorCell targetCell = allTargets[highestYIndex];
			deltaPoint.x = (int)targetCell.getBounds().getX();
			deltaPoint.y = highestY + (int)targetCell.getBounds().getHeight() + TARGET_BETWEEN_SPACING;
		}
		
		return deltaPoint;
	}
	
	public Point getLocationSelectedNonTargetNode(FactorCell[] selectedNodes, int nodeWidth)
	{
		Point nodeLocation = selectedNodes[0].getLocation();
		int x = Math.max(0, nodeLocation.x - DEFAULT_MOVE - nodeWidth);
		
		return new Point(x, nodeLocation.y);
	}
	
	protected void linkToPreviouslySelectedFactors(DiagramFactor newlyInserted, FactorCell[] nodesToLinkTo) throws Exception
	{
		if (! linkableType(newlyInserted.getWrappedType()))
			return;
		
		if (! containsLikableType(nodesToLinkTo))
			return;
		
		LinkCreator linkCreator = new LinkCreator(getProject());
		for(int i = 0; i < nodesToLinkTo.length; ++i)
		{
			DiagramFactor to = nodesToLinkTo[i].getDiagramFactor();
			if (newlyInserted.isGroupBoxFactor() || to.isGroupBoxFactor())
				linkCreator.createGroupBoxChildrenDiagramLinks(getDiagramModel(), newlyInserted, to);
			else
				linkCreator.createFactorLinkAndAddToDiagramUsingCommands(getDiagramModel(), newlyInserted, to);
		}
	}

	private boolean containsLikableType(FactorCell[] nodesToLinkTo)
	{
		for (int i = 0; i < nodesToLinkTo.length; ++i)
		{
			if (!linkableType(nodesToLinkTo[i].getWrappedType()))
					return false;
		}
		return true;
	}
	
	private boolean linkableType(int type)
	{
		if (type == TextBox.getObjectType())
			return false;
		
		return true; 
	}
	
	protected void notLinkingToAnyFactors() throws CommandFailedException
	{
	}

	protected void doExtraSetup(DiagramFactor newlyInserteddiagramFactor, FactorCell[] selectedFactorCells) throws Exception
	{
	}
	
	private DiagramModel getDiagramModel()
	{
		return getDiagramView().getDiagramModel();
	}
	
	public static final int TARGET_TOP_LOCATION = 150;
	public static final int TARGET_BETWEEN_SPACING = 20;
	public static final int TARGET_RIGHT_SPACING = 10;
	public static final int DEFAULT_MOVE = 150;
}
