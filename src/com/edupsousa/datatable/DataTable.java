package com.edupsousa.datatable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Objects;

public class DataTable {

	public static final int TYPE_INT = 0;
	public static final int TYPE_STRING = 1;

	public static final int FORMAT_CSV = 0;
	public static final int FORMAT_HTML = 1;

	private LinkedHashMap<String, Integer> columnsTypes = new LinkedHashMap<String, Integer>();
	private ArrayList<DataTableRow> rows = new ArrayList<DataTableRow>();

	public int columnsCount() {
		return columnsTypes.size();
	}

	public int rowsCount() {
		return rows.size();
	}

	public void addCollumn(String name, int type) {
		columnsTypes.put(name, type);
	}

	public boolean hasCollumn(String name) {
		return columnsTypes.containsKey(name);
	}

	public DataTableRow createRow() {
		return new DataTableRow(this);
	}

	public void insertRow(DataTableRow row) {
		checkRowCompatibilityAndThrows(row);
		rows.add(row);
	}

	public DataTableRow lastRow() {
		return rows.get(rows.size() - 1);
	}

	public int getCollumnType(String collumn) {
		return columnsTypes.get(collumn);
	}

	private void checkRowCompatibilityAndThrows(DataTableRow row) {
		for (String collumnName : columnsTypes.keySet()) {
			if (row.hasValueFor(collumnName)
					&& !(isValueCompatible(columnsTypes.get(collumnName),
							row.getValue(collumnName)))) {
				throw new ClassCastException("Wrong type for collumn "
						+ collumnName + ".");
			}
		}
	}

	private boolean isValueCompatible(int type, Object value) {
		if (type == this.TYPE_INT && !(value instanceof Integer)) {
			return false;
		} else if (type == this.TYPE_STRING && !(value instanceof String)) {
			return false;
		}
		return true;
	}

	public DataTableRow getRow(int i) {
		return rows.get(i);
	}

	public String export(int format) {
		DataTableRow row;
		String output = "";
		if (format == DataTable.FORMAT_CSV) {
			for (String collumnName : columnsTypes.keySet()) {
				output += collumnName + ";";
			}
			output += "\n";
			for (int i = 0; i < this.rowsCount(); i++) {
				row = this.getRow(i);
				for (String collumnName : columnsTypes.keySet()) {
					if (columnsTypes.get(collumnName) == DataTable.TYPE_STRING) {
						output += "\"" + row.getValue(collumnName) + "\";";
					} else {
						output += row.getValue(collumnName) + ";";
					}
				}
				output += "\n";
			}
		} else if (format == DataTable.FORMAT_HTML) {
			output += "<table>\n<tr>";
			for (String collumName : columnsTypes.keySet()) {
				output += "<td>" + collumName + "</td>";
			}
			output += "</tr>\n";
			for (int i = 0; i < this.rowsCount(); i++) {
				output += "<tr>";
				row = this.getRow(i);
				for (String collumName : columnsTypes.keySet()) {
					output += "<td>" + row.getValue(collumName) + "</td>";
				}
				output += "</tr>\n";
			}
			output += "</table>\n";
		}
		return output;
	}

	public void insertRowAt(DataTableRow row, int index) {
		rows.add(index, row);
	}

	public DataTable filterEqual(String collumn, Object value) {
		DataTable output = new DataTable();

		for (String collumName : columnsTypes.keySet()) {
			output.addCollumn(collumName, columnsTypes.get(collumName));
		}

		for (int i = 0; i < this.rowsCount(); i++) {
			DataTableRow rowTemp = this.getRow(i);

			if (Objects.equals(rowTemp.getValue(collumn), value)) {
				DataTableRow row = output.createRow();

				for (String collumName : columnsTypes.keySet()) {
					row.setValue(collumName, rowTemp.getValue(collumName));
				}

				output.insertRow(row);
			}
		}
		return output;
	}
	
	public DataTable filterNotEqual(String collumn, Object value) {
		DataTable output = new DataTable();

		for (String collumName : columnsTypes.keySet()) {
			output.addCollumn(collumName, columnsTypes.get(collumName));
		}

		for (int i = 0; i < this.rowsCount(); i++) {
			DataTableRow rowTemp = this.getRow(i);

			if (!Objects.equals(rowTemp.getValue(collumn), value)) {
				DataTableRow row = output.createRow();

				for (String collumName : columnsTypes.keySet()) {
					row.setValue(collumName, rowTemp.getValue(collumName));
				}

				output.insertRow(row);
			}
		}
		return output;
	}

	public DataTable sortAscending(String collumn) {
		if (columnsTypes.get(collumn) == TYPE_STRING) {
			throw new ClassCastException("Only Integer columns can be sorted.");
		}

		DataTable output = new DataTable();

		for (String collumName : columnsTypes.keySet()) {
			output.addCollumn(collumName, columnsTypes.get(collumName));
		}

		for(int i = 0; i < this.rowsCount(); i++){
			DataTableRow rowTemp= this.getRow(i);
			DataTableRow row;
			int j = 0;
			for(j = 0; j < output.rowsCount(); j++){
				row = output.getRow(j);
				if((Integer) (rowTemp.getValue(collumn)) < (Integer) row.getValue(collumn)){
					break;
				}
			}
			row = output.createRow();
			
			for (String collumName : columnsTypes.keySet()) {
				row.setValue(collumName, rowTemp.getValue(collumName));
			}
			
			output.insertRowAt(row, j);
		}
		
		return output;
	}
}
