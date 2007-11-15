/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objectdata.StringData;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.ObjectManager;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

public class Stress extends BaseObject
{
	public Stress(ObjectManager objectManager, BaseId idToUse)
	{
		super(objectManager, idToUse);
	}
	
	public Stress(BaseId idToUse)
	{
		super(idToUse);
	}
	
	public Stress(ObjectManager objectManager, int idAsInt, EnhancedJsonObject json) throws Exception
	{
		super(objectManager, new BaseId(idAsInt), json);
	}

	public Stress(int idAsInt, EnhancedJsonObject json) throws Exception
	{
		super(new BaseId(idAsInt), json);
	}

	public int getType()
	{
		return getObjectType();
	}
	
	public static int getObjectType()
	{
		return ObjectType.STRESS;
	}
	
	public String getTypeName()
	{
		return OBJECT_NAME;
	}

	public String toString()
	{
		return getLabel();
	}
	
	public void clear()
	{
		super.clear();
		
		shortLabel = new StringData();
		
		addField(TAG_SHORT_LABEL, shortLabel);
	}
	
	public static final String TAG_SHORT_LABEL = "ShortLabel";
	
	private StringData shortLabel;
	
	public static final String OBJECT_NAME = "Stress";
}
