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

		public int[] sortByColumn(int columnToUse, boolean ascendingToUse) 
		{
			int sortColumn =columnToUse;
			ArrayList al = new ArrayList();

			for (int i = 0; i<rowCount; ++i) {
				al.add(new ColumnObject(i, model.getValueAt(i,sortColumn)));
			}

			Collections.sort(al);

			int rows[] = new int[rowCount + summaryRow];
			for (int i = 0; i<rowCount; ++i) {
				rows[i] = ((ColumnObject) al.get(i)).getOldRow();
			}
			rows[rowCount] = rowCount;
			
			return rows;
		}
		
		int summaryRow = 1;
		boolean ascending = true;
		NonEditableThreatMatrixTableModel model;
		Project project;
		int rowCount;


	}