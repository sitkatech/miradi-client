/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.diagram;

import java.text.ParseException;

import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandCreateObject;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Indicator;
import org.miradi.objects.KeyEcologicalAttribute;
import org.miradi.project.Project;
import org.miradi.views.targetviability.doers.AbstractKeyEcologicalAttributeDoer;

public class CreateKeyEcologicalAttributeIndicatorDoer extends AbstractKeyEcologicalAttributeDoer
{	
	public int getRequiredObjectType()
	{
		return ObjectType.KEY_ECOLOGICAL_ATTRIBUTE;
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
