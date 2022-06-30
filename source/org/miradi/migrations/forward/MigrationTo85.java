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

import org.miradi.main.EAM;
import org.miradi.migrations.*;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.questions.TextBoxZOrderQuestion;
import org.miradi.utils.EnhancedJsonObject;

import java.util.Vector;

public class MigrationTo85 extends AbstractMigration
{
    public MigrationTo85(RawProject rawProjectToUse)
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

        Vector<Integer> typesToVisit = getTypesToMigrate();

        for(Integer typeToVisit : typesToVisit)
        {
            final ZOrderVisitor visitor = new ZOrderVisitor(typeToVisit, reverseMigration);
            visitAllORefsInPool(visitor);
            final MigrationResult thisMigrationResult = visitor.getMigrationResult();
            if (migrationResult == null)
                migrationResult = thisMigrationResult;
            else
                migrationResult.merge(thisMigrationResult);
        }

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
        return EAM.text("This migration removes the Textbox position field and adds a z-order field to Diagram Factors and Links.");
    }

    private Vector<Integer> getTypesToMigrate()
    {
        Vector<Integer> typesToMigrate = new Vector<Integer>();

        typesToMigrate.add(ObjectType.DIAGRAM_FACTOR);
        typesToMigrate.add(ObjectType.DIAGRAM_LINK);

        return typesToMigrate;
    }

    private class ZOrderVisitor extends AbstractMigrationORefVisitor
    {
        public ZOrderVisitor(int typeToVisit, boolean reverseMigration)
        {
            type = typeToVisit;
            isReverseMigration = reverseMigration;
        }

        public int getTypeToVisit()
        {
            return type;
        }

        @Override
        public MigrationResult internalVisit(ORef rawObjectRef) throws Exception
        {
            MigrationResult migrationResult = MigrationResult.createUninitializedResult();

            RawObject rawObject = getRawProject().findObject(rawObjectRef);
            if (rawObject != null)
            {
                if (isReverseMigration)
                    migrationResult = removeFields(rawObject);
                else
                    migrationResult = addFields(rawObject);
            }

            return migrationResult;
        }

        private MigrationResult addFields(RawObject rawObject) throws Exception
        {
            MigrationResult migrationResult = MigrationResult.createSuccess();

            rawObject.setData(TAG_Z_INDEX, deriveZIndex(rawObject));

            if (rawObject.hasValue(TAG_TEXT_BOX_Z_ORDER_CODE))
                rawObject.remove(TAG_TEXT_BOX_Z_ORDER_CODE);

            return migrationResult;
        }

        private MigrationResult removeFields(RawObject rawObject) throws Exception
        {
            MigrationResult migrationResult = MigrationResult.createSuccess();

            if (!rawObject.hasValue(TAG_TEXT_BOX_Z_ORDER_CODE))
                rawObject.setData(TAG_TEXT_BOX_Z_ORDER_CODE, TextBoxZOrderQuestion.DEFAULT_Z_ORDER);

            if (rawObject.hasValue(TAG_Z_INDEX))
                rawObject.remove(TAG_Z_INDEX);

            return migrationResult;
        }

        private String safeGetTag(RawObject rawObject, String tag)
        {
            if (rawObject.hasValue(tag))
                return rawObject.getData(tag);

            return "";
        }

        private String deriveZIndex(RawObject rawObject) throws Exception
        {
            String defaultZIndex = "3";

            if (getTypeToVisit() == ObjectType.DIAGRAM_LINK)
                return "4";

            if (getTypeToVisit() == ObjectType.DIAGRAM_FACTOR)
            {
                String wrappedRefAsRawString = safeGetTag(rawObject, TAG_WRAPPED_REF);
                EnhancedJsonObject wrappedRefAsString = new EnhancedJsonObject(wrappedRefAsRawString);
                ORef wrappedRef = new ORef(wrappedRefAsString);

                if (wrappedRef.getObjectType() == ObjectType.SCOPE_BOX)
                    return "1";

                if (wrappedRef.getObjectType() == ObjectType.GROUP_BOX)
                    return "2";

                if (wrappedRef.getObjectType() == ObjectType.TASK)
                    return "5";

                if (wrappedRef.getObjectType() == ObjectType.STRESS)
                    return "5";

                if (wrappedRef.getObjectType() == ObjectType.TEXT_BOX)
                {
                    String textBoxZOrder = safeGetTag(rawObject, TAG_TEXT_BOX_Z_ORDER_CODE);
                    if (textBoxZOrder.equals(TextBoxZOrderQuestion.FRONT_CODE))
                        return "6";
                    else
                        return "0";
                }
            }

            return defaultZIndex;
        }

        private int type;
        private boolean isReverseMigration;
    }

    public static final int VERSION_FROM = 84;
    public static final int VERSION_TO = 85;

    public static final String TAG_Z_INDEX = "ZIndex";
    public static final String TAG_TEXT_BOX_Z_ORDER_CODE = "TextBoxZOrderCode";
    public static final String TAG_WRAPPED_REF = "WrappedFactorRef";
}
