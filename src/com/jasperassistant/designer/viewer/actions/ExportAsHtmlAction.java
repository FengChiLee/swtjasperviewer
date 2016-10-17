/*
 * SWTJasperViewer - Free SWT/JFace report viewer for JasperReports.
 * Copyright (C) 2004  Peter Severin (peter_p_s@users.sourceforge.net)
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 */
package com.jasperassistant.designer.viewer.actions;

import java.io.File;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRExportProgressMonitor;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleHtmlReportConfiguration;

import org.eclipse.jface.resource.ImageDescriptor;

import com.jasperassistant.designer.viewer.IPosExportXlsHandler;
import com.jasperassistant.designer.viewer.IReportViewer;

/**
 * HTML export action
 * 
 * @author Peter Severin (peter_p_s@users.sourceforge.net)
 */
public class ExportAsHtmlAction extends AbstractExportAction {

	private static final ImageDescriptor ICON = ImageDescriptor.createFromFile(
			ExportAsHtmlAction.class, "images/save.gif"); //$NON-NLS-1$

	private static final ImageDescriptor DISABLED_ICON = ImageDescriptor
			.createFromFile(ExportAsHtmlAction.class, "images/saved.gif"); //$NON-NLS-1$

	private IPosExportXlsHandler posExportSingleXlsHandler;
	
	/**
	 * @see AbstractExportAction#AbstractExportAction(IReportViewer)
	 */
	public ExportAsHtmlAction(IReportViewer viewer) {
		super(viewer);

		setText(Messages.getString("ExportAsHtmlAction.label")); //$NON-NLS-1$
		setToolTipText(Messages.getString("ExportAsHtmlAction.tooltip")); //$NON-NLS-1$
		setImageDescriptor(ICON);
		setDisabledImageDescriptor(DISABLED_ICON);

		setFileExtensions(new String[] { "*.html", "*.htm" }); //$NON-NLS-1$ //$NON-NLS-2$
		setFilterNames(new String[] { Messages.getString("ExportAsHtmlAction.htmlFilterName"), Messages.getString("ExportAsHtmlAction.htmFilterName") }); //$NON-NLS-1$ //$NON-NLS-2$
		setDefaultFileExtension("html"); //$NON-NLS-1$
	}
	
    public void setIPosExportXlsHandler(IPosExportXlsHandler posExportSingleXlsHandler) {

        this.posExportSingleXlsHandler = posExportSingleXlsHandler;
    }

	/**
	 * @see com.jasperassistant.designer.viewer.actions.AbstractExportAction#exportWithProgress(java.io.File, net.sf.jasperreports.engine.export.JRExportProgressMonitor)
	 */
	protected void exportWithProgress(File file, JRExportProgressMonitor monitor)
			throws JRException {
	    
	    JasperPrint reportFile = null;

        if (posExportSingleXlsHandler != null) {
            reportFile = posExportSingleXlsHandler.getJasperPrint();
        }

        if (reportFile == null) {
            reportFile = getReportViewer().getDocument();
        }
	    
        HtmlExporter exporter = new HtmlExporter();
        
	    SimpleHtmlReportConfiguration configuration = new SimpleHtmlReportConfiguration();
        configuration.setProgressMonitor(monitor);
        configuration.setWhitePageBackground(false);
        configuration.setRemoveEmptySpaceBetweenRows(true);
                
        exporter.setConfiguration(configuration);
        
        exporter.setExporterInput(new SimpleExporterInput(reportFile));
        exporter.setExporterOutput(new SimpleHtmlExporterOutput(file));
                
//		JRHtmlExporter exporter = new JRHtmlExporter();
//		exporter.setParameter(JRExporterParameter.JASPER_PRINT, getReportViewer().getDocument()); 
//		exporter.setParameter(JRExporterParameter.OUTPUT_FILE, file);
//		exporter.setParameter(JRExporterParameter.PROGRESS_MONITOR, monitor);

        exporter.exportReport();
	}
	
}
