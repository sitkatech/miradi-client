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

import javax.swing.Icon;

import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.forms.summary.LocationTabForm;
import org.miradi.icons.LocationIcon;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.ProjectMetadata;
import org.miradi.project.Project;
import org.miradi.questions.CountriesQuestion;
import org.miradi.questions.QuestionManager;
import org.miradi.rtf.RtfFormExporter;
import org.miradi.rtf.RtfWriter;

public class SummaryLocationPanel extends ObjectDataInputPanel
{
	public SummaryLocationPanel(Project projectToUse, ORef orefToUse)
	{
		super(projectToUse, orefToUse);
		
		addFieldWithCustomLabelAndHint(createNumericField(ProjectMetadata.TAG_PROJECT_LATITUDE, 10), EAM.text("(Latitude must be -90.0000 to +90.0000)"));
		addFieldWithCustomLabelAndHint(createNumericField(ProjectMetadata.TAG_PROJECT_LONGITUDE, 10), EAM.text("(Longitude must be -180.0000 to +180.0000)"));
		
		addField(createCodeListField(ProjectMetadata.getObjectType(), ProjectMetadata.TAG_COUNTRIES, QuestionManager.getQuestion(CountriesQuestion.class), 1));
		addField(createStringField(ProjectMetadata.TAG_STATE_AND_PROVINCES));
		addField(createStringField(ProjectMetadata.TAG_MUNICIPALITIES));
		addField(createStringField(ProjectMetadata.TAG_LEGISLATIVE_DISTRICTS));
		addField(createMultilineField(ProjectMetadata.TAG_LOCATION_DETAIL));
		addField(createStringField(ProjectMetadata.TAG_SITE_MAP_REFERENCE));
		addField(createMultilineField(ProjectMetadata.TAG_LOCATION_COMMENTS));
		
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return PANEL_DESCRIPTION;
	}
	
	@Override
	public Icon getIcon()
	{
		return new LocationIcon();
	}
	
	@Override
	public boolean isRtfExportable()
	{
		return true;
	}
	
	@Override
	public void exportRtf(RtfWriter writer) throws Exception
	{
		RtfFormExporter rtfFormExporter = new RtfFormExporter(getProject(), writer, getSelectedRefs());
		rtfFormExporter.exportForm(new LocationTabForm());
	}
	
	public static final String PANEL_DESCRIPTION = EAM.text("Location");
}
