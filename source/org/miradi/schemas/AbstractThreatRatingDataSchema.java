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

package org.miradi.schemas;

import org.miradi.objects.AbstractThreatRatingData;

public abstract class AbstractThreatRatingDataSchema extends BaseObjectSchema
{
    public AbstractThreatRatingDataSchema()
    {
        super();
    }

    @Override
    protected void fillFieldSchemas()
    {
        super.fillFieldSchemas();

        createFieldSchemaRequiredRef(AbstractThreatRatingData.TAG_THREAT_REF);
        createFieldSchemaRequiredRef(AbstractThreatRatingData.TAG_TARGET_REF);
        createFieldSchemaMultiLineUserText(AbstractThreatRatingData.TAG_COMMENTS);

        // TODO: fields to be deprecated in post 4.5 release...only here to support migrations
        createFieldSchemaCodeToUserStringMap(AbstractThreatRatingData.TAG_SIMPLE_THREAT_RATING_COMMENTS_MAP);
        createFieldSchemaCodeToUserStringMap(AbstractThreatRatingData.TAG_STRESS_BASED_THREAT_RATING_COMMENTS_MAP);
    }
}
