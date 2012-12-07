package presenter.ui.presentation.tools;

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

		// update next checkpoint
		update();

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
		timer.preset(Presentation.getDuration());
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
			try {
				current = presetTime.clone();
			} catch (NullPointerException e) {
				current = new Time(0);
			}
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
		nextCheckpoint = Presentation.getNextCheckpoint();
	};
}
