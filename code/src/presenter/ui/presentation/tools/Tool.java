package presenter.ui.presentation.tools;

import org.eclipse.swt.widgets.Composite;

import presenter.ui.presentation.PresenterControl;

public abstract class Tool extends Composite {

	protected PresenterControl myControl;

	public Tool(Composite parent, int style, PresenterControl control) {
		super(parent, style);
		myControl = control;
	}

	public PresenterControl getControl() {
		return myControl;
	}

	public abstract void update();

}
