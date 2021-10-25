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

import org.miradi.migrations.forward.MigrationTo60;
import org.miradi.objecthelpers.ORef;
import org.miradi.schemas.ConceptualModelDiagramSchema;

public class TestMigrationTo60 extends AbstractTestMigration
{
    public TestMigrationTo60(String name)
    {
        super(name);
    }

    public void testFieldsRemovedAfterReverseMigration() throws Exception
    {
        getProject().createConceptualModelDiagram();

        RawProject rawProject = reverseMigrate(new VersionRange(MigrationTo60.VERSION_TO));
        RawPool rawConceptualModelDiagramPool = rawProject.getRawPoolForType(ConceptualModelDiagramSchema.getObjectType());
        for(ORef ref : rawConceptualModelDiagramPool.keySet())
        {
            RawObject rawConceptualModelDiagram = rawConceptualModelDiagramPool.get(ref);
            assertFalse("Field should have been removed during reverse migration?", rawConceptualModelDiagram.containsKey(MigrationTo60.TAG_IS_PROGRESS_STATUS_DISPLAY_ENABLED));
            assertFalse("Field should have been removed during reverse migration?", rawConceptualModelDiagram.containsKey(MigrationTo60.TAG_IS_RESULT_STATUS_DISPLAY_ENABLED));
        }
    }

    @Override
    protected int getFromVersion()
    {
        return MigrationTo60.VERSION_FROM;
    }

    @Override
    protected int getToVersion()
    {
        return MigrationTo60.VERSION_TO;
    }
}

