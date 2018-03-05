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
package org.miradi.objects;

import org.miradi.ids.BaseId;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.schemas.*;

public class ResultReport extends BaseObject
{
    public ResultReport(ObjectManager objectManager, BaseId idToUse, final BaseObjectSchema schema)
    {
        super(objectManager, idToUse, schema);
    }

    public ResultReport(ObjectManager objectManager, BaseId idToUse)
    {
        this(objectManager, idToUse, createSchema(objectManager));
    }

    public static ResultReportSchema createSchema(Project projectToUse)
    {
        return createSchema(projectToUse.getObjectManager());
    }

    public static ResultReportSchema createSchema(ObjectManager objectManager)
    {
        return (ResultReportSchema) objectManager.getSchemas().get(ObjectType.RESULT_REPORT);
    }

    public String getDateAsString()
    {
        return getData(TAG_RESULT_DATE);
    }

    @Override
    public String toString()
    {
        return getDateAsString() + ": " + getResultStatusChoice().getLabel();
    }

    @Override
    public String getFullName()
    {
        return toString();
    }

    @Override
    public int[] getTypesThatCanOwnUs()
    {
        return new int[] {
                IntermediateResultSchema.getObjectType(),
                ThreatReductionResultSchema.getObjectType(),
                BiophysicalResultSchema.getObjectType(),
        };
    }

    public ChoiceItem getResultStatusChoice()
    {
        return getChoiceItemData(TAG_RESULT_STATUS);
    }

    public String getDetails()
    {
        return getStringData(TAG_DETAILS);
    }

    public static boolean is(ORef ref)
    {
        return is(ref.getObjectType());
    }

    public static boolean is(int objectType)
    {
        return objectType == ResultReportSchema.getObjectType();
    }

    public static boolean is(BaseObject baseObject)
    {
        return is(baseObject.getType());
    }

    public static ResultReport find(ObjectManager objectManager, ORef resultReportRef)
    {
        return (ResultReport) objectManager.findObject(resultReportRef);
    }

    public static ResultReport find(Project project, ORef resultReportRef)
    {
        return find(project.getObjectManager(), resultReportRef);
    }

    public static final String TAG_RESULT_STATUS = "ResultStatus";
    public static final String TAG_RESULT_DATE = "ResultDate";
    public static final String TAG_DETAILS = "Details";
}

