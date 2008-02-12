/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
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
