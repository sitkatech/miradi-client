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

import javax.swing.Icon;

import org.miradi.dialogs.base.ObjectDataInputPanelWithSections;
import org.miradi.forms.objects.MiradiShareProjectDataForm;
import org.miradi.icons.MiradiShareIcon;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.project.Project;
import org.miradi.rtf.RtfFormExporter;
import org.miradi.rtf.RtfWriter;
import org.miradi.schemas.MiradiShareProjectDataSchema;

public class SummaryMiradiSharePanel extends ObjectDataInputPanelWithSections
{
	public SummaryMiradiSharePanel(Project projectToUse) throws Exception
	{
		super(projectToUse, MiradiShareProjectDataSchema.getObjectType());
		
		addSubPanelWithTitledBorder(new MiradiShareTaxonomyPanel(projectToUse, getSingletonRefs(projectToUse)));
		addSubPanelWithTitledBorder(new MiradiShareDetailsPanel(projectToUse, getSingletonRefs(projectToUse)));
		
		updateFieldsFromProject();
	}

	private static ORef[] getSingletonRefs(Project projectToUse)
	{
		ORef miradiShareProjectDataRef = projectToUse.getSafeSingleObjectRef(MiradiShareProjectDataSchema.getObjectType());
		ORef metadataRef = projectToUse.getMetadata().getRef();
		
		return new ORef[]{metadataRef, miradiShareProjectDataRef, };
	}

	@Override
	public String getPanelDescription()
	{
		return getPlanningPanelDescription();
	}

	public static String getPlanningPanelDescription()
	{
		return EAM.text("Label|Miradi Share");
	}
	
	@Override
	public Icon getIcon()
	{
		return new MiradiShareIcon();
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
		rtfFormExporter.exportForm(createMiradiShareProjectDataForm());
	}
	
	private static MiradiShareProjectDataForm createMiradiShareProjectDataForm()
	{
		return new MiradiShareProjectDataForm();
	}
}
