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

import org.miradi.migrations.forward.MigrationTo47;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.DiagramLink;
import org.miradi.schemas.DiagramLinkSchema;

public class TestMigrationTo47 extends AbstractTestMigration
{
    public TestMigrationTo47(String name)
    {
        super(name);
    }

    public void testFieldsRemovedAfterReverseMigration() throws Exception
    {
        ORef diagramLinkRef = getProject().createDiagramLink();
        DiagramLink diagramLink = DiagramLink.find(getProject(), diagramLinkRef);
        diagramLink.setData(DiagramLink.TAG_ANNOTATION, "sample annotation");
        diagramLink.setData(DiagramLink.TAG_IS_UNCERTAIN_LINK, DiagramLink.UNCERTAIN_LINK.toString());

        RawProject rawProject = reverseMigrate(new VersionRange(MigrationTo47.VERSION_TO));
        RawPool rawDiagramLinkPool = rawProject.getRawPoolForType(DiagramLinkSchema.getObjectType());
        for(ORef ref : rawDiagramLinkPool.keySet())
        {
            RawObject rawDiagramLink = rawDiagramLinkPool.get(ref);
            assertFalse("Field should have been removed during reverse migration?", rawDiagramLink.containsKey(MigrationTo47.TAG_ANNOTATION));
            assertFalse("Field should have been removed during reverse migration?", rawDiagramLink.containsKey(MigrationTo47.TAG_IS_UNCERTAIN_LINK));
        }
    }

    @Override
    protected int getFromVersion()
    {
        return MigrationTo47.VERSION_FROM;
    }

    @Override
    protected int getToVersion()
    {
        return MigrationTo47.VERSION_TO;
    }
}
