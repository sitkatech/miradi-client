/* 
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.*;
import org.miradi.project.FactorCommandHelper;
import org.miradi.project.Project;
import org.miradi.schemas.ScopeBoxSchema;
import org.miradi.schemas.StressSchema;
import org.miradi.schemas.TaskSchema;
import org.miradi.schemas.TextBoxSchema;

import java.awt.*;
import java.util.Vector;

abstract public class InsertFactorDoer extends LocationDoer
{
	abstract public int getTypeToInsert();
	abstract public String getInitialText();
	abstract public void forceVisibleInLayerManager() throws Exception;

	@Override
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

	@Override
	protected void doIt() throws Exception
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
			if((selectedFactors.length > 0) && (!isLinkToSelfType()))
				linkToPreviouslySelectedFactors(diagramFactor, selectedFactors);
			else if (isSplittableLink(selectedFactors, selectedDiagramLinks, diagramFactor))
				linkCreator.splitSelectedLinkToIncludeFactor(getDiagramModel(), selectedDiagramLinks[0], diagramFactor);
			
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

		doPostTransactionWork(getDiagramModel().getDiagramObject(), diagramFactor);
	}
	
	protected void doPostTransactionWork(DiagramObject diagramObject, DiagramFactor newDiagramFactor) throws CommandFailedException
	{
	}

	private boolean isLinkToSelfType()
	{
		if (Target.is(getTypeToInsert()))
			return true;
		
		if (GroupBox.is(getTypeToInsert()))
			return true;
		
		return HumanWelfareTarget.is(getTypeToInsert());
	}
	
	private boolean isSplittableLink(FactorCell[] selectedFactors, DiagramLink[] selectedDiagramLinks, DiagramFactor diagramFactor)
	{
		if (!LinkCreator.isValidLinkableType(diagramFactor.getWrappedType()))
			return false;
	
		return selectedFactors.length == 0 && selectedDiagramLinks.length == 1;
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
	
	private DiagramLink[] getSelectedDiagramLinks()
	{
		return getDiagramView().getDiagramPanel().getOnlySelectedLinks();
	}
	
	private void selectNewFactor(ORef factorRef)
	{
		getDiagramView().getDiagramPanel().selectFactor(factorRef);
	}
	
	private void launchPropertiesEditor(DiagramFactor diagramFactor) throws Exception
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
		Point ensuredNonOverlappingPoint = getDiagramModel().recursivelyGetNonOverlappingFactorPoint(snappedPoint);
		
		FactorCommandHelper factorCommandHelper = new FactorCommandHelper(project, getDiagramModel());
		CommandCreateObject createCommand = factorCommandHelper.createFactorAndDiagramFactor(factorType, ensuredNonOverlappingPoint, DiagramFactor.getDefaultSize(factorType), getInitialText());
		ORef newDiagramFactorRef =  createCommand.getObjectRef();
				
		DiagramFactor diagramFactor = (DiagramFactor) project.findObject(newDiagramFactorRef);
		doExtraSetup(diagramFactor, selectedNodes);

		applyCurrentlySelectedTaggedObjectSetsToNewDiagramFactor(diagramFactor);
		forceVisibleInLayerManager();
		getDiagramView().updateVisibilityOfFactorsAndClearSelectionModel();
		
		return diagramFactor;
	}

	private void applyCurrentlySelectedTaggedObjectSetsToNewDiagramFactor(DiagramFactor diagramFactor) throws Exception
	{
		DiagramObject diagramObject = getDiagramModel().getDiagramObject();
		if (diagramObject.isTaggingEnabled())
		{
			CommandSetObjectData appendCommand = CommandSetObjectData.createAppendORefListCommand(diagramFactor, DiagramFactor.TAG_TAGGED_OBJECT_SET_REFS, diagramObject.getSelectedTaggedObjectSetRefs());
			getProject().executeCommand(appendCommand);
		}
	}
	
	private Point getDeltaPoint(Point createAt, FactorCell[] selectedFactors, int factorType, int factorWidth) throws Exception
	{
		if (createAt != null)
			return createAt;
		
		if (Target.is(factorType))
			return getTargetLocation(factorWidth, getTargetRightSpacing());
		
		if (HumanWelfareTarget.is(factorType))
			return getTargetLocation(factorWidth, DEFAULT_ABSTRACT_TARGET_RIGHT_MARGIN);
		
		return getNonTargetDeltaPoint(selectedFactors, factorType, factorWidth);
	}
	
	private int getTargetRightSpacing()
	{
		if (getProject().getMetadata().isHumanWelfareTargetMode())
			return TARGET_RIGHT_MARGIN + TARGET_BETWEEN_SPACING;
		
		return DEFAULT_ABSTRACT_TARGET_RIGHT_MARGIN;
	}
	
	private Point getNonTargetDeltaPoint(FactorCell[] selectedFactors, int factorType, int factorWidth)
	{
		//TODO why are we asking if factorType is not target, the question was already answered in calling method
		if (selectedFactors.length > 0 && !(factorType == ObjectType.TARGET))
			return getLocationSelectedNonTargetNode(selectedFactors[0], factorWidth);
		
		return getCenterLocation(getDiagramVisibleRect());
	}
	
	private Rectangle getDiagramVisibleRect()
	{
		DiagramComponent diagramComponent = getDiagramView().getCurrentDiagramComponent();
		Rectangle visibleRectangle = diagramComponent.getVisibleRect();
		return visibleRectangle;
	}
	
	private Point getCenterLocation(Rectangle visibleRectangle)
	{
		Point deltaPoint = new Point();
		int centeredWidth = visibleRectangle.width / 2;
		int centeredHeight = visibleRectangle.height / 2;
		
		deltaPoint.x = visibleRectangle.x + centeredWidth;
		deltaPoint.y = visibleRectangle.y + centeredHeight;
		
		return deltaPoint;
	}
	
	private Point getTargetLocation(int factorWidth, int targetRightSpacing) throws Exception
	{
		Vector<FactorCell> allTargets = getDiagramModel().getFactorCells(getTypeToInsert());
		Rectangle visibleRectangle = getDiagramVisibleRect();
		Point deltaPoint = new Point();
		if (allTargets.size() == 0)
		{
			deltaPoint.x = visibleRectangle.width - targetRightSpacing - factorWidth;
			deltaPoint.y = TARGET_TOP_LOCATION;
		}
		else
		{
			int highestYIndex = 0;
			int highestY = 0;
			
			for (int i = 0; i < allTargets.size(); i++)
			{
				double y = allTargets.get(i).getBounds().getY();
				if (highestY < y)
				{
					highestY = (int) y;
					highestYIndex = i;
				}
			}
			
			FactorCell targetCell = allTargets.get(highestYIndex);
			deltaPoint.x = (int)targetCell.getBounds().getX();
			deltaPoint.y = highestY + (int)targetCell.getBounds().getHeight() + TARGET_BETWEEN_SPACING;
		}
		
		return deltaPoint;
	}
	
	private Point getLocationSelectedNonTargetNode(FactorCell selectedFactorCell, int nodeWidth)
	{
		Point nodeLocation = selectedFactorCell.getLocation();
		int x = Math.max(0, nodeLocation.x - DEFAULT_MOVE - nodeWidth);
		
		return new Point(x, nodeLocation.y);
	}
	
	private void linkToPreviouslySelectedFactors(DiagramFactor newlyInserted, FactorCell[] nodesToLinkTo) throws Exception
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
			
			linkCreator.createFactorLinkAndDiagramLinkVoid(getDiagramModel().getDiagramObject(), newlyInserted, to);
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
		if (type == TextBoxSchema.getObjectType())
			return false;
		
		if (type == StressSchema.getObjectType())
			return false;
		
		if (type == TaskSchema.getObjectType())
			return false;
		
		if (type == ScopeBoxSchema.getObjectType())
			return false;
		
		return true; 
	}
	
	protected void doExtraSetup(DiagramFactor newlyInsertedDiagramFactor, FactorCell[] selectedFactorCells) throws Exception
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
	
	private static final int TARGET_TOP_LOCATION = 150;
	private static final int TARGET_BETWEEN_SPACING = 20;
	private static final int TARGET_RIGHT_MARGIN = 130;
	private static final int DEFAULT_ABSTRACT_TARGET_RIGHT_MARGIN = 10;
	private static final int DEFAULT_MOVE = 150;
}
