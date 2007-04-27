package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.Target;

public class CreateViabiltiyKeyEcologicalAttributeDoer  extends CreateAnnotationDoer
{
	public boolean isAvailable()
	{
		if (getPicker()==null)
			return false;
		
		if (getPicker().getSelectedObjects().length != 1)
			return false;
		
		if (getPicker().getSelectedObjects()[0].getType() == ObjectType.TARGET)
			return ((Target)getPicker().getSelectedObjects()[0]).isViabilityModeTNC();
		
		if (getPicker().getSelectedObjects()[0].getType()  == ObjectType.KEY_ECOLOGICAL_ATTRIBUTE)
			return true;
		
		return false;
	}
	
	public Factor getSelectedFactor()
	{
		if (getPicker().getSelectedObjects()[0].getType() == ObjectType.TARGET)
			return (Factor) getPicker().getSelectedObjects()[0];
		
		if (getPicker().getSelectedObjects()[0].getType()  == ObjectType.KEY_ECOLOGICAL_ATTRIBUTE)
			return (Factor) getPicker().getSelectedObjects()[0].getOwner();
		
		return null;
	}
	
	int getAnnotationType()
	{
		return ObjectType.KEY_ECOLOGICAL_ATTRIBUTE;
	}
	
	String getAnnotationIdListTag()
	{
		return Factor.TAG_KEY_ECOLOGICAL_ATTRIBUTE_IDS;
	}
}
