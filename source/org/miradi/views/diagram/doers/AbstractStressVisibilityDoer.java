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
package org.miradi.views.diagram.doers;

import org.miradi.objecthelpers.ORef;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.ResultsChainDiagram;
import org.miradi.objects.Stress;
import org.miradi.objects.Target;

public abstract class AbstractStressVisibilityDoer extends AbstractVisibilityDoer
{
	@Override
	public boolean isAvailable()
	{
		boolean superIsAvailable = super.isAvailable();
		if (!superIsAvailable)
			return false;
		
		if (getDiagramView().isStategyBrainstormMode())
			return false;
		
		DiagramObject currentDiagramObject = getDiagramView().getDiagramModel().getDiagramObject();
		if (ResultsChainDiagram.is(currentDiagramObject.getType()))
			return false;
		
		ORef selectedStressRef = getSelectedStressRef();
		if (selectedStressRef.isInvalid())
			return false;
		
		return isAvailable(selectedStressRef);
	}
	
	protected ORef getSelectedStressRef()
	{
		return getSelectedRefOfType(Stress.getObjectType());
	}

	protected ORef getSelectedTargetRef()
	{
		return getSelectedRefOfType(Target.getObjectType());
	}
}
