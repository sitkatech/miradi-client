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
import org.miradi.objectdata.StringData;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.questions.FosTrainingTypeQuestion;
import org.miradi.utils.EnhancedJsonObject;

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
		
		trainingType = new ChoiceData(TAG_TRAINING_TYPE, getQuestion(FosTrainingTypeQuestion.class));
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
