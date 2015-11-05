/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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
package org.miradi.questions;

import org.miradi.main.EAM;
import org.miradi.objects.BaseObject;
import org.miradi.objects.DiagramObject;
import org.miradi.project.Project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DiagramChoiceQuestion extends ObjectQuestion
{
	public DiagramChoiceQuestion()
	{
		super(new BaseObject[]{});
	}

	public DiagramChoiceQuestion(Project projectToUse)
	{
		super(getAllDiagramObjects(projectToUse));
	}

	private static DiagramObject[] getAllDiagramObjects(Project projectToUse)
	{
		List<DiagramObject> diagramObjects =new ArrayList<DiagramObject>();

		if (projectToUse.getMetadata().shouldIncludeConceptualModelPage())
			Collections.addAll(diagramObjects, projectToUse.getConceptualModelDiagramPool().getAllDiagramObjects());

		if (projectToUse.getMetadata().shouldIncludeResultsChain())
			Collections.addAll(diagramObjects, projectToUse.getResultsChainDiagramPool().getAllDiagramObjects());

		return diagramObjects.toArray(new DiagramObject[diagramObjects.size()]);
	}

	@Override
	public ChoiceItem[] getChoices()
	{
		return createChoiceItemListWithUnspecifiedItem(super.getChoices(), getUnspecifiedChoiceText());
	}

	public static String getUnspecifiedChoiceText()
	{
		return EAM.text("All Diagrams");
	}

	@Override
	public void reloadQuestion(Project projectToUse)
	{
		super.reloadQuestion();
		setObjects(getAllDiagramObjects(projectToUse));
	}

	@Override
	public boolean canSelectMultiple()
	{
		return false;
	}
}
