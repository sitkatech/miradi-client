/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.dialogfields;

import java.util.Vector;

import javax.swing.Box;

import org.miradi.dialogfields.AnalysisLevelsChooserField.ComboBoxChangeHandler;
import org.miradi.dialogs.base.MiradiPanel;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.layout.OneColumnGridLayout;
import org.miradi.main.EAM;
import org.miradi.project.Project;
import org.miradi.questions.WorkPlanCategoryTypesQuestion;
import org.miradi.utils.CodeList;

public class AnalysisLevelsEditorComponent extends MiradiPanel
{
	public AnalysisLevelsEditorComponent(Project projectToUse)
	{
		super(new OneColumnGridLayout());
		
		editors = new Vector<SingleChoiceItemEditor>();
		WorkPlanCategoryTypesQuestion question = new WorkPlanCategoryTypesQuestion();
		int numberOfDropdowns = getNumberOfDropdowns(question);
		for (int level = 0; level < numberOfDropdowns; ++level)
		{
			SingleChoiceItemEditor levelEditor = new SingleChoiceItemEditor(question);
			editors.add(levelEditor);
			
			Box box = Box.createHorizontalBox();
			box.add(Box.createHorizontalStrut(level * INDENT_PER_LEVEL));
			box.add(new PanelTitleLabel(getLabel(level)));
			box.add(Box.createHorizontalStrut(GAP_BETWEEN_LABEL_AND_DROPDOWN));
			box.add(levelEditor);
			
			add(box);
		}
	}

	private String getLabel(int level)
	{
		if(level == 0)
			return EAM.text("Group by");
		
		return EAM.text("Then by");
	}

	private int getNumberOfDropdowns(WorkPlanCategoryTypesQuestion question)
	{
		final int UNSPECIFIED_CHOICE_COUNT = 1;
		
		return question.size() - UNSPECIFIED_CHOICE_COUNT;
	}
	
	public String getText() throws Exception
	{
		CodeList allCodes = new CodeList();
		for(SingleChoiceItemEditor levelEditor : editors)
		{
			allCodes.add(levelEditor.getText());
		}

		return allCodes.toString();
	}

	public void setText(String codes) throws Exception
	{
		CodeList allCodes = new CodeList(codes);
		if (allCodes.size() > editors.size())
		{
			allCodes = stripOutExtraCodes(allCodes);
			EAM.logError(EAM.substitute(EAM.text("Extra/unexpected configuration data was found, and will be ignored.\n CodeList= %s"), codes));
		}
		
		for (int index = 0; index < allCodes.size(); ++index)
		{
			SingleChoiceItemEditor levelEditor = editors.get(index);
			String singleLevelCode = allCodes.get(index);
			levelEditor.setText(singleLevelCode);
		}
	}
	
	private CodeList stripOutExtraCodes(CodeList allCodes)
	{
		CodeList strippedOfExtras = new CodeList();
		for (int index = 0; index < editors.size(); ++index)
		{
			String code = allCodes.get(index);
			strippedOfExtras.add(code);
		}
		
		return strippedOfExtras;
	}

	public void addActionListener(ComboBoxChangeHandler comboChangeHandler)
	{
		for(SingleChoiceItemEditor levelEditor : editors)
		{
			levelEditor.addActionListener(comboChangeHandler);
		}
	}

	private static final int INDENT_PER_LEVEL = 20;
	private static final int GAP_BETWEEN_LABEL_AND_DROPDOWN = 4;
	
	private Vector<SingleChoiceItemEditor> editors;
}
