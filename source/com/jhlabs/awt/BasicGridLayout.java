/*
 * This code was downloaded from: http://www.jhlabs.com/java/layout/index.html
 * on 2006-02-06. According to that page, here is the license:
 * 
 * 
 ***************
 * License

 The downloadable source code on this page is released under the Apache License. 
 Basically, this means that you are free to do whatever you like with this code, 
 including commercial use, but it's not my fault if your satellite/nuclear 
 power station/missile system fails as a result.

 Licensed under the Apache License, Version 2.0 (the "License"); 
 you may not use this code except in compliance with the License. 
 You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 
 Unless required by applicable law or agreed to in writing, software distributed under 
 the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS 
 OF ANY KIND, either express or implied. See the License for the specific language 
 governing permissions and limitations under the License.
 
 ***************
 * 
 * This file was modified slightly by Benetech to avoid compile warnings
 * 
 */

package com.jhlabs.awt;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;

public class BasicGridLayout extends ConstraintLayout {

	protected int hGap, vGap;
	protected int rows, cols, reqRows, reqCols;
	protected int[] rowHeights, colWidths;
	protected int alignment = Alignment.NORTHWEST;
	protected int fill = Alignment.FILL_BOTH;
	protected int colWeight = 0;
	protected int rowWeight = 0;

	public BasicGridLayout() {
		this(0, 1, 2, 2);
	}

	public BasicGridLayout(int rows, int cols) {
		this(rows, cols, 2, 2);
	}

	public BasicGridLayout(int rows, int cols, int hGap, int vGap) {
		this(rows, cols, hGap, vGap, 0, 0);
	}

	public BasicGridLayout(int rows, int cols, int hGap, int vGap, int hMargin, int vMargin) {
		// The following two lines were modified by Benetech, to avoid compile warnings
		// We ASSUME the "this." was missing from each of the middle values.
		this.reqRows = this.rows = rows;
		this.reqCols = this.cols = cols;
		this.hGap = hGap;
		this.vGap = vGap;
		this.hMargin = hMargin;
		this.vMargin = vMargin;
	}

	public void setColumns(int cols) {
		if (cols < 1)
			cols = 1;
		this.cols = cols;
	}
	
	public void setRows(int rows) {
		if (rows < 1)
			rows = 1;
		this.rows = rows;
	}

	public int getRows() {
		return rows;
	}
	
	public int getColumns() {
		return cols;
	}

	public void setAlignment(int a) {
		alignment = a;
	}

	public int getAlignment() {
		return alignment;
	}
	
	public void setFill(int f) {
		fill = f;
	}

	public int getFill() {
		return fill;
	}
	
	public void setColWeight(int colWeight) {
		this.colWeight = colWeight;
	}

	public int getColWeight() {
		return colWeight;
	}

	public void setRowWeight(int rowWeight) {
		this.rowWeight = rowWeight;
	}

	public int getRowWeight() {
		return rowWeight;
	}

	/**
	 * Override this to set alignment on a per-component basis.
	 */
	protected int alignmentFor(Component c, int row, int col) {
		return alignment;
	}

	/**
	 * Override this to set fill on a per-component basis.
	 */
	protected int fillFor(Component c, int row, int col) {
		return fill;
	}

	/**
	 * Override this to set weights on a per-row basis.
	 */
	protected int getRowWeight(int row) {
		return getRowWeight();
	}

	/**
	 * Override this to set weights on a per-column basis.
	 */
	protected int getColWeight(int col) {
		return getColWeight();
	}

	protected int sumArray(int[] array, int spacing, int size) {
		int i, total = 0;

		for (i = 0; i < size; i++)
			total += array[i];
		if (size > 1)
			total += (size-1) * spacing;
		return total;
	}

	protected void calcCellSizes(Container target, int type) {
		int i;
		int count = target.getComponentCount();

		if (reqCols <= 0) {
			rows = reqRows;
			cols = (count+reqRows-1)/reqRows;
		} else {
			rows = (count+reqCols-1)/reqCols;
			cols = reqCols;
		}
		
		colWidths = new int[cols];
		for (i = 0; i < cols; i++)
			colWidths[i] = 0;
		rowHeights = new int[rows];
		for (i = 0; i < rows; i++)
			rowHeights[i] = 0;

		int index = 0;
		for (i = 0; i < count; i++) {
			Component c = target.getComponent(i);
			if (includeComponent(c)) {
				Dimension size = getComponentSize(c, type);
				int row = index / cols;
				int col = index % cols;
				if (colWidths[col] < size.width)
					colWidths[col] = size.width;
				if (rowHeights[row] < size.height)
					rowHeights[row] = size.height;
				index++;
			}
		}

		Dimension size = target.getSize();
		Insets insets = target.getInsets();
		int totalWeight, totalSize, remainder;

		size.width -= insets.left+insets.right+2*hMargin;
		size.height -= insets.top+insets.bottom+2*vMargin;
		totalWeight = totalSize = 0;
		for (i = 0; i < cols; i++) {
			totalWeight += getColWeight(i);
			totalSize += colWidths[i];
			if (i != 0)
				totalSize += hGap;
		}
		if (totalWeight != 0 && totalSize < size.width) {
			remainder = size.width - totalSize;
			for (i = 0; i < cols; i++)
				colWidths[i] += remainder * getColWeight(i) / totalWeight;
		}

		totalWeight = totalSize = 0;
		for (i = 0; i < rows; i++) {
			totalWeight += getRowWeight(i);
			totalSize += rowHeights[i];
			if (i != 0)
				totalSize += vGap;
		}
		if (totalWeight != 0 && totalSize < size.height) {
			remainder = size.height - totalSize;
			for (i = 0; i < rows; i++)
				rowHeights[i] += remainder * getRowWeight(i) / totalWeight;
		}
	}

	public void measureLayout(Container target, Dimension dimension, int type)  {
		if (dimension != null) {
			calcCellSizes(target, type);
			dimension.width = sumArray(colWidths, hGap, cols);
			dimension.height = sumArray(rowHeights, vGap, rows);
			rowHeights = colWidths = null;
		} else {
			int count = target.getComponentCount();
			if (count > 0) {
				Insets insets = target.getInsets();
				int index = 0;

				calcCellSizes(target, type);

				for (int i = 0; i < count; i++) {
					Component c = target.getComponent(i);
					if (includeComponent(c)) {
						Dimension d = getComponentSize(c, type);
						Rectangle r = new Rectangle(0, 0, d.width, d.height);
						int row = index / cols;
						int col = index % cols;
						int x, y;

						x = insets.left+sumArray(colWidths, hGap, col)+hMargin;
						y = insets.top+sumArray(rowHeights, vGap, row)+vMargin;
						if (col > 0)
							x += hGap;
						if (row > 0)
							y += vGap;
						Rectangle cell = new Rectangle(x, y, colWidths[col], rowHeights[row]);
						Alignment.alignInCell(r, cell, alignmentFor(c, row, col), fillFor(c, row, col));
						c.setBounds(r.x, r.y, r.width, r.height);
						index++;
					}
				}
			}
			rowHeights = colWidths = null;
		}
	}
}

