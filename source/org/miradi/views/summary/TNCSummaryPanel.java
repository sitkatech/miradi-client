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
import org.miradi.forms.summary.TncTabForm;
import org.miradi.icons.TncIcon;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.ProjectMetadata;
import org.miradi.project.Project;
import org.miradi.rtf.RtfFormExporter;
import org.miradi.rtf.RtfWriter;
import org.miradi.schemas.TncProjectDataSchema;
import org.miradi.schemas.XenodataSchema;

public class TNCSummaryPanel extends ObjectDataInputPanelWithSections
{
	public TNCSummaryPanel(Project projectToUse, ProjectMetadata metadata) throws Exception
	{
		super(projectToUse, ORef.INVALID);

		ORef metadataRef = getProject().getMetadata().getRef();
		ORef tncObjectRef = getProject().getSingletonObjectRef(TncProjectDataSchema.getObjectType());
		ORef xenodataRef = getProject().getSafeSingleObjectRef(XenodataSchema.getObjectType());
		ORef[] refs = new ORef[]{metadataRef, tncObjectRef, xenodataRef};

		addSubPanelWithTitledBorder(new TncProjectSummarySubPanel(projectToUse, refs));
		addSubPanelWithTitledBorder(new TncBusinessPlanningSummarySubPanel(projectToUse, refs));
		addSubPanelWithTitledBorder(new TncCapLegacySummarySubPanel(projectToUse, refs));
	}
	
	@Override
	public String getPanelDescription()
	{
		return getTncPanelDescription();
	}
	
	public static String getTncPanelDescription()
	{
		return EAM.text("TNC"); 
	}
	
	@Override
	public Icon getIcon()
	{
		return new TncIcon();
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
		rtfFormExporter.exportForm(new TncTabForm());
	}
}
