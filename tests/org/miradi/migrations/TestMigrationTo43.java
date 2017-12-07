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
import org.miradi.objects.ConceptualModelDiagram;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.Strategy;
import org.miradi.objects.TaggedObjectSet;
import org.miradi.project.Project;
import org.miradi.schemas.DiagramFactorSchema;
import org.miradi.schemas.TaggedObjectSetSchema;

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
        getProject().tagDiagramFactorOld(conceptualModelDiagram, diagramFactor.getWrappedORef(), taggedObjectSet);

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

        RawPool rawTaggedObjectSetPool = rawProject.getRawPoolForType(TaggedObjectSetSchema.getObjectType());
        for(ORef ref : rawTaggedObjectSetPool.keySet())
        {
            RawObject rawTaggedObjectSet = rawTaggedObjectSetPool.get(ref);
            assertFalse("Field should have been removed during forward migration?", rawTaggedObjectSet.containsKey(MigrationTo43.TAG_TAGGED_OBJECT_REFS));
        }

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

        RawPool rawDiagramFactorPool = rawProject.getRawPoolForType(DiagramFactorSchema.getObjectType());
        for(ORef ref : rawDiagramFactorPool.keySet())
        {
            RawObject rawDiagramFactor = rawDiagramFactorPool.get(ref);
            assertFalse("Field should have been removed during forward migration?", rawDiagramFactor.containsKey(MigrationTo43.TAG_TAGGED_OBJECT_SET_REFS));
        }

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
