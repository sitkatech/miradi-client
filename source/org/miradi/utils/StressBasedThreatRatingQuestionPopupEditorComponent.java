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

package org.miradi.utils;

import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import org.miradi.main.EAM;
import org.miradi.objects.Stress;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;

public class StressBasedThreatRatingQuestionPopupEditorComponent extends AbstractThreatRatingQuestionPopupEditorComponent
{
	public StressBasedThreatRatingQuestionPopupEditorComponent(Project projectToUse, ChoiceQuestion questionToUse) throws Exception
	{
		super(projectToUse, questionToUse);
	}
	
	@Override
	protected void addListeners(PopupLauncherTableCellComponent holder)
	{
		holder.addAncestorListenersToEverything(new AncestorHandler());
	}

	public void setStressRef(Stress stressToUse)
	{
		stress = stressToUse;
	}
	
	@Override
	protected String getAdditionalLabel()
	{
		return EAM.substitute(EAM.text("Stress: %s"), stress.getFullName());
	}
	
	private class AncestorHandler implements AncestorListener
	{
		public void ancestorAdded(AncestorEvent event)
		{
			invokePopupEditor();
		}

		public void ancestorMoved(AncestorEvent event)
		{
		}

		public void ancestorRemoved(AncestorEvent event)
		{
		}
	}
	
	private Stress stress;
}
