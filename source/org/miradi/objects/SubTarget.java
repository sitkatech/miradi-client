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
	
	@Override
	public int getType()
	{
		return getObjectType();
	}
	
	@Override
	public String getTypeName()
	{
		return OBJECT_NAME;
	}

	@Override
	public int[] getTypesThatCanOwnUs()
	{
		return new int[] {
			Target.getObjectType(),
			HumanWelfareTarget.getObjectType(),
			};
	}
	
	public static int getObjectType()
	{
		return ObjectType.SUB_TARGET;
	}
	
	public boolean canHaveIndicators()
	{
		return false;
	}

	@Override
	public ORefList getOwnedObjects(int objectType)
	{
		return new ORefList();
	}
	
	@Override
	public String getShortLabel()
	{
		return getData(TAG_SHORT_LABEL);
	}
	
	@Override
	public String toString()
	{
		return getFullName();
	}
	
	public static boolean is(ORef ref)
	{
		return is(ref.getObjectType());
	}
	
	public static boolean is(int objectType)
	{
		return objectType == getObjectType();
	}
	
	public static boolean is(BaseObject object)
	{
		return is(object.getType());
	}
	
	public static SubTarget find(ObjectManager objectManager, ORef subTargetRef)
	{
		return (SubTarget) objectManager.findObject(subTargetRef);
	}
	
	public static SubTarget find(Project project, ORef subTargetRef)
	{
		return find(project.getObjectManager(), subTargetRef);
	}
	
	@Override
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
