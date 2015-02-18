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

package org.miradi.migrations.forward;

import java.util.HashMap;

import org.miradi.main.EAM;
import org.miradi.migrations.AbstractUpdateUiFieldData;
import org.miradi.migrations.RawProject;
import org.miradi.objects.ViewData;
import org.miradi.schemas.ViewDataSchema;

public class MigrationTo14 extends AbstractUpdateUiFieldData
{
	public MigrationTo14(RawProject rawProjectToUse)
	{
		super(rawProjectToUse, ViewDataSchema.getObjectType(), ViewData.TAG_PLANNING_SINGLE_LEVEL_CHOICE, createFromToDataMap());
	}

	private static HashMap<String, String> createFromToDataMap()
	{
		HashMap<String, String> fromToDataMap = new HashMap<String, String>();
		fromToDataMap.put(LEGACY_RESOURCE_CHOICE, "");
		fromToDataMap.put(LEGACY_FUTURE_STATUS_CHOICE, "");
		
		return fromToDataMap;
	}

	@Override
	protected int getToVersion()
	{
		return VERSION_TO;
	}
	
	@Override
	protected int getFromVersion() 
	{
		return VERSION_FROM;
	}

	@Override
	protected String getDescription()
	{
		return EAM.text("This migration clears choices that no longer exist.");
	}
	
	public static final int VERSION_FROM = 13;
	public static final int VERSION_TO = 14;
	
	public static final String LEGACY_RESOURCE_CHOICE = "ResourceAssignment";
	public static final String LEGACY_FUTURE_STATUS_CHOICE = "FutureStatus";
}
