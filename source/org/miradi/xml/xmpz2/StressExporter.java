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

package org.miradi.xml.xmpz2;

import org.miradi.objects.BaseObject;
import org.miradi.objects.Stress;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.schemas.StressSchema;

public class StressExporter extends BaseObjectExporter
{
	public StressExporter(Xmpz2XmlWriter writerToUse)
	{
		super(writerToUse, StressSchema.getObjectType());
	}
		
	@Override
	protected void writeFields(BaseObject baseObject, BaseObjectSchema baseObjectSchema) throws Exception
	{
		Stress stress = (Stress) baseObject;
		String rating = stress.getCalculatedStressRating();
		getWriter().writeCodeElement(baseObjectSchema.getXmpz2ElementName(), CALCULATED_STRESS_RATING, rating);
		
		super.writeFields(baseObject, baseObjectSchema);
	}
}
