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

import java.util.HashMap;
import java.util.Vector;

import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;

public abstract class AbstractTableExporter
{
	public AbstractTableExporter(Project projectToUse)
	{
		this(projectToUse, NO_UNIQUE_MODEL_IDENTIFIER);
	}
	
	public AbstractTableExporter(Project projectToUse, String uniqueModelIdentifierToUse)
	{
		project = projectToUse;
		uniqueModelIdentifier = uniqueModelIdentifierToUse;
	}

	public int convertToModelColumn(int tableColumn)
	{
		if (tableToModelColumnIndexMap == null)
			buildTableToModelColumnIndexMap();
		
		return tableToModelColumnIndexMap.get(tableColumn);
	}
	
	private void buildTableToModelColumnIndexMap()
	{
		CodeList modelColumnSequence = getModelColumnSequence();
		CodeList arrangedColumnCodes = ColumnSequenceSaver.calculateArrangedColumnCodes(getArrangedColumnCodes(), new CodeList(modelColumnSequence));
		tableToModelColumnIndexMap  = buildModelColumnIndexArray(arrangedColumnCodes, modelColumnSequence);
	}

	private CodeList getArrangedColumnCodes()
	{
		CodeList desiredColumnCodes = ColumnSequenceSaver.getDesiredColumnCodes(getProject(), getUniqueModelIdentifier());
		if (desiredColumnCodes != null)
			return desiredColumnCodes;
		
		return getModelColumnSequence();
	}

	public static HashMap<Integer, Integer> buildModelColumnIndexArray(CodeList desiredSequenceCodes, CodeList modelColumnCodes)
	{
		if (desiredSequenceCodes.size() == 0)
			return fillUsingModelColumnSameKeyAsValue(modelColumnCodes);
		
		HashMap<Integer, Integer> tableColumnToModelColumnMap = new HashMap();
		for (int tableColumn = 0; tableColumn < desiredSequenceCodes.size(); ++tableColumn)
		{
			String desiredCode = desiredSequenceCodes.get(tableColumn);
			int modelColumnIndexInModelColumnCodes = modelColumnCodes.find(desiredCode);
			if (modelColumnIndexInModelColumnCodes < 0)
				modelColumnIndexInModelColumnCodes = tableColumn;
			
			tableColumnToModelColumnMap.put(tableColumn, modelColumnIndexInModelColumnCodes);
		}
		
		return tableColumnToModelColumnMap;
	}

	private static HashMap<Integer, Integer> fillUsingModelColumnSameKeyAsValue(CodeList modelColumnCodes)
	{
		HashMap<Integer, Integer> modelColumnToModelColumnMap = new HashMap();
		for (int modelColumn = 0; modelColumn < modelColumnCodes.size(); ++modelColumn)
		{
			modelColumnToModelColumnMap.put(modelColumn, modelColumn);
		}
		
		return modelColumnToModelColumnMap;
	}
	
	private CodeList getModelColumnSequence()
	{
		CodeList currentColumnTagSequences = new CodeList();
		for (int modelColumn = 0; modelColumn < getColumnCount(); ++modelColumn)
		{	
			currentColumnTagSequences.add(getModelColumnName(modelColumn));
		}
		
		return currentColumnTagSequences;
	}
	
	public String getStyleTagAt(int row, int column)
	{
		return "";
	}
	
	public String getSafeValue(Object object)
	{
		if (object == null)
			return "";
		
		return object.toString();
	}
	
	protected String createExportableCodeList(CodeList codeList, ChoiceQuestion question)
	{
		StringBuffer codeListAsString = new StringBuffer();
		for (int index = 0; index < codeList.size(); ++index)
		{
			ChoiceItem choiceItem = question.findChoiceByCode(codeList.get(index));
			codeListAsString.append(choiceItem.getLabel());
			codeListAsString.append(CODE_LIST_SEPERATOR);
		}
		
		return codeListAsString.toString();
	}
	
	private Project getProject()
	{
		return project;
	}
	
	private String getUniqueModelIdentifier()
	{
		return uniqueModelIdentifier;
	}
	
	
	public int getDepth(int row, int tableColumn)
	{
		int modelColumn = convertToModelColumn(tableColumn);
		return getModelDepth(row, modelColumn);
	}
	
	public String getTextAt(int row, int tableColumn)
	{
		int modelColumn = convertToModelColumn(tableColumn);
		return getTextAt(row, modelColumn);
	}
	
	public ChoiceItem getChoiceItemAt(int row, int tableColumn)
	{
		int modelColumn = convertToModelColumn(tableColumn);
		return getModelChoiceItemAt(row, modelColumn);
	}
	
	public String getColumnName(int tableColumn)
	{
		int modelColumn = convertToModelColumn(tableColumn);
		return getModelColumnName(modelColumn);
	}
	
	abstract public int getMaxDepthCount();
	abstract public int getColumnCount();
	abstract public int getRowCount();
	abstract public int getRowType(int row);
	abstract public BaseObject getBaseObjectForRow(int row);
	
	abstract protected String getModelColumnName(int modelColumn);
	abstract protected String getModelTextAt(int row, int modelColumn);
	abstract protected ChoiceItem getModelChoiceItemAt(int row, int modelColumn);
	abstract protected int getModelDepth(int row, int modelColumn);
	
	//TODO these two methods were created to export details of tree or table.
	// we currently dont export details of tree or tables, and these methods might
	// need to be removed
	abstract public Vector<Integer> getAllTypes();
	abstract public ORefList getAllRefs(int objectType);
	
	private static final String CODE_LIST_SEPERATOR = ";";
	public static final String NO_UNIQUE_MODEL_IDENTIFIER = "";
	private String uniqueModelIdentifier;
	private Project project;
	private HashMap<Integer, Integer> tableToModelColumnIndexMap;  
}
