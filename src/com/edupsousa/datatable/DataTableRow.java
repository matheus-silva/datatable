package com.edupsousa.datatable;

import java.util.LinkedHashMap;

public class DataTableRow {

	private DataTable table;
	private LinkedHashMap<String, Object> values = new LinkedHashMap<String, Object>();

	public DataTableRow(DataTable dataTable) {
		table = dataTable;
	}

	public void setValue(String collumn, Object value) {
		values.put(collumn, value);
	}

	public Object getValue(String collumn) {
		return values.get(collumn);
	}

	public boolean hasValueFor(String collumnName) {
		return values.containsKey(collumnName);
	}

}
