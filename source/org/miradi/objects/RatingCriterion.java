/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.objects;

import org.miradi.ids.BaseId;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.utils.EnhancedJsonObject;

public class RatingCriterion extends BaseObject
{
	public RatingCriterion(ObjectManager objectManager, BaseId idToUse)
	{
		super(objectManager, idToUse);
	}
	
	public RatingCriterion(ObjectManager objectManager, int idAsInt, EnhancedJsonObject json) throws Exception
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
		return ObjectType.RATING_CRITERION;
	}
	
	
	public static boolean canOwnThisType(int type)
	{
		return false;
	}
	
	
	public static boolean canReferToThisType(int type)
	{
		return false;
	}
	
	public EnhancedJsonObject toJson()
	{
		EnhancedJsonObject json = super.toJson();

		return json;
	}

	static final String OBJECT_NAME = "RatingCriterion";

}
