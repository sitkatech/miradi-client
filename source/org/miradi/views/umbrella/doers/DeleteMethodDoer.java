/* 
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Indicator;
import org.miradi.objects.Method;
import org.miradi.schemas.MethodSchema;
import org.miradi.views.diagram.DeleteAnnotationDoer;

public class DeleteMethodDoer extends DeleteAnnotationDoer
{
	@Override
	public boolean isAvailable()
	{
		if(getSelectedMethod() == null)
			return false;

		return true;
	}

	@Override
	protected void doIt() throws Exception
	{
		if(!isAvailable())
			return;

		super.doIt();
	}

	@Override
	protected BaseObject getParent(BaseObject annotationToDelete)
	{
		return getReferrerParent(annotationToDelete);
	}

	@Override
	public String getAnnotationIdListTag()
	{
		return Indicator.TAG_METHOD_IDS;
	}

	@Override
	public int getAnnotationType()
	{
		return MethodSchema.getObjectType();
	}

	@Override
	public String[] getDialogText()
	{
		return new String[] { EAM.text("Are you sure you want to delete this Method?"),};
	}

	private Method getSelectedMethod()
	{
		if(getSelectedHierarchies().length != 1)
			return null;

		ORef methodRef = getSelectedHierarchies()[0].getRefForType(MethodSchema.getObjectType());
		if(methodRef == null || methodRef.isInvalid())
			return null;

		return Method.find(getProject(), methodRef);
	}
}
