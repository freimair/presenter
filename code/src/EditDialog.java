import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;


public class EditDialog extends ApplicationWindow {

	public EditDialog(Shell parentShell) {
		super(null);
	}

	@Override
	protected Control createContents(Composite parent) {
		Composite container = (Composite) super.createContents(parent);
		container.setLayout(new RowLayout(SWT.VERTICAL));

		for (Slide current : Presentation.getSlides())
			new SlideItem(container, SWT.None, current);

		return container;
	}

}
