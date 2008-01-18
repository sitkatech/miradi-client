/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/
package org.conservationmeasures.eam.views.targetviability.doers;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Indicator;

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
