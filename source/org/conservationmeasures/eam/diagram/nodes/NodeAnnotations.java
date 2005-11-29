/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram.nodes;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Vector;

abstract public class NodeAnnotations 
{
	abstract public void readDataFrom(DataInputStream dataIn) throws IOException;
	abstract public boolean equals(Object obj); 

	public NodeAnnotations()
	{
		annotations = new Vector();
	}
	
	public NodeAnnotations(DataInputStream dataIn) throws IOException
	{
		this();
		readDataFrom(dataIn);
	}

	public void writeDataTo(DataOutputStream dataOut) throws IOException 
	{
		dataOut.writeInt(size());
		for(int i = 0; i < size(); ++i)
		{
			dataOut.writeUTF((getAnnotation(i)).getAnnotation());
		}
	}

	public void add(NodeAnnotation annotation)
	{
		annotations.add(annotation);
	}
	
	public void setAnnotations(NodeAnnotation annotation)
	{
		Vector singleObject = new Vector();
		singleObject.add(annotation);
		annotations = singleObject;
	}

	public void setAnnotations(Vector annotationsToUse)
	{
		annotations = annotationsToUse;
	}

	public int size()
	{
		return annotations.size();
	}
	
	public boolean hasAnnotation()
	{
		for(int i = 0 ; i < size(); ++i)
		{
			if((getAnnotation(i)).hasAnnotation())
				return true;
		}
		return false;
	}
	
	
	public NodeAnnotation getAnnotation(int i)
	{
		return (NodeAnnotation)annotations.get(i);
	}
	

	Vector annotations;
}
