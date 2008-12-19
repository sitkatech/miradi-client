/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
		
		if (!Target.is(getSelectedParentFactor().getRef()))
			return false;
		
		Target target = ((Target)getSelectedParentFactor());
		if (target.getKeyEcologicalAttributes().size() == 0)
			return false;
		
		return true;
	}
	
	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;
		
		KeyEcologicalAttributeListTableModel keyEcologicalAttributeListTableModel = new KeyEcologicalAttributeListTableModel(getProject(), ((Factor)getSelectedParentFactor()).getFactorId());
		if (validUserChoiceForObjectToClone(new KeyEcologicalAttributeListTablePanelWithoutButtons(getMainWindow(), keyEcologicalAttributeListTableModel), EAM.text("Choose Key Ecological Attribute to Clone")))
			super.doIt();
	}
	
	protected void doExtraWork(ORef newlyCreatedObjectRef) throws Exception
	{
		String labelFromKea = getLabelForStress();
		CommandSetObjectData setStressLabel = new CommandSetObjectData(newlyCreatedObjectRef, Stress.TAG_LABEL, labelFromKea);
		getProject().executeCommand(setStressLabel);
		
		CreateStressDoer.createThreatStressRatingsForAttachedLinks(getProject(), newlyCreatedObjectRef, (Factor) getSelectedParentFactor());
	}

	private String getLabelForStress()
	{
		if (getAnnotationToClone().getLabel().length() == 0)
			return EAM.text("[NOT SPECIFIED]");
		
		return "[" + getAnnotationToClone().getLabel() + "]";
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
