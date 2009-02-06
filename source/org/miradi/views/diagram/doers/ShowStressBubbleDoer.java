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
import org.miradi.objects.Stress;
import org.miradi.objects.Target;

public class ShowStressBubbleDoer extends AbstractStressVisibilityDoer
{
	protected void doWork() throws Exception
	{
		showBubble(DiagramFactor.DEFAULT_STRESS_SIZE);
	}

	protected boolean isAvailable(ORef selectedStressRef)
	{
		return !isShowing(selectedStressRef);
	}
	
	protected Factor getFactor(ORef factorRef)
	{
		return Stress.find(getProject(), factorRef);
	}
	
	@Override
	protected ORef getSelectedAnnotationRef()
	{
		return getSelectedStressRef();
	}

	@Override
	protected ORef getParentRef()
	{
		return getSelectedTargetRef();
	}
	
	protected ORefList getAnnotationList()
	{
		return ((Target) getParent()).getStressRefs();
	}
}
