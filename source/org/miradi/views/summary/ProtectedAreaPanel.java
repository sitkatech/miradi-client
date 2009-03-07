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
package org.miradi.views.summary;

import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.WcpaProjectData;
import org.miradi.project.Project;
import org.miradi.questions.ProtectedAreaCategoryQuestion;

public class ProtectedAreaPanel extends ObjectDataInputPanel
{
	public ProtectedAreaPanel(Project projectToUse, ORef[] orefsToUse)
	{
		super(projectToUse, orefsToUse);
		
		ObjectDataInputField protectedAreaStatusField = createCodeListField(ProjectMetadata.TAG_PROTECTED_AREA_CATEGORIES, new ProtectedAreaCategoryQuestion(), 1);
		ObjectDataInputField protectedAreaStatusNotesField = createMultilineField(ProjectMetadata.getObjectType(), ProjectMetadata.TAG_PROTECTED_AREA_CATEGORY_NOTES, 25);
		addFieldsOnOneLine(EAM.text("Label|Protected Area Categories"), new ObjectDataInputField[]{protectedAreaStatusField, protectedAreaStatusNotesField});
		
		addField(createMultilineField(WcpaProjectData.getObjectType(), WcpaProjectData.TAG_LEGAL_STATUS));
		addField(createMultilineField(WcpaProjectData.getObjectType(), WcpaProjectData.TAG_LEGISLATIVE));
		addField(createMultilineField(WcpaProjectData.getObjectType(), WcpaProjectData.TAG_PHYSICAL_DESCRIPTION));
		addField(createMultilineField(WcpaProjectData.getObjectType(), WcpaProjectData.TAG_BIOLOGICAL_DESCRIPTION));
		addField(createMultilineField(WcpaProjectData.getObjectType(), WcpaProjectData.TAG_SOCIO_ECONOMIC_INFORMATION));
		addField(createMultilineField(WcpaProjectData.getObjectType(), WcpaProjectData.TAG_HISTORICAL_DESCRIPTION));
		addField(createMultilineField(WcpaProjectData.getObjectType(), WcpaProjectData.TAG_CULTURAL_DESCRIPTION));
		addField(createMultilineField(WcpaProjectData.getObjectType(), WcpaProjectData.TAG_ACCESS_INFORMATION));
		addField(createMultilineField(WcpaProjectData.getObjectType(), WcpaProjectData.TAG_VISITATION_INFORMATION));
		addField(createMultilineField(WcpaProjectData.getObjectType(), WcpaProjectData.TAG_CURRENT_LAND_USES));
		addField(createMultilineField(WcpaProjectData.getObjectType(), WcpaProjectData.TAG_MANAGEMENT_RESOURCES));				
						

	}

	@Override
	public String getPanelDescription()
	{
		return PANEL_DESCRIPTION; 
	}

	public static final String PANEL_DESCRIPTION = EAM.text("Protected Area Information");
}
