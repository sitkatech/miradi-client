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

import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.forms.summary.WcsTabForm;
import org.miradi.icons.WcsIcon;
import org.miradi.main.EAM;
import org.miradi.objects.WcsProjectData;
import org.miradi.project.Project;
import org.miradi.rtf.RtfFormExporter;
import org.miradi.rtf.RtfWriter;

public class WCSSummaryPanel extends ObjectDataInputPanel
{

	public WCSSummaryPanel(Project projectToUse)
	{
		super(projectToUse, projectToUse.getSingletonObjectRef(WcsProjectData.getObjectType()));

		addField(createStringField(WcsProjectData.TAG_ORGANIZATIONAL_FOCUS));
		addField(createStringField(WcsProjectData.TAG_ORGANIZATIONAL_LEVEL));
		
		ObjectDataInputField swotCompletedField = createCheckBoxField(WcsProjectData.TAG_SWOT_COMPLETED);
		ObjectDataInputField swotUrlField = createStringField(WcsProjectData.TAG_SWOT_URL);
		addFieldsOnOneLine(EAM.text("SWOT"), new ObjectDataInputField[]{swotCompletedField, swotUrlField});
		
		ObjectDataInputField stepCompletedField = createCheckBoxField(WcsProjectData.TAG_STEP_COMPLETED);
		ObjectDataInputField stepUrlField = createStringField(WcsProjectData.TAG_STEP_URL);
		addFieldsOnOneLine(EAM.text("STEP"), new ObjectDataInputField[]{stepCompletedField, stepUrlField});
		
		updateFieldsFromProject();
	}
	
	public String getPanelDescription()
	{
		return PANEL_DESCRIPTION;
	}
	
	public Icon getIcon()
	{
		return new WcsIcon();
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
		rtfFormExporter.exportForm(new WcsTabForm());
	}
	
	public static final String PANEL_DESCRIPTION = EAM.text("Label|WCS");
}
