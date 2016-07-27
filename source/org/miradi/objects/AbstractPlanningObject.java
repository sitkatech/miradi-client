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
package org.miradi.objects;

import org.miradi.ids.BaseId;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.objecthelpers.TimePeriodCosts;
import org.miradi.objecthelpers.TimePeriodCostsMap;
import org.miradi.project.ObjectManager;
import org.miradi.project.ProjectCalendar;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.schemas.IndicatorSchema;
import org.miradi.schemas.StrategySchema;
import org.miradi.schemas.TaskSchema;
import org.miradi.utils.DateRange;
import org.miradi.utils.DateUnitEffort;
import org.miradi.utils.DateUnitEffortList;
import org.miradi.utils.OptionalDouble;

abstract public class AbstractPlanningObject extends BaseObject
{
	public AbstractPlanningObject(ObjectManager objectManagerToUse, BaseId idToUse, final BaseObjectSchema schema)
	{
		super(objectManagerToUse, idToUse, schema);
	}
	
	public DateUnitEffortList getDateUnitEffortList() throws Exception
	{
		return new DateUnitEffortList(getData(TAG_DATEUNIT_DETAILS));
	}
	
	@Override
	public int[] getTypesThatCanOwnUs()
	{
		return new int[] {
			StrategySchema.getObjectType(), 
			IndicatorSchema.getObjectType(),
			TaskSchema.getObjectType(),
			};
	}
	
	@Override
	protected TimePeriodCostsMap getTimePeriodCostsMap(String tag) throws Exception
	{
		return convertDateUnitEffortList();
	}
		
	protected TimePeriodCostsMap convertDateUnitEffortList() throws Exception
	{
		DateUnitEffortList duel = getDateUnitEffortList();
		TimePeriodCostsMap tpcm = new TimePeriodCostsMap();

		boolean shouldAddNoDataInPlace = (!isEmpty()) || (isEmpty() && getProjectResourceTag() != null);
		if (duel.size() == 0 && shouldAddNoDataInPlace)
			addTimePeriodCostsInPlaceForNoData(duel, tpcm);

		for (int index = 0; index < duel.size(); ++index)
		{
			DateUnitEffort dateUnitEffort = duel.getDateUnitEffort(index);
			TimePeriodCosts timePeriodCosts = createTimePeriodCosts(new OptionalDouble(dateUnitEffort.getQuantity()));
			DateUnit dateUnit = dateUnitEffort.getDateUnit();
			if (shouldIncludeEffort(dateUnit))
			{
				tpcm.add(dateUnit, timePeriodCosts);
			}
		}
		
		return tpcm;
	}

	protected boolean shouldIncludeEffort(DateUnit dateUnit) throws Exception
	{
		if (yearlyDateUnitDoesNotMatchCurrentFiscalYearStartMonth(dateUnit))
			return false;

		return isWithinProjectDateRange(dateUnit);
	}

	private boolean yearlyDateUnitDoesNotMatchCurrentFiscalYearStartMonth(DateUnit dateUnit)
	{
		if (dateUnit.isYear())
		{
			int dateUnitStartMonth = dateUnit.getYearStartMonth();
			return dateUnitStartMonth != getProjectCalendar().getFiscalYearFirstMonth();
		}
		
		return false;
	}
	
	public boolean hasAnyYearDateUnitWithWrongStartMonth() throws Exception
	{	
		DateUnitEffortList duel = getDateUnitEffortList();
		for (int index = 0; index < duel.size(); ++index)
		{
			DateUnitEffort dateUnitEffort = duel.getDateUnitEffort(index);
			DateUnit dateUnit = dateUnitEffort.getDateUnit();
			if (yearlyDateUnitDoesNotMatchCurrentFiscalYearStartMonth(dateUnit))
				return true;
		}
		
		return false;
	}
	
	public TimePeriodCostsMap convertAllDateUnitEffortList() throws Exception
	{
		return createTimePeriodCostsMap(getDateUnitEffortList());
	}

	private TimePeriodCostsMap createTimePeriodCostsMap(DateUnitEffortList duel)
	{
		TimePeriodCostsMap tpcm = new TimePeriodCostsMap();
		for (int index = 0; index < duel.size(); ++index)
		{
			DateUnitEffort dateUnitEffort = duel.getDateUnitEffort(index);
			TimePeriodCosts timePeriodCosts = createTimePeriodCosts(new OptionalDouble(dateUnitEffort.getQuantity()));
			DateUnit dateUnit = dateUnitEffort.getDateUnit();
			tpcm.add(dateUnit, timePeriodCosts);
		}

		return tpcm;
	}
	
	private void addTimePeriodCostsInPlaceForNoData(DateUnitEffortList duel, TimePeriodCostsMap tpcm)
	{
		tpcm.add(new DateUnit(), createTimePeriodCosts(new OptionalDouble()));
	}

	private boolean isWithinProjectDateRange(final DateUnit dateUnit) throws Exception
	{
		DateRange projectPlanningDateRange = getProjectCalendar().getProjectPlanningDateRange();
		DateRange dateRange = getProjectCalendar().convertToDateRange(dateUnit);

		return projectPlanningDateRange.containsAtleastSome(dateRange);
	}

	private ProjectCalendar getProjectCalendar()
	{
		return getProject().getProjectCalendar();
	}

	protected String getProjectResourceTag()
	{
		return null;
	}

	abstract protected TimePeriodCosts createTimePeriodCosts(OptionalDouble quantity);
	
	public static final String TAG_DATEUNIT_DETAILS = "Details";
}
