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
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.Factor;
import org.miradi.objects.Strategy;
import org.miradi.objects.Task;

public class ShowActityBubbleDoer extends AbstractActivityVisibilityDoer
{
	protected void doWork() throws Exception
	{
		showBubble(DiagramFactor.DEFAULT_ACTIVITY_SIZE);
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

	@Override
	protected ORef getSelectedAnnotationRef()
	{
		return getSelectedActivityRef();
	}
	
	@Override
	protected ORef getParentRef()
	{
		return getSelectedStrategyRef();
	}

	@Override
	protected ORefList getAnnotationList()
	{
		return ((Strategy) getParent()).getActivityRefs();
	}
}
