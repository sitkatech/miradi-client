/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
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

package org.miradi.migrations;

import org.miradi.migrations.forward.MigrationTo12;
import org.miradi.objecthelpers.ORef;
import org.miradi.schemas.TncProjectDataSchema;

public class TestMigrationTo12 extends AbstractTestMigration
{
	public TestMigrationTo12(String name)
	{
		super(name);
	}
	
	public void testFieldsRemovedAfterReverseMigration() throws Exception
	{
		getProject().fillTncProjectData();
		RawProject rawProject = reverseMigrate(new VersionRange(MigrationTo12.VERSION_TO));
		ORef tncProjectDataRef = getProject().getTncProjectDataRef();
		RawObject tncProjectDataRawObject = rawProject.getRawPoolForType(TncProjectDataSchema.getObjectType()).get(tncProjectDataRef);
		assertFalse("Field should have been removed during reverse migration?", tncProjectDataRawObject.containsKey(MigrationTo12.TAG_PROJECT_FOCUS));
		assertFalse("Field should have been removed during reverse migration?", tncProjectDataRawObject.containsKey(MigrationTo12.TAG_PROJECT_SCALE));
	}
	
	@Override
	protected int getFromVersion()
	{
		return 11;
	}
	
	@Override
	protected int getToVersion()
	{
		return 12;
	}
}
