package org.conservationmeasures.eam.objecthelpers;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.conservationmeasures.eam.objects.ConceptualModelNode;

public class ConceptualModelNodeSet implements Collection
{
	public ConceptualModelNodeSet()
	{
		nodes = new HashSet();
	}
	
	public void attemptToAdd(ConceptualModelNode node)
	{
		if(isLegal(node))
		{
			nodes.add(node);
		}
	}
	
	public void attemptToAddAll(ConceptualModelNodeSet nodesToAdd)
	{
		ConceptualModelNode[] nodesAsArray = nodesToAdd.toNodeArray();
		for(int i = 0; i < nodesAsArray.length; ++i)
			attemptToAdd(nodesAsArray[i]);
	}
	
	public boolean isLegal(ConceptualModelNode node)
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

	public ConceptualModelNode[] toNodeArray()
	{
		return (ConceptualModelNode[])nodes.toArray(new ConceptualModelNode[0]);

	}
	
	public boolean contains(ConceptualModelNode node)
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
		if(!(rawOther instanceof ConceptualModelNodeSet))
			return false;
		
		ConceptualModelNodeSet other = (ConceptualModelNodeSet)rawOther;
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
			ConceptualModelNode node = (ConceptualModelNode)iter.next();
			result.append(node.getId());
			result.append(",");
		}
		result.append("]");
		return result.toString();
	}

	HashSet nodes;

}
