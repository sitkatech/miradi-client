/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs;

import java.util.Vector;

import javax.swing.JList;

import org.conservationmeasures.eam.dialogs.fieldComponents.PanelList;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objectpools.EAMObjectPool;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.project.Project;

public class AnnotationSelectionList extends DisposablePanel
{
	public AnnotationSelectionList(Project projectToUse, int annotationType, EAMObjectPool pool)
	{
		project = projectToUse;
		list = new PanelList(getData(annotationType, pool));
		add(list);
	}

	private Object[] getData(int annotationType, EAMObjectPool pool)
	{
		Vector items = new Vector();
		ORefList orefList = pool.getORefList();
		for (int i=0; i<orefList.size(); ++i)
		{
			ORef oref = orefList.get(i);
			Factor factor = (Factor)project.findObject(oref);
			ORefList annotations = factor.getOwnedObjects(annotationType);
			for (int j=0; j<annotations.size(); ++j)
			{
				BaseObject annotation = project.findObject(annotations.get(j));
				items.add(new Data(annotation, factor));
			}
		}
		
		return items.toArray(new Data[0]);
	}
	
	public BaseObject getSelectedAnnotaton()
	{
		Data data = (Data) list.getSelectedValue();
		return data.getAnnotationObject();
	}
	
	class Data extends Object
	{
		public Data(BaseObject annotationObjectToUse, Factor factorToUse)
		{
			annotationObject = annotationObjectToUse;
			factor = factorToUse;
		}
		
		public String toString()
		{
			return factor.getLabel() +  ":" + annotationObject.getLabel();
		}
		
		public BaseObject getAnnotationObject()
		{
			return annotationObject;
		}
		
		BaseObject annotationObject;
		Factor factor;
		
	}
	
	Project project;
	JList list;
}

