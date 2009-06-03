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


import org.miradi.ids.FactorId;
import org.miradi.objectdata.ORefListData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.utils.EnhancedJsonObject;


public class Target extends AbstractTarget
{
	public Target(ObjectManager objectManager, FactorId idToUse)
	{
		super(objectManager, idToUse, Factor.TYPE_TARGET);
		clear();
	}
	
	public Target(ObjectManager objectManager, FactorId idToUse, EnhancedJsonObject json) throws Exception
	{
		super(objectManager, idToUse, Factor.TYPE_TARGET, json);
	}
	
	@Override
	public boolean isTarget()
	{
		return true;
	}
	
	public ORefList getStressRefs()
	{
		return stressRefs.getORefList();
	}
	
	@Override
	public boolean isRefList(String tag)
	{
		if (tag.equals(TAG_STRESS_REFS))
			return true;
		
		return super.isRefList(tag);
	}

	@Override
	public int getAnnotationType(String tag)
	{
		if (tag.equals(TAG_STRESS_REFS))
			return Stress.getObjectType();
	
		return super.getAnnotationType(tag);
	}
	
	@Override
	public ORefList getAllObjectsToDeepCopy(ORefList deepCopiedFactorRefs)
	{
		ORefList deepObjectRefsToCopy = super.getAllObjectsToDeepCopy(deepCopiedFactorRefs);
		deepObjectRefsToCopy.addAll(getStressRefs());
		
		return deepObjectRefsToCopy;
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

	public static int getObjectType()
	{
		return ObjectType.TARGET;
	}
	
	public static Target find(ObjectManager objectManager, ORef targetRef)
	{
		return (Target) objectManager.findObject(targetRef);
	}
	
	public static Target find(Project project, ORef targetRef)
	{
		return find(project.getObjectManager(), targetRef);
	}

	public static boolean is(BaseObject baseObject)
	{
		return is(baseObject.getType());
	}
	
	public static boolean is(int objectType)
	{
		return objectType == getObjectType();
	}
		
	public static boolean is(ORef ref)
	{
		return is(ref.getObjectType());
	}
	
	@Override
	protected void clear()
	{
		super.clear();
		stressRefs = new ORefListData(TAG_STRESS_REFS);
		
		addField(TAG_STRESS_REFS, stressRefs);
	}

	public static final String OBJECT_NAME = "Target";
	
	public static final String TAG_STRESS_REFS = "StressRefs";
	
	private ORefListData stressRefs;
}
