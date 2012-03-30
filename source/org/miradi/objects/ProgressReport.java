/* 
Copyright 2005-2012, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.objects;

import org.miradi.ids.BaseId;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.schemas.IndicatorSchema;
import org.miradi.schemas.ProgressReportSchema;
import org.miradi.schemas.StrategySchema;
import org.miradi.utils.EnhancedJsonObject;

public class ProgressReport extends BaseObject
{
	public ProgressReport(ObjectManager objectManager, BaseId idToUse)
	{
		super(objectManager, idToUse, new ProgressReportSchema());
	}
		
	public ProgressReport(ObjectManager objectManager, int idAsInt, EnhancedJsonObject json) throws Exception
	{
		super(objectManager, new BaseId(idAsInt), json, new ProgressReportSchema());
	}
	
	@Override
	public int getType()
	{
		return getObjectType();
	}
	
	@Override
	public String getTypeName()
	{
		return OBJECT_NAME;
	}

	@Override
	public int[] getTypesThatCanOwnUs()
	{
		return new int[] {
			StrategySchema.getObjectType(),
			IndicatorSchema.getObjectType(),
			Task.getObjectType(),
		};
	}
	
	public static int getObjectType()
	{
		return ObjectType.PROGRESS_REPORT;
	}
	
	public boolean canHaveIndicators()
	{
		return false;
	}

	public String getDateAsString()
	{
		return getData(TAG_PROGRESS_DATE);
	}
	
	@Override
	public ORefList getOwnedObjects(int objectType)
	{
		return new ORefList();
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
		return is(ref.getObjectType());
	}

	public static boolean is(int objectType)
	{
		return (objectType == getObjectType());
	}
	
	public static ProgressReport find(ObjectManager objectManager, ORef progressReportRef)
	{
		return (ProgressReport) objectManager.findObject(progressReportRef);
	}
	
	public static ProgressReport find(Project project, ORef progressReportRef)
	{
		return find(project.getObjectManager(), progressReportRef);
	}
	
	public static final String OBJECT_NAME = "ProgressReport";
	
	public static final String TAG_PROGRESS_STATUS = "ProgressStatus";
	public static final String TAG_PROGRESS_DATE = "ProgressDate";
	public static final String TAG_DETAILS = "Details";
}
