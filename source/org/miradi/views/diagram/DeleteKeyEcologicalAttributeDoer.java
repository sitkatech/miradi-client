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

import org.miradi.main.EAM;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Factor;
import org.miradi.objects.KeyEcologicalAttribute;

public class DeleteKeyEcologicalAttributeDoer extends DeleteAnnotationDoer
{
	
	public boolean isAvailable()
	{
		if (getObjects().length != 1)
			return false;
		
		if (getSelectedObjectType() != ObjectType.KEY_ECOLOGICAL_ATTRIBUTE)
			return false;
		
		return true;
	}

	
	public String[] getDialogText()
	{
		return new String[] { EAM.text("Are you sure you want to delete this Key Ecological Attribute?"),};
	}

	public String getAnnotationIdListTag()
	{
		return Factor.TAG_KEY_ECOLOGICAL_ATTRIBUTE_IDS;
	}
	
	public int getAnnotationType()
	{
		return KeyEcologicalAttribute.getObjectType();
	}
}