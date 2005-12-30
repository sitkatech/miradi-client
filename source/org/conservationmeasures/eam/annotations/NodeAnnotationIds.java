/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.annotations;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Vector;

abstract public class NodeAnnotationIds 
{
	abstract public void readDataFrom(DataInputStream dataIn) throws IOException;

	public NodeAnnotationIds()
	{
		annotationIds = new Vector();
	}
	
	public NodeAnnotationIds(DataInputStream dataIn) throws IOException
	{
		this();
		readDataFrom(dataIn);
	}

	public void writeDataTo(DataOutputStream dataOut) throws IOException 
	{
		final int LEGACY_PLACEHOLDER = -1;
		dataOut.writeInt(LEGACY_PLACEHOLDER);
		dataOut.writeInt(size());
		for(int i = 0; i < size(); ++i)
		{
			dataOut.writeInt((getId(i)));
		}
	}

	public int getId(int i)
	{
		return ((Integer)annotationIds.get(i)).intValue();
	}
	
	public void addId(int annotationId)
	{
		annotationIds.add(new Integer(annotationId));
	}
	
	public void setAnnotationId(int id)
	{
		annotationIds.clear();
		addId(id);
	}

	public int size()
	{
		return annotationIds.size();
	}
	
	public boolean hasAnnotation()
	{
		return (size() > 0);
	}
	
	public boolean equals(Object obj) 
	{
		if(!getClass().equals(obj.getClass()))
			return false;
		
		if(!(obj instanceof NodeAnnotationIds))
			return false;
		
		NodeAnnotationIds other = (NodeAnnotationIds)obj;
		return (annotationIds.equals(other.annotationIds));
	}

	public int readCount(DataInputStream dataIn) throws IOException
	{
		int oldStyleObjectiveCount = dataIn.readInt();
		if(oldStyleObjectiveCount >= 0)
		{
			for(int i = 0; i < oldStyleObjectiveCount; ++i)
				dataIn.readUTF();
			return 0;
		}
		
		int size = dataIn.readInt();
		return size;
	}

	Vector annotationIds;
}
