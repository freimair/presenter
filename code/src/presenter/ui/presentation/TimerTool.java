package presenter.ui.presentation;

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

public class TimerTool extends Composite {
	private Label timerLabel;

	public TimerTool(Composite parent, int style) {
		super(parent, style);
		this.setLayout(new GridLayout(3, true));

		timerLabel = new Label(this, SWT.NONE);
		FontData newFontData = new FontData(
				timerLabel.getFont().getFontData()[0].toString());
		newFontData.height = 30;
		timerLabel.setFont(new Font(Display.getCurrent(), newFontData));
		timerLabel.setText(" 00:00:00 ");
		timerLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
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
					String[] tmp = dlg.getValue().split(":");

					int seconds = 0;
					for (int i = tmp.length - 1; i >= 0; i--)
						seconds += Integer.parseInt(tmp[i])
								* Math.pow(60, tmp.length - 1 - i);

					timer.preset(seconds);
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
		private int defaultseconds = 1;
		private int seconds = defaultseconds;
		private boolean active = false;

		@Override
		public void run() {
			seconds--;
			int hours = seconds / 3600, remainder = seconds % 3600, minutes = remainder / 60, seconds = remainder % 60;
	
			timerLabel.setText((0 > seconds ? "-" : " ")
					+ ((Math.abs(hours) < 10 ? "0" : "") + Math.abs(hours)
							+ ":" + (Math.abs(minutes) < 10 ? "0" : "")
							+ Math.abs(minutes) + ":"
							+ (Math.abs(seconds) < 10 ? "0" : "") + Math
								.abs(seconds)));

			if (active)
				Display.getCurrent().timerExec(1000, this);
		}

		public void preset(int seconds) {
			defaultseconds = seconds + 1;
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
