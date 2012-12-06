package presenter.ui.presentation.tools;

import java.util.regex.Pattern;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import presenter.model.Presentation;
import presenter.model.Time;
import presenter.ui.presentation.PresenterControl;

public class TimerTool extends Tool {
	private Label overallLabel;
	private Time nextCheckpoint;
	private Label nextLabel;

	public TimerTool(Composite parent, int style, PresenterControl control) {
		super(parent, style, control);
		this.setLayout(new GridLayout(3, true));

		nextLabel = new Label(this, SWT.NONE);
		FontData newFontData = new FontData(
				nextLabel.getFont().getFontData()[0].toString());
		newFontData.height = 30;
		nextLabel.setFont(new Font(Display.getCurrent(), newFontData));
		nextLabel.setText("next checkpoint");
		nextLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3,
				1));

		overallLabel = new Label(this, SWT.NONE);
		overallLabel.setFont(new Font(Display.getCurrent(), newFontData));
		overallLabel.setText(" 00:00:00 ");
		overallLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				3, 1));

		final Button startstopTimer = new Button(this, SWT.PUSH);
		startstopTimer.setText("start");
		startstopTimer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true));
		final Timer timer = new Timer();
		startstopTimer.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (startstopTimer.getText().equals("start")) {
					timer.start();
					startstopTimer.setText("pause");
				} else {
					timer.stop();
					startstopTimer.setText("start");
				}
			}
		});

		Button resetTimer = new Button(this, SWT.PUSH);
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

		Button presetTimer = new Button(this, SWT.PUSH);
		presetTimer.setText("preset");
		presetTimer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		presetTimer.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				InputDialog dlg = new InputDialog(Display.getCurrent()
						.getActiveShell(), "Preset Stopwatch",
						"hours:minutes:seconds/minutes:seconds/seconds", "",
						new TimeValidator());
				if (dlg.open() == Window.OK) {


					timer.preset(new Time(dlg.getValue()));
				}
			}
		});
		}

	private class TimeValidator implements IInputValidator {

		@Override
		public String isValid(String newText) {
			return Pattern.matches("\\d*:?\\d*:?\\d+", newText) ? null
					: "not a valid time";
		}

		}
		
	private class Timer implements Runnable {
		private Time presetTime;
		private Time current;
		private boolean active = false;

		@Override
		public void run() {
			overallLabel.setText(current.toString());

			try {
				nextLabel.setText(current.subtract(presetTime)
						.add(nextCheckpoint).toString());
			} catch (NullPointerException e) {
				nextLabel.setText("");
			}

			current.decrease();

			if (active)
				Display.getCurrent().timerExec(1000, this);
		}

		public void preset(Time time) {
			presetTime = time;
			reset();
		}

		public void reset() {
			current = presetTime.clone();
			run();
		}

		public void stop() {
			active = false;
		}
			
		public void start() {
			active = true;
			run();
		}
	}

	@Override
	public void update() {
			// there is a checkpoint associated with the current slide
			// so we gather the next checkpoint
			nextCheckpoint = Presentation.getNextCheckpoint();
	};
}
