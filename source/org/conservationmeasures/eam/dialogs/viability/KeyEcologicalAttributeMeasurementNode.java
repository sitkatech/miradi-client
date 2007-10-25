package org.conservationmeasures.eam.dialogs.viability;

import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Measurement;
import org.conservationmeasures.eam.views.TreeTableNode;

public class KeyEcologicalAttributeMeasurementNode extends TreeTableNode
{
	public KeyEcologicalAttributeMeasurementNode(KeyEcologicalAttributeIndicatorNode parent, Measurement measurementToUse)
	{
		measurement = measurementToUse;
		keyEcologicalAttributesNode = parent;
	}
	
	public BaseObject getObject()
	{
		return measurement;
	}

	public ORef getObjectReference()
	{
		return measurement.getRef();
	}
	
	public int getType()
	{
		return measurement.getType();
	}

	public String toString()
	{
		return measurement.toString();
	}

	public int getChildCount()
	{
		return 0;
	}

	public TreeTableNode getChild(int index)
	{
		return null;
	}
	
	public TreeTableNode getParentNode()
	{
		return keyEcologicalAttributesNode;
	}

	//TODO: this method could be pulled up to the supper
	public Object getValueAt(int column)
	{
		String tag = COLUMN_TAGS[column];
		return getObject().getData(tag);
	}

	public void rebuild() throws Exception
	{
	}
	
	//FIXME should add more valuable columns,  just did this so it works and i can move on.  
	public static final String[] COLUMN_TAGS = {Measurement.TAG_LABEL,Measurement.TAG_LABEL, Measurement.TAG_LABEL};
	
	private Measurement measurement;
	private KeyEcologicalAttributeIndicatorNode keyEcologicalAttributesNode;
}
