/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.threatmatrix;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import org.martus.swing.UiLabel;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.ids.FactorId;
import org.miradi.main.EAM;
import org.miradi.objects.ViewData;
import org.miradi.project.Project;
import org.miradi.project.SimpleThreatRatingFramework;
import org.miradi.project.ThreatRatingBundle;

public class ThreatGridPanel extends JPanel
{
	public ThreatGridPanel(ThreatMatrixView viewToUse,
			ThreatMatrixTableModel modelToUse)
			throws Exception
	{
		super(new BorderLayout());
		view = viewToUse;
		add(createHeading(), BorderLayout.BEFORE_FIRST_LINE);
		add(createThreatGridPanel(modelToUse));
	}

	private JComponent createHeading()
	{
		String targetLabelText = "<html><h2>TARGETS</h2></html>";
		UiLabel targetLabel = new PanelTitleLabel(EAM.text(targetLabelText));
		targetLabel.setHorizontalAlignment(SwingConstants.CENTER);
		return targetLabel;
	}
	
	public JScrollPane createThreatGridPanel(ThreatMatrixTableModel model) throws Exception
	{
		ThreatMatrixRowHeaderTableModel newRowHeaderData = new ThreatMatrixRowHeaderTableModel(model);
		rowHeaderTable =  new ThreatMatrixRowHeaderTable(newRowHeaderData, this);
		threatTable = new ThreatMatrixTable(model, this);
		return new ScrollPaneWithTableAndRowHeader(rowHeaderTable, threatTable);
	}

	
	public ThreatRatingBundle getSelectedBundle()
	{
		return highlightedBundle;
	}

	
	public void selectBundle(ThreatRatingBundle bundle) throws Exception
	{
		highlightedBundle = bundle;
		repaint();
	}
	
	public Project getProject()
	{
		return getThreatMatrixTable().getProject();
	}
	
	
	public SimpleThreatRatingFramework getThreatRatingFramework() 
	{
		return getProject().getSimpleThreatRatingFramework();
	}
	
	
	public ThreatMatrixView getThreatMatrixView() 
	{
		return view;
	}
	
	
	public ThreatMatrixTable getThreatMatrixTable() 
	{
		return threatTable;
	}
	
	
	public JTable getRowHeaderTable() 
	{
		return rowHeaderTable;
	}
	
	public void establishPriorSortState() throws Exception  
	{
		String currentSortBy = getProject().getViewData(
				getProject().getCurrentView()).getData(
				ViewData.TAG_CURRENT_SORT_BY);
		
		boolean hastPriorSortBy = currentSortBy.length() != 0;
		if(hastPriorSortBy)
		{
			String currentSortDirection = getProject().getViewData(
					getProject().getCurrentView()).getData(
					ViewData.TAG_CURRENT_SORT_DIRECTION);

			boolean sortOrder = currentSortDirection.equals(ViewData.SORT_ASCENDING);

			if(currentSortBy.equals(ViewData.SORT_THREATS))
			{
				rowHeaderTable.sort(sortOrder);
			}
			else
			{
				int columnToSort = threatTable.getSummaryColumn();
				if (!currentSortBy.equals(ViewData.SORT_SUMMARY)) 
				{
					FactorId nodeId = new FactorId(new Integer(currentSortBy).intValue());
					columnToSort= ((ThreatMatrixTableModel)threatTable.getModel()).findTargetIndexById(nodeId);
					if (wasTargetDeleted(columnToSort)) 
						return;
				}
				threatTable.sort(sortOrder, columnToSort);
			}
		}
	}


	private boolean wasTargetDeleted(int columnToSort)
	{
		return columnToSort<0;
	}

	private ThreatMatrixView view;
	private ThreatRatingBundle highlightedBundle;
	private ThreatMatrixTable threatTable;
	private ThreatMatrixRowHeaderTable rowHeaderTable;
	
	public final static int ABOUT_ONE_LINE = 20;
	public final static int ROW_HEIGHT = 2 * ABOUT_ONE_LINE;
	public final static int ABOUT_ONE_INCH = 82;
	public final static int LEFTMOST_COLUMN_WIDTH = 2 * ABOUT_ONE_INCH;
	public final static int DEFAULT_COLUMN_WIDTH = ABOUT_ONE_INCH;
	
}



