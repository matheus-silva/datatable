package com.edupsousa.datatable;

import java.util.LinkedHashMap;

public class HtmlExport implements InterfaceExport {

	@Override
	public String export(DataTable dataTable,
			LinkedHashMap<String, Integer> columnsTypes) {
		DataTableRow row;
		String output = "<table>\n<tr>";
		
		for (String collumName : columnsTypes.keySet()) {
			output += "<td>" + collumName + "</td>";
		}
		
		output += "</tr>\n";
		for (int i = 0; i < dataTable.rowsCount(); i++) {
			output += "<tr>";
			row = dataTable.getRow(i);
			
			for (String collumName : columnsTypes.keySet()) {
				output += "<td>" + row.getValue(collumName) + "</td>";
			}

			output += "</tr>\n";
		}
		output += "</table>\n";
		return output;
	}

}
