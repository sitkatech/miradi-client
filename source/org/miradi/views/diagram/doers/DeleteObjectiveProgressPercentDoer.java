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

import org.miradi.main.EAM;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Objective;
import org.miradi.objects.ProgressPercent;
import org.miradi.views.diagram.DeleteAnnotationDoer;

public class DeleteObjectiveProgressPercentDoer extends DeleteAnnotationDoer
{
	protected BaseObject getParent(BaseObject annotationToDelete)
	{
		return getSingleSelected(Objective.getObjectType());  
	}

	public String getAnnotationIdListTag()
	{
		return Objective.TAG_PROGRESS_PERCENT_REFS;
	}

	public int getAnnotationType()
	{
		return ProgressPercent.getObjectType();
	}

	public String[] getDialogText()
	{
		return new String[] { EAM.text("Are you sure you want to delete this Progress Percent?"),};
	}
}
