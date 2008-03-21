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
package org.miradi.reports;

import net.sf.jasperreports.engine.JRDataSource;

import org.miradi.dialogs.viability.KeyEcologicalAttributeNode;
import org.miradi.dialogs.viability.TargetViabilityNode;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.KeyEcologicalAttribute;
import org.miradi.objects.Target;

public class KeyEcologicalAttributesDataSource extends CommonDataSource
{
	public KeyEcologicalAttributesDataSource(Target target) throws Exception
	{
		super(target.getProject());
		if (target.isViabilityModeTNC())
		{
			KeyEcologicalAttributeNode[] keas = TargetViabilityNode.getKeaNodes(target);
			ORefList list = new ORefList();
			for (int i=0; i<keas.length; ++i)
				list.add(keas[i].getObject().getRef());
			setObjectList(list);
		}
		else
		{
			setObjectList(new ORefList());
		}
	}
	
	public JRDataSource getViabilityIndicatorsDataSource()
	{
		return new ViabilityIndicatorsDataSource((KeyEcologicalAttribute)getCurrentObject());
	}
} 
