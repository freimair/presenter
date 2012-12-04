package presenter.ui;

import java.util.regex.Pattern;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import presenter.model.Presentation;


public class PresenterControl extends ApplicationWindow {
	
	int current = 0;
	private PresenterWindow window;
	private Label timerLabel;
	private Canvas thumbnailCanvas;
	private Canvas notesCanvas;
    private ScaledImage thumbnail, notes;

	
	public PresenterControl() {
		super(null);
	    setShellStyle(SWT.RESIZE | SWT.MIN | SWT.MAX);
		this.setBlockOnOpen(true);
	}

	public void toggleFullscreen() {
		getShell().setFullScreen(!getShell().getFullScreen());
	}
	
	public void refreshImage() {
		window.setImage((Image) Presentation.getCurrent().getContent());
		try {
			this.setNotes((Image) Presentation.getCurrent().getNotes()
					.getContent());
			this.setImage((Image) Presentation.getCurrent().getContent());

		} catch (NullPointerException e) {
			this.setImage((Image) Presentation.getCurrent().getContent());
			this.setNotes((Image) Presentation.getCurrent().getContent());
		}
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
	    
		Composite slidecontrol = new Composite(controlsComposite, SWT.BORDER);
	    slidecontrol.setLayout(new RowLayout(SWT.HORIZONTAL));
	    Button buttonprev = new Button(slidecontrol, SWT.PUSH);
	    buttonprev.setText("prev");
	    buttonprev.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				Presentation.previous();
				refreshImage();
			}
		});
	    
	    Button buttonnext = new Button(slidecontrol, SWT.PUSH);
	    buttonnext.setText("next");
	    buttonnext.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				Presentation.next();
				refreshImage();
			}
		});
	    
		Composite timerComposite = new Composite(controlsComposite, SWT.BORDER);
	    timerComposite.setLayout(new GridLayout(3, true));
	    
	    timerLabel = new Label(timerComposite, SWT.NONE);
	    FontData newFontData = new FontData(timerLabel.getFont().getFontData()[0].toString());
	    newFontData.height = 30;
	    timerLabel.setFont(new Font(Display.getCurrent(), newFontData));
	    timerLabel.setText(" 00:00:00 ");
	    timerLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
	    
	    final Button startstopTimer = new Button(timerComposite, SWT.PUSH);
	    startstopTimer.setText("start");
	    startstopTimer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		final Timer timer = new Timer();
	    startstopTimer.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(startstopTimer.getText().equals("start")) {
					timer.start();
					startstopTimer.setText("pause");
				}
				else {
					timer.stop();
					startstopTimer.setText("start");
				}
			}
		});
	    
	    Button resetTimer = new Button(timerComposite, SWT.PUSH);
	    resetTimer.setText("reset");
	    resetTimer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	    resetTimer.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				timer.stop();
				timer.reset();
				startstopTimer.setText("start");
			}
		});
	    
	    Button presetTimer = new Button(timerComposite, SWT.PUSH);
	    presetTimer.setText("preset");
	    presetTimer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	    presetTimer.addSelectionListener(new SelectionAdapter() {
	        public void widgetSelected(SelectionEvent event) {
	          InputDialog dlg = new InputDialog(Display.getCurrent().getActiveShell(),
	              "Preset Stopwatch", "hours:minutes:seconds/minutes:seconds/seconds", "", new TimeValidator());
	          if (dlg.open() == Window.OK) {
	        	String[] tmp = dlg.getValue().split(":");
	        	
	        	int seconds = 0;
				for(int i = tmp.length - 1; i >= 0; i--)
	        		seconds += Integer.parseInt(tmp[i]) * Math.pow(60, tmp.length - 1 - i); 

	            timer.preset(seconds);
	          }
	        }
	      });

		parent.layout();
		refreshImage();

		return container;
	}
	Font font = null;
	
	private class TimeValidator implements IInputValidator {

		@Override
		public String isValid(String newText) {
			return Pattern.matches("\\d*:?\\d*:?\\d+", newText) ? null : "not a valid time";
		}
		
	}
	
    private class Timer implements Runnable {
		private int defaultseconds = 1;
    	private int seconds = defaultseconds;
		private boolean active = false;
		
		@Override
		public void run() {
			seconds--;
			int hours = seconds / 3600,
			remainder = seconds % 3600,
			minutes = remainder / 60,
			seconds = remainder % 60;

			timerLabel.setText((0 > seconds ? "-":" ") + ((Math.abs(hours) < 10 ? "0" : "") + Math.abs(hours)
			+ ":" + (Math.abs(minutes) < 10 ? "0" : "") + Math.abs(minutes)
			+ ":" + (Math.abs(seconds) < 10 ? "0" : "") + Math.abs(seconds)));
			
			if(active)
				Display.getCurrent().timerExec(1000, this);
		}
		
		public void preset(int seconds) {
			defaultseconds = seconds+1;
			reset();
		}
		
		public void reset() {
			seconds = defaultseconds;
			run();
		}
		
		public void stop() {
			active = false;
		}
		
		public void start() {
			active = true;
			run();
		}
	};
}
