/*
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
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

package org.miradi.migrations.forward;

import org.miradi.ids.BaseId;
import org.miradi.ids.IdList;
import org.miradi.main.EAM;
import org.miradi.migrations.*;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;

import java.text.ParseException;

public class MigrationTo43 extends AbstractMigration
{
    public MigrationTo43(RawProject rawProjectToUse)
    {
        super(rawProjectToUse);
    }

    @Override
    protected MigrationResult migrateForward() throws Exception
    {
        return migrate(false);
    }

    @Override
    protected MigrationResult reverseMigrate() throws Exception
    {
        return migrate(true);
    }

    private MigrationResult migrate(boolean reverseMigration) throws Exception
    {
        MigrationResult migrationResult = MigrationResult.createUninitializedResult();

        AbstractTagVisitor visitor = reverseMigration ? new DiagramFactorVisitor(): new TaggedObjectSetVisitor();

        visitAllORefsInPool(visitor);

        final MigrationResult visitMigrationResult = visitor.getMigrationResult();
        if (migrationResult == null)
            migrationResult = visitMigrationResult;
        else
            migrationResult.merge(visitMigrationResult);

        return migrationResult;
    }

    @Override
    protected int getToVersion()
    {
        return VERSION_TO;
    }

    @Override
    protected int getFromVersion()
    {
        return VERSION_FROM;
    }

    @Override
    protected String getDescription()
    {
        return EAM.text("This migration modifies diagram tags so that they are diagram-specific.");
    }

    abstract private class AbstractTagVisitor extends AbstractMigrationORefVisitor
    {
        protected String safeGetTag(RawObject rawObject, String tag)
        {
            if (rawObject.hasValue(tag))
                return rawObject.getData(tag);

            return "";
        }
    }

    private class TaggedObjectSetVisitor extends AbstractTagVisitor
    {
        @Override
        public int getTypeToVisit()
        {
            return ObjectType.TAGGED_OBJECT_SET;
        }

        @Override
        protected MigrationResult internalVisit(ORef rawObjectRef) throws Exception
        {
            RawObject taggedObjectSet = getRawProject().findObject(rawObjectRef);

            migrateTaggedObjectSetForDiagrams(ObjectType.CONCEPTUAL_MODEL_DIAGRAM, rawObjectRef, taggedObjectSet);
            migrateTaggedObjectSetForDiagrams(ObjectType.RESULTS_CHAIN_DIAGRAM, rawObjectRef, taggedObjectSet);

            if (taggedObjectSet.containsKey(TAG_TAGGED_OBJECT_REFS))
                taggedObjectSet.remove(TAG_TAGGED_OBJECT_REFS);

            return MigrationResult.createSuccess();
        }

        private void migrateTaggedObjectSetForDiagrams(int diagramObjectType, ORef taggedObjectSetRef, RawObject taggedObjectSet) throws Exception
        {
            String taggedObjectSetFactorRefsAsString = safeGetTag(taggedObjectSet, TAG_TAGGED_OBJECT_REFS);
            ORefList taggedObjectSetFactorRefs = new ORefList(taggedObjectSetFactorRefsAsString);

            if (!taggedObjectSetFactorRefs.isEmpty() && getRawProject().containsAnyObjectsOfType(diagramObjectType))
            {
                ORefList diagramObjectRefList = getRawProject().getAllRefsForType(diagramObjectType);
                for (ORef diagramObjectRef : diagramObjectRefList)
                {
                    RawObject diagram = getRawProject().findObject(diagramObjectRef);
                    String diagramFactorIdsAsString = safeGetTag(diagram, TAG_DIAGRAM_FACTOR_IDS);

                    if (!diagramFactorIdsAsString.isEmpty())
                    {
                        IdList diagramFactorIdList = new IdList(ObjectType.DIAGRAM_FACTOR, diagramFactorIdsAsString);
                        for (int i = 0; i < diagramFactorIdList.size(); i++)
                        {
                            BaseId diagramFactorId = diagramFactorIdList.get(i);
                            ORef diagramFactorRef = new ORef(ObjectType.DIAGRAM_FACTOR, diagramFactorId);
                            RawObject diagramFactor = getRawProject().findObject(diagramFactorRef);

                            String wrappedFactorRefAsString = safeGetTag(diagramFactor, TAG_WRAPPED_REF);
                            ORef wrappedFactorRef = ORef.createFromString(wrappedFactorRefAsString);

                            if (taggedObjectSetFactorRefs.contains(wrappedFactorRef))
                            {
                                String diagramFactorTaggedObjectSetRefsAsString = safeGetTag(diagramFactor, TAG_TAGGED_OBJECT_SET_REFS);
                                ORefList diagramFactorTaggedObjectSetRefs = new ORefList(diagramFactorTaggedObjectSetRefsAsString);
                                if (!diagramFactorTaggedObjectSetRefs.contains(taggedObjectSetRef))
                                {
                                    diagramFactorTaggedObjectSetRefs.add(taggedObjectSetRef);
                                    diagramFactor.setData(TAG_TAGGED_OBJECT_SET_REFS, diagramFactorTaggedObjectSetRefs.toJson().toString());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private class DiagramFactorVisitor extends AbstractTagVisitor
    {
        @Override
        public int getTypeToVisit()
        {
            return ObjectType.DIAGRAM_FACTOR;
        }

        @Override
        protected MigrationResult internalVisit(ORef rawObjectRef) throws Exception
        {
            RawObject diagramFactor = getRawProject().findObject(rawObjectRef);

            migrateTagForDiagramFactors(diagramFactor);

            if (diagramFactor.containsKey(TAG_TAGGED_OBJECT_SET_REFS))
                diagramFactor.remove(TAG_TAGGED_OBJECT_SET_REFS);

            return MigrationResult.createSuccess();
        }

        private void migrateTagForDiagramFactors(RawObject diagramFactor) throws ParseException
        {
            String wrappedFactorRefAsString = safeGetTag(diagramFactor, TAG_WRAPPED_REF);
            ORef wrappedFactorRef = ORef.createFromString(wrappedFactorRefAsString);

            String taggedObjectSetRefsAsString = safeGetTag(diagramFactor, TAG_TAGGED_OBJECT_SET_REFS);
            ORefList taggedObjectSetRefs = new ORefList(taggedObjectSetRefsAsString);

            for (ORef taggedObjectSetRef : taggedObjectSetRefs)
            {
                RawObject taggedObjectSet = getRawProject().findObject(taggedObjectSetRef);

                String taggedObjectSetFactorRefsAsString = safeGetTag(taggedObjectSet, TAG_TAGGED_OBJECT_REFS);
                ORefList taggedObjectSetFactorRefs = new ORefList(taggedObjectSetFactorRefsAsString);
                if (!taggedObjectSetFactorRefs.contains(wrappedFactorRef))
                {
                    taggedObjectSetFactorRefs.add(wrappedFactorRef);
                    taggedObjectSet.setData(TAG_TAGGED_OBJECT_REFS, taggedObjectSetFactorRefs.toJson().toString());
                }
            }
        }
    }

    public static final String TAG_TAGGED_OBJECT_SET_REFS = "TaggedObjectSetRefs";
    public static final String TAG_DIAGRAM_FACTOR_IDS = "DiagramFactorIds";
    public static final String TAG_TAGGED_OBJECT_REFS = "TaggedObjectRefs";
    public static final String TAG_WRAPPED_REF = "WrappedFactorRef";

    public static final int VERSION_FROM = 42;
    public static final int VERSION_TO = 43;
}