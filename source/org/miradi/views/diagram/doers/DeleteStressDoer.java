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

import org.miradi.main.EAM;
import org.miradi.objects.BaseObject;
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
	
	protected BaseObject getParent(BaseObject annotationToDelete)
	{
		return getSingleSelected(Target.getObjectType());  
	}

	public String[] getDialogText()
	{
		return new String[] { EAM.text("Are you sure you want to delete this Stress?"),};
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
