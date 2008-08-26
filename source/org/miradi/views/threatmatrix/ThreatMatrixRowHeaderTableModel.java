/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.views.threatmatrix;

import org.miradi.ids.FactorId;
import org.miradi.main.EAM;
import org.miradi.objects.Factor;
import org.miradi.objects.Target;
import org.miradi.project.Project;

public class ThreatMatrixRowHeaderTableModel extends AbstractThreatTargetTableModel
{
	public ThreatMatrixRowHeaderTableModel(Project project, ThreatMatrixTableModel modelToUse) 
	{
		super(project);
		
		model = modelToUse;
	}

	public int getColumnCount()
	{
		return 1;
	}

	public Object getValueAt(int row, int column)
	{
		if (row == getRowCount() - 1) 
			return EAM.text("Summary Target Rating");
		
		return model.getDirectThreats()[row];
	}

	public boolean isCellEditable(int row, int column)
	{
		return false;
	}
	
	public String getColumnName(int columnIndex) 
	{
		return 	EAM.text("THREATS");
	}
	
	public int getRowCount()
	{
		return model.getRowCount();
	}
	
	@Override
	public Factor getDirectThreat(int row)
	{
		return model.getDirectThreat(row);
	}

	@Override
	protected Factor[] getDirectThreats()
	{
		return model.getDirectThreats();
	}

	@Override
	public Target getTarget(int modelColumn)
	{
		return model.getTarget(modelColumn);
	}

	@Override
	public int getTargetCount()
	{
		return model.getTargetCount();
	}

	@Override
	public FactorId getTargetId(int targetIndex)
	{
		return model.getTargetId(targetIndex);
	}

	@Override
	public String getTargetName(int targetIndex)
	{
		return model.getTargetName(targetIndex);
	}

	@Override
	protected Factor[] getTargets()
	{
		return model.getTargets();
	}

	@Override
	public int getThreatCount()
	{
		return model.getThreatCount();
	}

	@Override
	public FactorId getThreatId(int threatIndex)
	{
		return model.getThreatId(threatIndex);
	}

	@Override
	public String getThreatName(int threatIndex)
	{
		return model.getThreatName(threatIndex);
	}

	private ThreatMatrixTableModel model;
}
