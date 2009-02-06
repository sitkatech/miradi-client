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
package org.miradi.views.diagram;

import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Factor;
import org.miradi.objects.Target;

public class CreateKeyEcologicalAttributeDoer extends CreateAnnotationDoer
{
	public boolean isAvailable() 
	{
		if (!super.isAvailable())
			return false;
		
		BaseObject selectedParent = getSelectedParentFactor();
		if (!Factor.isFactor(selectedParent.getRef()))
			return false;
		
		if (((Factor)selectedParent).isTarget())
			return ((Target)selectedParent).isViabilityModeTNC();

		return false;
	}

	public int getAnnotationType()
	{
		return ObjectType.KEY_ECOLOGICAL_ATTRIBUTE;
	}
	
	public String getAnnotationListTag()
	{
		return Factor.TAG_KEY_ECOLOGICAL_ATTRIBUTE_IDS;
	}
}
