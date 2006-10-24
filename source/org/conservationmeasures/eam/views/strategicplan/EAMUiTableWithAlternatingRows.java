/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import javax.swing.table.TableModel;

import org.conservationmeasures.eam.utils.UiTableWithAlternatingRows;
import org.martus.swing.UiButton;

public class EAMUiTableWithAlternatingRows extends UiTableWithAlternatingRows
{


	public EAMUiTableWithAlternatingRows(TableModel model)
	{
		super(model);
	}
	
	public UiButton getDoubleClickAction() {
		return doubleClickAction;
	}
	
	public void setDoubleClickAction(UiButton doubleClickActionIn) {
		 doubleClickAction = doubleClickActionIn;
	}
	
	UiButton doubleClickAction;

}
