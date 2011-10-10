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

import org.miradi.dialogs.base.AbstractObjectDataInputPanel;
import org.miradi.dialogs.base.OverlaidObjectDataInputPanel;
import org.miradi.dialogs.planning.propertiesPanel.BlankPropertiesPanel;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ThreatTargetVirtualLinkHelper;
import org.miradi.objects.Cause;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.Target;
import org.miradi.views.umbrella.ObjectPicker;

public class ThreatRatingMultiPropertiesPanel extends OverlaidObjectDataInputPanel
{
	public ThreatRatingMultiPropertiesPanel(MainWindow mainWindowToUse, ObjectPicker objectPickerToUse) throws Exception
	{
		super(mainWindowToUse, ORef.INVALID);
		
		objectPicker = objectPickerToUse;
		createPropertiesPanels();
		addPropertiesPanels();
	}
	
	@Override
	public void commandExecuted(CommandExecutedEvent event)
	{
		super.commandExecuted(event);
		if(event.isSetDataCommandWithThisTypeAndTag(ProjectMetadata.getObjectType(), ProjectMetadata.TAG_THREAT_RATING_MODE))
			reloadSelectedRefs();
	}
	
	private void createPropertiesPanels() throws Exception
	{
		simplePropertiesPanel = new SimpleThreatRatingPropertiesPanel(getMainWindow(), objectPicker);
		stressBasedPropertiesPanel = new StressBasedThreatRatingPropertiesPanel(getMainWindow(), objectPicker);
		blankPropertiesPanel = new BlankPropertiesPanel(getProject());
	}

	private void addPropertiesPanels()
	{
		addPanel(simplePropertiesPanel);
		addPanel(stressBasedPropertiesPanel);
		addPanel(blankPropertiesPanel);
	}
	
	@Override
	public String getPanelDescription()
	{
		return EAM.text("Threat Rating Properties Panel");
	}

	@Override
	protected AbstractObjectDataInputPanel findPanel(ORef[] orefsToUse)
	{
		return findPanel();
	}
	
	private AbstractObjectDataInputPanel findPanel()
	{
		ORef causeRef = getRefForType(Cause.getObjectType());
		if(causeRef.isInvalid())
			return blankPropertiesPanel;
		
		Cause cause = Cause.find(getProject(), causeRef);
		if (cause.isContributingFactor())
			return blankPropertiesPanel;
		
		ORef targetRef = getRefForType(Target.getObjectType());
		if (targetRef.isInvalid())
			return blankPropertiesPanel;
		
		if (!ThreatTargetVirtualLinkHelper.canSupportThreatRatings(getProject(), cause, targetRef))
			return blankPropertiesPanel;
		
		if (getProject().isStressBaseMode())
			return stressBasedPropertiesPanel;

		return simplePropertiesPanel;
	}
	
	private ObjectPicker objectPicker;
	
	private AbstractObjectDataInputPanel simplePropertiesPanel;
	private AbstractObjectDataInputPanel stressBasedPropertiesPanel;
	private BlankPropertiesPanel blankPropertiesPanel;
}
