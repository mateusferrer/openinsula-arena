package org.openinsula.arena.gwt.client;

import com.google.gwt.core.client.EntryPoint;

public class Main implements EntryPoint {

	public void onModuleLoad() {
	}


//	private class Apartamento {
//		List<Integer> diasOcupado;
//		String numero;
//
//		Apartamento(String numero, Integer ... diasOcupado) {
//			this.numero = numero;
//			this.diasOcupado = Arrays.asList(diasOcupado);
//		}
//
//		public List<Integer> getDiasOcupado() {
//			return diasOcupado;
//		}
//
//		public String getNumero() {
//			return numero;
//		}
//
//		public boolean isDiaOcupado(int dia) {
//			return diasOcupado.contains(dia);
//		}
//
//	}
//
//	public void onModuleLoad() {
//
//		DataGridModel<Apartamento, Integer> dataModel = new DefaultDataGridModel<Apartamento, Integer>();
//		Apartamento s1 = new Apartamento("Std - 01", 3, 5);
//		Apartamento s2 = new Apartamento("Std - 02", 2, 3, 4);
//		Apartamento s3 = new Apartamento("Std - 03", 1, 3, 7);
//		Apartamento s4 = new Apartamento("Std - 04", 6);
//
//
//		dataModel.setData(Arrays.asList(s1, s2, s3, s4),
//				Arrays.asList(1, 2, 3, 4, 5, 6, 7));
//
//		final DataGrid<Apartamento, Integer> grid = new DataGrid<Apartamento, Integer>();
//		grid.setDefaultCellRenderer(new DataGridCellRender<Apartamento, Integer>() {
//
//			public FocusWidget render(Apartamento rowValue, Integer columnValue) {
//				ToggleButton button = new ToggleButton();
//				if (rowValue.isDiaOcupado(columnValue)) {
//					button.setText("(Reservado)");
//					button.setEnabled(false);
//				} else {
//					button.setText("(Livre)");
//					button.setEnabled(true);
//				}
//
//				return button;
//			}
//		});
//		grid.setDefaultRowTitleRenderer(new TitleRender<Widget, Apartamento>() {
//			public Widget render(Apartamento value) {
//				return new Label(value.getNumero());
//			}
//		});
//
//		grid.addDataGridListener(new DataGridListener<Apartamento, Integer>() {
//			public void onCellClicked(FocusWidget sender, Apartamento rowValue, Integer columnValue, int row, int col) {
//			}
//
//			public void onInvalidSelection(FocusWidget sender, int row, int col) {
//				Window.alert("Seleção inválida");
//			}
//		});
//		grid.setModel(dataModel);
//
////		grid.setMultipleRowSelectionAllowed(true);
//		grid.setMultipleColumnSelectionAllowed(true);
//
//		Button reservarButton = new Button("Reservar");
//		reservarButton.addClickListener(new ClickListener() {
//			public void onClick(Widget sender) {
//				List<Entry<Apartamento, Integer>> selection = grid.getSelectedEntries();
//				StringBuilder sb = new StringBuilder();
//				for (Entry<Apartamento, Integer> entry : selection) {
//					sb.append("apartamento: ");
//					sb.append(entry.getRowValue().getNumero());
//					sb.append(", dia: ");
//					sb.append(entry.getColumnValue());
//					sb.append("\n");
//				}
//				Window.alert(sb.toString());
//			}
//		});
//
//		RootPanel.get("main").add(grid);
//		RootPanel.get("main").add(reservarButton);
//
//	}

}
