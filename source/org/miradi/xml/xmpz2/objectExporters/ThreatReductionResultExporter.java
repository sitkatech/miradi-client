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

package org.miradi.xml.xmpz2.objectExporters;

import org.miradi.objects.BaseObject;
import org.miradi.objects.ThreatReductionResult;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.schemas.ThreatReductionResultSchema;
import org.miradi.xml.xmpz2.BaseObjectExporter;
import org.miradi.xml.xmpz2.Xmpz2XmlWriter;

public class ThreatReductionResultExporter extends BaseObjectExporter
{
	public ThreatReductionResultExporter(Xmpz2XmlWriter writerToUse)
	{
		super(writerToUse, ThreatReductionResultSchema.getObjectType());
	}
	
	@Override
	protected boolean doesFieldRequireSpecialHandling(String tag)
	{
		if (tag.equals(ThreatReductionResult.TAG_RELATED_DIRECT_THREAT_REF))
			return true;
		
		return super.doesFieldRequireSpecialHandling(tag);
	}
	
	@Override
	protected void writeFields(final BaseObject baseObject,	BaseObjectSchema baseObjectSchema) throws Exception
	{
		super.writeFields(baseObject, baseObjectSchema);
		
		ThreatReductionResult threatReductionResult = (ThreatReductionResult) baseObject;
		getWriter().writeRef(threatReductionResult.getRelatedThreatRef(), baseObjectSchema.getXmpz2ElementName() +  RELATED_THREAT_ID, THREAT_ID);
	}	
}
