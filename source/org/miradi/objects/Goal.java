/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.objects;

import org.miradi.ids.BaseId;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.utils.EnhancedJsonObject;


public class Goal extends Desire 
{
	public Goal(ObjectManager objectManager, BaseId idToUse)
	{
		super(objectManager, idToUse);
	}
	
	public Goal(ObjectManager objectManager, int idAsInt, EnhancedJsonObject json) throws Exception
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
		return ObjectType.GOAL;
	}
	
	
	public static boolean canOwnThisType(int type)
	{
		return false;
	}
	
	
	public static boolean canReferToThisType(int type)
	{
		return false;
	}
	
	public static boolean is(ORef ref)
	{
		return is(ref.getObjectType());
	}
	
	public static boolean is(int objectType)
	{
		return objectType == getObjectType();
	}
	
	public static Goal find(ObjectManager objectManager, ORef goalRef)
	{
		return (Goal) objectManager.findObject(goalRef);
	}
	
	public static Goal find(Project project, ORef goalRef)
	{
		return find(project.getObjectManager(), goalRef);
	}
	
	public static final String OBJECT_NAME = "Goal";
}
