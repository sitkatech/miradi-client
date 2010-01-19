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

import org.miradi.dialogs.base.ObjectDataInputPanelWithSections;
import org.miradi.icons.RareIcon;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.RareProjectData;
import org.miradi.project.Project;
import org.miradi.questions.ReportTemplateContentQuestion;
import org.miradi.rtf.RtfWriter;
import org.miradi.rtf.viewExporters.SummaryViewRtfExporter;
import org.miradi.utils.CodeList;

public class RARESummaryPanel extends ObjectDataInputPanelWithSections
{
	public RARESummaryPanel(Project projectToUse) throws Exception
	{
		super(projectToUse, RareProjectData.getObjectType());
		
		addSubPanelWithTitledBorder(new RareTrackingSummarySubPanel(projectToUse));
		addSubPanelWithTitledBorder(new RareCampaignSummarySubPanel(projectToUse));
		addSubPanelWithTitledBorder(new RareCampaignPlanningSummarySubPanel(projectToUse));
		addSubPanelWithTitledBorder(new RareTeamInformationSummarySubPanel(projectToUse));

		setObjectRefs(new ORef[] {projectToUse.getSingletonObjectRef(RareProjectData.getObjectType()), projectToUse.getSingletonObjectRef(ProjectMetadata.getObjectType()),});
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Label|RARE");
	}
	
	@Override
	public Icon getIcon()
	{
		return new RareIcon();
	}
	
	@Override
	public boolean isRtfExportable()
	{
		return true;
	}
	
	@Override
	public void exportRtf(RtfWriter writer) throws Exception
	{
		SummaryViewRtfExporter viewExporter = new SummaryViewRtfExporter(getMainWindow());
		CodeList summaryCodes = new CodeList();
		summaryCodes.add(ReportTemplateContentQuestion.SUMMARY_VIEW_RARE_TAB_CODE);
		viewExporter.exportView(writer, summaryCodes);
	}
}
