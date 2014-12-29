package tailminuseff.ui;

import javax.swing.SwingUtilities;
import javax.swing.table.*;

import tailminuseff.*;

public class FileLineModelTableModelAdaptor extends AbstractTableModel implements TableModel {

	private static final long serialVersionUID = 5464195641404583553L;

	private final FileLineModel lineModel;

	public FileLineModelTableModelAdaptor(FileLineModel lineModel) {
		super();
		this.lineModel = lineModel;
		this.lineModel.addListener(listener);
	}

	private final FileLineModelListener listener = new FileLineModelListener() {
		@Override
		public void lineAdded(FileLineModelLineAddedEvent evt) {
			SwingUtilities.invokeLater(() -> fireTableDataChanged());
		}

		@Override
		public void reset(FileLineModelResetEvent evt) {
			SwingUtilities.invokeLater(() -> fireTableDataChanged());
		}
	};

	@Override
	public int getRowCount() {
		return this.lineModel.getLines().size();
	}

	@Override
	public int getColumnCount() {
		return 1;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return this.lineModel.getLines().get(rowIndex);
	}
}