/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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
