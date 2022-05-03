/* 
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
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
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.PlanningTreeRowColumnProvider;
import org.miradi.project.Project;
import org.miradi.schemas.ConceptualModelDiagramSchema;
import org.miradi.schemas.ResultsChainDiagramSchema;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DiagramChoiceQuestion extends ObjectQuestion
{
	public DiagramChoiceQuestion()
	{
		super(new BaseObject[]{});
	}

	public DiagramChoiceQuestion(Project projectToUse, PlanningTreeRowColumnProvider rowColumnProviderToUse)
	{
		super(new BaseObject[]{});
		rowColumnProvider = rowColumnProviderToUse;
		setObjects(getAllDiagramObjects(projectToUse, rowColumnProvider));
	}

	private DiagramObject[] getAllDiagramObjects(Project projectToUse, PlanningTreeRowColumnProvider rowColumnProvider)
	{
		List<DiagramObject> diagramObjects =new ArrayList<DiagramObject>();

		try
		{
			if (rowColumnProvider.shouldIncludeConceptualModelPage())
                Collections.addAll(diagramObjects, projectToUse.getConceptualModelDiagramPool().getAllDiagramObjects());

			if (rowColumnProvider.shouldIncludeResultsChain())
				Collections.addAll(diagramObjects, projectToUse.getResultsChainDiagramPool().getAllDiagramObjects());
		}
		catch (Exception e)
		{
			EAM.panic(e);
		}

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
		setObjects(getAllDiagramObjects(projectToUse, rowColumnProvider));
	}

	@Override
	public boolean canSelectMultiple()
	{
		return false;
	}

	public ChoiceItem getSelectedChoiceItem(Project project, String diagramFilter)
	{
		if (shouldResetDiagramFilter(diagramFilter))
		{
			return new ChoiceItem("", DiagramChoiceQuestion.getUnspecifiedChoiceText());
		}
		else
		{
			ORef diagramFilterObjectRef = ORef.createFromString(diagramFilter);
			DiagramObject diagramFilterObject = DiagramObject.findDiagramObject(project, diagramFilterObjectRef);
			return new ChoiceItem(diagramFilterObjectRef.toString(), diagramFilterObject.getFullName());
		}
	}

	public boolean shouldResetDiagramFilter(String diagramFilter)
	{
		try
		{
			if (diagramFilter.isEmpty())
				return true;

			ORef diagramFilterObjectRef = ORef.createFromString(diagramFilter);

			if (diagramFilterObjectRef.getObjectType() == ConceptualModelDiagramSchema.getObjectType() && !rowColumnProvider.shouldIncludeConceptualModelPage())
				return true;

			if (diagramFilterObjectRef.getObjectType() == ResultsChainDiagramSchema.getObjectType() && !rowColumnProvider.shouldIncludeResultsChain())
				return true;

		}
		catch (Exception e)
		{
			EAM.panic(e);
		}

		return false;
	}

	private PlanningTreeRowColumnProvider rowColumnProvider;
}
