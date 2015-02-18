/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
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

package org.miradi.xml.wcs;

import org.martus.util.UnicodeWriter;
import org.miradi.objects.BaseObject;
import org.miradi.objects.KeyEcologicalAttribute;
import org.miradi.questions.KeyEcologicalAttributeTypeQuestion;
import org.miradi.schemas.KeyEcologicalAttributeSchema;

public class KeyEcologicalAttributePoolExporter extends BaseObjectPoolExporter
{
	public KeyEcologicalAttributePoolExporter(Xmpz1XmlExporter wcsXmlExporterToUse)
	{
		super(wcsXmlExporterToUse, KEY_ECOLOGICAL_ATTRIBUTE, KeyEcologicalAttributeSchema.getObjectType());
	}
	
	@Override
	protected void exportFields(UnicodeWriter writer, BaseObject baseObject) throws Exception
	{
		super.exportFields(writer, baseObject);
		
		writeOptionalElementWithSameTag(baseObject, KeyEcologicalAttribute.TAG_SHORT_LABEL);
		writeOptionalElementWithSameTag(baseObject, KeyEcologicalAttribute.TAG_DETAILS);
		writeOptionalElementWithSameTag(baseObject, KeyEcologicalAttribute.TAG_DESCRIPTION);
		KeyEcologicalAttribute keyEcologicalAttribute = (KeyEcologicalAttribute) baseObject;
		writeOptionalIndicatorIds(keyEcologicalAttribute.getIndicatorRefs());
		writeCodeElementSameAsTag(keyEcologicalAttribute, KeyEcologicalAttribute.TAG_KEY_ECOLOGICAL_ATTRIBUTE_TYPE, new KeyEcologicalAttributeTypeQuestion());
	}
}
