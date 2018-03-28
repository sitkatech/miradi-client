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

import org.miradi.migrations.forward.MigrationTo64;
import org.miradi.objecthelpers.CodeToUserStringMap;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.AbstractThreatRatingData;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Cause;
import org.miradi.objects.Target;
import org.miradi.project.ProjectForTesting;
import org.miradi.project.ProjectSaverForTesting;

public class TestMigrationTo64 extends AbstractTestMigration
{
    public TestMigrationTo64(String name)
    {
        super(name);
    }

    public void testCommentsSplitAfterForwardMigration() throws Exception
    {
        Cause cause = getProject().createCause();
        Target target = getProject().createTarget();

        assertTrue("project is not in simple threat rating mode?", getProject().isSimpleThreatRatingMode());
        assertEquals(0, getProject().getObjectManager().getPool(ObjectType.THREAT_SIMPLE_RATING_DATA).size());
        assertEquals(0, getProject().getObjectManager().getPool(ObjectType.THREAT_STRESS_RATING_DATA).size());

        createLegacyThreatRatingDataComments(cause.getRef(), target.getRef(), ProjectForTesting.SIMPLE_THREAT_RATING_COMMENT, MigrationTo64.TAG_SIMPLE_THREAT_RATING_COMMENTS_MAP);
        createLegacyThreatRatingDataComments(cause.getRef(), target.getRef(), ProjectForTesting.STRESS_BASED_THREAT_RATING_COMMENT, MigrationTo64.TAG_STRESS_BASED_THREAT_RATING_COMMENTS_MAP);

        assertEquals(0, getProject().getObjectManager().getPool(ObjectType.THREAT_SIMPLE_RATING_DATA).size());
        assertEquals(1, getProject().getObjectManager().getPool(ObjectType.THREAT_STRESS_RATING_DATA).size());

        String projectAsString = ProjectSaverForTesting.createSnapShot(getProject(), new VersionRange(MigrationTo64.VERSION_FROM));
        final RawProject projectToMigrate = RawProjectLoader.loadProject(projectAsString);
        migrateProject(projectToMigrate, new VersionRange(MigrationTo64.VERSION_TO));

        assertEquals(1, projectToMigrate.getRawPoolForType(ObjectType.THREAT_SIMPLE_RATING_DATA).size());
        ORef simpleThreatRatingDataRef = projectToMigrate.getRawPoolForType(ObjectType.THREAT_SIMPLE_RATING_DATA).getSortedReflist().getFirstElement();
        RawObject simpleThreatRatingData = projectToMigrate.findObject(simpleThreatRatingDataRef);
        assertEquals(simpleThreatRatingData.getData(AbstractThreatRatingData.TAG_COMMENTS), ProjectForTesting.SIMPLE_THREAT_RATING_COMMENT);

        assertEquals(1, projectToMigrate.getRawPoolForType(ObjectType.THREAT_STRESS_RATING_DATA).size());
        ORef stressThreatRatingDataRef = projectToMigrate.getRawPoolForType(ObjectType.THREAT_STRESS_RATING_DATA).getSortedReflist().getFirstElement();
        RawObject stressThreatRatingData = projectToMigrate.findObject(stressThreatRatingDataRef);
        assertEquals(stressThreatRatingData.getData(AbstractThreatRatingData.TAG_COMMENTS), ProjectForTesting.STRESS_BASED_THREAT_RATING_COMMENT);
    }

    public void testCommentsMergedAfterReverseMigration() throws Exception
    {
        Cause cause1 = getProject().createCause();
        Target target1 = getProject().createTarget();

        Cause cause2 = getProject().createCause();
        Target target2 = getProject().createTarget();

        assertTrue("project is not in simple threat rating mode?", getProject().isSimpleThreatRatingMode());

        getProject().populateSimpleThreatRatingCommentsData(cause1.getRef(), target1.getRef(), ProjectForTesting.SIMPLE_THREAT_RATING_COMMENT);
        verifyThreatRatingComments(cause1, target1, ObjectType.THREAT_SIMPLE_RATING_DATA, ProjectForTesting.SIMPLE_THREAT_RATING_COMMENT);

        getProject().populateStressThreatRatingCommentsData(cause1.getRef(), target1.getRef(), ProjectForTesting.STRESS_BASED_THREAT_RATING_COMMENT);
        verifyThreatRatingComments(cause1, target1, ObjectType.THREAT_STRESS_RATING_DATA, ProjectForTesting.STRESS_BASED_THREAT_RATING_COMMENT);

        getProject().populateSimpleThreatRatingCommentsData(cause2.getRef(), target2.getRef(), ProjectForTesting.SIMPLE_THREAT_RATING_COMMENT);
        verifyThreatRatingComments(cause2, target2, ObjectType.THREAT_SIMPLE_RATING_DATA, ProjectForTesting.SIMPLE_THREAT_RATING_COMMENT);

        getProject().populateStressThreatRatingCommentsData(cause2.getRef(), target2.getRef(), ProjectForTesting.STRESS_BASED_THREAT_RATING_COMMENT);
        verifyThreatRatingComments(cause2, target2, ObjectType.THREAT_STRESS_RATING_DATA, ProjectForTesting.STRESS_BASED_THREAT_RATING_COMMENT);

        RawProject rawProject = reverseMigrate(new VersionRange(MigrationTo64.VERSION_TO));

        assertFalse(rawProject.containsAnyObjectsOfType(ObjectType.THREAT_SIMPLE_RATING_DATA));
        RawPool stressThreatRatingDataPool = rawProject.getRawPoolForType(ObjectType.THREAT_STRESS_RATING_DATA);
        assertEquals(1, stressThreatRatingDataPool.size());
        ORef stressThreatRatingDataRef = stressThreatRatingDataPool.getSortedReflist().getFirstElement();
        RawObject stressThreatRatingData = rawProject.findObject(stressThreatRatingDataRef);
        assertTrue(stressThreatRatingData.hasValue(MigrationTo64.TAG_STRESS_BASED_THREAT_RATING_COMMENTS_MAP));
        assertTrue(stressThreatRatingData.hasValue(MigrationTo64.TAG_SIMPLE_THREAT_RATING_COMMENTS_MAP));

        String simpleCommentsStringMapAsString = stressThreatRatingData.getData(MigrationTo64.TAG_SIMPLE_THREAT_RATING_COMMENTS_MAP);
        CodeToUserStringMap simpleCommentsStringMap = new CodeToUserStringMap(simpleCommentsStringMapAsString);
        assertEquals(simpleCommentsStringMap.getUserString(createLegacyThreatRatingDataMapKey(cause1.getRef(), target1.getRef())), ProjectForTesting.SIMPLE_THREAT_RATING_COMMENT);
        assertEquals(simpleCommentsStringMap.getUserString(createLegacyThreatRatingDataMapKey(cause2.getRef(), target2.getRef())), ProjectForTesting.SIMPLE_THREAT_RATING_COMMENT);

        String stressCommentsStringMapAsString = stressThreatRatingData.getData(MigrationTo64.TAG_STRESS_BASED_THREAT_RATING_COMMENTS_MAP);
        CodeToUserStringMap stressCommentsStringMap = new CodeToUserStringMap(stressCommentsStringMapAsString);
        assertEquals(stressCommentsStringMap.getUserString(createLegacyThreatRatingDataMapKey(cause1.getRef(), target1.getRef())), ProjectForTesting.STRESS_BASED_THREAT_RATING_COMMENT);
        assertEquals(stressCommentsStringMap.getUserString(createLegacyThreatRatingDataMapKey(cause2.getRef(), target2.getRef())), ProjectForTesting.STRESS_BASED_THREAT_RATING_COMMENT);
    }

    private void verifyThreatRatingComments(Cause cause, Target target, int objectType, String expectedComments)
    {
        AbstractThreatRatingData threatRatingData = AbstractThreatRatingData.findThreatRatingData(getProject(), cause.getRef(), target.getRef(), objectType);
        assertNotNull(threatRatingData);
        assertEquals(expectedComments, threatRatingData.getComments());
    }

    private void createLegacyThreatRatingDataComments(ORef threatRef, ORef targetRef, String comments, String commentsTag) throws Exception
    {
        ORef singletonThreatRatingDataRef = getProject().getSafeSingleObjectRef(ObjectType.THREAT_STRESS_RATING_DATA);
        if (singletonThreatRatingDataRef.isInvalid())
        {
            singletonThreatRatingDataRef = getProject().createObject(ObjectType.THREAT_STRESS_RATING_DATA);
            CodeToUserStringMap commentsStringMap = new CodeToUserStringMap("");
            getProject().fillObjectUsingCommand(singletonThreatRatingDataRef, MigrationTo64.TAG_SIMPLE_THREAT_RATING_COMMENTS_MAP, commentsStringMap.toJsonString());
            getProject().fillObjectUsingCommand(singletonThreatRatingDataRef, MigrationTo64.TAG_STRESS_BASED_THREAT_RATING_COMMENTS_MAP, commentsStringMap.toJsonString());
        }

        BaseObject singletonThreatRatingData = getProject().findObject(singletonThreatRatingDataRef);
        CodeToUserStringMap commentsStringMap = singletonThreatRatingData.getCodeToUserStringMapData(commentsTag);
        String mapKey = createLegacyThreatRatingDataMapKey(threatRef, targetRef);
        commentsStringMap.putUserString(mapKey, comments);
        getProject().fillObjectUsingCommand(singletonThreatRatingDataRef, commentsTag, commentsStringMap.toJsonString());
    }

    private String createLegacyThreatRatingDataMapKey(ORef threatRef, ORef targetRef)
    {
        return threatRef.toString() + targetRef.toString();
    }

    @Override
    protected int getFromVersion()
    {
        return MigrationTo64.VERSION_FROM;
    }

    @Override
    protected int getToVersion()
    {
        return MigrationTo64.VERSION_TO;
    }
}
