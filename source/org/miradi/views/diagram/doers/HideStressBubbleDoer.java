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

import java.util.Vector;

import org.miradi.diagram.DiagramModel;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.Factor;
import org.miradi.objects.Stress;

public class HideStressBubbleDoer extends AbstractStressVisibilityDoer
{
	protected boolean isAvailable(ORef selectedStressRef)
	{
		return isShowing(selectedStressRef);
	}
	
	protected void doWork() throws Exception
	{
		ORef selectedStressRef = getSelectedStressRef();
		DiagramModel diagramModel = getDiagramView().getDiagramModel();
		ORefList diagramFactorReferrerRefs = getDiagramFactorReferrerRefs(selectedStressRef);
		ORefList diagramFactorRefsFromCurrentDiagram = diagramModel.getDiagramObject().getAllDiagramFactorRefs();		
		ORefList diagramFactorRefsToBeRemoved = diagramFactorReferrerRefs.getOverlappingRefs(diagramFactorRefsFromCurrentDiagram);
		
		Vector commandsToHideStressBubble = hideDiagramFactors(diagramModel.getDiagramObject(), diagramFactorRefsToBeRemoved);
		getProject().executeCommandsWithoutTransaction(commandsToHideStressBubble);
	}
	
	protected Factor getFactor(ORef factorRef)
	{
		return Stress.find(getProject(), factorRef);
	}
}