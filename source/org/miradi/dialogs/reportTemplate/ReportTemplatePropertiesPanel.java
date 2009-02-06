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
package org.miradi.dialogs.reportTemplate;

import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.icons.ReportTemplateIcon;
import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.objects.ReportTemplate;
import org.miradi.project.Project;
import org.miradi.questions.ReportTemplateContentQuestion;

public class ReportTemplatePropertiesPanel extends ObjectDataInputPanel
{
	public ReportTemplatePropertiesPanel(Project projectToUse) throws Exception
	{
		super(projectToUse, ReportTemplate.getObjectType(), BaseId.INVALID);
			
		ObjectDataInputField shortLabelField = createStringField(ReportTemplate.TAG_SHORT_LABEL, 10);
		ObjectDataInputField labelField = createExpandableField(ReportTemplate.TAG_LABEL);
		addFieldsOnOneLine(EAM.text("Report Template"), new ReportTemplateIcon(), new ObjectDataInputField[]{shortLabelField, labelField,});
		
		addField(createSingleColumnCodeListField(ReportTemplate.getObjectType(), ReportTemplate.TAG_INCLUDE_SECTION_CODES, getProject().getQuestion(ReportTemplateContentQuestion.class)));
		addField(createMultilineField(ReportTemplate.TAG_COMMENT));
		updateFieldsFromProject();
	}
	
	public String getPanelDescription()
	{
		return EAM.text("Title|Report Template Properties");
	}
}
