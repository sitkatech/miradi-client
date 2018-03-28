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

import org.miradi.migrations.forward.MigrationTo62;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.AbstractThreatRatingData;
import org.miradi.objects.Cause;
import org.miradi.objects.Target;
import org.miradi.project.ProjectForTesting;

public class TestMigrationTo62 extends AbstractTestMigration
{
    public TestMigrationTo62(String name)
    {
        super(name);
    }

    public void testPoolRemovedAfterReverseMigration() throws Exception
    {
        Cause cause = getProject().createCause();
        Target target = getProject().createTarget();

        assertTrue("project is not in simple threat rating mode?", getProject().isSimpleThreatRatingMode());
        getProject().populateSimpleThreatRatingCommentsData(cause.getRef(), target.getRef(), ProjectForTesting.SIMPLE_THREAT_RATING_COMMENT);

        AbstractThreatRatingData threatRatingData = AbstractThreatRatingData.findThreatRatingData(getProject(), cause.getRef(), target.getRef(), ObjectType.THREAT_SIMPLE_RATING_DATA);
        assertNotNull(threatRatingData);

        RawProject rawProject = reverseMigrate(new VersionRange(MigrationTo62.VERSION_TO));

        assertFalse("Pool should have been removed during reverse migration", rawProject.containsAnyObjectsOfType(ObjectType.THREAT_SIMPLE_RATING_DATA));
    }

    @Override
    protected int getFromVersion()
    {
        return MigrationTo62.VERSION_FROM;
    }

    @Override
    protected int getToVersion()
    {
        return MigrationTo62.VERSION_TO;
    }
}
