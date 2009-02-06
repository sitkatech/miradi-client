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

import org.miradi.actions.views.ActionViewSummary;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.forms.summary.ProjectTabForm;
import org.miradi.icons.MiradiApplicationIcon;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.ProjectMetadata;
import org.miradi.project.Project;
import org.miradi.rtf.RtfFormExporter;
import org.miradi.rtf.RtfWriter;

public class SummaryProjectPanel extends ObjectDataInputPanel
{
	public SummaryProjectPanel(Project projectToUse, ORef refToUse)
	{
		super(projectToUse, refToUse);
		
		addField(createStringField(ProjectMetadata.TAG_PROJECT_NAME));
		addField(createDateChooserField(ProjectMetadata.TAG_DATA_EFFECTIVE_DATE));
		addField(createReadonlyTextField(ProjectMetadata.PSEUDO_TAG_PROJECT_FILENAME));
		addBlankHorizontalLine();
		
		addField(createShortStringField(ProjectMetadata.TAG_OTHER_ORG_PROJECT_NUMBER));
		addField(createStringField(ProjectMetadata.TAG_OTHER_ORG_RELATED_PROJECTS));
		addField(createStringField(ProjectMetadata.TAG_PROJECT_URL));
		addField(createMultilineField(ProjectMetadata.TAG_PROJECT_DESCRIPTION));
		
		addField(createMultilineField(ProjectMetadata.TAG_PROJECT_STATUS));
		addField(createMultilineField(ProjectMetadata.TAG_NEXT_STEPS));
		
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return PANEL_DESCRIPTION;
	}
	
	@Override
	public Icon getIcon()
	{
		return new MiradiApplicationIcon();
	}
	
	@Override
	public Class getJumpActionClass()
	{
		return ActionViewSummary.class;
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
		rtfFormExporter.exportForm(new ProjectTabForm());
	}
	
	public static final String PANEL_DESCRIPTION = EAM.text("Project");
}
