/**
 * http://tutorials.jenkov.com/javafx/index.html
 */

package org.fc.edit;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.fc.widgets.DynaConstants;
import org.fc.widgets.DynaDialog;
import org.fc.widgets.DynaGridBagPanel;
import org.fc.widgets.DynaPanel;
import org.fc.widgets.SmartDialog;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {
	public final static String CMD_FILE_OPEN_FLAT = "file.open.flat";
	public final static String CMD_FILE_OPEN_VSAM = "file.open.vsam";
	public final static String CMD_FILE_SAVE = "file.save";
	public final static String CMD_FILE_SAVE_AS = "file.save.as";
	public final static String CMD_FILE_CLOSE = "file.close";
	public final static String CMD_FILE_EXIT = "file.exit";
	public final static String CMD_EDIT_CUT = "edit.cut";
	public final static String CMD_EDIT_COPY = "edit.copy";
	public final static String CMD_EDIT_PASTE = "edit.paste";

	public final static String CMD_EDIT_INSERT_ABOVE = "edit.insert.above";
	public final static String CMD_EDIT_INSERT_BELOW = "edit.insert.below";
	public final static String CMD_EDIT_DELETE = "edit.delete";
	public final static String CMD_EDIT_DELETE_MULTIPLE = "edit.delete.multiple";
	public final static String CMD_VIEW_ASCII = "view.ascii";
	public final static String CMD_VIEW_EBCDIC = "view.ebcdic";

	public final static String CMD_SEARCH_FIND = "search.find";
	public final static String CMD_SEARCH_PGUP = "search.find.page.up";
	public final static String CMD_SEARCH_PGDW = "search.find.page.down";
	public final static String CMD_SEARCH_FIND_NEXT = "search.find.next"; 
	public final static String CMD_SEARCH_FIND_PREV = "search.find.prev";
	public final static String CMD_SEARCH_REPLACE = "search.replace";
	public final static String CMD_SEARCH_TOP = "search.top";
	public final static String CMD_SEARCH_BOTTOM = "search.bottom";

	public final static String CMD_TOGGLE_HEXMODE = "toggle.hexmode";
	public final static String CMD_TOGGLE_RECNUM = "toggle.recnum";
	public final static String CMD_TOGGLE_CROSSBEAM = "toggle.cross.beam";

	public final static String CMD_HELP_CONTENTS = "help.contents";
	public final static String CMD_HELP_ABOUT = "help.about";

	MenuBar menuBar;
	Menu mFile;
	MenuItem miOpenFlat;
	MenuItem miSave;
	MenuItem miSaveAs;
	MenuItem miClose;
	MenuItem miExit;

	Menu mSearch;
	MenuItem miPageDown;
	MenuItem miPageUp;
	MenuItem miSearchTop;
	MenuItem miSearchBottom;
	MenuItem miFind;
	MenuItem miFindPrev;
	MenuItem miFindNext;
	MenuItem miReplace;

	Menu mEdit;
	MenuItem miCut;
	MenuItem miCopy;
	MenuItem miPaste;
	MenuItem miInsertAbove;
	MenuItem miInsertBelow;
	MenuItem miDelete;
	MenuItem miDeleteMultiple;
	Menu mView;
	RadioMenuItem mrbViewAscii;
	RadioMenuItem mrbViewEbcdic;

	Menu mHelp;
	MenuItem miHelpContents;
	MenuItem miHelpAbout;

	Button btnFileOpenFlat;
	Button btnFileSave;
	Button btnFileSaveAs;
	Button btnEditCut;
	Button btnEditCopy;
	Button btnEditPaste;
	Button btnSearchFind;
	Button btnSearchFindPrev;
	Button btnSearchFindNext;
	Button btnSearchReplace;
	Button btnSearchTop;
	Button btnSearchBottom;

	ToggleButton btnToggleHexmode;
	ToggleButton btnToggleRecnum;
	ToggleButton btnToggleCrossBeam;
	ResourceBundle messages = null;
	Logger logger = Logger.getLogger(this.getClass().getName());

	FileEditorPane fe=null;
	
	@Override
	public void start(Stage stage) throws Exception {
		// StackPane root = new StackPane();
		// //FXMLLoader.load(getClass().getResource("scene.fxml"));
		// ObservableList list = root.getChildren();

		messages = ResourceBundle.getBundle("org.fc.edit.messages");

		Pane root = initWidgets();

		// root.getChildren().add(new Label("RAVA"));

		Scene scene = new Scene(root, 800, 600);
		// scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
		/*
		 * Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); Dimension
		 * frameSize = new Dimension((long)scene.getWidth(), (long)scene.getHeight());
		 * if (frameSize.height > screenSize.height) { frameSize.height =
		 * screenSize.height; } if (frameSize.width > screenSize.width) {
		 * frameSize.width = screenSize.width; } int posx = (screenSize.width -
		 * frameSize.width) / 2; int posy = (screenSize.height - frameSize.height) / 2;
		 * this.setLocation(posx, posy);
		 */

		setMenuState();
		// this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		stage.setTitle("FileEdit " + PackageInfo.getVersion());
		stage.setScene(scene);
		
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
		    @Override
		    public void handle(WindowEvent t) {
		    	doExit();
		    }
		});
		stage.show();
	}

	public static void main(String[] args) {
		// TODO: command line arguments
		launch(args);
	}

	private Pane initWidgets() {
		ToolBar toolBar = new ToolBar();

		btnFileOpenFlat = addToolbarButton(toolBar, CMD_FILE_OPEN_FLAT);
		btnFileSave = addToolbarButton(toolBar, CMD_FILE_SAVE);
		btnFileSaveAs = addToolbarButton(toolBar, CMD_FILE_SAVE_AS);

		toolBar.getItems().add(new Separator());

		btnEditCut = addToolbarButton(toolBar, CMD_EDIT_CUT);
		btnEditCopy = addToolbarButton(toolBar, CMD_EDIT_COPY);
		btnEditPaste = addToolbarButton(toolBar, CMD_EDIT_PASTE);

		toolBar.getItems().add(new Separator());

		btnSearchFind = addToolbarButton(toolBar, CMD_SEARCH_FIND);
		btnSearchFindPrev = addToolbarButton(toolBar, CMD_SEARCH_FIND_PREV);
		btnSearchFindNext = addToolbarButton(toolBar, CMD_SEARCH_FIND_NEXT);
		btnSearchReplace = addToolbarButton(toolBar, CMD_SEARCH_REPLACE);
		btnSearchTop = addToolbarButton(toolBar, CMD_SEARCH_TOP);
		btnSearchBottom = addToolbarButton(toolBar, CMD_SEARCH_BOTTOM);

		toolBar.getItems().add(new Separator());

		btnToggleHexmode = addToolbarToggleButton(toolBar, CMD_TOGGLE_HEXMODE);
		btnToggleRecnum = addToolbarToggleButton(toolBar, CMD_TOGGLE_RECNUM);
		btnToggleCrossBeam = addToolbarToggleButton(toolBar, CMD_TOGGLE_CROSSBEAM);

		menuBar = new MenuBar();
		mFile = new Menu(messages.getString("menu.file"));
		miOpenFlat = addMenuItem(mFile, CMD_FILE_OPEN_FLAT);
		miSave = addMenuItem(mFile, CMD_FILE_SAVE);
		miSaveAs = addMenuItem(mFile, CMD_FILE_SAVE_AS);
		miClose = addMenuItem(mFile, CMD_FILE_CLOSE);
		mFile.getItems().add(new SeparatorMenuItem());
		miExit = addMenuItem(mFile, CMD_FILE_EXIT);
		menuBar.getMenus().add(mFile);

		mEdit = new Menu();
		mEdit.setText(messages.getString("menu.edit"));
		miCut = addMenuItem(mEdit, CMD_EDIT_CUT);
		miCopy = addMenuItem(mEdit, CMD_EDIT_COPY);
		miPaste = addMenuItem(mEdit, CMD_EDIT_PASTE);
		mEdit.getItems().add(new SeparatorMenuItem());
		miInsertAbove = addMenuItem(mEdit, CMD_EDIT_INSERT_ABOVE);
		miInsertBelow = addMenuItem(mEdit, CMD_EDIT_INSERT_BELOW);
		miDelete = addMenuItem(mEdit, CMD_EDIT_DELETE);
		miDeleteMultiple = addMenuItem(mEdit, CMD_EDIT_DELETE_MULTIPLE);
		mEdit.getItems().add(new SeparatorMenuItem());
		mView = new Menu();
		mView.setText(messages.getString("menu.view"));
		ToggleGroup group = new ToggleGroup();
		mrbViewAscii = addRadioButtonMenu(mView, group, CMD_VIEW_ASCII);
		mrbViewAscii.setSelected(true);
		mrbViewEbcdic = addRadioButtonMenu(mView, group, CMD_VIEW_EBCDIC);
		mEdit.getItems().add(mView);
		menuBar.getMenus().add(mEdit);

		mSearch = new Menu();
		mSearch.setText(messages.getString("menu.search"));
		miPageUp = addMenuItem(mSearch, CMD_SEARCH_PGUP, new KeyCodeCombination(KeyCode.F7));
		miPageDown = addMenuItem(mSearch, CMD_SEARCH_PGDW, new KeyCodeCombination(KeyCode.F8));
		mSearch.getItems().add(new SeparatorMenuItem());
		miFind = addMenuItem(mSearch, CMD_SEARCH_FIND, new KeyCodeCombination(KeyCode.F7, KeyCombination.CONTROL_DOWN));
		miFindPrev = addMenuItem(mSearch, CMD_SEARCH_FIND_PREV,
				new KeyCodeCombination(KeyCode.F3, KeyCombination.SHIFT_DOWN));
		miFindNext = addMenuItem(mSearch, CMD_SEARCH_FIND_NEXT, new KeyCodeCombination(KeyCode.F3));
		miReplace = addMenuItem(mSearch, CMD_SEARCH_REPLACE);
		mSearch.getItems().add(new SeparatorMenuItem());
		miSearchTop = addMenuItem(mSearch, CMD_SEARCH_TOP, new KeyCodeCombination(KeyCode.HOME));
		miSearchBottom = addMenuItem(mSearch, CMD_SEARCH_BOTTOM, new KeyCodeCombination(KeyCode.END));
		menuBar.getMenus().add(mSearch);

		mHelp = new Menu();
		mHelp.setText(messages.getString("menu.help"));
		miHelpContents = addMenuItem(mHelp, CMD_HELP_CONTENTS);
		miHelpAbout = addMenuItem(mHelp, CMD_HELP_ABOUT);
		menuBar.getMenus().add(mHelp);

		logger.severe("MANCA IL PANNELLO FILEPANE");
		fe=new FileEditorPane(this, messages);
		BorderPane bp=new BorderPane();
		bp.setTop(new VBox(menuBar, toolBar));
		bp.setCenter(fe);
		return bp;
	}

	MenuItem addMenuItem(Menu m, String cmd, KeyCodeCombination ks) {
		MenuItem mi = addMenuItem(m, cmd);
		mi.setAccelerator(ks);
		return mi;
	}

	MenuItem addMenuItem(Menu m, String cmd) {
		MenuItem mi = new MenuItem(messages.getString("menu." + cmd));
		//String res = "graphics/" + cmd + ".png";
		//mi.setGraphic(new ImageView(new Image(getClass().getResourceAsStream(res))));
		mi.setOnAction(value -> {
			actionPerformed(cmd);
		});
		m.getItems().add(mi);
		return mi;
	}
 
	RadioMenuItem addRadioButtonMenu(Menu m, ToggleGroup group, String cmd) {
		RadioMenuItem mi = new RadioMenuItem(messages.getString("menu." + cmd));
		//String res = "graphics/" + cmd + ".png";
		//mi.setGraphic(new ImageView(new Image(getClass().getResourceAsStream(res))));
		mi.setOnAction(value -> {
			actionPerformed(cmd);
		});
		group.getToggles().add(mi);
		m.getItems().add(mi);
		return mi;
	}

	private Button addToolbarButton(ToolBar t, String cmd) {
		return (Button)addTollbarAbstractButton(t, cmd, new Button());
	}

	private ToggleButton addToolbarToggleButton(ToolBar t, String cmd) {
		return (ToggleButton)addTollbarAbstractButton(t, cmd, new ToggleButton());
	}

	private ButtonBase addTollbarAbstractButton(ToolBar t, String cmd, ButtonBase b) {
		String res = "graphics/" + cmd + ".png";
		try {
			ImageView iv = new ImageView(new Image(getClass().getResourceAsStream(res)));
			iv.setFitWidth(24);
			iv.setFitHeight(24);
			b.setGraphic(iv);
		} catch (Exception e) {
			try {
				b.setText(messages.getString("toolbar." + cmd));
			} catch (Exception ie) {
				b.setText(cmd);
			}
		}
		b.setOnAction(value -> {
			actionPerformed(cmd);
		});
		t.getItems().add(b);
		return b;
	}

	void setMenuState() {
		boolean fileClose = true;
		boolean noSelection = true;
		boolean flat = false;
		
		if (fe != null) {
			fileClose = !fe.isOpened();
			noSelection = !fe.hasSelection();
			flat = fe.isFlat();
		}
		miOpenFlat.setDisable(false);
		miSave.setDisable(fileClose);
		miSaveAs.setDisable(true);
		miClose.setDisable(fileClose);
		miExit.setDisable(false);

		mSearch.setDisable(fileClose);
		//miSearchTop.setEnabled(file);
		// miSearchBottom.setEnabled(b);

		mEdit.setDisable(fileClose);
		miCut.setDisable(fileClose || noSelection);
		miCopy.setDisable(fileClose || noSelection);
		miPaste.setDisable(fileClose);
		miReplace.setDisable(fileClose);

		btnFileOpenFlat.setDisable(false);

		btnFileSave.setDisable(fileClose);
		btnFileSaveAs.setDisable(fileClose);

		btnEditCut.setDisable(fileClose ||noSelection);
		btnEditCopy.setDisable(fileClose || noSelection);
		btnEditPaste.setDisable(fileClose);
		btnSearchFind.setDisable(fileClose);
		btnSearchFindPrev.setDisable(fileClose);
		btnSearchFindNext.setDisable(fileClose);
		btnSearchReplace.setDisable(fileClose);
		btnSearchTop.setDisable(fileClose);
		btnSearchBottom.setDisable(fileClose);
		miDeleteMultiple.setDisable(!flat);

		btnToggleHexmode.setDisable(fileClose);
		btnToggleRecnum.setDisable(fileClose);
		btnToggleCrossBeam.setDisable(fileClose);

	}

	public static void wrongParms(String m) {
		System.err.println("wrong parameters: " + m);
		Main.showUsage();
	}

	public static void showUsage() {
		System.err.println("usage:\n");
		System.err.println("  FileEdit [options] <cluster|file> [catalog]\n");
		System.err.println("options:");
		System.err.println("  -flat         open a flat file");
		System.err.println("  -length <l>   specifies length <l> for flat file (ignore for vsam)");
		System.err.println("  -ro           open file read only");
		System.err.println("  -version      show version");
		System.err.println("  -help         show this help");
		System.exit(0);
	}
	
	public static void showVersion() {
		System.err.println("Jedi version " + PackageInfo.getVersion());
		System.exit(0);
	}
	
	public void doOpen(boolean flat) {
		if (!doClose()) {
			return;
		}
		//TODO: open file dialog
		/**
		if (flat) {
			DynaGridBagPanel dp;
			if (chooser == null) {
				chooser = new JFileChooser();
				dp = new DynaGridBagPanel(this, "");
				dp.addField("RECLEN", messages.getString("labels.record.length"), DynaDialog.INTEGER, null, true);
				dp.addField("READONLY", messages.getString("labels.read.only"), DynaDialog.BOOLEAN, null);
				dp.addField("VARLEN", messages.getString("labels.variable.length"), DynaDialog.BOOLEAN, null);
				dp.addField("VARLENLE", messages.getString("labels.variable.length.le"), DynaDialog.BOOLEAN, null);
				dp.setPanelBorder(DynaPanel.BORDER_NONE);
				dp.pack();
				chooser.setAccessory(dp);
			} else {
				dp = (DynaGridBagPanel) chooser.getAccessory();
			}
			for (;;) {
				int returnVal = chooser.showOpenDialog(parent);
				if (returnVal != JFileChooser.APPROVE_OPTION) {
					return;
				}
				if (dp.verifyData()) {
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						doClose();
						Integer reclen = (Integer) dp.getValue("RECLEN");
						Boolean ro = (Boolean) dp.getValue("READONLY");
						Boolean varlen=(Boolean) dp.getValue("VARLEN");
						Boolean varlenLE=(Boolean) dp.getValue("VARLENLE");
						if (varlen.booleanValue()) {
							ro=new Boolean(true);
							reclen=new Integer(32*1024);
						}
						System.out.println("apro a " + reclen + " ro=" + ro);
						open(chooser.getSelectedFile(), reclen.intValue(), ro.booleanValue(), varlen.booleanValue(), varlenLE.booleanValue());
					}
					break;
				}
			}
		} else {
		}
		*/
		setMenuState();
	}

	public void doSave() {
		try {
			System.out.println("doSave()");
			fe.save();
			System.out.println("file saved");
			FXDialog.messageBox(this, messages.getString("msg.file.saved"));
		} catch (IOException e) {
			FXDialog.errorBox(e);
		}
		setMenuState();
	}

	public void doSaveAs() {
	}

	public void doExit() {
		if (doClose()) {
			System.exit(0);
		}
	}

	public boolean doClose() {
		try {
			if (fe.isModified()) {
				Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
				alert.setTitle(messages.getString("dialog.file.changed"));
				alert.setContentText(messages.getString("msg.confirm.file.changed"));
				ButtonType yes = new ButtonType(messages.getString("button.yes"), ButtonData.YES);
				ButtonType no = new ButtonType(messages.getString("button.no"), ButtonData.NO);
				ButtonType canc = new ButtonType(messages.getString("button.cancel"), ButtonData.CANCEL_CLOSE);
				alert.getButtonTypes().setAll(yes, no, canc);
				
				Optional<ButtonType> result = alert.showAndWait();
				switch (result.get().getButtonData())
				{
				case YES:
					fe.save();
					break;
					
				case NO:
					fe.revertChanges();
					break;

				case CANCEL_CLOSE:
				default:
					return false;
				}
			}
			fe.close();
		} catch (IOException e) {
			FXDialog.errorBox(e);
		}
		setMenuState();
		return true;
	}
	
	public void open(File f, int reclen, boolean ro, boolean vl, boolean vlle) {
		new OpenFileThread(f, reclen, ro, vl, vlle).start();
		/*
		 * try { fe.open(f,reclen, ro); fe.requestFocus(); } catch (Exception e) {
		 * showException(e); }
		 */
	}

	public void doToggleHexmode() {
		fe.setHexMode(btnToggleHexmode.isSelected());
	}

	public void doToggleRecnum() {
		fe.setShowRecordNumber(btnToggleRecnum.isSelected());
	}

	public void doToggleCrossBeam() {
		fe.setShowCrossBeam(btnToggleCrossBeam.isSelected());
	}

	public void doChangeConversionMode(int m) {
		fe.setConversionMode(m);
	}

	public void doPageUp() {
		fe.pageUp();
	}

	public void doPageDown() {
		fe.pageDown();
	}

	public void doLocateTop() {
		fe.locateTop();
	}

	public void doLocateBottom() {
		fe.locateBottom();
	}

	public void doCopy() {
		fe.copySelection();
	}

	public void doPaste() {
		fe.pasteClipboardContents();
	}

	public void doCut() {
		fe.cutSelection();
	}

	public void doFind() {
		// TODO: find dialog
//		if (findDialog == null) {
//			findDialog = new FindDialog(this, messages, false);
//		}
//		findDialog.setVisible(true);
//
//		if (findDialog.isOk()) {
//			System.out.println("INIZIO IL FIND");
//			byte[] s = findDialog.getFindWhat();
//			fe.find(s, findDialog.isIgnoreCase(), (findDialog.getDirection() > 0), findDialog.isOnRange(), findDialog
//					.getRangeFrom(), findDialog.getRangeTo());
//		}
	}

	public void doReplace() {
		/// TODO: replace dialog
		
//		if (replaceDialog == null) {
//			replaceDialog = new FindDialog(this, messages, true);
//		}
//		replaceDialog.setVisible(true);
//
//		if (replaceDialog.isOk()) {
//			System.out.println("INIZIO REPLACE");
//			byte[] s = replaceDialog.getFindWhat();
//			byte[] r = replaceDialog.getReplaceWith();
//
//			fe.replace(s, r, replaceDialog.isIgnoreCase(), (replaceDialog.getDirection() > 0), replaceDialog.isOnRange(),
//					replaceDialog.getRangeFrom(), replaceDialog.getRangeTo());
//		}
	}

	public void doFindPrevious() {
		fe.findPrevious();
	}

	public void doFindNext() {
		fe.findNext();
	}

	public void doInsertRecord(boolean above) {
		boolean rc = fe.insertRecord(above);
		if (!rc) {
			FXDialog.messageBox(messages.getString("msg.cannot.insert.here"));
		}
	}

	public void doDeleteRecord() {
		fe.deleteRecord();
	}

	public void doDeleteMultiple() {
		/// TODO: delete multiple
//		DynaDialog d = new DynaDialog(this, true, "Delete multiple");
//		d.addField("FROM", messages.getString("labels.delete.multiple.from"), DynaConstants.INTEGER, null, true);
//		d.addField("TO", messages.getString("labels.delete.multiple.to"), DynaConstants.INTEGER, null, true);
//		d.pack();
//		d.setVisible(true);
//		if (d.isOk()) {
//			Integer f = (Integer) d.getValue("FROM");
//			Integer t = (Integer) d.getValue("TO");
//			if (t.longValue() < f.longValue()) {
//				SmartDialog.messageBox(this, messages.getString("msg.wrong.range"));
//			} else {
//				fe.deleteMultiple(f.intValue(), t.longValue());
//			}
//		}
	}

	public void doHelp() {
		FXDialog.errorBox("You ingenuous!!!");
	}

	public void doAbout() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(messages.getString("caption.about"));
		alert.setHeaderText(org.fc.edit.PackageInfo.getName());
		alert.setContentText("Version " + org.fc.edit.PackageInfo.getVersion());
		alert.showAndWait();
	}

	public long acceptNumberedKey(long rrn) {
		return 0;
	}

	public void actionPerformed(String cmd) {
		//String cmd = evt.getActionCommand();
		if (cmd.equals(CMD_FILE_OPEN_FLAT)) {
			doOpen(true);
		} else if (cmd.equals(CMD_FILE_OPEN_VSAM)) {
			doOpen(false);
		} else if (cmd.equals(CMD_FILE_EXIT)) {
			doExit();
		} else if (cmd.equals(CMD_FILE_SAVE)) {
			doSave();
		} else if (cmd.equals(CMD_FILE_SAVE_AS)) {
			doSaveAs();
		} else if (cmd.equals(CMD_FILE_CLOSE)) {
			doClose();
		} else if (cmd.equals(CMD_EDIT_COPY)) {
			doCopy();
		} else if (cmd.equals(CMD_EDIT_PASTE)) {
			doPaste();
		} else if (cmd.equals(CMD_EDIT_CUT)) {
			doCut();
		} else if (cmd.equals(CMD_TOGGLE_HEXMODE)) {
			doToggleHexmode();
		} else if (cmd.equals(CMD_TOGGLE_RECNUM)) {
			doToggleRecnum();
		} else if (cmd.equals(CMD_TOGGLE_CROSSBEAM)) {
			doToggleCrossBeam();
		} else if (cmd.equals(CMD_SEARCH_PGUP)) {
			doPageUp();
		} else if (cmd.equals(CMD_SEARCH_PGDW)) {
			doPageDown();
		} else if (cmd.equals(CMD_SEARCH_TOP)) {
			doLocateTop();
		} else if (cmd.equals(CMD_SEARCH_BOTTOM)) {
			doLocateBottom();
		} else if (cmd.equals(CMD_SEARCH_FIND)) {
			doFind();
		} else if (cmd.equals(CMD_SEARCH_REPLACE)) {
			doReplace();
		} else if (cmd.equals(CMD_SEARCH_FIND_PREV)) {
			doFindPrevious();
		} else if (cmd.equals(CMD_SEARCH_FIND_NEXT)) {
			doFindNext();
		} else if (cmd.equals(CMD_EDIT_INSERT_ABOVE)) {
			doInsertRecord(true);
		} else if (cmd.equals(CMD_EDIT_INSERT_BELOW)) {
			doInsertRecord(false);
		} else if (cmd.equals(CMD_EDIT_DELETE)) {
			doDeleteRecord();
		} else if (cmd.equals(CMD_EDIT_DELETE_MULTIPLE)) {
			doDeleteMultiple();
		} else if (cmd.equals(CMD_VIEW_ASCII)) {
			doChangeConversionMode(FileEditorPane.CONVERSION_NONE);
		} else if (cmd.equals(CMD_VIEW_EBCDIC)) {
			doChangeConversionMode(FileEditorPane.CONVERSION_EBCDIC);
		} else if (cmd.equals(CMD_HELP_CONTENTS)) {
			doHelp();
		} else if (cmd.equals(CMD_HELP_ABOUT)) {
			doAbout();
		}
		fe.requestFocus();
	}
	
	class OpenFileThread extends Thread {
		File file;
		int reclen;
		boolean ro;
		boolean varlen;
		boolean varlenLE;

		public OpenFileThread(File f, int r, boolean b, boolean vl, boolean vlle) {
			file = f;
			reclen = r;
			ro = b;
			varlen=vl;
			varlenLE=vlle;
		}

		public void run() {
			try {
				fe.open(file, reclen, ro, varlen, varlenLE);
				fe.requestFocus();
				setMenuState();
			} catch (Exception e) {
				FXDialog.errorBox(e);
			}
		}
	}
}