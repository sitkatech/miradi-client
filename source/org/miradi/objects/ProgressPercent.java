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
import org.miradi.objectdata.DateData;
import org.miradi.objectdata.NumberData;
import org.miradi.objectdata.StringData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.utils.EnhancedJsonObject;

public class ProgressPercent extends BaseObject
{
	public ProgressPercent(ObjectManager objectManager, BaseId idToUse)
	{
		super(objectManager, idToUse);
		clear();
	}
		
	public ProgressPercent(ObjectManager objectManager, int idAsInt, EnhancedJsonObject json) throws Exception
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
		return ObjectType.PROGRESS_PERCENT;
	}
	
	//FIXME medium: verify this is correct label
	public String getLabel()
	{
		return getData(TAG_DATE);	
	}
	
	public static boolean is(ORef ref)
	{
		return is(ref.getObjectType());
	}
	
	public static boolean is(int objectType)
	{
		return objectType == getObjectType();
	}
	
	public static ProgressPercent find(ObjectManager objectManager, ORef progressPercentRef)
	{
		return (ProgressPercent) objectManager.findObject(progressPercentRef);
	}
	
	public static ProgressPercent find(Project project, ORef progressPercentRef)
	{
		return find(project.getObjectManager(), progressPercentRef);
	}
	
	void clear()
	{
		super.clear();
		
		date = new DateData(TAG_DATE);
		percentComplete = new NumberData(TAG_PERCENT_COMPLETE);
		percentCompleteNotes = new StringData(TAG_PERCENT_COMPLETE_NOTES);
		
		addField(TAG_DATE, date);
		addField(TAG_PERCENT_COMPLETE, percentComplete);
		addField(TAG_PERCENT_COMPLETE_NOTES, percentCompleteNotes);
	}
	
	public static final String TAG_DATE = "PercentDate";
	public static final String TAG_PERCENT_COMPLETE = "PercentComplete";
	public static final String TAG_PERCENT_COMPLETE_NOTES = "PercentCompleteNotes";
	
	private DateData date;
	private NumberData percentComplete;
	private StringData percentCompleteNotes;

	public static final String OBJECT_NAME = "ProgressPercent";
}
