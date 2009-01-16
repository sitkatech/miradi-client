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
package org.miradi.views.diagram.doers;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Vector;

import org.miradi.commands.Command;
import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.diagram.DiagramModel;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.ids.DiagramFactorId;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.Factor;
import org.miradi.objects.TaggedObjectSet;
import org.miradi.project.FactorCommandHelper;
import org.miradi.project.FactorDeleteHelper;
import org.miradi.views.ObjectsDoer;

abstract public class AbstractVisibilityDoer extends ObjectsDoer
{
	@Override
	public boolean isAvailable()
	{
		return isInDiagram();
	}
	
	@Override
	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;

		if (!isInDiagram())
			throw new RuntimeException("Doer executed from wrong view");
		
		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			doWork();
		}
		catch(Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
		finally
		{
			getProject().executeCommand(new CommandEndTransaction());
		}
	}
	
	protected ORef getSelectedRefOfType(int selectedType)
	{
		ORefList[] selectedHierarchies = getSelectedHierarchies();
		if (selectedHierarchies.length != 1)
			return ORef.INVALID;
		
		ORefList selectedHierarchy = selectedHierarchies[0];
		return selectedHierarchy.getRefForType(selectedType);
	}
	
	private Vector<Command> hideDiagramFactors(ORefList diagramFactorRefs) throws Exception
	{
		DiagramObject diagramObject = getDiagramObject();
		Vector<Command> commandsToHide = new Vector();
		for (int refIndex = 0; refIndex < diagramFactorRefs.size(); ++refIndex)
		{
			ORef diagramFactorRef = diagramFactorRefs.get(refIndex);
			if (diagramObject.getAllDiagramFactorRefs().contains(diagramFactorRef))
			{
				DiagramFactor diagramFactorToDelete = DiagramFactor.find(diagramObject.getProject(), diagramFactorRef);
				commandsToHide.addAll(createCommandsToHideDiagramFactor(diagramObject, diagramFactorToDelete));
			}
		}
		
		return commandsToHide;
	}

	public static Vector<Command> createCommandsToHideDiagramFactor(DiagramObject diagramObject, DiagramFactor diagramFactorToDelete) throws Exception
	{
		Vector<Command> commandsToHide = new Vector();
		FactorDeleteHelper helper = FactorDeleteHelper.createFactorDeleteHelper(diagramObject);
		commandsToHide.add(helper.buildCommandToRemoveNodeFromDiagram(diagramObject, diagramFactorToDelete.getDiagramFactorId()));
		commandsToHide.addAll(helper.buildCommandsToDeleteDiagramFactor(diagramFactorToDelete));
		
		return commandsToHide;
	}
	
	protected boolean isShowing(ORef factorRef)
	{
		ORefList diagramFactorRefsFromDiagram = getDiagramObject().getAllDiagramFactorRefs();
		ORefList diagramFactorReferrerRefs = getDiagramFactorReferrerRefs(factorRef);
		
		return diagramFactorRefsFromDiagram.containsAnyOf(diagramFactorReferrerRefs);
	}

	protected ORefList getDiagramFactorReferrerRefs(ORef factorRef)
	{
		Factor factor = getFactor(factorRef);
		ORefList diagramFactorReferrers = factor.findObjectsThatReferToUs(DiagramFactor.getObjectType());
		return diagramFactorReferrers;
	}
	
	protected void setLocation(FactorCommandHelper helper, DiagramFactorId ownedDiagramFactorId)	throws Exception
	{
		DiagramFactor parentDiagramFactor = getDiagramModel().getDiagramFactor(getParent().getRef());
		ORef annotationRef = getSelectedAnnotationRef();
		int offset = getAnnotationList().find(annotationRef);
		Point location = new Point(parentDiagramFactor.getLocation());
		location.x += (offset * getProject().getGridSize()); 
		location.y += parentDiagramFactor.getSize().height;
		helper.setDiagramFactorLocation(ownedDiagramFactorId, location);
	}
		
	private void setSize(FactorCommandHelper helper, DiagramFactorId diagramFactorId, Dimension size) throws CommandFailedException
	{
		helper.setDiagramFactorSize(diagramFactorId, size);
	}
	
	private void selectParentDiagramFactor()
	{
		getDiagramView().getCurrentDiagramComponent().selectFactor(getParentRef());
	}
	
	protected BaseObject getParent()
	{
		return getProject().findObject(getParentRef());
	}
	
	protected void showBubble(Dimension defaultSize) throws Exception, CommandFailedException
	{
		ORef selectedAnnotationRef = getSelectedAnnotationRef();
		FactorCommandHelper helper = new FactorCommandHelper(getProject(), getDiagramModel());
		DiagramFactorId annotationDiagramFactorId = (DiagramFactorId) helper.createDiagramFactor(getDiagramObject(), selectedAnnotationRef).getCreatedId();

		setLocation(helper, annotationDiagramFactorId);
		setSize(helper, annotationDiagramFactorId, defaultSize);
		selectParentDiagramFactor();
		
		includeBubbleInSelectedTags(selectedAnnotationRef);
	}

	private void includeBubbleInSelectedTags(ORef selectedAnnotationRef) throws Exception
	{
		ORefList selectedTaggedObjectSetRefs = getDiagramObject().getSelectedTaggedObjectSetRefs();
		for (int index = 0; index < selectedTaggedObjectSetRefs.size(); ++index)
		{
			TaggedObjectSet selectedTaggedObjectSet = TaggedObjectSet.find(getProject(), selectedTaggedObjectSetRefs.get(index));
			CommandSetObjectData tagFactorCommand = CommandSetObjectData.createAppendORefCommand(selectedTaggedObjectSet, TaggedObjectSet.TAG_TAGGED_OBJECT_REFS, selectedAnnotationRef);
			getProject().executeCommand(tagFactorCommand);
		}
	}

	protected void hideBubble() throws Exception, CommandFailedException
	{
		ORef selectedAnnotationRef = getSelectedAnnotationRef();
		ORefList diagramFactorReferrerRefs = getDiagramFactorReferrerRefs(selectedAnnotationRef);
		ORefList diagramFactorRefsFromCurrentDiagram = getDiagramObject().getAllDiagramFactorRefs();		
		ORefList diagramFactorRefsToBeRemoved = diagramFactorReferrerRefs.getOverlappingRefs(diagramFactorRefsFromCurrentDiagram);
		
		Vector commandsToHideBubble = hideDiagramFactors(diagramFactorRefsToBeRemoved);
		getProject().executeCommandsWithoutTransaction(commandsToHideBubble);
	}
	
	private DiagramObject getDiagramObject()
	{
		return getDiagramModel().getDiagramObject();
	}

	private DiagramModel getDiagramModel()
	{
		return getDiagramView().getDiagramModel();
	}
	
	abstract protected Factor getFactor(ORef factorRef);
	
	abstract protected void doWork() throws Exception;
	
	abstract protected boolean isAvailable(ORef selectedFactorRef);
	
	abstract protected ORef getSelectedAnnotationRef();
	
	abstract protected ORef getParentRef();
	
	abstract protected ORefList getAnnotationList();
}
