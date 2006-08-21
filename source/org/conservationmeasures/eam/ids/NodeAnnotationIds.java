/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.ids;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


abstract public class NodeAnnotationIds extends IdList
{
	abstract public void readDataFrom(DataInputStream dataIn) throws IOException;

	public NodeAnnotationIds()
	{
	}
	
	public NodeAnnotationIds(IdList ids)
	{
		super(ids);
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
			dataOut.writeInt(getId(i).asInt());
		}
	}

	public BaseId getId(int i)
	{
		return get(i);
	}
	
	public void addId(BaseId annotationId)
	{
		add(annotationId);
	}
	
	public void setAnnotationId(BaseId id)
	{
		clear();
		addId(id);
	}

	public boolean hasAnnotation()
	{
		return (size() > 0);
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
}
