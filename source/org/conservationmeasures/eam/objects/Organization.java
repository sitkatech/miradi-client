/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objectdata.StringData;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.ObjectManager;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

public class Organization extends BaseObject
{
	public Organization(ObjectManager objectManager, BaseId idToUse)
	{
		super(objectManager, idToUse);
		clear();
	}
		
	public Organization(ObjectManager objectManager, int idAsInt, EnhancedJsonObject json) throws Exception
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
		return ObjectType.ORGANIZATION;
	}
	
	public static boolean canOwnThisType(int type)
	{
		return false;
	}
	
	public boolean canHaveIndicators()
	{
		return false;
	}

	public ORefList getOwnedObjects(int objectType)
	{
		return new ORefList();
	}
	
	public String getShortLabel()
	{
		return getData(TAG_SHORT_LABEL);
	}
	
	public static boolean is(ORef ref)
	{
		if (ref.getObjectType() == ObjectType.ORGANIZATION)
			return true;
		
		return false;
	}
	
	public static Organization find(ObjectManager objectManager, ORef organizationRef)
	{
		return (Organization) objectManager.findObject(organizationRef);
	}
	
	public static Organization find(Project project, ORef organizationRef)
	{
		return find(project.getObjectManager(), organizationRef);
	}
	
	void clear()
	{
		super.clear();
		
		shortLabel = new StringData(TAG_SHORT_LABEL);
		
		addField(TAG_SHORT_LABEL, shortLabel);
	}
	
	public static final String OBJECT_NAME = "Organization";
	
	public static final String TAG_SHORT_LABEL = "ShortLabel";
	
	private StringData shortLabel;
}
