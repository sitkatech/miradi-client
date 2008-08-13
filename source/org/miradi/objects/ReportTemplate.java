/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.objects;

import org.miradi.ids.BaseId;
import org.miradi.objectdata.StringData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.utils.EnhancedJsonObject;

public class ReportTemplate extends BaseObject
{
	public ReportTemplate(ObjectManager objectManager, BaseId idToUse)
	{
		super(objectManager, idToUse);
		clear();
	}
		
	public ReportTemplate(ObjectManager objectManager, int idAsInt, EnhancedJsonObject json) throws Exception
	{
		super(objectManager, new BaseId(idAsInt), json);
	}
	
	public int getType()
	{
		return getObjectType();
	}
	
	public String getTypeName()
	{
		return OBJECT_NAME;
	}

	public static int getObjectType()
	{
		return ObjectType.REPORT_TEMPLATE;
	}
	
	public static boolean is(ORef ref)
	{
		return is(ref.getObjectType());
	}
	
	public static boolean is(int objectType)
	{
		return objectType == getObjectType();
	}
	
	public static ReportTemplate find(ObjectManager objectManager, ORef eportTemplateRef)
	{
		return (ReportTemplate) objectManager.findObject(eportTemplateRef);
	}
	
	public static ReportTemplate find(Project project, ORef eportTemplateRef)
	{
		return find(project.getObjectManager(), eportTemplateRef);
	}

	@Override
	void clear()
	{
		super.clear();

		shortLabel = new StringData(TAG_SHORT_LABEL);
		comment = new StringData(TAG_COMMENT);

		addField(TAG_SHORT_LABEL, shortLabel);
		addField(TAG_COMMENT, comment);
	}

	public static final String TAG_SHORT_LABEL = "ShortLabel";	
	public static final String TAG_COMMENT = "Comment";

	private StringData shortLabel;	
	private StringData comment;
	
	public static final String OBJECT_NAME = "ReportTemplate";
}
