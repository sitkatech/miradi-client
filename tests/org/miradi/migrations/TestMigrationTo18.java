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

package org.miradi.migrations;

import org.miradi.ids.BaseId;
import org.miradi.migrations.forward.MigrationTo18;
import org.miradi.objecthelpers.ORef;
import org.miradi.schemas.ProjectMetadataSchema;

public class TestMigrationTo18 extends AbstractTestMigration
{
	public TestMigrationTo18(String name)
	{
		super(name);
	}
	
	public void testFieldsRemovedAfterReverseMigration() throws Exception
	{
		RawProject rawProject = reverseMigrate(new VersionRange(MigrationTo18.VERSION_TO));
        ORef metadataRef = new ORef(ProjectMetadataSchema.getObjectType(), new BaseId(rawProject.getProjectMetadataId()));

        RawObject rawMetadata = rawProject.findObject(metadataRef);
        assertNotNull(rawMetadata);
        assertFalse("Field should have been removed during reverse migration?", rawMetadata.containsKey(MigrationTo18.TAG_FACTOR_MODE));
	}
	
	@Override
	protected int getFromVersion()
	{
		return MigrationTo18.VERSION_FROM;
	}
	
	@Override
	protected int getToVersion()
	{
		return MigrationTo18.VERSION_TO;
	}
}
