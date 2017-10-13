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

package org.miradi.views.summary;

import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.forms.objects.MiradiShareTaxonomyDataForm;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.MiradiShareProjectData;
import org.miradi.project.Project;
import org.miradi.schemas.MiradiShareProjectDataSchema;
import org.miradi.utils.Translation;

public class MiradiShareBasicsPanel extends ObjectDataInputPanel
{
	public MiradiShareBasicsPanel(Project projectToUse, ORef[] orefsToUse) throws Exception
	{
		super(projectToUse, orefsToUse);
		
		String upperHelpContent = getUpperHelpContent();
		String lowerHelpContent = getLowerHelpContent();
		createFieldsFromForm(new MiradiShareTaxonomyDataForm(upperHelpContent, lowerHelpContent));

		updateFieldsFromProject();
	}
	
	private String getLowerHelpContent() throws Exception
	{
		return Translation.getHtmlContent("MiradiShareProjectHelpLowerText.html");
	}

	private String getUpperHelpContent() throws Exception
	{
		String html = Translation.getHtmlContent("MiradiShareProjectHelpUpperText.html");

		return  EAM.substituteSingleString(html, getMiradiShareProjectUrl());
	}

	private String getMiradiShareProjectUrl()
	{
		ORef miradiShareProjectDataRef = getProject().getSingletonObjectRef(MiradiShareProjectDataSchema.getObjectType());
		MiradiShareProjectData miradiShareProjectData = MiradiShareProjectData.find(getProject(), miradiShareProjectDataRef);
		String programUrl = miradiShareProjectData.getData(MiradiShareProjectData.TAG_PROGRAM_URL);
		String programName = miradiShareProjectData.getData(MiradiShareProjectData.TAG_PROGRAM_NAME);
		if (programUrl.length() == 0)
			return programName;
		
		return "<a href=\"" + programUrl + "\">" + programName + "</a>";
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Basics");
	}
}
