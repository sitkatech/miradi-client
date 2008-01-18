/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.reports;

import net.sf.jasperreports.engine.JRDataSource;

import org.conservationmeasures.eam.dialogs.viability.KeyEcologicalAttributeNode;
import org.conservationmeasures.eam.dialogs.viability.TargetViabilityNode;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.KeyEcologicalAttribute;
import org.conservationmeasures.eam.objects.Target;

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
