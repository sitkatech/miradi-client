/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.util.ArrayList;
import java.util.Collections;

import javax.swing.table.DefaultTableModel;

import org.conservationmeasures.eam.project.Project;

public class ThreatTableSorter
	{

		public ThreatTableSorter(Project projectToUse, DefaultTableModel modelToUse, String tableTypeToUse)
		{
			model = modelToUse;
			project = projectToUse;
			rowCount = model.getRowCount()-summaryRow;
			tableType = tableTypeToUse;
		}

		public int[] sortByColumn(int column, boolean ascending) 
		{
			int sortColumn = column;
			ArrayList columnData = new ArrayList();

			for (int rowIndex = 0; rowIndex<rowCount; ++rowIndex) 
			{
				if (tableType.equals("ROWHEADER"))
					columnData.add(new ComparableNode(model.getValueAt(rowIndex,sortColumn).toString(), rowIndex));
				else
					columnData.add(new ColumnObject(rowIndex, model.getValueAt(rowIndex,sortColumn)));
			}

			Collections.sort(columnData);
			
			if (!ascending) 
				Collections.reverse(columnData);

			int rows[] = new int[rowCount + summaryRow];
			for (int rowIndex = 0; rowIndex<rowCount; ++rowIndex) 
			{
				if (tableType.equals("ROWHEADER"))
					rows[rowIndex] = ((ComparableNode) columnData.get(rowIndex)).getOldRow();
				else
					rows[rowIndex] = ((ColumnObject) columnData.get(rowIndex)).getOldRow();
			}
			rows[rowCount] = rowCount;
			
			return rows;
		}
		
		int summaryRow = 1;
		DefaultTableModel model;
		Project project;
		int rowCount;
		String tableType;


	}