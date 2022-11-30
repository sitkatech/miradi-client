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

package org.miradi.xml.xmpz2.objectExporters;

import org.miradi.objects.AbstractAssumption;
import org.miradi.objects.BaseObject;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.xml.xmpz2.BaseObjectExporter;
import org.miradi.xml.xmpz2.Xmpz2XmlWriter;

abstract public class AbstractAssumptionExporter extends BaseObjectExporter
{
    public AbstractAssumptionExporter(final Xmpz2XmlWriter writerToUse, final int objectTypeToUse)
    {
        super(writerToUse, objectTypeToUse);
    }

	@Override
	protected void writeFields(final BaseObject baseObject, BaseObjectSchema baseObjectSchema) throws Exception
	{
		super.writeFields(baseObject, baseObjectSchema);

		final AbstractAssumption analyticalQuestionOrSubAssumption = (AbstractAssumption) baseObject;
		final String objectName = baseObjectSchema.getObjectName();

		writeRelevantIndicatorIds(objectName, analyticalQuestionOrSubAssumption);
	}

	@Override
	protected boolean doesFieldRequireSpecialHandling(final String tag)
	{
		if (tag.equals(AbstractAssumption.TAG_INDICATOR_IDS))
			return true;

		return super.doesFieldRequireSpecialHandling(tag);
	}

	private void writeRelevantIndicatorIds(final String objectName, AbstractAssumption analyticalQuestionOrSubAssumption) throws Exception
	{
		getWriter().writeReflist(objectName, RELEVANT_INDICATOR_IDS, INDICATOR, analyticalQuestionOrSubAssumption.getRelevantIndicatorRefList());
	}
}
