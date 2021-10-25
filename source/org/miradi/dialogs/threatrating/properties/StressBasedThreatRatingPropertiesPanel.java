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
package org.miradi.dialogs.threatrating.properties;

import org.miradi.layout.OneColumnGridLayout;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Target;
import org.miradi.schemas.StressSchema;
import org.miradi.schemas.TargetSchema;
import org.miradi.schemas.ThreatStressRatingDataSchema;
import org.miradi.schemas.ThreatStressRatingSchema;
import org.miradi.views.umbrella.ObjectPicker;

import java.awt.*;

public class StressBasedThreatRatingPropertiesPanel extends AbstractThreatRatingPropertiesPanel
{
	public StressBasedThreatRatingPropertiesPanel(MainWindow mainWindowToUse, ObjectPicker objectPickerToUse) throws Exception
	{
		super(mainWindowToUse.getProject(), ObjectType.THREAT_STRESS_RATING, ThreatStressRatingDataSchema.getObjectType());
		
		setLayout(new OneColumnGridLayout());
		
		factorsPanel = new LinkPropertiesFactorsSubPanel(getProject(), mainWindowToUse.getActions());
		threatRatingDataRollupSubPanel = new ThreatRatingDataRollupSubPanel(mainWindowToUse.getProject(), mainWindowToUse.getActions());
		editorComponent = new ThreatStressRatingEditorComponent(mainWindowToUse, objectPickerToUse);

		addSubPanelWithoutTitledBorder(factorsPanel);
		addSubPanelWithoutTitledBorder(threatRatingDataRollupSubPanel);
		addSubPanelWithoutTitledBorder(new ThreatRatingDataEvidenceSubPanel(getProject(), mainWindowToUse.getActions()));
		add(editorComponent);
		
		updateFieldsFromProject();
	}
	
	@Override
	public void dispose()
	{
		super.dispose();
		editorComponent.dispose();
		editorComponent = null;
	}
	
	@Override
	public String getPanelDescription()
	{
		return EAM.text("Title|Stress-Based Threat Rating");
	}

	@Override
	public void addFieldComponent(Component component)
	{
		add(component);
	}

	@Override
	protected void setEditorEnabled(boolean isEditable)
	{
		editorComponent.setEnabled(isEditable);
	}

	@Override
	protected void setEditorObjectRefs(ORef[] hierarchyToSelectedRef)
	{
		editorComponent.refreshModel();
	}

	@Override
	public void commandExecuted(CommandExecutedEvent event)
	{
		super.commandExecuted(event);
		
		if (event.isSetDataCommandWithThisType(ThreatStressRatingSchema.getObjectType()) ||
			event.isSetDataCommandWithThisType(StressSchema.getObjectType()))
			editorComponent.fireTableDataChanged();
		
		if (event.isDeleteCommandForThisType(ThreatStressRatingSchema.getObjectType()) ||
			event.isCreateCommandForThisType(ThreatStressRatingSchema.getObjectType()) ||
			event.isSetDataCommandWithThisTypeAndTag(TargetSchema.getObjectType(), Target.TAG_STRESS_REFS)) 
			editorComponent.refreshModel();
	}
	
	private LinkPropertiesFactorsSubPanel factorsPanel;
	private ThreatRatingDataRollupSubPanel threatRatingDataRollupSubPanel;
	private ThreatStressRatingEditorComponent editorComponent;
}
