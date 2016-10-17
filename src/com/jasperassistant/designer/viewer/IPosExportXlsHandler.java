package com.jasperassistant.designer.viewer;

import net.sf.jasperreports.engine.JasperPrint;

public interface IPosExportXlsHandler {

	JasperPrint getJasperPrint();

	void removeParameter(String paraName);
	
}
