package org.conservationmeasures.eam.dialogs.viability;

import java.text.Collator;
import java.util.Comparator;

import org.conservationmeasures.eam.objects.KeyEcologicalAttribute;

public class KeaNodeComparator implements Comparator
{
	public int compare(Object node1, Object node2)
	{
		KeyEcologicalAttribute kea1 =(KeyEcologicalAttribute)((KeyEcologicalAttributeNode) node1).getObject();
		String type1 =kea1.getKeyEcologicalAttributeType();
		KeyEcologicalAttribute kea2 =(KeyEcologicalAttribute)((KeyEcologicalAttributeNode) node2).getObject();
		String type2 =kea2.getKeyEcologicalAttributeType();
		Collator myCollator = Collator.getInstance();
		return myCollator.compare(type1, type2);
	}
}
