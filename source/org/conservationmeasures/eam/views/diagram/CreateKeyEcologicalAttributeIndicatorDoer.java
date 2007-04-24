/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import java.text.ParseException;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.KeyEcologicalAttribute;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.ObjectsDoer;

public class CreateKeyEcologicalAttributeIndicatorDoer extends ObjectsDoer
{
	public boolean isAvailable()
	{
		if (getObjects().length != 1)
			return false;
		
		if (getSelectedObjectType() != ObjectType.KEY_ECOLOGICAL_ATTRIBUTE)
			return false;
		
		return true;
	}

	public void doIt() throws CommandFailedException
	{
		doInsertKEAIndicator();
	}

	private void doInsertKEAIndicator() throws CommandFailedException
	{
		if(!isAvailable())
			return;

		KeyEcologicalAttribute kea = (KeyEcologicalAttribute)getObjects()[0];

		try
		{
			insertKEAIndicator(getProject(), kea, kea.getIndicatorIds().size());
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
	}

	public void insertKEAIndicator(Project project, KeyEcologicalAttribute kea, int childIndex) throws CommandFailedException, ParseException, Exception
	{
		project.executeCommand(new CommandBeginTransaction());
		try
		{
			CommandCreateObject create = new CommandCreateObject(ObjectType.INDICATOR);
			project.executeCommand(create);
			BaseId createdId = create.getCreatedId();
	
			CommandSetObjectData addChild = CommandSetObjectData.createInsertIdCommand(kea, 
					KeyEcologicalAttribute.TAG_INDICATOR_IDS, createdId, childIndex);
			project.executeCommand(addChild);
			getPicker().ensureObjectVisible(new ORef(Indicator.getObjectType(), createdId));
		}
		finally
		{
			project.executeCommand(new CommandEndTransaction());
		}
	}
}
