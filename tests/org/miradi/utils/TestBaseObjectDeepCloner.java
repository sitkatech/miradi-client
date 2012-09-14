/* 
Copyright 2005-2012, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.utils;

import java.util.Vector;

import org.miradi.main.TestCaseWithProject;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Indicator;
import org.miradi.objects.Strategy;
import org.miradi.objects.Target;

public class TestBaseObjectDeepCloner extends TestCaseWithProject
{
	public TestBaseObjectDeepCloner(String name)
	{
		super(name);
	}
	
	public void testTargetCopy() throws Exception
	{
		Target target = getProject().createTarget();
		verifyClone(target);
	
		getProject().populateTarget(target);
		verifyClone(target);
	}
	
	public void testStrategyCopy() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		getProject().populateStrategy(strategy);
		Indicator indicator = getProject().createIndicator(strategy);
		getProject().addResourceAssignment(indicator);
		getProject().addProgressReport(strategy);
		getProject().addProgressReport(indicator);
		verifyClone(indicator);
	}
	
	private void verifyClone(BaseObject baseObjectToClone) throws Exception
	{
		BaseObjectDeepCopier cloner = new BaseObjectDeepCopier(getProject());
		BaseObject clonedBaseObject = cloner.createDeepClone(baseObjectToClone);
		assertTrue("Cloned baseObject is not the same as actaul baseObject?", areEquals(baseObjectToClone, clonedBaseObject));
	}

	private boolean areEquals(BaseObject baseObjectToClone,	BaseObject clonedBaseObject)
	{
		Vector<String> storedFieldTags = baseObjectToClone.getStoredFieldTags();
		for(String tag : storedFieldTags)
		{
			if (baseObjectToClone.isRefList(tag) || baseObjectToClone.isIdListTag(tag))
			{
				final ORefList refListToBeCloned = baseObjectToClone.getSafeRefListData(tag);
				final ORefList clonedRefList = clonedBaseObject.getSafeRefListData(tag);
				if (!haveOwnedObjectsBeenCloned(refListToBeCloned, clonedRefList))
					return false;
			}
			else
			{
				String actualData = baseObjectToClone.getData(tag);
				String clonedData = clonedBaseObject.getData(tag);
				if (!actualData.equals(clonedData))
					return false; 
			}
		}
		
		return true;
	}

	private boolean haveOwnedObjectsBeenCloned(ORefList refListToBeCloned, ORefList clonedRefList)
	{
		if (refListToBeCloned.size() != clonedRefList.size())
			return false;
		
		if (refListToBeCloned.containsAnyOf(clonedRefList))
			return false;
		
		for (ORef clonedOwnedBaseObjectRef : clonedRefList)
		{
			BaseObject clonedBaseObjectRef = BaseObject.find(getProject(), clonedOwnedBaseObjectRef);
			if (clonedBaseObjectRef == null)
				return false;
		}
		
		return true;
	}
}
