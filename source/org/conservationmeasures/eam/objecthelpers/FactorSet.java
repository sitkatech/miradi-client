/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objecthelpers;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.conservationmeasures.eam.objects.Factor;

public class FactorSet implements Collection
{
	public FactorSet()
	{
		nodes = new HashSet();
	}
	
	public void attemptToAdd(Factor node)
	{
		if(isLegal(node))
		{
			nodes.add(node);
		}
	}
	
	public void attemptToAddAll(FactorSet nodesToAdd)
	{
		attemptToAddAll(nodesToAdd.toFactorArray());
	}
	
	public void attemptToAddAll(Factor[] nodesToAdd)
	{
		for(int i = 0; i < nodesToAdd.length; ++i)
			attemptToAdd(nodesToAdd[i]);
	}
	
	
	public boolean isLegal(Factor node)
	{
		
		return true;
	}

	public int size()
	{
		
		return nodes.size();
	}

	public Object[] toArray()
	{
		return nodes.toArray();

	}

	public Factor[] toFactorArray()
	{
		return (Factor[])nodes.toArray(new Factor[0]);

	}
	
	public boolean contains(Factor node)
	{
		return nodes.contains(node);
	}

	public void clear()
	{
		nodes.clear();
	}

	public boolean isEmpty()
	{
		return nodes.isEmpty();
	}

	public boolean add(Object o)
	{
		throw new RuntimeException("Must use attemptToAdd instead of add");
	}

	public boolean contains(Object o)
	{
		return nodes.contains(o);
	}

	public boolean remove(Object o)
	{
		return nodes.remove(o);
	}

	public boolean addAll(Collection c)
	{
		throw new RuntimeException("Must use attemptToAdd instead of addAll");
	}

	public boolean containsAll(Collection c)
	{
		return nodes.containsAll(c);
	}

	public boolean removeAll(Collection c)
	{
		return nodes.retainAll(c);
	}

	public boolean retainAll(Collection c)
	{
		return nodes.retainAll(c);
	}

	public Iterator iterator()
	{
		return nodes.iterator();
	}

	public Object[] toArray(Object[] a)
	{
		return nodes.toArray(a);
	}
	
	public boolean equals(Object rawOther)
	{
		if(!(rawOther instanceof FactorSet))
			return false;
		
		FactorSet other = (FactorSet)rawOther;
		return nodes.equals(other.nodes);
	}

	public int hashCode()
	{
		return nodes.hashCode();
	}
	
	public String toString()
	{
		StringBuffer result = new StringBuffer();
		result.append("[");
		Iterator iter = iterator();
		while(iter.hasNext())
		{
			Factor node = (Factor)iter.next();
			result.append(node.getId());
			result.append(",");
		}
		result.append("]");
		return result.toString();
	}

	HashSet nodes;

}
