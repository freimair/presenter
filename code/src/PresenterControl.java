import java.io.File;
import java.io.IOException;
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
import org.eclipse.swt.events.SelectionListener;
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
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;


public class PresenterControl extends ApplicationWindow {
	
	private static final String TYPE_IMAGE = "type";
	int current = 0;
	private PresenterWindow window;
	private Label timerLabel;
	private Element root;
	private String dir;
	private Canvas thumbnailCanvas;
//    private Image thumb;
	private Canvas notesCanvas;
//    private Image note;
    private ScaledImage thumbnail, notes;

	
    public PresenterControl(PresenterWindow window) {
		super(null);
	    setShellStyle(SWT.RESIZE | SWT.MIN | SWT.MAX);
		this.setBlockOnOpen(true);	    
	    this.window = window;
	}

	public void toggleFullscreen() {
		getShell().setFullScreen(!getShell().getFullScreen());
	}
	
	public void open(String file) {
		dir = (new File(file)).getParent() + "/";
	    SAXBuilder builder = new SAXBuilder();
	    // Create the document
	    try {
	      root = builder.build(file).getRootElement();
	      
	    } catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	    } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    refreshImage();
	}
	
	public void refreshImage() {
		Element element = (Element) root.getChildren().get(current);
		
		window.setImage(new Image(Display.getCurrent(), dir + element.getChildText("content")));
		this.setImage(new Image(Display.getCurrent(), dir + element.getChildText("content")));
		if(null == dir + element.getChildText("notes"))
			this.setNotes(new Image(Display.getCurrent(), dir + element.getChildText("content")));
		else
			this.setNotes(new Image(Display.getCurrent(), dir + element.getChildText("notes")));
		
			
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
	    getShell().setSize(1440, 860);
	    
		Composite container = (Composite) super.createContents(parent);
		container.setLayout(new GridLayout(2, false));

		Composite slidenotes = new Composite(container, SWT.BORDER);
		slidenotes.setLayout(new FillLayout());
		slidenotes.setLayoutData(new GridData(GridData.FILL_BOTH));
	    notesCanvas = new Canvas(slidenotes, SWT.NONE);
		// notesCanvas.setBounds(0, 0, 1200, 810);
	    notesCanvas.addPaintListener(new PaintListener() {

			public void paintControl(PaintEvent e) {
		        if(null != notes) {		        	
		        	e.gc.drawImage(notes.getImage(), notes.centerX(notesCanvas.getBounds().width), notes.centerY(notesCanvas.getBounds().height));
		        }
		      }
		    });
	    
		Composite controlsComposite = new Composite(container, SWT.NONE);
		controlsComposite.setLayout(new RowLayout(SWT.VERTICAL));

		Button open = new Button(controlsComposite, SWT.PUSH);
	    open.setText("open presentation");
	    open.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
		        FileDialog fd = new FileDialog(getShell(), SWT.OPEN);
		        fd.setText("Open");
		        fd.setFilterPath("C:/");
		        String[] filterExt = { "*.xml" };
		        fd.setFilterExtensions(filterExt);
		        open(fd.open());
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	    
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
	    buttonprev.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				current--;
				current %= root.getChildren().size();
				refreshImage();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	    
	    Button buttonnext = new Button(slidecontrol, SWT.PUSH);
	    buttonnext.setText("next");
	    buttonnext.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				current++;
				current %= root.getChildren().size();
				refreshImage();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
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
	    startstopTimer.addSelectionListener(new SelectionListener() {
			
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
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	    
	    Button resetTimer = new Button(timerComposite, SWT.PUSH);
	    resetTimer.setText("reset");
	    resetTimer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	    resetTimer.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				timer.stop();
				timer.reset();
				startstopTimer.setText("start");
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
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
