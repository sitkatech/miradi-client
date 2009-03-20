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

package org.miradi.dialogs.threatrating.properties;

import org.miradi.actions.Actions;
import org.miradi.diagram.factortypes.FactorType;
import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.layout.OneRowGridLayout;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Cause;
import org.miradi.objects.Factor;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.Target;
import org.miradi.project.Project;

//TODO need to rename and remove link from name
public class LinkPropertiesFactorsSubpanel extends ObjectDataInputPanel
{
	public LinkPropertiesFactorsSubpanel(Project projectToUse, Actions actions) throws Exception
	{
		super(projectToUse, ORef.INVALID);
		final OneRowGridLayout layout = new OneRowGridLayout();
		layout.setGaps(10);
		setLayout(layout);
		
		threatLabel = new PanelTitleLabel();
		threatNameField = createExpandableField(ObjectType.FAKE, Factor.TAG_LABEL);
		threatNameField.setEditable(false);
		addFieldWithCustomLabel(threatNameField, threatLabel);

		targetLabel = new PanelTitleLabel();
		targetNameField = createExpandableField(ObjectType.FAKE, Factor.TAG_LABEL);
		targetNameField.setEditable(false);
		addFieldWithCustomLabel(targetNameField, targetLabel);

		updateFieldsFromProject();
	}
	
	@Override
	public void commandExecuted(CommandExecutedEvent event)
	{
		super.commandExecuted(event);
		if(event.isSetDataCommandWithThisTypeAndTag(ProjectMetadata.getObjectType(), ProjectMetadata.TAG_THREAT_RATING_MODE))
		{
			setObjectRefs(getSelectedRefs());
		}
	}
	
	@Override
	public void setObjectRefs(ORef[] orefsToUse)
	{
		updateFieldLabels(orefsToUse);
		super.setObjectRefs(orefsToUse);
	}

	private void updateFieldLabels(ORef[] orefsToUse)
	{
		ORefList refs = new ORefList(orefsToUse);
		ORef threatRef = refs.getRefForType(Cause.getObjectType());
		ORef targetRef = refs.getRefForType(Target.getObjectType());		
		if(threatRef.isInvalid() || targetRef.isInvalid())
		{
			threatLabel.setText("");
			threatLabel.setIcon(null);
			threatNameField.setObjectType(ObjectType.FAKE);
			targetLabel.setText("");
			targetLabel.setIcon(null);
			targetNameField.setObjectType(ObjectType.FAKE);
			return;
		}
		
		try
		{
			Factor threat = Factor.findFactor(getProject(), threatRef);
			threatLabel.setText(FactorType.getFactorTypeLabel(threat));
			threatLabel.setIcon(FactorType.getFactorIcon(threat));
			threatNameField.setObjectType(threat.getType());

			Factor target = Factor.findFactor(getProject(), targetRef);
			targetLabel.setText(FactorType.getFactorTypeLabel(target));
			targetLabel.setIcon(FactorType.getFactorIcon(target));
			targetNameField.setObjectType(target.getType());
		}
		catch(Exception e)
		{
			EAM.panic(e);
		}
	}

	public String getPanelDescription()
	{
		return "LinkPropertiesFactorsSubpanel";
	}
	
	private PanelTitleLabel threatLabel;
	private PanelTitleLabel targetLabel;
	private ObjectDataInputField threatNameField;
	private ObjectDataInputField targetNameField;
}
