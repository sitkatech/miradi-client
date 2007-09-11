package org.conservationmeasures.eam.dialogs.planning;

import java.util.Vector;

import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.TreeTableNode;

public abstract class AbstractPlanningTreeNode extends TreeTableNode
{
	abstract public boolean attemptToAdd(ORef refToAdd);

	public AbstractPlanningTreeNode(Project projectToUse)
	{
		project = projectToUse;
		children = new Vector();
	}
	
	public ORef getObjectReference()
	{
		if(getObject() == null)
			return ORef.INVALID;
		return getObject().getRef();
	}
	
	public TreeTableNode getChild(int index)
	{
		return children.get(index);
	}

	public int getChildCount()
	{
		return children.size();
	}

	public Object getValueAt(int column)
	{
		return null;
	}

	public void rebuild() throws Exception
	{
		throw new Exception("Can't call rebuild on " + getClass().getCanonicalName());
	}

	public String toString()
	{
		return getObject().getLabel();
	}

	protected boolean attemptToAddToChildren(ORef refToAdd)
	{
		boolean wasAdded = false;
		
		for(int i = 0; i < children.size(); ++i)
		{
			if(children.get(i).attemptToAdd(refToAdd))
				wasAdded = true;
		}
		return wasAdded;
	}

	protected Project project;
	protected Vector<AbstractPlanningTreeNode> children;
}
