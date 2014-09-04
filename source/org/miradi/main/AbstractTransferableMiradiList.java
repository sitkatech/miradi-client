/* 
Copyright 2005-2014, Foundations of Success, Bethesda, Maryland
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 

package org.miradi.main;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Vector;

import org.miradi.diagram.cells.EAMGraphCell;
import org.miradi.ids.BaseId;
import org.miradi.objecthelpers.BaseObjectDeepCopierWithRelatedObjectsToJson;
import org.miradi.objecthelpers.EAMGraphCellByFactorTypeSorter;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.Factor;
import org.miradi.objects.FactorLink;
import org.miradi.objects.Stress;
import org.miradi.objects.Task;
import org.miradi.objects.ThreatStressRating;
import org.miradi.project.Project;
import org.miradi.utils.EnhancedJsonObject;
import org.miradi.views.diagram.DiagramPaster;


abstract public class AbstractTransferableMiradiList implements Transferable, Serializable
{
	public AbstractTransferableMiradiList(Project projectToUse, ORef diagramObjectRefCopiedFromToUse)
	{
		project = projectToUse;
		projectName = project.getFilename();
		diagramObjectRefCopiedFrom = diagramObjectRefCopiedFromToUse;

	}
	
	public String getProjectFileName()
	{
		return projectName;
	}

	private void clear()
	{
		factorDeepCopies = new Vector<String>();
		diagramFactorDeepCopies = new Vector<String>();
		threatStressRatingCopies = new Vector<String>();
		
		factorLinkDeepCopies = new Vector<String>();
		diagramLinkDeepCopies = new Vector<String>();
	}

	public void storeData(EAMGraphCell[] selectedCellsToCopy) throws Exception
	{
		Arrays.sort(selectedCellsToCopy, new EAMGraphCellByFactorTypeSorter());
		BaseObjectDeepCopierWithRelatedObjectsToJson deepCopier = createObjectDeepCopier();
		for (int i = 0; i < selectedCellsToCopy.length; i++) 
		{
			EAMGraphCell cell = selectedCellsToCopy[i];
			if (cell.isFactor())
				addFactorDeepCopies(getSafeCurrentlyDeepCopiedFactorRefs(), deepCopier, cell.getDiagramFactor());
			
			if (cell.isFactorLink())
				addFactorLinkDeepCopies(deepCopier, cell.getDiagramLink());
		}
		
		fillThreatStressRatingList();
	}

	private void fillThreatStressRatingList() throws ParseException
	{
		for (int index = 0; index < factorDeepCopies.size(); index++)
		{
			String jsonStringOfObject = factorDeepCopies.get(index);
			EnhancedJsonObject json = new EnhancedJsonObject(jsonStringOfObject);
			int objectType = json.getInt(DiagramPaster.FAKE_TAG_TYPE);
			if (ThreatStressRating.is(objectType))
			{
				threatStressRatingCopies.add(jsonStringOfObject);
			}
		}
		
		factorDeepCopies.removeAll(threatStressRatingCopies);
	}

	public void storeData(HashSet<DiagramFactor> diagramFactors, HashSet<DiagramLink> diagramLinks)
	{	
		BaseObjectDeepCopierWithRelatedObjectsToJson deepCopier = createObjectDeepCopier();		
		for(DiagramFactor diagramFactor : diagramFactors)
		{
			addFactorDeepCopies(deepCopier, diagramFactor);
		}
		
		for(DiagramLink diagramLink : diagramLinks)
		{
			addFactorLinkDeepCopies(deepCopier, diagramLink);
		}
	}

	private BaseObjectDeepCopierWithRelatedObjectsToJson createObjectDeepCopier()
	{
		clear();
		return new BaseObjectDeepCopierWithRelatedObjectsToJson(project);
	}

	private void addFactorDeepCopies(BaseObjectDeepCopierWithRelatedObjectsToJson deepCopier, DiagramFactor diagramFactor)
	{
		addFactorDeepCopies(new ORefList(), deepCopier, diagramFactor);
	}

	private void addFactorDeepCopies(ORefList deepCopiedFactorRefs, BaseObjectDeepCopierWithRelatedObjectsToJson deepCopier, DiagramFactor diagramFactor)
	{
		if (shouldDeepCopyFactor(diagramFactor.getWrappedType()))
		{
			Factor factor = diagramFactor.getWrappedFactor();		
			Vector<String> factorJsonStrings = deepCopier.createDeepCopy(deepCopiedFactorRefs, factor);
			factorDeepCopies.addAll(factorJsonStrings);
		}
	
		Vector<String> diagramFactorJsonStrings = deepCopier.createDeepCopy(deepCopiedFactorRefs, diagramFactor);
		diagramFactorDeepCopies.addAll(diagramFactorJsonStrings);
		
		addToUpperMostLeftMostCorner(diagramFactor);
	}

	private boolean shouldDeepCopyFactor(int type)
	{
		if (Stress.is(type))
			return false;
		
		if (Task.is(type))
			return false;
		
		return true;
	}

	private void addToUpperMostLeftMostCorner(DiagramFactor diagramFactor)
	{
		Point location = diagramFactor.getLocation();
		if (rectWithUpperMostLeftMostCorner == null)
			rectWithUpperMostLeftMostCorner = new Rectangle(location);
		
		rectWithUpperMostLeftMostCorner.add(location);
	}

	private void addFactorLinkDeepCopies(BaseObjectDeepCopierWithRelatedObjectsToJson deepCopier, DiagramLink diagramLink)
	{
		FactorLink factorLink = diagramLink.getWrappedFactorLink();
		Vector<String> factorLinkJsonStrings = deepCopier.createDeepCopy(factorLink);
		factorLinkDeepCopies.addAll(factorLinkJsonStrings);
	
		Vector<String> diagramLinkJsonStrings = deepCopier.createDeepCopy(diagramLink);
		diagramLinkDeepCopies.addAll(diagramLinkJsonStrings);	
	}

	public Vector<String> getDiagramFactorDeepCopies()
	{
		return diagramFactorDeepCopies;
	}

	public Vector<String> getThreatStressRatingDeepCopies()
	{
		return threatStressRatingCopies;
	}

	public Vector<String> getFactorDeepCopies()
	{
		return factorDeepCopies;
	}

	private ORefList getSafeCurrentlyDeepCopiedFactorRefs()
	{
		try
		{
			return getFactorRefs();
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return new ORefList();
		}
	}

	public ORefList getFactorRefs() throws Exception
	{
		ORefList factorRefs = new ORefList();
		for (int i = 0; i < factorDeepCopies.size(); ++i)
		{
			String jsonAsString = factorDeepCopies.get(i);
			EnhancedJsonObject json = new EnhancedJsonObject(jsonAsString);
			int objectToBeFoundType = json.getInt(DiagramPaster.FAKE_TAG_TYPE);
			BaseId objectToBeFoundId = json.getId(BaseObject.TAG_ID);
			factorRefs.add(new ORef(objectToBeFoundType, objectToBeFoundId));
		}
		
		return factorRefs;
	}

	public Vector<String> getDiagramLinkDeepCopies()
	{
		return diagramLinkDeepCopies;
	}

	public Vector<String> getFactorLinkDeepCopies()
	{
		return factorLinkDeepCopies;
	}

	public Point getUpperMostLeftMostCorner()
	{
		if (rectWithUpperMostLeftMostCorner == null)
			return new Point(0, 0);
		
		return rectWithUpperMostLeftMostCorner.getLocation();
	}

	public ORef getDiagramObjectRefCopiedFrom()
	{
		return diagramObjectRefCopiedFrom;
	}
	
	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		ObjectOutputStream objectOut = new ObjectOutputStream(out);
		
		objectOut.writeInt(diagramObjectRefCopiedFrom.getObjectType());
		objectOut.writeInt(diagramObjectRefCopiedFrom.getObjectId().asInt());
		objectOut.writeObject(projectName);
		objectOut.writeObject(factorDeepCopies);
		objectOut.writeObject(diagramFactorDeepCopies);
		objectOut.writeObject(threatStressRatingCopies);
		objectOut.writeObject(factorLinkDeepCopies);
		objectOut.writeObject(diagramLinkDeepCopies);
		objectOut.writeObject(rectWithUpperMostLeftMostCorner);
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		diagramObjectRefCopiedFrom = ORef.INVALID;
		project = null;
		ObjectInputStream objectIn = new ObjectInputStream(in);
				
		int diagramType = objectIn.readInt();
		int diagramId = objectIn.readInt();
		diagramObjectRefCopiedFrom =  new ORef(diagramType, new BaseId(diagramId)); 
		
		projectName = (String) objectIn.readObject();
		factorDeepCopies = readStringVector(objectIn);
		diagramFactorDeepCopies = readStringVector(objectIn);
		threatStressRatingCopies = readStringVector(objectIn);
		factorLinkDeepCopies = readStringVector(objectIn);
		diagramLinkDeepCopies = readStringVector(objectIn);
		rectWithUpperMostLeftMostCorner = (Rectangle) objectIn.readObject();
	}

	@SuppressWarnings("unchecked")
	private Vector<String> readStringVector(ObjectInputStream objectIn) throws IOException,	ClassNotFoundException
	{
		return (Vector<String>) objectIn.readObject();
	}
	
	public DataFlavor[] getTransferDataFlavors()
	{
		return null;
	}
	
	public boolean isDataFlavorSupported(DataFlavor flavor)
	{
		DataFlavor[] flavors = getTransferDataFlavors();
		for(int i = 0; i < flavors.length; ++i)
		{
			if(flavor.equals(flavors[i]))
				return true;
		}
		
		return false;
	}

	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException
	{
		if(isDataFlavorSupported(flavor))
			return this;
		
		throw new UnsupportedFlavorException(flavor);
	}
	
	public Project getProject()
	{
		return project;
	}
	
	abstract public boolean isLegacyTransferableMiradiList();
	
	public Project project;
	public ORef diagramObjectRefCopiedFrom;
	public String projectName;
	public Vector<String> factorDeepCopies;
	public Vector<String> diagramFactorDeepCopies;
	public Vector<String> threatStressRatingCopies;
	public Vector<String> factorLinkDeepCopies;
	public Vector<String> diagramLinkDeepCopies;
	public Rectangle rectWithUpperMostLeftMostCorner;
}
