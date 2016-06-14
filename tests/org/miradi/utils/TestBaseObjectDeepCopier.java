/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

public class TestBaseObjectDeepCopier extends TestCaseWithProject
{
	public TestBaseObjectDeepCopier(String name)
	{
		super(name);
	}
	
	public void testTargetCopy() throws Exception
	{
		Target target = getProject().createTarget();
		verifyCopy(target);
	
		getProject().populateTarget(target);
		verifyCopy(target);
	}
	
	public void testStrategyCopy() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		getProject().populateStrategy(strategy);
		Indicator indicator = getProject().createIndicator(strategy);
		getProject().addProgressReport(strategy);
		getProject().addProgressReport(indicator);
		verifyCopy(indicator);
	}
	
	private void verifyCopy(BaseObject baseObjectToCopier) throws Exception
	{
		verifyUsingDeepCopier(new BaseObjectDeepCopierNotUsingCommands(getProject()), baseObjectToCopier);
		verifyUsingDeepCopier(new BaseObjectDeepCopierUsingCommands(getProject()), baseObjectToCopier);
	}

	public void verifyUsingDeepCopier(BaseObjectDeepCopier copier, BaseObject baseObjectToCopier) throws Exception
	{
		BaseObject copiedBaseObject = copier.createDeepCopier(baseObjectToCopier);
		assertTrue("Cloned baseObject is not the same as actaul baseObject?", areEquals(baseObjectToCopier, copiedBaseObject));
	}

	private boolean areEquals(BaseObject baseObjectToCopy,	BaseObject copiedBaseObject)
	{
		Vector<String> storedFieldTags = baseObjectToCopy.getStoredFieldTags();
		for(String tag : storedFieldTags)
		{
			if (baseObjectToCopy.isRefList(tag) || baseObjectToCopy.isIdListTag(tag))
			{
				final ORefList refListToBeCloned = baseObjectToCopy.getSafeRefListData(tag);
				final ORefList clonedRefList = copiedBaseObject.getSafeRefListData(tag);
				if (!haveOwnedObjectsBeenCopied(refListToBeCloned, clonedRefList))
					return false;
			}
			else
			{
				String actualData = baseObjectToCopy.getData(tag);
				String copiedData = copiedBaseObject.getData(tag);
				if (!actualData.equals(copiedData))
					return false; 
			}
		}
		
		return true;
	}

	private boolean haveOwnedObjectsBeenCopied(ORefList refListToBeCloned, ORefList clonedRefList)
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
