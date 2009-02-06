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

import org.martus.util.MultiCalendar;
import org.miradi.ids.BaseId;
import org.miradi.objectdata.ChoiceData;
import org.miradi.objectdata.DateData;
import org.miradi.objectdata.StringData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.questions.StatusConfidenceQuestion;
import org.miradi.questions.StatusQuestion;
import org.miradi.questions.TrendQuestion;
import org.miradi.utils.EnhancedJsonObject;

public class Measurement extends BaseObject
{
	public Measurement(ObjectManager objectManager, BaseId idToUse)
	{
		super(objectManager, idToUse);
		clear();
	}
	
	public Measurement(ObjectManager objectManager, int idAsInt, EnhancedJsonObject json) throws Exception
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
		return ObjectType.MEASUREMENT;
	}
	
	public static boolean canOwnThisType(int type)
	{
		return false;
	}
	
	public static boolean canReferToThisType(int type)
	{
		return false;
	}
	
	public MultiCalendar getDate()
	{
		return date.getDate();
	}
	
	public String getStatus()
	{
		return status.get();
	}
	
	public String getSummary()
	{
		return summary.get();
	}
	
	public String toString()
	{
		return date.toString();
	}
	
	public static boolean is(BaseObject object)
	{
		if(object == null)
			return false;
		return is(object.getRef());
	}
	
	public static boolean is(ORef ref)
	{
		return is(ref.getObjectType());
	}
	
	public static boolean is(int objectType)
	{
		return objectType == getObjectType();
	}
	
	public static Measurement find(ObjectManager objectManager, ORef measurementRef)
	{
		return (Measurement) objectManager.findObject(measurementRef);
	}
	
	public static Measurement find(Project project, ORef measurementRef)
	{
		return find(project.getObjectManager(), measurementRef);
	}
	
	public void clear()
	{
		super.clear();
		
		trend= new ChoiceData(TAG_TREND, getQuestion(TrendQuestion.class));
		status= new ChoiceData(TAG_STATUS, getQuestion(StatusQuestion.class));
		date= new DateData(TAG_DATE);
		summary= new StringData(TAG_SUMMARY);
		detail= new StringData(TAG_DETAIL);
		statusConfidence = new ChoiceData(TAG_STATUS_CONFIDENCE, getQuestion(StatusConfidenceQuestion.class));
		comment = new StringData(TAG_COMMENT);

		addField(TAG_TREND, trend);
		addField(TAG_STATUS, status);
		addField(TAG_DATE, date);
		addField(TAG_SUMMARY, summary);
		addField(TAG_DETAIL, detail);
		addField(TAG_STATUS_CONFIDENCE, statusConfidence);
		addField(TAG_COMMENT, comment);
	}
	
	public static final String OBJECT_NAME = "Measurement";
	
	public static final String TAG_TREND = "Trend";
	public static final String TAG_STATUS  = "Status";
	public static final String TAG_DATE = "Date";
	public static final String TAG_SUMMARY = "Summary";
	public static final String TAG_DETAIL = "Detail";
	public static final String TAG_STATUS_CONFIDENCE = "StatusConfidence";
	public static final String TAG_COMMENT = "Comment";

	public static final String META_COLUMN_TAG = "MeasurementMetaColumnTag";

	private ChoiceData trend;
	private ChoiceData status;
	private DateData date;
	private StringData summary;
	private StringData detail;
	private ChoiceData statusConfidence;
	private StringData comment;
}
