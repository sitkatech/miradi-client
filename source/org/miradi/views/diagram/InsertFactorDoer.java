/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.views.diagram;

import java.awt.Point;
import java.awt.Rectangle;

import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandCreateObject;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.diagram.DiagramComponent;
import org.miradi.diagram.DiagramModel;
import org.miradi.diagram.cells.FactorCell;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.Stress;
import org.miradi.objects.TaggedObjectSet;
import org.miradi.objects.Task;
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
		DiagramLink[] selectedDiagramLinks = getSelectedDiagramLinks();
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

		// NOTE: Set up a second transaction, so the link creation is independently undoable
		getProject().executeBeginTransaction();
		try
		{
			LinkCreator linkCreator = new LinkCreator(getProject());
			ORef factorRef = diagramFactor.getWrappedORef();
			if((selectedFactors.length > 0) && (getTypeToInsert()!= ObjectType.TARGET) && (getTypeToInsert()!= ObjectType.GROUP_BOX))
				linkToPreviouslySelectedFactors(diagramFactor, selectedFactors);
			else if (selectedFactors.length == 0 && selectedDiagramLinks.length == 1)
				linkCreator.splitSelectedLinkToIncludeFactor(getDiagramModel(), selectedDiagramLinks[0], diagramFactor);
			else
				notLinkingToAnyFactors();
			
			selectNewFactor(factorRef);
			ensureNewFactorIsVisible(diagramFactor);
			launchPropertiesEditor(diagramFactor);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
		finally
		{
			getProject().executeEndTransaction();
		}
	}
	
	private void ensureNewFactorIsVisible(DiagramFactor diagramFactor) throws Exception
	{
		FactorCell newCell = getDiagramModel().getFactorCellByRef(diagramFactor.getRef());
		getDiagramView().getCurrentDiagramComponent().scrollCellToVisible(newCell);
	}
	
	protected FactorCell[] getSelectedFactorCells()
	{
		return getDiagramView().getDiagramPanel().getOnlySelectedFactorCells();
	}
	
	protected DiagramLink[] getSelectedDiagramLinks()
	{
		return getDiagramView().getDiagramPanel().getOnlySelectedLinks();
	}
	
	protected void selectNewFactor(ORef factorRef)
	{
		getDiagramView().getDiagramPanel().selectFactor(factorRef);
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

		includeNewFactorInActiveTags(diagramFactor.getWrappedORef());
		forceVisibleInLayerManager();
		getDiagramView().updateVisibilityOfFactorsAndClearSelectionModel();
		
		return diagramFactor;
	}
	private void includeNewFactorInActiveTags(ORef newFactorRefToTag) throws Exception
	{
		DiagramObject diagramObject = getDiagramModel().getDiagramObject();
		ORefList activeTags = diagramObject.getSelectedTaggedObjectSetRefs();
		for (int index = 0; index < activeTags.size(); ++index)
		{
			TaggedObjectSet taggedObjectSet = TaggedObjectSet.find(getProject(), activeTags.get(index));			
			CommandSetObjectData setTaggedFactors = CommandSetObjectData.createAppendORefCommand(taggedObjectSet, TaggedObjectSet.TAG_TAGGED_OBJECT_REFS, newFactorRefToTag);
			getProject().executeCommand(setTaggedFactors);
		}
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
		DiagramComponent diagramComponent = getDiagramView().getCurrentDiagramComponent();
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
		FactorCell[] allTargets = diagramModel.getAllDiagramTargetsAsArray();

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
			if (getDiagramModel().areLinked(newlyInserted.getWrappedORef(), to.getWrappedORef()))
				continue;
			
			linkCreator.createFactorLinkAndDiagramLink(getDiagramModel(), newlyInserted, to);
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
		
		if (type == Stress.getObjectType())
			return false;
		
		if (type == Task.getObjectType())
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
	
	protected LayerManager getCurrentLayerManager()
	{
		return getDiagramView().getDiagramModel().getLayerManager();
	}
	
	public static final int TARGET_TOP_LOCATION = 150;
	public static final int TARGET_BETWEEN_SPACING = 20;
	public static final int TARGET_RIGHT_SPACING = 10;
	public static final int DEFAULT_MOVE = 150;
}
