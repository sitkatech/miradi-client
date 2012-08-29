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

//FIXME urgent, this test deep cloner class is still incomplete.  We will be
//fixing the owned object bug before we finish this
public class TestBaseObjectDeepCloner extends TestCaseWithProject
{
	public TestBaseObjectDeepCloner(String name)
	{
		super(name);
	}
	
	public void testNonOwningFields() throws Exception
	{
		fail();
		Strategy strategy = getProject().createStrategy();
		Indicator indicator = getProject().createIndicator(strategy);
		verifyClone(indicator);
	}
	
	private void verifyClone(BaseObject baseObjectToClone)
	{
		BaseObjectDeepCloner cloner = new BaseObjectDeepCloner(getProject());
		ORef clonedObjectRef = cloner.createDeepClone(baseObjectToClone);
		BaseObject clonedBaseObject = BaseObject.find(getProject(), clonedObjectRef);
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
			else if (baseObjectToClone.getField(tag).isRefData())
			{
				//Compare the two
			}
			else if (baseObjectToClone.getField(tag).isBaseIdData())
			{
				//Compare the two
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
		return false;
	}
}
