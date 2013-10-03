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

import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.dialogs.fieldComponents.HtmlFormViewer;
import org.miradi.forms.objects.MiradiShareTaxonomyDataForm;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.ProjectMetadata;
import org.miradi.project.Project;
import org.miradi.schemas.ProjectMetadataSchema;
import org.miradi.utils.FillerLabel;
import org.miradi.utils.Translation;

public class MiradiShareTaxonomyPanel extends ObjectDataInputPanel
{
	public MiradiShareTaxonomyPanel(Project projectToUse, ORef[] orefsToUse) throws Exception
	{
		super(projectToUse, orefsToUse);
		
		String html = Translation.getHtmlContent("MiradiShareSharedProjectHelpPanel.html");
		add(new HtmlFormViewer(EAM.getMainWindow(), html, EAM.getMainWindow().getHyperlinkHandler()));
		add(new FillerLabel());

		addField(createReadonlyTextField(ProjectMetadataSchema.getObjectType(), ProjectMetadata.TAG_PROJECT_NAME));
		createFieldsFromForm(new MiradiShareTaxonomyDataForm());

		updateFieldsFromProject();

	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Basics");
	}
}
