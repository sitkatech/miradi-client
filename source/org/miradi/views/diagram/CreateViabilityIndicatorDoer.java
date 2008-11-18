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
package org.miradi.views.diagram;

import java.util.Vector;

import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandCreateObject;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.KeyEcologicalAttribute;
import org.miradi.objects.Target;
import org.miradi.views.targetviability.doers.AbstractKeyEcologicalAttributeDoer;

public class CreateViabilityIndicatorDoer extends AbstractKeyEcologicalAttributeDoer
{	
	@Override
	public boolean isAvailable()
	{
		boolean superIsAvailable = super.isAvailable();
		if (!superIsAvailable)
			return false;
		
		BaseObject baseObject = getObjects()[0];
		if (!Target.is(baseObject))
			return true;
		
		Target target = (Target) baseObject;
		if (target.isViabilityModeTNC())
			return false;
		
		return true;
	}
	
	public Vector<Integer> getRequiredObjectType()
	{
		Vector<Integer> types = new Vector();
		types.add(Target.getObjectType());
		types.add(KeyEcologicalAttribute.getObjectType());

		return types;
	}

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;

		getProject().executeCommand(new CommandBeginTransaction());
		try
		{		
			BaseObject baseObject = getObjects()[0];
			CommandCreateObject create = new CommandCreateObject(ObjectType.INDICATOR);
			getProject().executeCommand(create);
			
			ORef createdRef = create.getObjectRef();
			CommandSetObjectData addChild = CommandSetObjectData.createInsertIdCommand(baseObject,	getIndicatorListTag(baseObject), createdRef.getObjectId(), getIndicatorIndex(baseObject));
			getProject().executeCommand(addChild);
			
			getPicker().ensureObjectVisible(createdRef);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
		finally
		{
			getProject().executeCommand(new CommandEndTransaction());
		}
	}

	private int getIndicatorIndex(BaseObject baseObject)
	{
		if (Target.is(baseObject))
			return ((Target)baseObject).getIndicatorRefs().size();
		
		if (KeyEcologicalAttribute.is(baseObject))
			return ((KeyEcologicalAttribute)baseObject).getIndicatorRefs().size();
		
		throw new RuntimeException("This method is only for kea or targets.");
	}
}
