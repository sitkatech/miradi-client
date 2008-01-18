/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.diagram;

import org.conservationmeasures.eam.actions.jump.ActionJumpDiagramWizardLinkDirectThreatsToTargetsStep;
import org.conservationmeasures.eam.dialogs.base.ObjectDataInputPanel;
import org.conservationmeasures.eam.dialogs.threatstressrating.properties.ThreatStressRatingPropertiesPanel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objectdata.BooleanData;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.DiagramLink;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.umbrella.ObjectPicker;

public class FactorLinkPropertiesPanel extends ObjectDataInputPanel
{
	public FactorLinkPropertiesPanel(Project projectToUse, DiagramLink link)
	{
		super(projectToUse, ObjectType.FACTOR_LINK, link.getWrappedId());

		addField(createCheckBoxField(FactorLink.TAG_BIDIRECTIONAL_LINK, BooleanData.BOOLEAN_TRUE, BooleanData.BOOLEAN_FALSE));
		
		updateFieldsFromProject();
	}

	public FactorLinkPropertiesPanel(MainWindow mainWindow, DiagramLink link, ObjectPicker objectPicker) throws Exception
	{
		super(mainWindow.getProject(), ObjectType.FACTOR_LINK, link.getWrappedId());
		
		ThreatStressRatingPropertiesPanel threatStressRatingPropertiesPanel = new ThreatStressRatingPropertiesPanel(mainWindow, objectPicker);
		addSubPanel(threatStressRatingPropertiesPanel);
		add(threatStressRatingPropertiesPanel);
		
		setObjectRefs(objectPicker.getSelectedHierarchies()[0]);
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Link Properties");
	}
	
	public Class getJumpActionClass()
	{
		return ActionJumpDiagramWizardLinkDirectThreatsToTargetsStep.class;
	}
}
