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

package org.miradi.migrations.forward;

import org.miradi.main.EAM;
import org.miradi.migrations.NewlyAddedFieldsMigration;
import org.miradi.migrations.RawProject;
import org.miradi.schemas.DiagramLinkSchema;

import java.util.HashMap;

public class MigrationTo47 extends NewlyAddedFieldsMigration
{
    public MigrationTo47(RawProject rawProjectToUse)
    {
        super(rawProjectToUse, DiagramLinkSchema.getObjectType());
    }

    @Override
    protected HashMap<String, String> createFieldsToLabelMapToModify()
    {
        HashMap<String, String> fieldsToAdd = new HashMap<String, String>();
        fieldsToAdd.put(TAG_IS_UNCERTAIN_LINK, EAM.text("Diagram Link Uncertain Flag"));
        fieldsToAdd.put(TAG_ANNOTATION, EAM.text("Diagram Link Annotation"));

        return fieldsToAdd;
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
        return EAM.text("This migration adds new fields to the Diagram Link properties.");
    }

    public static final int VERSION_FROM = 46;
    public static final int VERSION_TO = 47;

    public static final String TAG_IS_UNCERTAIN_LINK = "IsUncertainLink";
    public static final String TAG_ANNOTATION = "Annotation";
}
