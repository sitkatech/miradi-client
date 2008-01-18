/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objectdata.ChoiceData;
import org.conservationmeasures.eam.objectdata.DateData;
import org.conservationmeasures.eam.objectdata.StringData;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.ObjectManager;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;
import org.martus.util.MultiCalendar;

public class Measurement extends BaseObject
{
	public Measurement(ObjectManager objectManager, BaseId idToUse)
	{
		super(objectManager, idToUse);
		clear();
	}
	
	public Measurement(BaseId idToUse)
	{
		super(idToUse);
		clear();
	}
	
	public Measurement(ObjectManager objectManager, int idAsInt, EnhancedJsonObject json) throws Exception
	{
		super(objectManager, new BaseId(idAsInt), json);
	}

	
	public Measurement(int idAsInt, EnhancedJsonObject json) throws Exception
	{
		super(new BaseId(idAsInt), json);
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
	
	public String toString()
	{
		return date.toString();
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
		
		trend= new ChoiceData();
		status= new ChoiceData();
		date= new DateData();
		summary= new StringData();
		detail= new StringData();
		statusConfidence = new ChoiceData();
		comment = new StringData();

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
