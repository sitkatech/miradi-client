/*
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

package org.miradi.dialogs.fieldComponents;

import org.miradi.icons.WarningIcon;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.utils.MiradiScrollPane;
import org.miradi.views.MiradiFeatureInterface;
import org.miradi.views.MiradiTabContentsPanelInterface;
import org.miradi.views.TabbedView;
import org.miradi.views.umbrella.UmbrellaView;

import java.awt.*;

public class RemovedFeatureLabel extends ClickablePanelTitleLabel
{
    public RemovedFeatureLabel()
    {
        super(EAM.text("This feature is scheduled to be removed in the next release (click for details)."), new WarningIcon(), "FeaturesToBeRemovedInNextRelease.html");
    }

    public void updateVisibility()
    {
        setVisible(isAvailable());
    }

    private boolean isAvailable()
    {
        MainWindow mainWindow = getMainWindow();
        if (mainWindow == null)
            return false;

        UmbrellaView currentView = mainWindow.getCurrentView();
        if (currentView == null)
            return false;

        MiradiTabContentsPanelInterface currentTabPanel = currentView.getCurrentTabPanel();
        if (currentTabPanel != null)
            return currentTabPanel.isFeatureToBeRemoved();

        if (currentView instanceof TabbedView)
        {
            TabbedView tabbedView = (TabbedView) currentView;
            Component tabContents = tabbedView.getCurrentTabContents();
            if (tabContents instanceof MiradiFeatureInterface)
            {
                MiradiFeatureInterface featureInterface = (MiradiFeatureInterface) tabContents;
                return featureInterface.isFeatureToBeRemoved();
            }

            if (tabContents instanceof MiradiScrollPane)
            {
                MiradiScrollPane scrollPane = (MiradiScrollPane) tabContents;
                Component viewComponent = scrollPane.getViewComponent();
                if (viewComponent instanceof MiradiFeatureInterface)
                {
                    MiradiFeatureInterface featureInterface = (MiradiFeatureInterface) viewComponent;
                    return featureInterface.isFeatureToBeRemoved();
                }
            }
        }

        return false;
    }
}

