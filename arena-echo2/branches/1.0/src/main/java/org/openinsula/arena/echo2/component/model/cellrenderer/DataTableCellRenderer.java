package org.openinsula.arena.echo2.component.model.cellrenderer;

import nextapp.echo2.app.Alignment;
import nextapp.echo2.app.Color;
import nextapp.echo2.app.Component;
import nextapp.echo2.app.Extent;
import nextapp.echo2.app.Font;
import nextapp.echo2.app.Insets;
import nextapp.echo2.app.Label;
import nextapp.echo2.app.Table;
import nextapp.echo2.app.button.AbstractButton;
import nextapp.echo2.app.layout.TableLayoutData;
import nextapp.echo2.app.table.TableCellRenderer;
import org.openinsula.arena.echo2.component.div.Div;

public class DataTableCellRenderer implements TableCellRenderer {
	private static final long serialVersionUID = 1L;

	public Component getTableCellRendererComponent(Table table, Object value, int column, int row) {
		Component component;
		TableLayoutData layoutData = new TableLayoutData();
		layoutData.setInsets(new Insets(2));

		if (value == null) {
			component = new Label("(Nenhum)");
			component.setFont(new Font(Font.ARIAL, Font.PLAIN, new Extent(10, Extent.PX)));
		}
		else if (value instanceof AbstractButton) {
			component = (AbstractButton) value;
			layoutData.setAlignment(new Alignment(Alignment.CENTER, Alignment.CENTER));
		}
		else if (value instanceof Div) {
			component = (Div) value;
		}
		else {
			component = new Label(value.toString());
			component.setFont(new Font(Font.ARIAL, Font.PLAIN, new Extent(10, Extent.PX)));
			((Label) component).setLineWrap(false);
		}
		component.setFont(new Font(Font.ARIAL, Font.PLAIN, new Extent(10, Extent.PX)));

		if (row % 2 == 0) {
			layoutData.setBackground(Color.WHITE);
		}
		else {
			layoutData.setBackground(new Color(237, 237, 237));
		}
		component.setLayoutData(layoutData);
		return component;
	}

}
