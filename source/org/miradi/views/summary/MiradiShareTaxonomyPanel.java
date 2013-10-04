/* 
Copyright 2005-2013, Foundations of Success, Bethesda, Maryland 
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

import java.util.HashMap;

import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.dialogs.fieldComponents.HtmlFormViewer;
import org.miradi.forms.objects.MiradiShareTaxonomyDataForm;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.MiradiShareProjectData;
import org.miradi.project.Project;
import org.miradi.schemas.MiradiShareProjectDataSchema;
import org.miradi.utils.FillerLabel;
import org.miradi.utils.Translation;

public class MiradiShareTaxonomyPanel extends ObjectDataInputPanel
{
	public MiradiShareTaxonomyPanel(Project projectToUse, ORef[] orefsToUse) throws Exception
	{
		super(projectToUse, orefsToUse);
		
		String html = Translation.getHtmlContent("MiradiShareSharedProjectHelpPanel.html");
		html = EAM.substitute(html, getMiradiShareProjectUrl());
		add(new FillerLabel());
		add(new HtmlFormViewer(EAM.getMainWindow(), html, EAM.getMainWindow().getHyperlinkHandler()));

		createFieldsFromForm(new MiradiShareTaxonomyDataForm());

		updateFieldsFromProject();
	}

	private String getMiradiShareProjectUrl()
	{
		ORef miradiShareProjectDataRef = getProject().getSingletonObjectRef(MiradiShareProjectDataSchema.getObjectType());
		MiradiShareProjectData miradiShareProjectData = MiradiShareProjectData.find(getProject(), miradiShareProjectDataRef);
		String projectUrl = miradiShareProjectData.getData(MiradiShareProjectData.TAG_PROJECT_URL);
		String programName = miradiShareProjectData.getData(MiradiShareProjectData.TAG_PROGRAM_NAME);
		if (projectUrl.length() == 0)
			return programName;
		
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%projectUrl", projectUrl);
		tokenReplacementMap.put("%programName", programName);
		
		return EAM.substitute("<a href=\"%projectUrl\">%programName</a>", tokenReplacementMap);
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Basics");
	}
}