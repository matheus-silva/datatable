package com.edupsousa.datatable;

import java.util.LinkedHashMap;

public class CsvExport implements InterfaceExport {

	@Override
	public String export(DataTable dataTable,
			LinkedHashMap<String, Integer> columnsTypes) {
		DataTableRow row;
		String output = "";

		for (String collumnName : columnsTypes.keySet()) {
			output += collumnName + ";";
		}

		output += "\n";
		for (int i = 0; i < dataTable.rowsCount(); i++) {
			row = dataTable.getRow(i);

			for (String collumnName : columnsTypes.keySet()) {
				if (columnsTypes.get(collumnName) == DataTable.TYPE_STRING) {
					output += "\"" + row.getValue(collumnName) + "\";";
				} else {
					output += row.getValue(collumnName) + ";";
				}
			}

			output += "\n";
		}
		return output;
	}

}
