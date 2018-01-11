/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
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
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.schemas.FosProjectDataSchema;

public class FosProjectData extends BaseObject
{
	public FosProjectData(ObjectManager objectManager, BaseId id)
	{
		super(objectManager, id, createSchema(objectManager));
	}

	public static FosProjectDataSchema createSchema(Project projectToUse)
	{
		return createSchema(projectToUse.getObjectManager());
	}

	public static FosProjectDataSchema createSchema(ObjectManager objectManager)
	{
		return (FosProjectDataSchema) objectManager.getSchemas().get(ObjectType.FOS_PROJECT_DATA);
	}

	@Override
	public int[] getTypesThatCanOwnUs()
	{
		return NO_OWNERS;
	}
	
	public static boolean canOwnThisType(int type)
	{
		return false;
	}
	
	public static boolean canReferToThisType(int type)
	{
		return false;
	}
	
	public static FosProjectData find(ObjectManager objectManager, ORef fosProjectDataRef)
	{
		return (FosProjectData) objectManager.findObject(fosProjectDataRef);
	}
	
	public static FosProjectData find(Project project, ORef fosProjectDataRef)
	{
		return find(project.getObjectManager(), fosProjectDataRef);
	}
	
	public static final String TAG_TRAINING_TYPE = "TrainingType";
	public static final String TAG_TRAINING_DATES = "TrainingDates";
	public static final String TAG_TRAINERS = "Trainers";
	public static final String TAG_COACHES = "Coaches";
}
