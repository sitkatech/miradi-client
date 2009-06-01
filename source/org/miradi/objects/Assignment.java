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
import org.miradi.objectdata.DateUnitEffortListData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.TimePeriodCosts;
import org.miradi.objecthelpers.TimePeriodCostsMap;
import org.miradi.project.ObjectManager;
import org.miradi.utils.DateUnitEffort;
import org.miradi.utils.DateUnitEffortList;
import org.miradi.utils.EnhancedJsonObject;
import org.miradi.utils.OptionalDouble;

abstract public class Assignment extends BaseObject
{
	public Assignment(ObjectManager objectManagerToUse, BaseId idToUse)
	{
		super(objectManagerToUse, idToUse);
	}
	
	public Assignment(ObjectManager objectManager, BaseId idToUse, EnhancedJsonObject json) throws Exception
	{
		super(objectManager, idToUse, json);
	}
	
	public DateUnitEffortList getDateUnitEffortList() throws Exception
	{
		return new DateUnitEffortList(getData(TAG_DATEUNIT_EFFORTS));
	}
	
	@Override
	protected TimePeriodCostsMap getTimePeriodCostsMap(String tag) throws Exception
	{
		return convertDateUnitEffortList();
	}
	
	public static boolean isAssignment(BaseObject baseObject)
	{
		return isAssignment(baseObject.getType());
	}
	
	public static boolean is(ORef ref)
	{
		return isAssignment(ref.getObjectType());
	}
	
	public static boolean isAssignment(int objectType)
	{
		if (ResourceAssignment.is(objectType))
			return true;
		
		return ExpenseAssignment.is(objectType);
	}
	
	@Override
	public void clear()
	{
		super.clear();
		detailListData = new DateUnitEffortListData(TAG_DATEUNIT_EFFORTS);
		
		addField(TAG_DATEUNIT_EFFORTS, detailListData);
	}
	
	protected TimePeriodCostsMap convertDateUnitEffortList() throws Exception
	{
		TimePeriodCostsMap tpcm = new TimePeriodCostsMap();
		DateUnitEffortList duel = getDateUnitEffortList();
		for (int index = 0; index < duel.size(); ++index)
		{
			DateUnitEffort dateUnitEffort = duel.getDateUnitEffort(index);
			TimePeriodCosts timePeriodCostsToUpdate = new TimePeriodCosts();
			updateTimePeriodCosts(timePeriodCostsToUpdate, new OptionalDouble(dateUnitEffort.getQuantity()));
			tpcm.add(dateUnitEffort.getDateUnit(), timePeriodCostsToUpdate);
		}
		
		return tpcm;	
	}
	
	abstract protected void updateTimePeriodCosts(TimePeriodCosts timePeriodCosts, OptionalDouble quantity);
	
	public static final String TAG_DATEUNIT_EFFORTS = "Details";
	
	private DateUnitEffortListData detailListData;
}
