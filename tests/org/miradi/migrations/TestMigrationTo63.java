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

import org.miradi.migrations.forward.MigrationTo63;
import org.miradi.objecthelpers.CodeToUserStringMap;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.*;
import org.miradi.project.ProjectForTesting;

public class TestMigrationTo63 extends AbstractTestMigration
{
    public TestMigrationTo63(String name)
    {
        super(name);
    }

    public void testCommentsMovedAfterReverseMigration() throws Exception
    {
        Cause cause = getProject().createCause();
        Target target = getProject().createTarget();

        assertTrue("project is not in simple threat rating mode?", getProject().isSimpleThreatRatingMode());
        getProject().populateSimpleThreatRatingCommentsData(cause.getRef(), target.getRef(), ProjectForTesting.SIMPLE_THREAT_RATING_COMMENT);

        AbstractThreatRatingData simpleThreatRatingData = getProject().getSingletonSimpleThreatRatingData();
        assertNotNull(simpleThreatRatingData);
        assertEquals(ProjectForTesting.SIMPLE_THREAT_RATING_COMMENT, simpleThreatRatingData.findComment(cause.getRef(), target.getRef()));

        AbstractThreatRatingData stressThreatRatingData = getProject().getSingletonStressThreatRatingData();
        assertNotNull(stressThreatRatingData);
        assertEquals("", stressThreatRatingData.findComment(cause.getRef(), target.getRef()));

        RawProject rawProject = reverseMigrate(new VersionRange(MigrationTo63.VERSION_TO));

        RawPool stressThreatRatingDataPool = rawProject.getRawPoolForType(ObjectType.THREAT_STRESS_RATING_DATA);
        ORef stressThreatRatingDataRef = stressThreatRatingDataPool.getSortedReflist().getFirstElement();
        RawObject rawStressThreatRatingData = rawProject.findObject(stressThreatRatingDataRef);
        String rawStressThreatRatingCommentsMapAsString = rawStressThreatRatingData.getData(MigrationTo63.TAG_SIMPLE_THREAT_RATING_COMMENTS_MAP);
        CodeToUserStringMap rawStressThreatRatingCommentsMap = new CodeToUserStringMap(rawStressThreatRatingCommentsMapAsString);
        String commentsMapKey = AbstractThreatRatingData.createKey(cause.getRef(), target.getRef());
        assertTrue(rawStressThreatRatingCommentsMap.contains(commentsMapKey));
        assertEquals(ProjectForTesting.SIMPLE_THREAT_RATING_COMMENT, rawStressThreatRatingCommentsMap.getUserString(commentsMapKey));
    }

    @Override
    protected int getFromVersion()
    {
        return MigrationTo63.VERSION_FROM;
    }

    @Override
    protected int getToVersion()
    {
        return MigrationTo63.VERSION_TO;
    }
}
