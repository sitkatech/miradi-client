/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.diagram.doers;

import org.miradi.commands.CommandCreateObject;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.dialogs.viability.KeyEcologicalAttributeListTableModel;
import org.miradi.dialogs.viability.KeyEcologicalAttributeListTablePanelWithoutButtons;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Factor;
import org.miradi.objects.Stress;
import org.miradi.objects.Target;
import org.miradi.views.diagram.CreateAnnotationDoer;

public class CreateStressFromKeaDoer extends CreateAnnotationDoer
{
	public boolean isAvailable() 
	{
		if (!super.isAvailable())
			return false;
		
		if (((Factor)getSelectedParent()).getKeyEcologicalAttributes().size() == 0)
			return false;
		
		return true;
	}
	
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
