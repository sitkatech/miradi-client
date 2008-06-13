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
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ConceptualModelDiagram;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.Stress;
import org.miradi.objects.Target;
import org.miradi.views.diagram.DeleteAnnotationDoer;

public class DeleteStressDoer extends DeleteAnnotationDoer
{
	public boolean isAvailable()
	{
		if (getObjects().length == 0)
			return false;
		
		if (getSelectedObjectType() != Stress.getObjectType())
			return false;
		
		return true;
	}
	
	protected Vector<Command> doWorkBeforeDelete(BaseObject annotationToDelete) throws Exception
	{
		Vector<Command> commandsToHide = new Vector();
		ORefList diagramFactorRefs = annotationToDelete.findObjectsThatReferToUs(DiagramFactor.getObjectType());
		for (int index = 0; index < diagramFactorRefs.size(); ++index)
		{
			DiagramFactor diagramFactor = DiagramFactor.find(getProject(), diagramFactorRefs.get(index));
			ORefList conceptualModelRefs = diagramFactor.findObjectsThatReferToUs(ConceptualModelDiagram.getObjectType());
			for (int diagramRefIndex = 0; diagramRefIndex < conceptualModelRefs.size(); ++diagramRefIndex)
			{
				ConceptualModelDiagram conceptualModel = ConceptualModelDiagram.find(getProject(), conceptualModelRefs.get(diagramRefIndex));
				commandsToHide.addAll(HideStressBubbleDoer.createCommandsToHideStressDiagramFactor(conceptualModel, diagramFactor));
			}
		}
		
		//FIXME this is temporarly here,  until the caller to this method is changed in the next commit
		getProject().executeCommandsWithoutTransaction(commandsToHide);
		
		return commandsToHide;
	}
	
	protected BaseObject getParent(BaseObject annotationToDelete)
	{
		return getSingleSelected(Target.getObjectType());  
	}

	public String[] getDialogText()
	{
		return new String[] { "Are you sure you want to delete this Stress?",};
	}

	public String getAnnotationIdListTag()
	{
		return Target.TAG_STRESS_REFS;
	}
	
	public int getAnnotationType()
	{
		return Stress.getObjectType();
	}
}
