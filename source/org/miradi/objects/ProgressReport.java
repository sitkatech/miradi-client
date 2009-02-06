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
package org.miradi.objects;

import org.miradi.ids.BaseId;
import org.miradi.objectdata.ChoiceData;
import org.miradi.objectdata.DateData;
import org.miradi.objectdata.StringData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ProgressReportStatusQuestion;
import org.miradi.utils.EnhancedJsonObject;

public class ProgressReport extends BaseObject
{
	public ProgressReport(ObjectManager objectManager, BaseId idToUse)
	{
		super(objectManager, idToUse);
		clear();
	}
		
	public ProgressReport(ObjectManager objectManager, int idAsInt, EnhancedJsonObject json) throws Exception
	{
		super(objectManager, new BaseId(idAsInt), json);
	}
	
	public int getType()
	{
		return getObjectType();
	}
	
	public String getTypeName()
	{
		return OBJECT_NAME;
	}

	public static int getObjectType()
	{
		return ObjectType.PROGRESS_REPORT;
	}
	
	public static boolean canOwnThisType(int type)
	{
		return false;
	}
	
	public boolean canHaveIndicators()
	{
		return false;
	}

	public String getDateAsString()
	{
		return progressDate.get();
	}
	
	public ORefList getOwnedObjects(int objectType)
	{
		return new ORefList();
	}
	
	public String toString()
	{
		return getDateAsString() + " " + getLabel();
	}
	
	public String getLabel()
	{
		return getProgressStatusChoice().getLabel();	
	}
	
	public ChoiceItem getProgressStatusChoice()
	{
		return new ProgressReportStatusQuestion().findChoiceByCode(progressStatus.get());
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
	
	void clear()
	{
		super.clear();
		
		progressStatus = new ChoiceData(TAG_PROGRESS_STATUS, getQuestion(ProgressReportStatusQuestion.class));
		progressDate = new DateData(TAG_PROGRESS_DATE);
		details = new StringData(TAG_DETAILS);
		
		addField(TAG_PROGRESS_STATUS, progressStatus);
		addField(TAG_PROGRESS_DATE, progressDate);
		addField(TAG_DETAILS, details);
	}
	
	public static final String OBJECT_NAME = "ProgressReport";
	
	public static final String TAG_PROGRESS_STATUS = "ProgressStatus";
	public static final String TAG_PROGRESS_DATE = "ProgressDate";
	public static final String TAG_DETAILS = "Details";
	
	private ChoiceData progressStatus;
	private DateData progressDate;
	private StringData details;
}
