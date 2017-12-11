/*
Copyright 2005-2017, Foundations of Success, Bethesda, Maryland
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

import org.miradi.migrations.forward.MigrationTo43;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.*;
import org.miradi.project.Project;
import org.miradi.schemas.DiagramFactorSchema;
import org.miradi.schemas.TaggedObjectSetSchema;

import java.text.ParseException;

import static org.miradi.migrations.forward.MigrationTo43.TAG_TAGGED_OBJECT_SET_REFS;


public class TestMigrationTo43 extends AbstractTestMigration
{
    public TestMigrationTo43(String name)
    {
        super(name);
    }

    public void testTagsCreatedByForwardMigrationSimpleTestCase() throws Exception
    {
        String tagLabel = "TestTag";

        ConceptualModelDiagram conceptualModelDiagram = ConceptualModelDiagram.find(getProject(), getProject().createConceptualModelDiagram());
        Strategy strategy = getProject().createStrategy();
        DiagramFactor diagramFactor = getProject().createAndAddFactorToDiagram(conceptualModelDiagram, strategy.getRef());
        TaggedObjectSet taggedObjectSet = getProject().createLabeledTaggedObjectSet(tagLabel);
        tagDiagramFactorOld(conceptualModelDiagram, diagramFactor.getWrappedORef(), taggedObjectSet);

        RawProject rawProject = reverseMigrate(new VersionRange(MigrationTo43.VERSION_TO));
        migrateProject(rawProject, new VersionRange(Project.VERSION_HIGH));

        RawPool rawDiagramFactorPool = rawProject.getRawPoolForType(DiagramFactorSchema.getObjectType());
        for(ORef ref : rawDiagramFactorPool.keySet())
        {
            if (ref.equals(diagramFactor.getRef()))
            {
                RawObject rawDiagramFactor = rawDiagramFactorPool.get(ref);
                assertTrue("Field should have been added during forward migration?", rawDiagramFactor.containsKey(MigrationTo43.TAG_TAGGED_OBJECT_SET_REFS));
                String diagramTaggedObjectSetRefsAsString = rawDiagramFactor.getData(TAG_TAGGED_OBJECT_SET_REFS);
                ORefList diagramTaggedObjectSetRefs = new ORefList(diagramTaggedObjectSetRefsAsString);
                assertTrue("DiagramFactor should reference tag", diagramTaggedObjectSetRefs.contains(taggedObjectSet.getRef()));
            }
        }

        verifyTagRemovedForAllObjectsInPool(rawProject, TaggedObjectSetSchema.getObjectType(), MigrationTo43.TAG_TAGGED_OBJECT_REFS);

        verifyFullCircleMigrations(new VersionRange(42, 43));
    }

    public void testTagsCreatedByForwardMigrationComplexTestCase() throws Exception
    {
        String tagSharedFactorLabel = "TestSharedFactorTag";
        String tagCMFactorLabel = "TestCMTag";
        String tagRCFactorLabel = "TestRCTag";

        ConceptualModelDiagram conceptualModelDiagram = ConceptualModelDiagram.find(getProject(), getProject().createConceptualModelDiagram());
        ResultsChainDiagram resultsChainDiagram = ResultsChainDiagram.find(getProject(), getProject().createResultsChainDiagram());

        Strategy strategy = getProject().createStrategy();
        DiagramFactor cmDiagramFactorStrategy = getProject().createAndAddFactorToDiagram(conceptualModelDiagram, strategy.getRef());
        DiagramFactor rcDiagramFactorStrategy = getProject().createAndAddFactorToDiagram(resultsChainDiagram, strategy.getRef());

        TaggedObjectSet taggedObjectSetShared = getProject().createLabeledTaggedObjectSet(tagSharedFactorLabel);
        tagDiagramFactorOld(conceptualModelDiagram, cmDiagramFactorStrategy.getWrappedORef(), taggedObjectSetShared, false);
        tagDiagramFactorOld(resultsChainDiagram, rcDiagramFactorStrategy.getWrappedORef(), taggedObjectSetShared, true);

        Target cmTarget = getProject().createTarget();
        DiagramFactor cmDiagramFactorTarget = getProject().createAndAddFactorToDiagram(conceptualModelDiagram, cmTarget.getRef());

        TaggedObjectSet taggedObjectSetCM = getProject().createLabeledTaggedObjectSet(tagCMFactorLabel);
        tagDiagramFactorOld(conceptualModelDiagram, cmTarget.getRef(), taggedObjectSetCM);

        BiophysicalResult biophysicalResult = getProject().createBiophysicalResult();
        DiagramFactor rcDiagramFactorBiophysicalResult = getProject().createAndAddFactorToDiagram(resultsChainDiagram, biophysicalResult.getRef());

        TaggedObjectSet taggedObjectSetRC = getProject().createLabeledTaggedObjectSet(tagRCFactorLabel);
        tagDiagramFactorOld(resultsChainDiagram, biophysicalResult.getRef(), taggedObjectSetRC, false);

        RawProject rawProject = reverseMigrate(new VersionRange(MigrationTo43.VERSION_TO));
        migrateProject(rawProject, new VersionRange(Project.VERSION_HIGH));

       RawPool rawDiagramFactorPool = rawProject.getRawPoolForType(DiagramFactorSchema.getObjectType());
        for(ORef ref : rawDiagramFactorPool.keySet())
        {
            RawObject rawDiagramFactor = rawDiagramFactorPool.get(ref);
            String diagramTaggedObjectSetRefsAsString = rawDiagramFactor.getData(TAG_TAGGED_OBJECT_SET_REFS);
            ORefList diagramTaggedObjectSetRefs = new ORefList(diagramTaggedObjectSetRefsAsString);

            if (ref.equals(cmDiagramFactorStrategy.getRef()) || ref.equals(rcDiagramFactorStrategy.getRef()))
            {
                assertTrue("Field should have been added during forward migration?", rawDiagramFactor.containsKey(MigrationTo43.TAG_TAGGED_OBJECT_SET_REFS));
                assertTrue("DiagramFactor should reference shared tag", diagramTaggedObjectSetRefs.contains(taggedObjectSetShared.getRef()));
                assertEquals(diagramTaggedObjectSetRefs.size(), 1);
            }
            if (ref.equals(cmDiagramFactorTarget.getRef()))
            {
                assertTrue("Field should have been added during forward migration?", rawDiagramFactor.containsKey(MigrationTo43.TAG_TAGGED_OBJECT_SET_REFS));
                assertTrue("DiagramFactor should reference CM only tag", diagramTaggedObjectSetRefs.contains(taggedObjectSetCM.getRef()));
                assertEquals(diagramTaggedObjectSetRefs.size(), 1);
            }
            if (ref.equals(rcDiagramFactorBiophysicalResult.getRef()))
            {
                assertTrue("Field should have been added during forward migration?", rawDiagramFactor.containsKey(MigrationTo43.TAG_TAGGED_OBJECT_SET_REFS));
                assertTrue("DiagramFactor should reference CM only tag", diagramTaggedObjectSetRefs.contains(taggedObjectSetRC.getRef()));
                assertEquals(diagramTaggedObjectSetRefs.size(), 1);
            }
        }

        verifyTagRemovedForAllObjectsInPool(rawProject, TaggedObjectSetSchema.getObjectType(), MigrationTo43.TAG_TAGGED_OBJECT_REFS);
        verifyDiagramSelectedTaggedObjectSetRefs(rawProject, conceptualModelDiagram, taggedObjectSetShared, taggedObjectSetCM, resultsChainDiagram);

        verifyFullCircleMigrations(new VersionRange(42, 43));
    }

    public void testTagsRemovedByReverseMigrationSimpleTestCase() throws Exception
    {
        String tagLabel = "TestTag";

        ConceptualModelDiagram conceptualModelDiagram = ConceptualModelDiagram.find(getProject(), getProject().createConceptualModelDiagram());
        Strategy strategy = getProject().createStrategy();
        DiagramFactor diagramFactor = getProject().createAndAddFactorToDiagram(conceptualModelDiagram, strategy.getRef());
        TaggedObjectSet taggedObjectSet = getProject().createLabeledTaggedObjectSet(tagLabel);
        getProject().tagDiagramFactor(conceptualModelDiagram, diagramFactor, taggedObjectSet);

        RawProject rawProject = reverseMigrate(new VersionRange(MigrationTo43.VERSION_TO));

        verifyTagRemovedForAllObjectsInPool(rawProject, DiagramFactorSchema.getObjectType(), MigrationTo43.TAG_TAGGED_OBJECT_SET_REFS);

        RawPool rawTaggedObjectSetPool = rawProject.getRawPoolForType(TaggedObjectSetSchema.getObjectType());
        for(ORef ref : rawTaggedObjectSetPool.keySet())
        {
            if (ref.equals(taggedObjectSet.getRef()))
            {
                RawObject rawTaggedObjectSet = rawTaggedObjectSetPool.get(ref);
                assertTrue("Field should have been added during forward migration?", rawTaggedObjectSet.containsKey(MigrationTo43.TAG_TAGGED_OBJECT_REFS));
                String taggedObjectSetFactorRefsAsString = rawTaggedObjectSet.getData(MigrationTo43.TAG_TAGGED_OBJECT_REFS);
                ORefList taggedObjectSetFactorRefs = new ORefList(taggedObjectSetFactorRefsAsString);
                assertTrue("Tag should reference DiagramFactor wrapped factor", taggedObjectSetFactorRefs.contains(diagramFactor.getWrappedORef()));
            }
        }
    }

    public void testTagsRemovedByReverseMigrationComplexTestCase() throws Exception
    {
        String tagSharedFactorLabel = "TestSharedFactorTag";
        String tagCMFactorLabel = "TestCMTag";
        String tagRCFactorLabel = "TestRCTag";

        ConceptualModelDiagram conceptualModelDiagram = ConceptualModelDiagram.find(getProject(), getProject().createConceptualModelDiagram());
        ResultsChainDiagram resultsChainDiagram = ResultsChainDiagram.find(getProject(), getProject().createResultsChainDiagram());

        Strategy strategy = getProject().createStrategy();
        DiagramFactor cmDiagramFactorStrategy = getProject().createAndAddFactorToDiagram(conceptualModelDiagram, strategy.getRef());
        DiagramFactor rcDiagramFactorStrategy = getProject().createAndAddFactorToDiagram(resultsChainDiagram, strategy.getRef());

        TaggedObjectSet taggedObjectSetShared = getProject().createLabeledTaggedObjectSet(tagSharedFactorLabel);
        getProject().tagDiagramFactor(conceptualModelDiagram, cmDiagramFactorStrategy, taggedObjectSetShared, false);
        getProject().tagDiagramFactor(resultsChainDiagram, rcDiagramFactorStrategy, taggedObjectSetShared, true);

        Target cmTarget = getProject().createTarget();
        DiagramFactor cmDiagramFactorTarget = getProject().createAndAddFactorToDiagram(conceptualModelDiagram, cmTarget.getRef());

        TaggedObjectSet taggedObjectSetCM = getProject().createLabeledTaggedObjectSet(tagCMFactorLabel);
        getProject().tagDiagramFactor(conceptualModelDiagram, cmDiagramFactorTarget, taggedObjectSetCM);

        BiophysicalResult biophysicalResult = getProject().createBiophysicalResult();
        DiagramFactor rcDiagramFactorBiophysicalResult = getProject().createAndAddFactorToDiagram(resultsChainDiagram, biophysicalResult.getRef());

        TaggedObjectSet taggedObjectSetRC = getProject().createLabeledTaggedObjectSet(tagRCFactorLabel);
        getProject().tagDiagramFactor(resultsChainDiagram, rcDiagramFactorBiophysicalResult, taggedObjectSetRC, false);

        RawProject rawProject = reverseMigrate(new VersionRange(MigrationTo43.VERSION_TO));

        verifyTagRemovedForAllObjectsInPool(rawProject, DiagramFactorSchema.getObjectType(), MigrationTo43.TAG_TAGGED_OBJECT_SET_REFS);
        verifyDiagramSelectedTaggedObjectSetRefs(rawProject, conceptualModelDiagram, taggedObjectSetShared, taggedObjectSetCM, resultsChainDiagram);

        RawPool rawTaggedObjectSetPool = rawProject.getRawPoolForType(TaggedObjectSetSchema.getObjectType());
        for(ORef ref : rawTaggedObjectSetPool.keySet())
        {
            RawObject rawTaggedObjectSet = rawTaggedObjectSetPool.get(ref);
            assertTrue("Field should have been added during forward migration?", rawTaggedObjectSet.containsKey(MigrationTo43.TAG_TAGGED_OBJECT_REFS));
            String taggedObjectSetFactorRefsAsString = rawTaggedObjectSet.getData(MigrationTo43.TAG_TAGGED_OBJECT_REFS);
            ORefList taggedObjectSetFactorRefs = new ORefList(taggedObjectSetFactorRefsAsString);

            if (ref.equals(taggedObjectSetShared.getRef()))
                assertTrue("Tag should reference DiagramFactor wrapped factor", taggedObjectSetFactorRefs.equals(new ORefList(strategy.getRef())));

            if (ref.equals(taggedObjectSetCM.getRef()))
                assertTrue("Tag should reference DiagramFactor wrapped factor", taggedObjectSetFactorRefs.equals(new ORefList(cmTarget.getRef())));

            if (ref.equals(taggedObjectSetRC.getRef()))
                assertTrue("Tag should reference DiagramFactor wrapped factor", taggedObjectSetFactorRefs.equals(new ORefList(biophysicalResult.getRef())));
        }
    }

    private void verifyTagRemovedForAllObjectsInPool(RawProject rawProject, int objectType, String tag)
    {
        RawPool rawObjectPool = rawProject.getRawPoolForType(objectType);
        for(ORef ref : rawObjectPool.keySet())
        {
            RawObject rawObject = rawObjectPool.get(ref);
            assertFalse("Field should have been removed during forward migration?", rawObject.containsKey(tag));
        }
    }

    private void verifyDiagramSelectedTaggedObjectSetRefs(RawProject rawProject, ConceptualModelDiagram conceptualModelDiagram, TaggedObjectSet taggedObjectSetShared, TaggedObjectSet taggedObjectSetCM, ResultsChainDiagram resultsChainDiagram) throws ParseException
    {
        RawPool rcDiagramPool = rawProject.getRawPoolForType(ObjectType.RESULTS_CHAIN_DIAGRAM);
        assertNotNull(rcDiagramPool);
        for (ORef rcDiagramRef : rcDiagramPool.keySet())
        {
            if (rcDiagramRef.equals(resultsChainDiagram.getRef()))
            {
                RawObject rcDiagram = rcDiagramPool.get(rcDiagramRef);
                String diagramSelectedTaggedObjectSetRefsAsString = rcDiagram.getData(DiagramObject.TAG_SELECTED_TAGGED_OBJECT_SET_REFS);
                ORefList diagramSelectedTaggedObjectSetRefs = new ORefList(diagramSelectedTaggedObjectSetRefsAsString);
                assertTrue("RC Diagram should only have shared tag selected", diagramSelectedTaggedObjectSetRefs.equals(new ORefList(taggedObjectSetShared)));
            }
        }

        RawPool cmDiagramPool = rawProject.getRawPoolForType(ObjectType.CONCEPTUAL_MODEL_DIAGRAM);
        assertNotNull(cmDiagramPool);
        for (ORef cmDiagramRef : cmDiagramPool.keySet())
        {
            if (cmDiagramRef.equals(conceptualModelDiagram.getRef()))
            {
                RawObject cmDiagram = cmDiagramPool.get(cmDiagramRef);
                String diagramSelectedTaggedObjectSetRefsAsString = cmDiagram.getData(DiagramObject.TAG_SELECTED_TAGGED_OBJECT_SET_REFS);
                ORefList diagramSelectedTaggedObjectSetRefs = new ORefList(diagramSelectedTaggedObjectSetRefsAsString);
                assertTrue("CM Diagram should only have shared tag selected", diagramSelectedTaggedObjectSetRefs.equals(new ORefList(taggedObjectSetCM)));
            }
        }
    }

    private void tagDiagramFactorOld(DiagramObject diagramObject, ORef refToTag, TaggedObjectSet taggedObjectSet, boolean selectTag) throws Exception
    {
        ORefList taggedFactorRefs = new ORefList(refToTag);
        getProject().setObjectData(taggedObjectSet, TaggedObjectSet.TAG_TAGGED_OBJECT_REFS, taggedFactorRefs.toString());

        if (selectTag)
        {
            ORefList taggedObjectSetRefs = new ORefList(taggedObjectSet.getRef());
            getProject().setObjectData(diagramObject, DiagramObject.TAG_SELECTED_TAGGED_OBJECT_SET_REFS, taggedObjectSetRefs.toString());
        }
    }

    private void tagDiagramFactorOld(DiagramObject diagramObject, ORef refToTag, TaggedObjectSet taggedObjectSet) throws Exception
    {
        tagDiagramFactorOld(diagramObject, refToTag, taggedObjectSet, true);
    }

    @Override
    protected int getFromVersion()
    {
        return MigrationTo43.VERSION_FROM;
    }

    @Override
    protected int getToVersion()
    {
        return MigrationTo43.VERSION_TO;
    }
}
