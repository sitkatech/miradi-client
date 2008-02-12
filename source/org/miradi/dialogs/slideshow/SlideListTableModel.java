/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.slideshow;

import org.miradi.dialogs.base.ObjectListTableModel;
import org.miradi.ids.IdList;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.Slide;
import org.miradi.objects.SlideShow;
import org.miradi.project.Project;

public class SlideListTableModel extends ObjectListTableModel
{
	public SlideListTableModel(Project project, ORef containingOref)
	{
		super(project, containingOref.getObjectType(), containingOref.getObjectId(), SlideShow.TAG_SLIDE_REFS, Slide.getObjectType(), COLUMN_TAGS);
	}

	public ORefList getLatestRefListFromProject()
	{
		return new ORefList(getRowObjectType(), getLatestIdListFromProject());
	}
	
	private IdList getLatestIdListFromProject()
	{
		try
		{
			ORefList orefList = new ORefList(getContainingObject().getData(SlideShow.TAG_SLIDE_REFS));
			IdList list = new IdList(Slide.getObjectType());
			for (int i=0; i<orefList.size(); ++i)
				list.add(orefList.get(i).getObjectId());
			return list;
		}
		catch(Exception e)
		{
			EAM.logException(e);
			throw new RuntimeException();
		}
	}
	
	private static final String[] COLUMN_TAGS = new String[] {
		Slide.TAG_LABEL,
	};
}
