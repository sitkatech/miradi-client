/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.dialogs.audience;

import org.miradi.dialogs.base.EditableObjectPoolRefsTableModel;
import org.miradi.objects.Audience;
import org.miradi.project.Project;

public class AudienceEditablePoolTableModel extends EditableObjectPoolRefsTableModel
{
	public AudienceEditablePoolTableModel(Project projectToUse)
	{
		super(projectToUse);
	}

	@Override
	public String getUniqueTableModelIdentifier()
	{
		return "AudienceEditablePoolTableModel";
	}

	@Override
	protected int getObjectType()
	{
		return Audience.getObjectType();
	}
	
	public boolean isPeopleCountColumn(int modelColumn)
	{
		return isColumnForTag(modelColumn, Audience.TAG_PEOPLE_COUNT);
	}
	
	public boolean isSummaryColumn(int modelColumn)
	{
		return isColumnForTag(modelColumn, Audience.TAG_SUMMARY);
	}

	@Override
	protected String[] getColumnTags()
	{
		return new String[]{
				Audience.TAG_LABEL,
				Audience.TAG_PEOPLE_COUNT,
				Audience.TAG_SUMMARY,
			};
	}
}
