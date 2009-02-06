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
package org.miradi.views.targetviability.doers;

import java.util.Vector;

import org.miradi.objects.BaseObject;
import org.miradi.objects.KeyEcologicalAttribute;
import org.miradi.objects.Target;
import org.miradi.views.ObjectsDoer;

abstract public class AbstractKeyEcologicalAttributeDoer extends ObjectsDoer
{
	public boolean isAvailable()
	{
		if (getObjects() == null)
			return false;
		
		if ((getObjects().length != 1))
			return false;
		
		return getRequiredObjectTypes().contains(getSelectedObjectType());
	}
	
	protected static String getIndicatorListTag(BaseObject baseObject)
	{
		if (Target.is(baseObject))
			return Target.TAG_INDICATOR_IDS;
		
		if (KeyEcologicalAttribute.is(baseObject))
			return KeyEcologicalAttribute.TAG_INDICATOR_IDS;
		
		throw new RuntimeException("Can only add indicators to targets and keas");
	}
	
	abstract public Vector<Integer> getRequiredObjectTypes();
}
