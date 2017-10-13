/* 
Copyright 2005-2017, Foundations of Success, Bethesda, Maryland
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

package org.miradi.xml.xmpz2.objectExporters;

import org.miradi.schemas.TimeframeSchema;
import org.miradi.xml.xmpz2.Xmpz2XmlWriter;


public class TimeframeExporter extends AbstractPlanningObjectExporter
{
	public TimeframeExporter(Xmpz2XmlWriter writerToUse)
	{
		super(writerToUse, TimeframeSchema.getObjectType());
	}
	
	@Override
	protected String getDateUnitElementName()
	{
		return TIMEFRAMES_DATE_UNIT;
	}

	@Override
	protected String getDayElementName()
	{
		return TIMEFRAMES_DAY;
	}

	@Override
	protected String getMonthElementName()
	{
		return TIMEFRAMES_MONTH;
	}

	@Override
	protected String getQuarterElementName()
	{
		return TIMEFRAMES_QUARTER;
	}

	@Override
	protected String getYearElementName()
	{
		return TIMEFRAMES_YEAR;
	}

	@Override
	protected String getFullProjectTimespanElementName()
	{
		return TIMEFRAMES_FULL_PROJECT_TIMESPAN;
	}

	@Override
	protected String getQuantityElementName()
	{
		return "";
	}

	@Override
	protected boolean supportsQuantityElement()
	{
		return false;
	}

	@Override
	protected String getDateUnitsElementName()
	{
		return DATE_UNITS_TIMEFRAME;
	}

	@Override
	protected String getPoolName()
	{
		return TIMEFRAME;
	}

}
