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

package org.miradi.xml.xmpz2.xmpz2schema;

import org.miradi.objects.Measurement;
import org.miradi.schemas.BaseObjectSchema;

public class MeasurementSchemaWriter extends BaseObjectSchemaWriter
{
    public MeasurementSchemaWriter(Xmpz2XmlSchemaCreator creatorToUse, BaseObjectSchema baseObjectSchemaToUse)
    {
        super(creatorToUse, baseObjectSchemaToUse);
    }

    @Override
    protected boolean shouldOmitField(String tag)
    {
        // TODO: fields to be deprecated in post 4.5 release...only here to support migrations
        // listed here so they can be excluded from the schema

        if (tag.equals(Measurement.TAG_STATUS_CONFIDENCE))
            return true;

        return super.shouldOmitField(tag);
    }
}
