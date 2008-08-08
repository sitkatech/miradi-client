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
package org.miradi.rtf;

import org.miradi.forms.FieldPanelSpec;
import org.miradi.forms.objects.ActivityPropertiesForm;
import org.miradi.forms.objects.MeasurementPropertiesForm;
import org.miradi.forms.objects.TargetPropertiesForm;
import org.miradi.objects.AccountingCode;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Target;
import org.miradi.objects.Task;

public class ObjectToFormMap
{
	//FIXME finish this mapper
	public static FieldPanelSpec getForm(BaseObject baseObject)
	{
		int objectType = baseObject.getType();
		if (Target.is(objectType))
			return new TargetPropertiesForm();
		
		if (AccountingCode.is(objectType))
			return new TargetPropertiesForm();

		if (Task.is(objectType))
			return getTaskForm((Task) baseObject);
		
		throw new RuntimeException("Form not found for type:" + objectType);
	}

	private static FieldPanelSpec getTaskForm(Task task)
	{
		if (task.isActivity())
			return new ActivityPropertiesForm();
		
		if (task.isMethod())
			return new MeasurementPropertiesForm();
		
		//FIXME needs to return task form
		return null;
	}
}
