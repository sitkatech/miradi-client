/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.objectpools;

import java.util.Comparator;

import org.miradi.ids.IdAssigner;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;

public abstract class DiagramObjectPool extends EAMNormalObjectPool
{
	public DiagramObjectPool(IdAssigner idAssignerToUse, int objectTypeToStore)
	{
		super(idAssignerToUse, objectTypeToStore);
	}

	@Override
	protected ORefList getSortedRefList()
	{
		ORefList refs = super.getSortedRefList();
		refs.sort(new DiagramObjectComparator());
		return refs;
	}
	
	class DiagramObjectComparator implements Comparator<ORef>
	{
		public int compare(ORef ref1, ORef ref2)
		{
			if(ref1 == null && ref2 == null)
				return 0;
			if(ref1 == null)
				return -1;
			if(ref2 == null)
				return 1;
			
			String label1 = findObject(ref1).toString();
			String label2 = findObject(ref2).toString();
			return label1.compareToIgnoreCase(label2);
		}
	}
}
