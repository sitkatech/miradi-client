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
package org.miradi.rtf.legend;

import java.util.Vector;

import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.RtfLegendObjectsQuestion;
import org.miradi.rtf.RtfWriter;
import org.miradi.utils.AbstractSingleTableExporter;

public class RtfLegendExporter
{
	public RtfLegendExporter(Project projectToUse)
	{
		project = projectToUse;
	}
	
	public void exportLegend(RtfWriter writer) throws Exception
	{
		writer.newParagraph();
		ChoiceQuestion question = getProject().getQuestion(RtfLegendObjectsQuestion.class);
		writer.writeRtfTable(new LegendModelExporter(getProject(), question.getChoices()));
		writer.newParagraph();
	}
	
	private Project getProject()
	{
		return project;
	}
	
	private Project project;
}

class LegendModelExporter extends AbstractSingleTableExporter
{
	public LegendModelExporter(Project projectToUse, ChoiceItem[]  choicesToUse)
	{
		super(projectToUse, NO_IDENTIFIER_BECAUSE_USER_CANT_CHANGE_COLUMNS);
		
		choices = choicesToUse;
	}
	
	@Override
	public ORefList getAllRefs(int objectType)
	{
		return new ORefList();
	}

	@Override
	public Vector<Integer> getAllTypes()
	{
		return new Vector<Integer>();
	}

	@Override
	public BaseObject getBaseObjectForRow(int row)
	{
		return null;
	}

	@Override
	public int getColumnCount()
	{
		return COLUMN_COUNT;
	}

	@Override
	public int getModelDepth(int row, int modelColumn)
	{
		return 0;
	}

	@Override
	public String getModelColumnName(int modelColumn)
	{
		return EAM.text("Legend Table");
	}
	
	@Override
	public ChoiceItem getModelChoiceItemAt(int row, int modelColumn)
	{
		return getChoice(row);
	}

	@Override
	public int getMaxDepthCount()
	{
		return 0;
	}

	@Override
	public int getRowCount()
	{
		return choices.length;
	}

	@Override
	public int getRowType(int row)
	{
		return ObjectType.FAKE;
	}

	@Override
	public String getModelTextAt(int row, int modelColumn)
	{
		return "";
	}
	
	private ChoiceItem getChoice(int row)
	{
		return choices[row];
	}
	
	public String getColumnGroupName(int modelColumn)
	{
		return getModelColumnName(modelColumn);
	}
		
	private static final String NO_IDENTIFIER_BECAUSE_USER_CANT_CHANGE_COLUMNS = "";

	private static final int COLUMN_COUNT = 1;
	private ChoiceItem[] choices;
}