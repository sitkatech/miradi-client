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

import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.Stress;
import org.miradi.views.ObjectsDoer;

public abstract class AbstractStressVisibilityDoer extends ObjectsDoer
{
	protected ORef getSelectedStress()
	{
		ORefList[] selectedHierarchies = getSelectedHierarchies();
		if (selectedHierarchies.length != 1)
			return ORef.INVALID;
		
		ORefList selectedHierarchy = selectedHierarchies[0];
		return selectedHierarchy.getRefForType(Stress.getObjectType());
	}
	
	protected boolean isShowing(ORef stressRef)
	{
		return getDiagramFactorReferrerRefs(stressRef).size() > 0;
	}

	protected ORefList getDiagramFactorReferrerRefs(ORef stressRef)
	{
		Stress stress = Stress.find(getProject(), stressRef);
		ORefList diagramFactorReferrers = stress.findObjectsThatReferToUs(DiagramFactor.getObjectType());
		return diagramFactorReferrers;
	}
}
