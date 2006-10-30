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

		public ThreatTableSorter(Project projectToUse, DefaultTableModel modelToUse)
		{
			model = (NonEditableThreatMatrixTableModel)modelToUse;
			project = projectToUse;
			rowCount = model.getRowCount()-summaryRow;
		}

		public int[] sortByColumn(int column, boolean ascending) 
		{
			int sortColumn = column;
			ArrayList columnData = new ArrayList();

			for (int rowIndex = 0; rowIndex<rowCount; ++rowIndex) {
				columnData.add(new ColumnObject(rowIndex, model.getValueAt(rowIndex,sortColumn)));
			}

			Collections.sort(columnData);

			int rows[] = new int[rowCount + summaryRow];
			for (int rowIndex = 0; rowIndex<rowCount; ++rowIndex) {
				rows[rowIndex] = ((ColumnObject) columnData.get(rowIndex)).getOldRow();
			}
			rows[rowCount] = rowCount;
			
			
			if (!ascending) 
			{
				int reverserows[] = new int[rowCount + summaryRow];
				for (int rowIndex=0; rowIndex<rows.length; ++rowIndex) 
				{
					reverserows[rowIndex] = rows[rows.length-rowIndex-1];
					System.out.println(rows.length-rowIndex-1 + " oldRow=" + rows[rows.length-rowIndex-1]);
				}
				//rows = reverserows;
			}
			
			return rows;
		}
		
		int summaryRow = 1;
		NonEditableThreatMatrixTableModel model;
		Project project;
		int rowCount;


	}