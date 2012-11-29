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

package org.miradi.schemas;

import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.ReportTemplate;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.ReportTemplateContentQuestion;

public class ReportTemplateSchema extends BaseObjectSchema
{
	public ReportTemplateSchema(final Project projectToUse)
	{
		super();
		
		project = projectToUse;
	}
	
	@Override
	protected void fillFieldSchemas()
	{
		super.fillFieldSchemas();
		
		createFieldSchemaSingleLineUserText(ReportTemplate.TAG_SHORT_LABEL);
		ChoiceQuestion reportContentsQuestion = new ReportTemplateContentQuestion(getProject());
		createFieldSchemaCodeList(ReportTemplate.TAG_INCLUDE_SECTION_CODES, reportContentsQuestion);
		createFieldSchemaMultiLineUserText(ReportTemplate.TAG_COMMENTS);
	}
	
	private Project getProject()
	{
		return project;
	}
	
	public static int getObjectType()
	{
		return ObjectType.REPORT_TEMPLATE;
	}
	
	@Override
	public int getType()
	{
		return getObjectType();
	}

	@Override
	public String getObjectName()
	{
		return OBJECT_NAME;
	}

	private Project project;
	public static final String OBJECT_NAME = "ReportTemplate";
}
