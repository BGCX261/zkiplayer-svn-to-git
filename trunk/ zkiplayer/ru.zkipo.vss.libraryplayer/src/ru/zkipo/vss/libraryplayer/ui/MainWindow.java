package ru.zkipo.vss.libraryplayer.ui;

import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import ru.zkipo.vss.libraryplayer.dispatcher.PlayerActionDispatcher;
import ru.zkipo.vss.libraryplayer.flac.utils.FLACPlayer;
import ru.zkipo.vss.libraryplayer.thread.PlayerThread;

public class MainWindow {

	protected Shell shell;
	protected Display display;
	protected Thread aCurrentPlayingThread;
	protected Boolean isPlaying = false;

	protected MenuItem aTOPFileMenuOpen;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			MainWindow window = new MainWindow();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		display = Display.getDefault();
		createContents();
		addDefaultListeners();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(841, 560);
		shell.setText("SWT Application");
		shell.setLayout(new GridLayout(1, false));

		Composite compositeTOP = new Composite(shell, SWT.NONE);
		compositeTOP.setLayout(new GridLayout(3, false));
		compositeTOP.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		Composite aVolumeComposite = new Composite(compositeTOP, SWT.NONE);

		Composite aPlayControlComposite = new Composite(compositeTOP, SWT.NONE);
		aPlayControlComposite.setLayout(new GridLayout(3, false));
		aPlayControlComposite.setLayoutData(new GridData(SWT.CENTER,
				SWT.CENTER, true, false, 1, 1));

		Button aBtnPrevious = new Button(aPlayControlComposite, SWT.NONE);
		aBtnPrevious.setBounds(0, 0, 94, 28);
		aBtnPrevious.setText("<");

		Button aBtnPlayPause = new Button(aPlayControlComposite, SWT.NONE);
		aBtnPlayPause.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				if (!isPlaying) {

					FileDialog fd = new FileDialog(shell);
					final String aFilename = fd.open();
					if (aFilename == null || aFilename.isEmpty())
						return;
					PlayerActionDispatcher.setDisplay(display);
					PlayerActionDispatcher.setPlayerThread(new PlayerThread(
							aFilename, new FLACPlayer()));

					Runnable runnable = new Runnable() {
						@Override
						public void run() {
							FLACPlayer fp = new FLACPlayer();
							try {
								fp.decode(aFilename);
							} catch (IOException | LineUnavailableException e) {
								e.printStackTrace();
							}

						}
					};
					aCurrentPlayingThread = new Thread(runnable);
					aCurrentPlayingThread.start();
					isPlaying = true;
				} else {
					// aCurrentPlayingThread.;
					isPlaying = false;
				}
			}

		});
		aBtnPlayPause.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false,
				false, 1, 1));
		aBtnPlayPause.setText("ZKIPO");

		Button aBtnNext = new Button(aPlayControlComposite, SWT.NONE);
		aBtnNext.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		aBtnNext.setText(">");

		Composite aSearchComposite = new Composite(compositeTOP, SWT.NONE);
		aSearchComposite.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1));

		Composite compositeMain = new Composite(shell, SWT.NONE);
		compositeMain.setLayout(new GridLayout(1, false));
		compositeMain.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));

		SashForm sashMain = new SashForm(compositeMain, SWT.NONE);
		sashMain.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,
				1));

		Composite compositeLeft = new Composite(sashMain, SWT.NONE);
		compositeLeft.setLayout(new GridLayout(1, false));

		Composite compositeMid = new Composite(sashMain, SWT.NONE);

		Composite compositeRight = new Composite(sashMain, SWT.NONE);
		sashMain.setWeights(new int[] { 3, 10, 1 });

		Composite compositeBottom = new Composite(shell, SWT.NONE);
		compositeBottom.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);

		MenuItem mntmFile_1 = new MenuItem(menu, SWT.CASCADE);
		mntmFile_1.setText("File");

		Menu aTOPFileMenu = new Menu(mntmFile_1);
		mntmFile_1.setMenu(aTOPFileMenu);

		aTOPFileMenuOpen = new MenuItem(aTOPFileMenu, SWT.NONE);
		aTOPFileMenuOpen.setText("Open");

	}

	private void addDefaultListeners() {
		aTOPFileMenuOpen.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fd = new FileDialog(shell);
				String aFilename = fd.open();
				PlayerActionDispatcher.setDisplay(display);
				PlayerActionDispatcher.setPlayerThread(new PlayerThread(
						aFilename, new FLACPlayer()));
				PlayerActionDispatcher.StartPlay(aFilename);
			}
		});
	}
}
