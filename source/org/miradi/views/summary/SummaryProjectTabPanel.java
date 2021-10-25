/* 
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
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

import org.miradi.actions.views.ActionViewSummary;
import org.miradi.dialogs.base.ObjectDataInputPanelWithSections;
import org.miradi.dialogs.progressReport.ExtendedProgressReportSubPanel;
import org.miradi.forms.summary.ProjectTabForm;
import org.miradi.icons.MiradiApplicationIcon;
import org.miradi.layout.OneColumnGridLayout;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.rtf.RtfFormExporter;
import org.miradi.rtf.RtfWriter;
import org.miradi.schemas.MiradiShareProjectDataSchema;
import org.miradi.schemas.ProjectMetadataSchema;

import javax.swing.*;

public class SummaryProjectTabPanel extends ObjectDataInputPanelWithSections
{
	public SummaryProjectTabPanel(MainWindow mainWindow, ORef refToUse) throws Exception
	{
		super(mainWindow.getProject(), ProjectMetadataSchema.getObjectType());
		setLayout(new OneColumnGridLayout());

		addSubPanelWithTitledBorder(new SummaryProjectPanel(mainWindow, refToUse));
		addSubPanelWithTitledBorder(new ExtendedProgressReportSubPanel(getMainWindow()));

		setObjectRefs(new ORef[]{refToUse, getProject().getSingletonObjectRef(MiradiShareProjectDataSchema.getObjectType()),});

		updateFieldsFromProject();
	}

	@Override
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
