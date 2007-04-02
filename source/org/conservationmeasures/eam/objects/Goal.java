/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.ObjectManager;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;


public class Goal extends Desire 
{
	public Goal(ObjectManager objectManager, BaseId idToUse)
	{
		super(objectManager, idToUse);
	}
	
	public Goal(BaseId idToUse)
	{
		super(idToUse);
	}
	
	public Goal(ObjectManager objectManager, int idAsInt, EnhancedJsonObject json) throws Exception
	{
		super(objectManager, new BaseId(idAsInt), json);
	}
	
	public Goal(int idAsInt, EnhancedJsonObject json) throws Exception
	{
		super(new BaseId(idAsInt), json);
	}
	
	public int getType()
	{
		return getObjectType();
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

	public static final String OBJECT_NAME = "Goal";
}
