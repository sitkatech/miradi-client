/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.slideshow;

import org.conservationmeasures.eam.dialogs.ObjectDataInputPanel;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objectpools.EAMObjectPool;
import org.conservationmeasures.eam.objects.Slide;
import org.conservationmeasures.eam.objects.SlideShow;
import org.conservationmeasures.eam.project.Project;

public class SlideShowPropertiesPanel extends ObjectDataInputPanel
{
	public SlideShowPropertiesPanel(Project projectToUse, BaseId idToEdit) throws Exception
	{
		super(projectToUse, ObjectType.SLIDE, idToEdit);
		addField(createStringField(SlideShow.getObjectType(),SlideShow.TAG_LABEL));
		addField(createStringField(Slide.getObjectType(), Slide.TAG_LABEL));
		updateFieldsFromProject();
	}
	
	public void setObjectRefs(ORef[] orefsToUse)
	{
		ORefList list = new ORefList();
		addSlideShowRef(list);
		list.addAll(orefsToUse);
		super.setObjectRefs(list.toArray());
	}
	
	public void addSlideShowRef(ORefList list)
	{
		EAMObjectPool pool = getProject().getPool(SlideShow.getObjectType());
		if (pool.size()==0)
			return;
		list.add(new ORef(SlideShow.getObjectType(),pool.getIds()[0]));
	}

	public String getPanelDescription()
	{
		return EAM.text("Title|Slide Properties");
	}
}
