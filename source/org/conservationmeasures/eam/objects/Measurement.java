package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objectdata.ChoiceData;
import org.conservationmeasures.eam.objectdata.DateData;
import org.conservationmeasures.eam.objectdata.StringData;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.ObjectManager;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

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
	
	public String toString()
	{
		return getLabel();
	}
	
	public void clear()
	{
		super.clear();
		
		measurementTrend= new ChoiceData();
		measurementStatus= new ChoiceData();
		measurementDate= new DateData();;
		measurementSummary= new StringData();
		measurementDetail= new StringData();
		measurementStatusConfidence = new ChoiceData();		

		addField(TAG_MEASUREMENT_TREND, measurementTrend);
		addField(TAG_MEASUREMENT_STATUS, measurementStatus);
		addField(TAG_MEASUREMENT_DATE, measurementDate);
		addField(TAG_MEASUREMENT_SUMMARY, measurementSummary);
		addField(TAG_MEASUREMENT_DETAIL, measurementDetail);
		addField(TAG_MEASUREMENT_STATUS_CONFIDENCE, measurementStatusConfidence);
	}
	
	public static final String OBJECT_NAME = "Measurement";
	
	public static final String TAG_MEASUREMENT_TREND = "MeasurementTrend";
	public static final String TAG_MEASUREMENT_STATUS  = "MeasurementStatus";
	public static final String TAG_MEASUREMENT_DATE = "MeasurementDate";
	public static final String TAG_MEASUREMENT_SUMMARY = "MeasurementSummary";
	public static final String TAG_MEASUREMENT_DETAIL = "MeasurementDetail";
	public static final String TAG_MEASUREMENT_STATUS_CONFIDENCE = "MeasurementStatusConfidence";

	private ChoiceData measurementTrend;
	private ChoiceData measurementStatus;
	private DateData measurementDate;
	private StringData measurementSummary;
	private StringData measurementDetail;
	private ChoiceData measurementStatusConfidence;
}
