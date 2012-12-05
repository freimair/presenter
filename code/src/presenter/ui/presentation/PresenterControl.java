package presenter.ui.presentation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import presenter.model.Presentation;


public class PresenterControl extends ApplicationWindow {
	
	int current = 0;
	private PresenterWindow window;
	private List<Tool> tools = new ArrayList<Tool>();

	
	public PresenterControl() {
		super(null);
	    setShellStyle(SWT.RESIZE | SWT.MIN | SWT.MAX);
		this.setBlockOnOpen(true);
	}

	public void toggleFullscreen() {
		getShell().setFullScreen(!getShell().getFullScreen());
	}
	
	public void update() {
		window.setImage((Image) Presentation.getCurrent().getContent());

		for (Tool current : tools)
			current.update();
	}
	
	protected Control createContents(Composite parent) {
	    getShell().setText("Presenter");
		getShell().setSize(1000, 700);

		window = new PresenterWindow(this);
		window.open();
	    
		Composite container = (Composite) super.createContents(parent);
		container.setLayout(new GridLayout(2, false));

		tools.add(new NotesTool(container, SWT.BORDER, this));
		tools.add(new SlidesTool(container, SWT.BORDER, this));
	    
		Composite controlsComposite = new Composite(container, SWT.NONE);
		controlsComposite.setLayout(new RowLayout(SWT.VERTICAL));


		tools.add(new NavigationTool(controlsComposite, SWT.NONE, this));
		tools.add(new TimerTool(controlsComposite, SWT.BORDER, this));
		tools.add(new ProgressTool(controlsComposite, SWT.BORDER, this));



	    getShell().addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent e) {
				window.close();
			}
		});

		parent.layout();
		update();

		return container;
	}
	Font font = null;
	
}
