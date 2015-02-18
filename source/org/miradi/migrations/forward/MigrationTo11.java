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
import org.miradi.migrations.RawProject;
import org.miradi.migrations.RemoveMultipleFieldsMigration;
import org.miradi.schemas.TncProjectDataSchema;

public class MigrationTo11 extends RemoveMultipleFieldsMigration
{
	public MigrationTo11(RawProject rawProjectToUse)
	{
		super(rawProjectToUse, TncProjectDataSchema.getObjectType());
	}

	@Override
	protected HashMap<String, String> createFieldsToLabelMapToModify()
	{
		HashMap<String, String> fieldsToRemove = new HashMap<String, String>();
		fieldsToRemove.put(LEGACY_TAG_TNC_PROJECT_TYPES, EAM.text("Project Place Types"));
		fieldsToRemove.put(LEGACY_TAG_TNC_ORGANIZATIONAL_PRIORITIES, EAM.text("Organizational Priorities"));
		
		return fieldsToRemove;
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
		return EAM.text("This migration removes 2 Tnc fields");
	}
	
	private static final int VERSION_FROM = 10;
	public static final int VERSION_TO = 11;
	
	public final static String LEGACY_TAG_TNC_PROJECT_TYPES = "ProjectPlaceTypes";
	public final static String LEGACY_TAG_TNC_ORGANIZATIONAL_PRIORITIES = "OrganizationalPriorities";
}
