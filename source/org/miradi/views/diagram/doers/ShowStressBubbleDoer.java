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
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.Target;
import org.miradi.project.FactorCommandHelper;

public class ShowStressBubbleDoer extends AbstractStressVisibilityDoer
{
	protected boolean isAvailable(ORef selectedStressRef)
	{
		return !isShowing(selectedStressRef);
	}
	
	protected void doWork() throws Exception
	{
		DiagramModel diagramModel = getDiagramView().getDiagramModel();		
		DiagramObject diagramObject = diagramModel.getDiagramObject();
		ORef selectedStressRef = getSelectedStressRef();
		FactorCommandHelper helper = new FactorCommandHelper(getProject(), diagramModel);
		DiagramFactorId stressDiagramFactorId = (DiagramFactorId) helper.createDiagramFactor(diagramObject, selectedStressRef).getCreatedId();

		Target stressTargetParent = Target.find(getProject(), getSelectedTargetRef());
		setStressLocation(diagramModel, helper, stressDiagramFactorId, stressTargetParent, selectedStressRef);
		setStressSize(helper, stressDiagramFactorId);
		
		getDiagramView().getDiagramComponent().selectFactor(stressTargetParent.getFactorId());
	}

	private void setStressSize(FactorCommandHelper helper, DiagramFactorId stressDiagramFactorId) throws CommandFailedException
	{
		helper.setDiagramFactorSize(stressDiagramFactorId, DiagramFactor.DEFAULT_STRESS_SIZE);
	}

	private void setStressLocation(DiagramModel diagramModel, FactorCommandHelper helper, DiagramFactorId stressDiagramFactorId, Target stressTargetParent, ORef selectedStressRef)	throws Exception
	{
		DiagramFactor targetDiagramFactor = diagramModel.getDiagramFactor(stressTargetParent.getFactorId());
		int offset = stressTargetParent.getStressRefs().find(selectedStressRef);
		Point stressLocation = new Point(targetDiagramFactor.getLocation());
		stressLocation.x += (offset * getProject().getGridSize()); 
		stressLocation.y += targetDiagramFactor.getSize().height;
		helper.setDiagramFactorLocation(stressDiagramFactorId, stressLocation);
	}
}
