import net.imagej.table.GenericTable;

abstract class PlotDialogBase {
	private String title;
	private Boolean was_oked = false;
	protected GenericTable table;
	protected TableColumnSelectorDialog dialog = null;

	PlotDialogBase(final String title, final GenericTable table) {
		this.title = title;
		this.table = table;
	}

	void showDialog() {
		create();
		addControls();
		show();
		readControls();
	}

	protected abstract void readControls();

	protected abstract void addControls();

	private void create() {
		dialog = new TableColumnSelectorDialog(title);
	}

	private void show() {
		dialog.showDialog();
		was_oked = dialog.wasOKed();
	}

	public Boolean wasOked() { return was_oked; }
}
