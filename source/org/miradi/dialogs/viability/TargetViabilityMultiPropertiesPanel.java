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
package org.miradi.dialogs.viability;

import java.awt.CardLayout;
import java.awt.Rectangle;

import org.miradi.dialogs.base.AbstractObjectDataInputPanel;
import org.miradi.dialogs.base.OverlaidObjectDataInputPanel;
import org.miradi.dialogs.planning.MeasurementPropertiesPanel;
import org.miradi.dialogs.planning.propertiesPanel.BlankPropertiesPanel;
import org.miradi.ids.BaseId;
import org.miradi.ids.FactorId;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Goal;
import org.miradi.objects.Indicator;
import org.miradi.objects.KeyEcologicalAttribute;
import org.miradi.objects.Measurement;
import org.miradi.objects.Target;

public class TargetViabilityMultiPropertiesPanel extends OverlaidObjectDataInputPanel
{
	public TargetViabilityMultiPropertiesPanel(MainWindow mainWindow) throws Exception
	{
		super(mainWindow.getProject(), new ORef(ObjectType.TARGET, new FactorId(BaseId.INVALID.asInt())));		
				
		cardLayout = new CardLayout();
		setLayout(cardLayout);
		
		blankPropertiesPanel = new BlankPropertiesPanel(getProject());
		targetSimpleModePropertiesPanel = new NonDiagramSimpleModeTargetPropertiesPanel(getProject());
		targetKeaModePropertiesPanel = new NonDiagramKeaModeTargetPropertiesPanel(getProject());
		targetViabilityKeaPropertiesPanel = new TargetViabilityKeaPropertiesPanel(getProject(), mainWindow.getActions());
		targetViabilityIndicatorPropertiesPanel = new IndicatorPropertiesPanel(mainWindow, getPicker());
		targetViabilityMeasurementPropertiesPanel = new MeasurementPropertiesPanel(getProject());
		futureStatusPropertiesPanel = new IndicatorFutureStatusSubPanel(getProject());
		
		addPanel(blankPropertiesPanel);
		addPanel(targetSimpleModePropertiesPanel);
		addPanel(targetKeaModePropertiesPanel);
		addPanel(targetViabilityKeaPropertiesPanel);
		addPanel(targetViabilityIndicatorPropertiesPanel);
		addPanel(targetViabilityMeasurementPropertiesPanel);
		addPanel(futureStatusPropertiesPanel);

		updateFieldsFromProject();
	}
	
	public String getPanelDescription()
	{
		return EAM.text("Title|Target Viability Properties");
	}

	public void setObjectRefs(ORef[] orefsToUse)
	{
		super.setObjectRefs(orefsToUse);
		currentCard = findPanel(orefsToUse);
		String panelDescription = currentCard.getPanelDescription();
		cardLayout.show(this, panelDescription);

		scrollRectToVisible(new Rectangle(0,0,0,0));
		
		// NOTE: The following are an attempt to fix a reported problem 
		// where the screen was not fully repainted when switching objects
		// This code is duplicated in PlanningTreePropertiesPanel.java 
		// and DirectIndicatorPropertiesPanel.java
		validate();
		repaint();
	}
	
	private AbstractObjectDataInputPanel findPanel(ORef[] orefsToUse)
	{
		if(orefsToUse.length == 0)
			return blankPropertiesPanel;
		
		int objectType = orefsToUse[0].getObjectType();
		if(Target.is(objectType))
			return getTargetPropertiesPanel(orefsToUse[0]);
		if(objectType == KeyEcologicalAttribute.getObjectType())
			return targetViabilityKeaPropertiesPanel;
		if(objectType == Indicator.getObjectType())
			return targetViabilityIndicatorPropertiesPanel;
		if(objectType == Measurement.getObjectType())
			return targetViabilityMeasurementPropertiesPanel;
		if(objectType == Goal.getObjectType())
			return futureStatusPropertiesPanel;

		return blankPropertiesPanel;
	}

	private AbstractObjectDataInputPanel getTargetPropertiesPanel(ORef targetRef)
	{
		Target target = Target.find(getProject(), targetRef);
		if (target.isViabilityModeTNC())
			return targetKeaModePropertiesPanel;
		
		return targetSimpleModePropertiesPanel;
	}
	
	@Override
	public void setFocusOnFirstField()
	{
		currentCard.setFocusOnFirstField();
	}
	
	@Override
	public void commandExecuted(CommandExecutedEvent event)
	{
		super.commandExecuted(event);
		if (event.isSetDataCommandWithThisTypeAndTag(Target.getObjectType(), Target.TAG_VIABILITY_MODE))
			reloadSelectedRefs();		
	}

	private CardLayout cardLayout;
	private AbstractObjectDataInputPanel currentCard;
	private BlankPropertiesPanel blankPropertiesPanel;
	private NonDiagramSimpleModeTargetPropertiesPanel targetSimpleModePropertiesPanel;
	private NonDiagramKeaModeTargetPropertiesPanel targetKeaModePropertiesPanel;
	private TargetViabilityKeaPropertiesPanel targetViabilityKeaPropertiesPanel;
	private IndicatorPropertiesPanel targetViabilityIndicatorPropertiesPanel;
	private MeasurementPropertiesPanel targetViabilityMeasurementPropertiesPanel;
	private IndicatorFutureStatusSubPanel futureStatusPropertiesPanel;
}
