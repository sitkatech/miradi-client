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
package org.miradi.dialogs.planning;

import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.PlanningTreeConfiguration;
import org.miradi.objects.ViewData;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.CustomPlanningColumnsQuestion;
import org.miradi.utils.CodeList;
import org.miradi.utils.StringList;

public class ConfigurableRowColumnProvider extends PlanningViewRowColumnProvider
{
	public ConfigurableRowColumnProvider(Project projectToUse)
	{
		super(projectToUse);
	}

	public CodeList getColumnCodesToShow() throws Exception
	{
		return getVisibleColumnsForCustomization(getCurrentViewData());
	}

	public CodeList getRowCodesToShow() throws Exception
	{
		return getVisibleRowsForCustomization(getCurrentViewData());
	}

	public static CodeList getVisibleRowsForCustomization(ViewData viewData)
	{
		try
		{
			ORef customizationRef = viewData.getORef(ViewData.TAG_TREE_CONFIGURATION_REF);
			if(customizationRef.isInvalid())
				return new CodeList();
			PlanningTreeConfiguration customization = (PlanningTreeConfiguration)viewData.getProject().findObject(customizationRef);
			return customization.getRowCodesToShow();
		}
		catch(Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog("Error: Unable to read customized rows");
			return new CodeList();
		}
	}

	public static CodeList getVisibleColumnsForCustomization(ViewData viewData)
	{
		try
		{
			ORef customizationRef = viewData.getORef(ViewData.TAG_TREE_CONFIGURATION_REF);
			if(customizationRef.isInvalid())
				return new CodeList();
			
			PlanningTreeConfiguration customization = (PlanningTreeConfiguration)viewData.getProject().findObject(customizationRef);
			CodeList columnCodes = customization.getColumnCodesToShow();
			omitUnknownColumnTagsInPlace(viewData.getProject(), columnCodes);
			
			return columnCodes;
		}
		catch(Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog("Error: Unable to read customized columns");
			return new CodeList();
		}
	}

	private static void omitUnknownColumnTagsInPlace(Project project, CodeList rawCodes)
	{
		ChoiceQuestion question = project.getQuestion(CustomPlanningColumnsQuestion.class);
		CodeList validColumnCodes = question.getAllCodes();
		validColumnCodes.addAll(getLegacyUselessButHarmlessColumnCodes());
		CodeList originalCodeList = new CodeList(rawCodes);
		rawCodes.retainAll(validColumnCodes);
		
		boolean wereCodesRemoved = originalCodeList.size() != rawCodes.size();
		originalCodeList.subtract(validColumnCodes);
		if (wereCodesRemoved)
			EAM.logWarning(("Custom tab list of custom codes was filtered and had unknown codes removed from it. Codes removed:" + originalCodeList));
	}

	private static StringList getLegacyUselessButHarmlessColumnCodes()
	{
		StringList legacyCodes = new StringList();
		legacyCodes.add("PseudoTaskBudgetTotal");
		legacyCodes.add("Who");
		return legacyCodes;
	}
}
