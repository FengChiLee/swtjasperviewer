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
import net.sf.jasperreports.engine.export.JRExportProgressMonitor;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

import org.eclipse.jface.resource.ImageDescriptor;

import com.jasperassistant.designer.viewer.IPosExportXlsHandler;
import com.jasperassistant.designer.viewer.IReportViewer;

/**
 * Single sheet excel export action
 * 
 * 
 */
public class ExportAsSingleXlsxAction extends AbstractExportAction {

	private static final ImageDescriptor ICON = ImageDescriptor.createFromFile(
			ExportAsSingleXlsAction.class, "images/save.gif"); //$NON-NLS-1$

	private static final ImageDescriptor DISABLED_ICON = ImageDescriptor
			.createFromFile(ExportAsSingleXlsAction.class, "images/saved.gif"); //$NON-NLS-1$

	private IPosExportXlsHandler posExportXlsHandler;
	
	/**
	 * @see AbstractExportAction#AbstractExportAction(IReportViewer)
	 */
	public ExportAsSingleXlsxAction(IReportViewer viewer) {
		super(viewer);

		setText(Messages.getString("ExportAsSingleXlsxAction.label")); //$NON-NLS-1$
		setToolTipText(Messages.getString("ExportAsSingleXlsxAction.tooltip")); //$NON-NLS-1$
		setImageDescriptor(ICON);
		setDisabledImageDescriptor(DISABLED_ICON);

		setFileExtensions(new String[] { "*.xlsx" }); //$NON-NLS-1$
		setFilterNames(new String[] { Messages.getString("ExportAsSingleXlsxAction.filterName") }); //$NON-NLS-1$
		setDefaultFileExtension("xls"); //$NON-NLS-1$
	}
	
	public void setIPosExportXlsHandler(IPosExportXlsHandler posExportXlsHandler) {
		
		this.posExportXlsHandler = posExportXlsHandler;
	}
	
	/**
	 * @see com.jasperassistant.designer.viewer.actions.AbstractExportAction#exportWithProgress(java.io.File, net.sf.jasperreports.engine.export.JRExportProgressMonitor)
	 */
	protected void exportWithProgress(File file, JRExportProgressMonitor monitor)
			throws JRException {
		
		JasperPrint reportFile = null;
				
		if(posExportXlsHandler != null) {
			reportFile = posExportXlsHandler.getJasperPrint();
		}
		
		if(reportFile == null) {
			reportFile = getReportViewer().getDocument();
		}
		
		JRXlsxExporter exporter = new JRXlsxExporter();
		
		SimpleXlsxReportConfiguration strc = new SimpleXlsxReportConfiguration();		
		strc.setProgressMonitor(monitor);
		strc.setDetectCellType(true);
		strc.setIgnorePageMargins(true);
		strc.setOnePagePerSheet(false);
		strc.setWhitePageBackground(false);
		strc.setIgnoreGraphics(true);
		strc.setRemoveEmptySpaceBetweenColumns(true);
		strc.setRemoveEmptySpaceBetweenRows(true);
		strc.setFontSizeFixEnabled(true);
				
		exporter.setConfiguration(strc);
		
		exporter.setExporterInput(new SimpleExporterInput(reportFile));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(file));
		
		exporter.exportReport();
		
	}
	
}
