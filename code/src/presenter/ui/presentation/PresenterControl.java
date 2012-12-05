package presenter.ui.presentation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import presenter.model.Presentation;


public class PresenterControl extends ApplicationWindow {
	
	int current = 0;
	private PresenterWindow window;
	private Canvas thumbnailCanvas;
	private Canvas notesCanvas;
    private ScaledImage thumbnail, notes;
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
		try {
			this.setNotes((Image) Presentation.getCurrent().getNotes()
					.getContent());
			this.setImage((Image) Presentation.getCurrent().getContent());

		} catch (NullPointerException e) {
			this.setImage((Image) Presentation.getCurrent().getContent());
			this.setNotes((Image) Presentation.getCurrent().getContent());
		}

		for (Tool current : tools)
			current.update();
	}
	
	public void setImage(Image thumbnail) {
		this.thumbnail = new ScaledImage(thumbnail, thumbnailCanvas.getBounds().width, thumbnailCanvas.getBounds().height);
		this.thumbnailCanvas.redraw();
		this.thumbnailCanvas.update();
	}
	
	public void setNotes(Image notes) {
		this.notes = new ScaledImage(notes, notesCanvas.getBounds().width, notesCanvas.getBounds().height);
		this.notesCanvas.redraw();
		this.notesCanvas.update();
	}
	
	protected Control createContents(Composite parent) {
	    getShell().setText("Presenter");
		getShell().setSize(1000, 700);

		window = new PresenterWindow(this);
		window.open();
	    
		Composite container = (Composite) super.createContents(parent);
		container.setLayout(new GridLayout(2, false));

		Composite slidenotes = new Composite(container, SWT.BORDER);
		slidenotes.setLayout(new FillLayout());
		slidenotes.setLayoutData(new GridData(GridData.FILL_BOTH));
	    notesCanvas = new Canvas(slidenotes, SWT.NONE);
	    notesCanvas.addPaintListener(new PaintListener() {

			public void paintControl(PaintEvent e) {
		        if(null != notes) {		        	
		        	e.gc.drawImage(notes.getImage(), notes.centerX(notesCanvas.getBounds().width), notes.centerY(notesCanvas.getBounds().height));
		        }
		      }
		    });
	    
		Composite controlsComposite = new Composite(container, SWT.NONE);
		controlsComposite.setLayout(new RowLayout(SWT.VERTICAL));

		Composite slidethumbnail = new Composite(controlsComposite, SWT.BORDER);
	    thumbnailCanvas = new Canvas(slidethumbnail, SWT.NONE);
	    thumbnailCanvas.setBounds(0, 0, 200, 200);
	    thumbnailCanvas.addPaintListener(new PaintListener() {

			public void paintControl(PaintEvent e) {
		        if(null != thumbnail) {
		        	e.gc.drawImage(thumbnail.getImage(), thumbnail.centerX(thumbnailCanvas.getBounds().width), thumbnail.centerY(thumbnailCanvas.getBounds().height));
		        }
		      }
		    });

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
