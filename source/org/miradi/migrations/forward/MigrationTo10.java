/* 
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
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

import org.miradi.main.EAM;
import org.miradi.migrations.RawProject;
import org.miradi.migrations.RenameMultipleFieldMigration;
import org.miradi.schemas.TncProjectDataSchema;
import org.miradi.utils.BiDirectionalHashMap;

public class MigrationTo10 extends RenameMultipleFieldMigration
{
	public MigrationTo10(RawProject rawProject)
	{
		super(rawProject, TncProjectDataSchema.getObjectType());

		createLegacyToNewMap();
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
		return EAM.text("This migration renames 2 fields");
	}

	@Override
	protected BiDirectionalHashMap createLegacyToNewMap()
	{
		BiDirectionalHashMap oldToNewTagMap = new BiDirectionalHashMap();
		oldToNewTagMap.put(LEGACY_TAG_MAKING_THE_CASE, TAG_OVERALL_PROJECT_GOAL);
		oldToNewTagMap.put(LEGACY_TAG_CAPACITY_AND_FUNDING, TAG_FINANCIAL_PLAN);
		
		return oldToNewTagMap;
	}
	
	private static final int VERSION_FROM = 9;
	public static final int VERSION_TO = 10;
	
	public final static String LEGACY_TAG_MAKING_THE_CASE = "MakingTheCase";
	public final static String LEGACY_TAG_CAPACITY_AND_FUNDING = "CapacityAndFunding";
	
	public final static String TAG_OVERALL_PROJECT_GOAL = "OverallProjectGoal";
	public final static String TAG_FINANCIAL_PLAN = "FinancialPlan";
}
