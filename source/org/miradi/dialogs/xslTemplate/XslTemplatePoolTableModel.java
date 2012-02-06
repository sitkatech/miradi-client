/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.dialogs.xslTemplate;

import org.miradi.dialogs.base.ObjectPoolTableModel;
import org.miradi.objects.ReportTemplate;
import org.miradi.objects.XslTemplate;
import org.miradi.project.Project;

public class XslTemplatePoolTableModel extends ObjectPoolTableModel
{
	public XslTemplatePoolTableModel(Project projectToUse)
	{
		super(projectToUse, XslTemplate.getObjectType(), COLUMN_TAGS);
	}
	
	@Override
	public String getUniqueTableModelIdentifier()
	{
		return UNIQUE_MODEL_IDENTIFIER;
	}
	
	private static final String UNIQUE_MODEL_IDENTIFIER = "XslTemplatePoolTableModel";

	public static final String[] COLUMN_TAGS = new String[] {
		ReportTemplate.TAG_LABEL,
	};
}
