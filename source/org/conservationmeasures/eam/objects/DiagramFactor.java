/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import java.awt.Dimension;
import java.awt.Point;

import org.conservationmeasures.eam.diagram.cells.FactorDataMap;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.objectdata.BaseIdData;
import org.conservationmeasures.eam.objectdata.DimensionData;
import org.conservationmeasures.eam.objectdata.PointData;
import org.conservationmeasures.eam.objecthelpers.CreateDiagramFactorParameter;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

public class DiagramFactor extends EAMBaseObject
{
	public DiagramFactor(DiagramFactorId diagramFactorIdToUse, CreateDiagramFactorParameter extraInfo)
	{
		super(diagramFactorIdToUse);
		
		clear();
		underlyingObjectId.setId(extraInfo.getFactorId());
		size.setDimension(getDefaultSize());
	}
	
	public DiagramFactor(int idToUse, EnhancedJsonObject json) throws Exception
	{
		super(new DiagramFactorId(idToUse), json);
		
		underlyingObjectId.setId(json.getId(TAG_WRAPPED_ID));
	}
	
	public EnhancedJsonObject toJson()
	{
		EnhancedJsonObject jsonObject = super.toJson();
		jsonObject.put(TAG_WRAPPED_ID, underlyingObjectId.toString());
		
		return jsonObject;
	}
	
	public static Dimension getDefaultSize()
	{
		return new Dimension(120, 60);
	}
	
	public int getType()
	{
		return ObjectType.DIAGRAM_FACTOR;
	}
		
	public DiagramFactorId getDiagramFactorId()
	{
		return (DiagramFactorId)getId(); 
	}
	
	public FactorId getWrappedId()
	{
		return new FactorId(underlyingObjectId.getId().asInt());
	}
	
	public Dimension getSize()
	{
		return size.getDimension();
	}
	
	public Point getLocation()
	{
		return location.getPoint();
	}
	
	public void setLocation(Point pointToUse)
	{
		location.setPoint(pointToUse);
	}
	
	public void setSize(Dimension dimensionToUse)
	{
		size.setDimension(dimensionToUse);
	}
	
	public FactorDataMap createFactorDataMap(String factorType, String factorLabel)
	{
		FactorDataMap dataMap = new FactorDataMap();
		dataMap.putId(TAG_ID, getDiagramFactorId());
		dataMap.putId(TAG_WRAPPED_ID, getWrappedId());
		dataMap.putString(TAG_LOCATION, location.get());
		dataMap.putString(TAG_SIZE, size.get());
		
		
		// FIXME: This is a crude hack, so we can re-create the node if it gets pasted. 
		// Really, for each node copied to the clipboard, we should copy 
		// the json for both the ConceptualModelNode and for the DiagramNode.
		// That will also fix the current bug that objectives and goals are not copied
		dataMap.put(Factor.TAG_NODE_TYPE, factorType);
		dataMap.put(Factor.TAG_LABEL, factorLabel);
		
		return dataMap;
	}

	public CreateObjectParameter getCreationExtraInfo()
	{
		return new CreateDiagramFactorParameter(getWrappedId());
	}
	
	void clear()
	{
		super.clear();

		underlyingObjectId = new BaseIdData();
		size = new DimensionData();
		location = new PointData();
		
		addField(TAG_WRAPPED_ID, underlyingObjectId);
		addField(TAG_SIZE, size);
		addField(TAG_LOCATION, location);
	}
	
	public static final String TAG_LOCATION = "Location";
	public static final String TAG_SIZE = "Size";
	public static final String TAG_WRAPPED_ID = "WrappedFactorId";
	
	private BaseIdData underlyingObjectId;
	private DimensionData size;
	private PointData location;
}
