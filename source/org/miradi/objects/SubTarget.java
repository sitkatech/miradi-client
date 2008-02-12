/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.objects;

import org.miradi.ids.BaseId;
import org.miradi.objectdata.StringData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.utils.EnhancedJsonObject;

public class SubTarget extends BaseObject
{
	public SubTarget(ObjectManager objectManager, BaseId idToUse)
	{
		super(objectManager, idToUse);
		clear();
	}
		
	public SubTarget(ObjectManager objectManager, int idAsInt, EnhancedJsonObject json) throws Exception
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
		return ObjectType.SUB_TARGET;
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
		if (ref.getObjectType() == ObjectType.SUB_TARGET)
			return true;
		
		return false;
	}
	
	public static SubTarget find(ObjectManager objectManager, ORef subTargetRef)
	{
		return (SubTarget) objectManager.findObject(subTargetRef);
	}
	
	public static SubTarget find(Project project, ORef subTargetRef)
	{
		return find(project.getObjectManager(), subTargetRef);
	}
	
	void clear()
	{
		super.clear();
		
		shortLabel = new StringData(TAG_SHORT_LABEL);
		details = new StringData(TAG_DETAIL);
		
		addField(TAG_SHORT_LABEL, shortLabel);
		addField(TAG_DETAIL, details);
	}
	
	public static final String OBJECT_NAME = "SubTarget";
	
	public static final String TAG_SHORT_LABEL = "ShortLabel";
	public static final String TAG_DETAIL = "Detail";
	
	private StringData shortLabel;
	private StringData details;
}
