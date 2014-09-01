/* 
Copyright 2005-2013, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.migrations;

import org.miradi.migrations.forward.MigrationTo16;
import org.miradi.objecthelpers.ORef;
import org.miradi.schemas.DiagramFactorSchema;

public class TestMigrationTo16 extends AbstractTestMigration
{
	public TestMigrationTo16(String name)
	{
		super(name);
	}
	
	public void testFieldsRemovedAfterReverseMigration() throws Exception
	{
        getProject().createAndPopulateDiagramFactor();
		RawProject rawProject = reverseMigrate(new VersionRange(MigrationTo16.VERSION_TO));
        RawPool rawDiagramFactorPool = rawProject.getRawPoolForType(DiagramFactorSchema.getObjectType());
        for(ORef ref : rawDiagramFactorPool.keySet())
        {
            RawObject rawDiagramFactor = rawDiagramFactorPool.get(ref);
            assertFalse("Field should have been removed during reverse migration?", rawDiagramFactor.containsKey(MigrationTo16.TAG_HEADER_HEIGHT));
        }
	}
	
	@Override
	protected int getFromVersion()
	{
		return MigrationTo16.VERSION_FROM;
	}
	
	@Override
	protected int getToVersion()
	{
		return MigrationTo16.VERSION_TO;
	}
}
