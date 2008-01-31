/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objectdata.ChoiceData;
import org.conservationmeasures.eam.objectdata.StringData;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.ObjectManager;
import org.conservationmeasures.eam.questions.TrainingTypeQuestion;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

public class FosProjectData extends BaseObject
{
	public FosProjectData(ObjectManager objectManager, BaseId id)
	{
		super(objectManager, id);
		clear();
	}
	
	public FosProjectData(ObjectManager objectManager, int idAsInt, EnhancedJsonObject jsonObject) throws Exception 
	{
		super(objectManager, new BaseId(idAsInt), jsonObject);
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
		return ObjectType.FOS_PROJECT_DATA;
	}
	
	public static boolean canOwnThisType(int type)
	{
		return false;
	}
	
	public static boolean canReferToThisType(int type)
	{
		return false;
	}
	
	void clear()
	{
		super.clear();
		
		trainingType = new ChoiceData(TAG_TRAINING_TYPE, getQuestion(TrainingTypeQuestion.class));
		trainingDates = new StringData(TAG_TRAINING_DATES);
		trainers = new StringData(TAG_TRAINERS);
		coaches = new StringData(TAG_COACHES);
		
		addField(TAG_TRAINING_TYPE, trainingType);
		addField(TAG_TRAINING_DATES, trainingDates);
		addField(TAG_TRAINERS, trainers);
		addField(TAG_COACHES, coaches);
	}
	
	public static final String OBJECT_NAME = "FosProjectData";
	
	public static final String TAG_TRAINING_TYPE = "TrainingType";
	public static final String TAG_TRAINING_DATES = "TrainingDates";
	public static final String TAG_TRAINERS = "Trainers";
	public static final String TAG_COACHES = "Coaches";
	
	private ChoiceData trainingType;
	private StringData trainingDates;
	private StringData trainers;
	private StringData coaches;
}
