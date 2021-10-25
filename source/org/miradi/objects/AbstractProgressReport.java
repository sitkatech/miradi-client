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
package org.miradi.objects;

import org.miradi.ids.BaseId;
import org.miradi.objecthelpers.ORef;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.schemas.BaseObjectSchema;

abstract public class AbstractProgressReport extends BaseObject
{
	public AbstractProgressReport(ObjectManager objectManager, BaseId idToUse, final BaseObjectSchema schema)
	{
		super(objectManager, idToUse, schema);
	}

	public String getDateAsString()
	{
		return getData(TAG_PROGRESS_DATE);
	}
	
	@Override
	public String toString()
	{
		return getDateAsString() + ": " + getProgressStatusChoice().getLabel();
	}

	@Override
	public String getFullName()
	{
		return toString();
	}
	
	public ChoiceItem getProgressStatusChoice()
	{
		return getChoiceItemData(TAG_PROGRESS_STATUS);
	}
	
	public String getDetails()
	{
		return getStringData(TAG_DETAILS);
	}

	public static boolean is(ORef ref)
	{
		return isProgressReport(ref.getObjectType());
	}

	public static boolean is(BaseObject object)
	{
		if(object == null)
			return false;
		return is(object.getRef());
	}

	public static boolean isProgressReport(int objectType)
	{
		if (ProgressReport.is(objectType))
			return true;

		return ExtendedProgressReport.is(objectType);
	}

	public static AbstractProgressReport find(ObjectManager objectManager, ORef progressReportRef)
	{
		return (AbstractProgressReport) objectManager.findObject(progressReportRef);
	}
	
	public static AbstractProgressReport find(Project project, ORef progressReportRef)
	{
		return find(project.getObjectManager(), progressReportRef);
	}
	
	public static final String TAG_PROGRESS_STATUS = "ProgressStatus";
	public static final String TAG_PROGRESS_DATE = "ProgressDate";
	public static final String TAG_DETAILS = "Details";
}
