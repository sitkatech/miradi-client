/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram.doers;

import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.dialogs.viability.KeyEcologicalAttributeListTableModel;
import org.conservationmeasures.eam.dialogs.viability.KeyEcologicalAttributeListTablePanelWithoutButtons;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.Stress;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.views.diagram.CreateAnnotationDoer;

public class CreateStressFromKeaDoer extends CreateAnnotationDoer
{
	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;
		
		KeyEcologicalAttributeListTableModel keyEcologicalAttributeListTableModel = new KeyEcologicalAttributeListTableModel(getProject(), ((Factor)getSelectedParent()).getFactorId());
		if (validUserChoiceForObjectToClone(new KeyEcologicalAttributeListTablePanelWithoutButtons(getProject(), keyEcologicalAttributeListTableModel), EAM.text("Choose Key Ecological Attribute to Clone")))
			super.doIt();
	}
	
	protected void doExtraWork(ORef newlyCreatedObjectRef) throws Exception
	{
		String labelFromKea = getLabelForStress();
		CommandSetObjectData setStressLabel = new CommandSetObjectData(newlyCreatedObjectRef, Stress.TAG_LABEL, labelFromKea);
		getProject().executeCommand(setStressLabel);
		
		CreateStressDoer.createThreatStressRatingsForAttachedLinks(getProject(), newlyCreatedObjectRef, (Factor) getSelectedParent());
	}

	private String getLabelForStress()
	{
		if (getAnnotationToClone().getLabel().length() == 0)
			return EAM.text("[NOT SPECIFIED]");
		
		return EAM.text("["+getAnnotationToClone().getLabel()+"]");
	}
	
	protected ORef createObject() throws CommandFailedException
	{
		CommandCreateObject create = new CommandCreateObject(getAnnotationType());
		getProject().executeCommand(create);
		return create.getObjectRef();
	}

	public String getAnnotationListTag()
	{
		return Target.TAG_STRESS_REFS;
	}

	public int getAnnotationType()
	{
		return Stress.getObjectType();
	}
}
