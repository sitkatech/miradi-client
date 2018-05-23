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

import org.miradi.migrations.forward.MigrationTo71;
import org.miradi.objecthelpers.ORef;
import org.miradi.schemas.ResultsChainDiagramSchema;

public class TestMigrationTo71 extends AbstractTestMigration
{
    public TestMigrationTo71(String name)
    {
        super(name);
    }

    public void testFieldsRemovedAfterReverseMigration() throws Exception
    {
        getProject().createResultsChainDiagram();

        RawProject rawProject = reverseMigrate(new VersionRange(MigrationTo71.VERSION_TO));
        RawPool rawResultsChainDiagramPool = rawProject.getRawPoolForType(ResultsChainDiagramSchema.getObjectType());
        for(ORef ref : rawResultsChainDiagramPool.keySet())
        {
            RawObject rawResultsChainDiagram = rawResultsChainDiagramPool.get(ref);
            assertFalse("Field should have been removed during reverse migration?", rawResultsChainDiagram.containsKey(MigrationTo71.TAG_COMMENTS));
        }
    }

    @Override
    protected int getFromVersion()
    {
        return MigrationTo71.VERSION_FROM;
    }

    @Override
    protected int getToVersion()
    {
        return MigrationTo71.VERSION_TO;
    }
}
