package presenter.ui.presentation.tools;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
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
	private Label latencyLabel;
	private Timer timer;

	public TimerTool(Composite parent, int style, PresenterControl control) {
		super(parent, style, control);
		this.setLayout(new GridLayout(2, false));
		this.setLayoutData(new RowData(250, SWT.DEFAULT));

		// update next checkpoint
		init();

		Label nextLabelLabel = new Label(this, SWT.NONE);
		nextLabelLabel.setText("Time until next checkpoint");
		nextLabelLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				false, 2, 1));
		nextLabel = new Label(this, SWT.RIGHT);
		FontData newFontData = new FontData(
				nextLabel.getFont().getFontData()[0].toString());
		newFontData.height = 30;
		nextLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,
				2, 1));
		nextLabel.setFont(new Font(Display.getCurrent(), newFontData));

		Label latencyLabelLabel = new Label(this, SWT.NONE);
		latencyLabelLabel.setText("Latency to checkpoints");
		latencyLabelLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				false, 2, 1));
		latencyLabel = new Label(this, SWT.RIGHT);
		latencyLabel.setFont(new Font(Display.getCurrent(), newFontData));
		latencyLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				false, 2, 1));

		Label overallLabelLabel = new Label(this, SWT.NONE);
		overallLabelLabel.setText("overall time left");
		overallLabelLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				false, 2, 1));
		overallLabel = new Label(this, SWT.RIGHT);
		overallLabel.setFont(new Font(Display.getCurrent(), newFontData));
		overallLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				false, 2, 1));

		final Button startstopTimer = new Button(this, SWT.PUSH);
		startstopTimer.setText("start");
		startstopTimer.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		timer = new Timer();
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
		resetTimer.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
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
		private Time latency;

		@Override
		public void run() {
			if (!active)
				return;

			Display.getCurrent().timerExec(1000, this);

			updateControls();

			current.decrease();
		}

		private void updateControls() {
			overallLabel.setText(current.toString());

			try {
				Time tmp = current.subtract(presetTime).add(nextCheckpoint);
				if (0 < tmp.compareTo(new Time(0)))
					latency.decrease();
				latencyLabel.setText(latency.toString());
				nextLabel.setText(tmp.toString());
			} catch (NullPointerException e) {
				nextLabel.setText("");
			}
			overallLabel.getParent().layout();
		}

		public void preset(Time time) {
			presetTime = time;
			reset();
		}

		public void reset() {
			latency = new Time(0);
			try {
				current = presetTime.clone();
			} catch (NullPointerException e) {
				current = new Time(0);
			}
			updateControls();
		}

		public void stop() {
			active = false;
		}
			
		public void start() {
			active = true;
			run();
		}

		public void adjustLatency() {
			if (!active)
				return;
			try {
				Time tmp = current.subtract(presetTime).add(nextCheckpoint);
				if (0 > tmp.compareTo(new Time(0)))
					latency = latency.add(tmp);
				updateControls();
			} catch (Exception e) {
				// don't care
			}
		}
	}

	private void init() {
		nextCheckpoint = Presentation.getNextCheckpoint();
	}

	@Override
	public void update() {
		timer.adjustLatency();
		init();
	};

	@Override
	public void dispose() {
		timer.stop();
		super.dispose();
	}
}
