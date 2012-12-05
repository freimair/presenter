package presenter.ui.presentation;

import org.eclipse.swt.widgets.Composite;

public abstract class Tool extends Composite {

	public Tool(Composite parent, int style) {
		super(parent, style);
		// TODO Auto-generated constructor stub
	}

	public abstract void update();

}
