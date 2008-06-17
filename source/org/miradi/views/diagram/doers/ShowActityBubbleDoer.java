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

import java.awt.Point;

import org.miradi.diagram.DiagramModel;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.ids.DiagramFactorId;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.Factor;
import org.miradi.objects.Strategy;
import org.miradi.objects.Task;
import org.miradi.project.FactorCommandHelper;

public class ShowActityBubbleDoer extends AbstractActivityVisibilityDoer
{
	protected void doWork() throws Exception
	{
		DiagramModel diagramModel = getDiagramView().getDiagramModel();		
		DiagramObject diagramObject = diagramModel.getDiagramObject();
		ORef selectedActivityRef = getSelectedActivityRef();
		FactorCommandHelper helper = new FactorCommandHelper(getProject(), diagramModel);
		DiagramFactorId activityDiagramFactorId = (DiagramFactorId) helper.createDiagramFactor(diagramObject, selectedActivityRef).getCreatedId();

		Strategy activityStrategyParent = Strategy.find(getProject(), getSelectedStrategyRef());
		DiagramFactor strategyDiagramFactor = diagramModel.getDiagramFactor(activityStrategyParent.getFactorId());
		setActivityLocation(diagramModel, helper, strategyDiagramFactor, activityDiagramFactorId, activityStrategyParent.getActivityRefs(), selectedActivityRef);
		setActivitySize(helper, activityDiagramFactorId);
		
		getDiagramView().getDiagramComponent().selectFactor(activityStrategyParent.getFactorId());
	}

	private void setActivitySize(FactorCommandHelper helper, DiagramFactorId activityDiagramFactorId) throws CommandFailedException
	{
		helper.setDiagramFactorSize(activityDiagramFactorId, DiagramFactor.DEFAULT_ACTIVITY_SIZE);
	}

	protected void setActivityLocation(DiagramModel diagramModel, FactorCommandHelper helper, DiagramFactor parentDiagramFactor, DiagramFactorId ownedDiagramFactorId, ORefList annotationRefList, ORef annotationRef)	throws Exception
	{
		int offset = annotationRefList.find(annotationRef);
		Point location = new Point(parentDiagramFactor.getLocation());
		location.x += (offset * getProject().getGridSize()); 
		location.y += parentDiagramFactor.getSize().height;
		helper.setDiagramFactorLocation(ownedDiagramFactorId, location);
	}

	@Override
	protected boolean isAvailable(ORef selectedFactorRef)
	{
		return !isShowing(selectedFactorRef);
	}
	
	protected Factor getFactor(ORef factorRef)
	{
		return Task.find(getProject(), factorRef);
	}
}
