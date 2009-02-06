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
package org.miradi.dialogs.progressPercent;

import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.objects.ProgressPercent;
import org.miradi.project.Project;

public class ProgressPercentPropertiesPanel extends ObjectDataInputPanel
{
	public ProgressPercentPropertiesPanel(Project projectToUse) throws Exception
	{
		super(projectToUse, ProgressPercent.getObjectType(), BaseId.INVALID);
			
		addField(createDateChooserField(ProgressPercent.TAG_DATE));
		addField(createNumericField(ProgressPercent.TAG_PERCENT_COMPLETE));
		addField(createMultilineField(ProgressPercent.TAG_PERCENT_COMPLETE_NOTES));
		
		updateFieldsFromProject();
	}
	
	public String getPanelDescription()
	{
		return EAM.text("Title|Progress Percent Properties");
	}
}
