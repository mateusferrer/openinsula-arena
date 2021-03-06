package org.openinsula.arena.echo2.component.dialog;

import java.util.HashMap;
import java.util.Map;

import nextapp.echo2.app.Alignment;
import nextapp.echo2.app.Button;
import nextapp.echo2.app.Column;
import nextapp.echo2.app.Component;
import nextapp.echo2.app.Extent;
import nextapp.echo2.app.Insets;
import nextapp.echo2.app.Row;
import nextapp.echo2.app.SplitPane;
import nextapp.echo2.app.TextArea;
import nextapp.echo2.app.WindowPane;
import nextapp.echo2.app.event.ActionEvent;
import nextapp.echo2.app.event.ActionListener;
import nextapp.echo2.webcontainer.syncpeer.WindowPanePeer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openinsula.arena.echo2.component.util.FormFactory;
import org.openinsula.arena.echo2.component.util.Styles;
import org.openinsula.arena.echo2.pane.AbstractEchoWindow;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Classe utilit�ria para exibi��o de caixas de Mensagem, Confirma��o e Op��o.
 *
 * @author fabiano
 *
 */
public class DialogUtils extends AbstractEchoWindow {

	private static final long serialVersionUID = 1L;

	private final ActionListener closeActionListener = new ActionListener() {
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent event) {
			hide();
		}
	};

	@Autowired
	private Styles styles;

	private TextArea messageTextArea;

	private Row buttonRow;

	private Button okButton;

	private Button yesButton;

	private Button noButton;

	private final Map<Button, ActionListener> actionMap = new HashMap<Button, ActionListener>();

	private final Log logger = LogFactory.getLog(getClass());

	private SplitPane mainSplitPane;

	public DialogUtils() {
		logger.warn("instanciando DialogUtils: Styles = " + styles);
		
//		try {
//			afterPropertiesSet();
//		}
//		catch (final Exception ex) {
//			logger.warn(ex);
//		}
	}

	public void initComponents() {

		messageTextArea = FormFactory.textArea(385, 100);
		FormFactory.disableField(messageTextArea, true);

		okButton = FormFactory.button("Ok");
		yesButton = FormFactory.button("Sim");
		noButton = FormFactory.button("N�o");

		final Column column = new Column();
		column.add(messageTextArea);
		column.setInsets(new Insets(10, 5));

		buttonRow = new Row();

		buttonRow.setAlignment(new Alignment(Alignment.CENTER, Alignment.CENTER));
		buttonRow.setCellSpacing(new Extent(10, Extent.PX));

		mainSplitPane = new SplitPane(SplitPane.ORIENTATION_VERTICAL_BOTTOM_TOP);

		mainSplitPane.add(buttonRow);
		mainSplitPane.add(column);

		add(mainSplitPane);

		if (styles != null) {
			setStyles(styles);
		}
	}

	@Override
	public void clearComponents() {
		// TODO Auto-generated method stub
		
	}

	public void setStyles(Styles styles) {
		if (logger.isDebugEnabled()) {
			logger.debug("Setando styles: " + styles);
		}
		this.styles = styles;

		try {
			if (styles != null) {
				setStyle(styles.getDialogWindow());

				okButton.setStyle(styles.getDialogButton());
				yesButton.setStyle(styles.getDialogButton());
				noButton.setStyle(styles.getDialogButton());
				buttonRow.setStyle(styles.getDialogButtonRow());
				mainSplitPane.setStyle(styles.getDialogSplitPane());
			}
		}
		catch (final NullPointerException e) {
			logger.warn("Componente nulo.", e);
		}
	}

	public void customizeComponents() {
		setModal(true);
		setClosable(false);
		setDefaultCloseOperation(WindowPane.HIDE_ON_CLOSE);
		if (styles != null) {
			setStyles(styles);
		}
		setProperty(WindowPanePeer.PROPERTY_IE_ALPHA_RENDER_BORDER, Boolean.TRUE);
		setWidth(new Extent(440, Extent.PX));
		setHeight(new Extent(200, Extent.PX));

		hide();
	}

	public void initActions() {
	}

	private void setButtonAction(final Button button, final ActionListener actionListener) {
		if (!closeActionListener.equals(actionListener)) {
			final ActionListener oldAction = actionMap.get(button);
			button.removeActionListener(oldAction);
			actionMap.put(button, actionListener);
		}

		button.removeActionListener(closeActionListener);
		button.addActionListener(actionListener);
		if (!closeActionListener.equals(actionListener)) {
			button.addActionListener(closeActionListener);
		}
	}

	private void setButtons(final Component... components) {
		buttonRow.removeAll();
		for (final Component component : components) {
			buttonRow.add(component);
		}
	}

	public void showConfirmDialog(final String message, final ActionListener confirmListener) {
		setButtonAction(yesButton, confirmListener);
		setButtonAction(noButton, closeActionListener);
		setButtons(yesButton, noButton);
		messageTextArea.setText(message);

		show();
	}

	/**
	 * This is a static method that....
	 * @param pane The pane where the dialog will be added.
	 * @param message The message that will be displayed.
	 * @param okListener The listener to be executed when the OK button receive the click event.
	 */
	public static void showMessageDialog(final Component pane, final String message, final ActionListener okListener) {
		DialogUtils dialogUtils = new DialogUtils();
		pane.add(dialogUtils);
		if (okListener != null) {
			dialogUtils.showMessageDialog(message, okListener);
		} else {
			dialogUtils.showMessageDialog(message);
		}
	}
	
	public void showMessageDialog(final String message, final ActionListener okListener) {
		setButtonAction(okButton, okListener);
		setButtons(okButton);
		messageTextArea.setText(message);

		show();
	}

	public void showMessageDialog(final String message) {
		showMessageDialog(message, closeActionListener);
	}

	public void showOptionDialog(final String message, final Component... components) {
		messageTextArea.setText(message);
		setButtons(components);
		show();
	}

	public void verifyComponents() {
	}

	private void show() {
		super.setVisible(true);
	}

	/**
	 * Esconde a caixa de di�logo exibida.
	 */
	public void hide() {
		super.setVisible(false);
	}

	/**
	 * @see nextapp.echo2.app.Component#setVisible(boolean)
	 */
	@Override
	public void setVisible(final boolean newValue) {
		if (newValue) {
			show();
		}
		else {
			hide();
		}
	}

	public Styles getStyles() {
		return styles;
	}

}
