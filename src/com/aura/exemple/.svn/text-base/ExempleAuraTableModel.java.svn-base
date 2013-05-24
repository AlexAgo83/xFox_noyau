package com.aura.exemple;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public abstract class ExempleAuraTableModel<E> extends AbstractTableModel {
	
	private E[] data;
	public E[] getData() {
		if (data == null)
			data = getAll();
		return data;
	}
	public abstract E[] getAll();
		
	private boolean inverseRow = false;
	public ExempleAuraTableModel(boolean inverseRow) {
		super();
		this.inverseRow = inverseRow;
	}
		
	@Override
	public int getColumnCount() {
		return 1;
	}
	
	@Override
	public int getRowCount() {
		return getData().length;
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return implGetData(getData()[inverseRow ? (getRowCount()-1) - rowIndex : rowIndex]);
	}
	public abstract Object implGetData(E e);
	
	@Override
	public void fireTableDataChanged() {
		data = null;
		super.fireTableDataChanged();
	}
}