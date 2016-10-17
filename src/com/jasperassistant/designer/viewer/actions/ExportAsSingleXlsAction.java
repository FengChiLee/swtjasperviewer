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
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;

import org.eclipse.jface.resource.ImageDescriptor;

import com.jasperassistant.designer.viewer.IPosExportXlsHandler;
import com.jasperassistant.designer.viewer.IReportViewer;

/**
 * Single sheet excel export action
 * 
 * @author Peter Severin (peter_p_s@users.sourceforge.net)
 */
public class ExportAsSingleXlsAction extends AbstractExportAction {

	private static final ImageDescriptor ICON = ImageDescriptor.createFromFile(
			ExportAsSingleXlsAction.class, "images/save.gif"); //$NON-NLS-1$

	private static final ImageDescriptor DISABLED_ICON = ImageDescriptor
			.createFromFile(ExportAsSingleXlsAction.class, "images/saved.gif"); //$NON-NLS-1$

	private IPosExportXlsHandler posExportSingleXlsHandler;
	
	/**
	 * @see AbstractExportAction#AbstractExportAction(IReportViewer)
	 */
	public ExportAsSingleXlsAction(IReportViewer viewer) {
		super(viewer);

		setText(Messages.getString("ExportAsSingleXlsAction.label")); //$NON-NLS-1$
		setToolTipText(Messages.getString("ExportAsSingleXlsAction.tooltip")); //$NON-NLS-1$
		setImageDescriptor(ICON);
		setDisabledImageDescriptor(DISABLED_ICON);

		setFileExtensions(new String[] { "*.xls" }); //$NON-NLS-1$
		setFilterNames(new String[] { Messages.getString("ExportAsSingleXlsAction.filterName") }); //$NON-NLS-1$
		setDefaultFileExtension("xls"); //$NON-NLS-1$
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
				
		if(posExportSingleXlsHandler != null) {
			reportFile = posExportSingleXlsHandler.getJasperPrint();
		}
		
		if(reportFile == null) {
			reportFile = getReportViewer().getDocument();
		}
		
		JRXlsExporter exporter = new JRXlsExporter();
				
		SimpleXlsReportConfiguration strc = new SimpleXlsReportConfiguration();
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
		
//		exporter.setParameter(JRExporterParameter.JASPER_PRINT, reportFile);
//		exporter.setParameter(JRExporterParameter.OUTPUT_FILE, file);
//		exporter.setParameter(JRExporterParameter.PROGRESS_MONITOR, monitor);
//		
////		exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
//		exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
//		// Configura el exporter 
//        exporter.setParameter(JRXlsExporterParameter.IGNORE_PAGE_MARGINS, Boolean.TRUE); 
//        exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE); 
//        exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_GRAPHICS, Boolean.TRUE); 
//        exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
//        exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
//        exporter.setParameter(JRXlsExporterParameter.IS_FONT_SIZE_FIX_ENABLED, Boolean.TRUE);
// 		Compatibility.setOnePagePerSheetParameter(exporter, Boolean.FALSE);
		
		exporter.exportReport();
		
		// another(file, monitor);
	}
	
//	private void another(File file, JRExportProgressMonitor monitor) throws JRException {
//		// Crea Excel Exporter
//        //JRExporter exporter = new JRXlsExporter();  // Este es de POI, no funciona bien
//        JRExporter exporter = new JExcelApiExporter();
//
//        // Configura el exporter
//        //exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, outFileName);
//        exporter.setParameter(JRExporterParameter.OUTPUT_FILE, file);
//        exporter.setParameter(JRExporterParameter.JASPER_PRINT, getReportViewer().getDocument());
//        exporter.setParameter(JRExporterParameter.PROGRESS_MONITOR, monitor);
//        
//        exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE); 
//        exporter.setParameter(JRXlsExporterParameter.IGNORE_PAGE_MARGINS, Boolean.TRUE); 
//        exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE); 
//        exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_GRAPHICS, Boolean.TRUE); 
//        exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
//        exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
//
//        // Exporta a Excel
//        exporter.exportReport();
//	}
}
