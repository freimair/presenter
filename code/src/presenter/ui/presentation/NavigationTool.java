package presenter.ui.presentation;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import presenter.model.Presentation;

public class NavigationTool extends Tool {

	public NavigationTool(Composite parent, int style,
 PresenterControl control) {
		super(parent, style, control);
		this.setLayout(new RowLayout(SWT.HORIZONTAL));
		Button buttonprev = new Button(this, SWT.PUSH);
		buttonprev.setText("prev");
		buttonprev.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				Presentation.previous();
				getControl().update();
			}
		});

		Button buttonnext = new Button(this, SWT.PUSH);
		buttonnext.setText("next");
		buttonnext.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				Presentation.next();
				getControl().update();
			}
		});
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

}
