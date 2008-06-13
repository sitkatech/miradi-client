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

import org.miradi.commands.Command;
import org.miradi.diagram.DiagramModel;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramObject;
import org.miradi.project.FactorDeleteHelper;

public class HideStressBubbleDoer extends AbstractStressVisibilityDoer
{
	protected boolean isAvailable(ORef selectedStressRef)
	{
		return isShowing(selectedStressRef);
	}
	
	protected void doWork() throws Exception
	{
		ORef selectedStressRef = getSelectedStress();
		DiagramModel diagramModel = getDiagramView().getDiagramModel();
		ORefList diagramFactorReferrerRefs = getDiagramFactorReferrerRefs(selectedStressRef);
		ORefList diagramFactorRefsFromCurrentDiagram = diagramModel.getDiagramObject().getAllDiagramFactorRefs();		
		ORefList diagramFactorRefsToBeRemoved = diagramFactorReferrerRefs.getOverlappingRefs(diagramFactorRefsFromCurrentDiagram);
		
		Vector commandsToHideStressBubble = hideDiagramFactors(diagramModel.getDiagramObject(), diagramFactorRefsToBeRemoved);
		getProject().executeCommandsWithoutTransaction(commandsToHideStressBubble);
	}

	private Vector<Command> hideDiagramFactors(DiagramObject diagramObject, ORefList diagramFactorRefs) throws Exception
	{
		Vector<Command> commandsToHide = new Vector();
		for (int refIndex = 0; refIndex < diagramFactorRefs.size(); ++refIndex)
		{
			ORef diagramFactorRef = diagramFactorRefs.get(refIndex);
			if (diagramObject.getAllDiagramFactorRefs().contains(diagramFactorRef))
			{
				DiagramFactor diagramFactorToDelete = DiagramFactor.find(diagramObject.getProject(), diagramFactorRef);
				commandsToHide.addAll(createCommandsToHideStressDiagramFactor(diagramObject, diagramFactorToDelete));
			}
		}
		
		return commandsToHide;
	}

	public static Vector<Command> createCommandsToHideStressDiagramFactor(DiagramObject diagramObject, DiagramFactor diagramFactorToDelete) throws Exception
	{
		Vector<Command> commandsToHide = new Vector();
		FactorDeleteHelper helper = new FactorDeleteHelper(diagramObject);
		commandsToHide.add(helper.buildCommandToRemoveNodeFromDiagram(diagramObject, diagramFactorToDelete.getDiagramFactorId()));
		commandsToHide.addAll(helper.buildCommandsToDeleteDiagramFactor(diagramFactorToDelete));
		
		return commandsToHide;
	}
}