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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;

import org.martus.swing.UiScrollPane;
import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.objects.ValueOption;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;


public class MatrixTableImageCreator  
{  
	
	static public BufferedImage getImage(Project project)
	{
		try
		{
			ThreatMatrixTableModel model = new ThreatMatrixTableModel(project);
			ThreatGridPanel grid = new ThreatGridPanel(null, model);
			//TODO: is there a better way to do this
			JFrame frame = new JFrame();
			frame.add(new UiScrollPane(grid));
			frame.pack();
			return createImage(project, grid.getThreatMatrixTable(), grid.getRowHeaderTable());
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return new BufferedImage(0, 0, BufferedImage.TYPE_INT_RGB);  
		}
	}

	
    public static BufferedImage createImage(Project project, JTable table, JTable rowHeaderTable) 
    { 
		try
		{
			ValueOption myValueOption = new WhiteInvalidValueOption(project.getObjectManager());
	    	((ThreatMatrixTableModel)table.getModel()).setDefaultValueOption(myValueOption);
	    	TableHeaderRenderer.SetOverRideColor(Color.WHITE);

	        JTableHeader tableHeaderSet = table.getTableHeader(); 
	        JTableHeader rowHeaderSet = rowHeaderTable.getTableHeader(); 
	         
	        int totalWidth	= table.getWidth() + rowHeaderTable.getWidth();
	        int totalHeight = tableHeaderSet.getHeight() + table.getHeight(); 
	
	        BufferedImage tableImage = new BufferedImage(totalWidth, totalHeight, BufferedImage.TYPE_INT_RGB); 
	        Graphics2D g2D = (Graphics2D)tableImage.getGraphics(); 
	 
	        writeData(rowHeaderSet, g2D, 0 ,0); 
	        writeData(tableHeaderSet, g2D, rowHeaderSet.getWidth(), 0); 
	        writeData(rowHeaderTable, g2D, -rowHeaderSet.getWidth(), rowHeaderSet.getHeight()); 
	        writeData(table, g2D, rowHeaderSet.getWidth(), 0); 

	        return tableImage;  
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return new BufferedImage(0, 0, BufferedImage.TYPE_INT_RGB);  
		}
		finally 
		{
	    	((ThreatMatrixTableModel)table.getModel()).setDefaultValueOption(null);
	    	TableHeaderRenderer.SetOverRideColor(null);
		}
		
    }


	private static void writeData(JComponent component, Graphics2D g2D, int offsetWidth, int offsetHeight)
	{
		g2D.translate(offsetWidth, offsetHeight);
		component.paint(g2D); 
	}

} 

 class WhiteInvalidValueOption extends ValueOption
	{
		public WhiteInvalidValueOption(ObjectManager objectManager) throws Exception
		{
			super(objectManager, new BaseId(-1), "", -1, Color.WHITE);
		}

		public Color getColor()
		{
			return Color.WHITE;
		}
	}