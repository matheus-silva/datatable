package com.edupsousa.datatable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Objects;

public class DataTable {

	public static final int TYPE_INT = 0;
	public static final int TYPE_STRING = 1;
	public static final int FORMAT_CSV = 0;
	public static final int FORMAT_HTML = 1;
	private final boolean ASCENDING = false;
	private final boolean DESCENDING = true;
	private final boolean EQUAL = true;
	private final boolean NOT_EQUAL = false;

	private LinkedHashMap<String, Integer> columnsTypes = new LinkedHashMap<String, Integer>();
	private ArrayList<DataTableRow> rows = new ArrayList<DataTableRow>();
	private InterfaceExport export;

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
		if (format == DataTable.FORMAT_CSV) {
			export = new CsvExport();

		} else if (format == DataTable.FORMAT_HTML) {
			export = new HtmlExport();
		}

		return export.export(this, columnsTypes);
	}

	public void insertRowAt(DataTableRow row, int index) {
		rows.add(index, row);
	}

	public DataTable filterEqual(String collumn, Object value) {
		return equal(collumn, value, true);
	}

	public DataTable filterNotEqual(String collumn, Object value) {
		return equal(collumn, value, false);
	}

	private DataTable equal(String collumn, Object value, boolean equal) {
		DataTable output = new DataTable();

		this.copyColumnsType(output);

		for (int i = 0; i < this.rowsCount(); i++) {
			DataTableRow rowTemp = this.getRow(i);

			if (equal == objectComparator(rowTemp, collumn, value)) {
				DataTableRow row = output.createRow();

				this.copyDataTableRow(row, rowTemp);

				output.insertRow(row);
			}
		}
		return output;
	}

	private boolean objectComparator(DataTableRow rowTemp, String collumn,
			Object value) {
		return Objects.equals(rowTemp.getValue(collumn), value);
	}

	public DataTable sortAscending(String collumn) {
		return sort(collumn, ASCENDING);
	}

	public DataTable sortDescending(String collumn) {
		return sort(collumn, DESCENDING);
	}

	private DataTable sort(String collumn, boolean equal) {
		if (columnsTypes.get(collumn) == TYPE_STRING) {
			throw new ClassCastException("Only Integer columns can be sorted.");
		}
		DataTable output = new DataTable();

		this.copyColumnsType(output);

		for (int i = 0; i < this.rowsCount(); i++) {
			DataTableRow rowTemp = this.getRow(i);
			DataTableRow row;
			int j = 0;
			for (j = 0; j < output.rowsCount(); j++) {
				row = output.getRow(j);
				if (equal == biggerThan(collumn, rowTemp, row)) {
					break;
				}
			}
			row = output.createRow();

			this.copyDataTableRow(row, rowTemp);

			output.insertRowAt(row, j);
		}

		return output;
	}

	private boolean biggerThan(String collumn, DataTableRow rowTemp,
			DataTableRow row) {
		return (Integer) (rowTemp.getValue(collumn)) >= (Integer) row
				.getValue(collumn);
	}

	private void copyColumnsType(DataTable output) {
		for (String collumName : columnsTypes.keySet()) {
			output.addCollumn(collumName, columnsTypes.get(collumName));
		}
	}

	private void copyDataTableRow(DataTableRow row, DataTableRow rowTemp) {
		for (String collumName : columnsTypes.keySet()) {
			row.setValue(collumName, rowTemp.getValue(collumName));
		}
	}
}
