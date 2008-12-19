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
package org.miradi.views.umbrella.doers;

import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Desire;
import org.miradi.objects.ProgressPercent;
import org.miradi.views.diagram.CreateAnnotationDoer;

abstract public class AbstractCreateProgressPercent extends CreateAnnotationDoer
{
	public BaseObject getSelectedParentFactor()
	{
		if (getPicker() == null)
			return null;
		
		ORefList selectionRefs = getPicker().getSelectedHierarchies()[0];
		ORef parentRef = selectionRefs.getRefForType(getParentType());
		
		return Desire.findDesire(getProject(), parentRef);
	}
	
	public String getAnnotationListTag()
	{
		return Desire.TAG_PROGRESS_PERCENT_REFS;
	}

	public int getAnnotationType()
	{
		return ProgressPercent.getObjectType();
	}
	
	abstract protected int getParentType();
}
