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

import org.miradi.objects.Assumption;
import org.miradi.objects.BaseObject;
import org.miradi.schemas.AssumptionSchema;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.xml.xmpz2.Xmpz2XmlWriter;

public class AssumptionExporter extends AbstractAssumptionExporter
{
    public AssumptionExporter(Xmpz2XmlWriter writerToUse)
    {
        super(writerToUse, AssumptionSchema.getObjectType());
    }

	@Override
	protected void writeFields(BaseObject baseObject, BaseObjectSchema baseObjectSchema) throws Exception
	{
		super.writeFields(baseObject, baseObjectSchema);

		final Assumption assumption = (Assumption) baseObject;
		writeSubAssumptionRefs(baseObjectSchema, assumption);
	}

	@Override
	protected boolean doesFieldRequireSpecialHandling(String tag)
	{
		if (tag.equals(Assumption.TAG_SUB_ASSUMPTION_IDS))
			return true;

		return super.doesFieldRequireSpecialHandling(tag);
	}

	private void writeSubAssumptionRefs(BaseObjectSchema baseObjectSchema, final Assumption assumption) throws Exception
	{
		getWriter().writeReflist(baseObjectSchema.getObjectName() + ASSUMPTION_IDS, SUB_ASSUMPTION, assumption.getSubAssumptionRefs());
	}
}
