package org.conservationmeasures.eam.objects;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

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
			add(node);
		}
	}
	
	public boolean isLegal(ConceptualModelNode node)
	{
		
		return true;
	}

	public int size()
	{
		
		return nodes.size();
	}

	HashSet nodes;

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
		return nodes.add(o);
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
		return nodes.addAll(c);
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
}
