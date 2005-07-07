/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.Action;

import org.conservationmeasures.eam.actions.ActionContextualHelp;
import org.conservationmeasures.eam.diagram.cells.CellType;
import org.conservationmeasures.eam.diagram.cells.CellTypeThreat;
import org.conservationmeasures.eam.diagram.cells.CellViewFactory;
import org.conservationmeasures.eam.diagram.cells.FlexibleCell;
import org.conservationmeasures.eam.main.ComponentWithContextMenu;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.KeyBinder;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.main.MouseContextMenuAdapter;
import org.jgraph.JGraph;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.GraphConstants;

public class DiagramComponent extends JGraph implements ComponentWithContextMenu
{
	public DiagramComponent(MainWindow mainWindowToUse)
	{
		super(new DefaultGraphModel());
		mainWindow = mainWindowToUse;
		diagramContextMenuHandler = new DiagramContextMenuHandler(this);
		getGraphLayoutCache().setFactory(new CellViewFactory());		
		installKeyBindings();
		addMouseListener(new MouseContextMenuAdapter(this));
		
		createBox(new CellTypeThreat(), 500, 250, EAM.text("[Edit this threat]"));

	}
	
	public MainWindow getMainWindow()
	{
		return mainWindow;
	}
	
	void installKeyBindings()
	{
		Action helpAction = new ActionContextualHelp(mainWindow);
		KeyBinder.bindKey(this, KeyEvent.VK_F1, KeyBinder.KEY_MODIFIER_NONE, helpAction);
	}
	
	public void showContextMenu(MouseEvent e)
	{
		diagramContextMenuHandler.showContextMenu(e);
	}

	private void createBox(CellType cellType, int x, int y, String label)
	{
		String formattedLabel = "<html><div align='center'><font size='4'>" + label + "</font></div></html>";
		Color color = cellType.getColor();
		Map attributeMap = createCellAttributeMap(formattedLabel, new Point(x, y), color);
		insertNewCell(attributeMap, cellType);
	}
	
	private Map createCellAttributeMap(String cellText, Point at, Color color)
	{
		Map cellAttributes = new HashMap();

		Point2D snapped = snap(at);
		at = new Point((int)snapped.getX(), (int)snapped.getY());
		double width = 120 * scale;
		double height = 60 * scale;
		Dimension size = new Dimension((int)width, (int)height);
		GraphConstants.setBounds(cellAttributes, new Rectangle(at, size));
		GraphConstants.setBorderColor(cellAttributes, Color.black);
		GraphConstants.setBackground(cellAttributes, color);
		GraphConstants.setForeground(cellAttributes, Color.black);
		// TODO: I don't know why the font size is ignored
		double fontSize = 8 * scale;
		Font font = getFont().deriveFont(Font.BOLD, (float)fontSize);
		GraphConstants.setFont(cellAttributes, font);
		
		GraphConstants.setOpaque(cellAttributes, true);
		GraphConstants.setValue(cellAttributes, cellText);
		return cellAttributes;
	}
	
	private DefaultGraphCell insertNewCell(Map cellAttributes, CellType type)
	{
		Hashtable nest = new Hashtable();
		DefaultGraphCell cell = new FlexibleCell(type);
		cell.add(new DefaultPort());
	    nest.put(cell, cellAttributes);
	    Object[] arg = new Object[]{cell};
	    getModel().insert(arg, nest, null, null, null);
	    return cell;
	}

	MainWindow mainWindow;
	DiagramContextMenuHandler diagramContextMenuHandler;
}

