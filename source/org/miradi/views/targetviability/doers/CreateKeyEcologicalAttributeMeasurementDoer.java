/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/
package org.miradi.views.targetviability.doers;

import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandCreateObject;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Indicator;

public class CreateKeyEcologicalAttributeMeasurementDoer extends AbstractKeyEcologicalAttributeDoer
{
	public int getRequiredObjectType()
	{
		return ObjectType.INDICATOR;
	}

	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;
		
		try
		{
			Indicator indicator = (Indicator)getObjects()[0];
			insertMeasurement(indicator);
		}
		catch (Exception e)
		{
			throw new CommandFailedException(e);
		}
	}

	private void insertMeasurement(Indicator indicator) throws Exception
	{
		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			CommandCreateObject createMeasurement = new CommandCreateObject(ObjectType.MEASUREMENT);
			getProject().executeCommand(createMeasurement);
			ORef newMeasurementRef = createMeasurement.getObjectRef();
	
			CommandSetObjectData addMeasurement = CommandSetObjectData.createAppendORefCommand(indicator, Indicator.TAG_MEASUREMENT_REFS, newMeasurementRef);
			getProject().executeCommand(addMeasurement);
			
			getPicker().ensureObjectVisible(newMeasurementRef);
		}
		finally
		{
			getProject().executeCommand(new CommandEndTransaction());
		}	
	}
}
