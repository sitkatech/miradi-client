package org.conservationmeasures.eam.reports;

import net.sf.jasperreports.engine.JRDataSource;

import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.BaseObject;

public class RelatedObjectDataSource extends CommonDataSource
{
	public RelatedObjectDataSource(BaseObject object, int relatedObjectTypeToFind)
	{
		super(object.getObjectManager().getProject());
		ORefList list = object.findObjectThatReferToUs();
		ORef oref = object.getOwnerRef();
		list.add(oref);
		ORefList newList = new ORefList();
		for (int i=0; i<list.size(); ++i)
		{
			if (list.get(i).getObjectType()==relatedObjectTypeToFind)
				newList.add(list.get(i));
		}
		setObjectList(newList);
	}
	
	public JRDataSource getRelatedObjectDataSource(int relatedObjectTypeToFind)
	{
		return new RelatedObjectDataSource(getCurrentObject(), relatedObjectTypeToFind);
	}
} 