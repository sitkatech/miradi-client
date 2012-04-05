/* 
Copyright 2005-2012, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

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

import org.miradi.objectdata.BooleanData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.objects.TncProjectData;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.schemas.TncProjectDataSchema;
import org.miradi.xml.generic.XmlSchemaCreator;

public class ProjectMetadataExporter extends BaseObjectExporter
{
	public ProjectMetadataExporter(Xmpz2XmlUnicodeWriter writerToUse)
	{
		super(writerToUse);
	}
	
	@Override
	protected void writeFields(BaseObject baseObject, BaseObjectSchema baseObjectSchema) throws Exception
	{
		super.writeFields(baseObject, baseObjectSchema);
		
		writeShareOutsideOrganizationElement();
	}
	
	private void writeShareOutsideOrganizationElement() throws Exception
	{
		String shareOutSideOfTnc = "0";
		if (getTncProjectData().canShareOutsideOfTnc())
			shareOutSideOfTnc = BooleanData.BOOLEAN_TRUE;
		
		getWriter().writeElement(PROJECT_SUMMARY + XmlSchemaCreator.PROJECT_SHARE_OUTSIDE_ORGANIZATION, shareOutSideOfTnc);
	}

	private TncProjectData getTncProjectData()
	{
		final ORef ref = getWriter().getProject().getSingletonObjectRef(TncProjectDataSchema.getObjectType());
		return TncProjectData.find(getWriter().getProject(), ref);
	}
}
