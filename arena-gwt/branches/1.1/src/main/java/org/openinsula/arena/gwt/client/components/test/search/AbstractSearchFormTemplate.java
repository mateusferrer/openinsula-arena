package org.openinsula.arena.gwt.client.components.test.search;

import java.util.ArrayList;
import java.util.List;

import org.openinsula.arena.gwt.client.ui.FocusComposite;
import org.openinsula.arena.gwt.client.ui.FocusUtils;
import org.openinsula.arena.gwt.client.ui.form.FormBuilder;
import org.openinsula.arena.gwt.client.ui.form.FormItem;
import org.openinsula.arena.gwt.client.ui.form.GroupFormItem;
import org.openinsula.arena.gwt.client.ui.form.validator.FormItemValidatorNew;
import org.openinsula.arena.gwt.client.ui.form.validator.ValidatorChain;
import org.openinsula.arena.gwt.client.ui.suggest.BeanSuggestBox;
import org.openinsula.arena.gwt.client.ui.suggest.BeanSuggestBoxListener;
import org.openinsula.arena.gwt.client.ui.suggest.RemoteBeanSuggestOracle;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.HasFocus;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Widget;

public abstract class AbstractSearchFormTemplate<T> extends FocusComposite implements
		FormItemValidatorNew<AbstractSearchFormTemplate<T>> {

	private DeckPanel forms;

	private T editInstance;

	private SearchForm searchForm;

	protected abstract String getSuggestBoxLabel();

	protected abstract String getSuggestBoxHint();

	private AbstractDetailsSearchFormTemplate<T> detailsForm;

	private List<SearchFormActionListener<T>> actionListeners;

	private List<SearchFormTransitionListener<T>> transitionListeners;

	protected abstract RemoteBeanSuggestOracle<T> getSuggestOracle();

	protected abstract String getSuggestBoxContent(T bean);

	protected abstract T createNewEditableInstance(String suggestBoxContent);

	protected abstract AbstractDetailsSearchFormTemplate<T> createDetailsSearchForm(HasFocus nextFocusableComponent);

	protected abstract String getErrorMessage();

	protected void customizeSearchForm(FormBuilder formBuilder) {
	}

	public AbstractSearchFormTemplate(HasFocus nextFocusableComponent) {
		forms = new DeckPanel();
		initWidget(forms);

		searchForm = new SearchForm();
		detailsForm = createDetailsSearchForm(nextFocusableComponent);
		FocusUtils.nextOnEnter(searchForm, nextFocusableComponent);

		forms.add(searchForm);
		forms.add(detailsForm);

		searchForm.addEditListener(new ClickListener() {
			public void onClick(Widget sender) {
				GWT.log("editInstance: " + editInstance, null);
				if (editInstance != null) {
					getDetailsForm().modelToView(editInstance, true);
					showDetailForm(true);
				}
			}
		});

		showSearchForm();
	}

	public T mergeViewToModel() {
		if (!validateView()) {
			GWT.log("validateView() falhou no mergeViewToModel()", null);
			return null;
		}

		if (forms.getVisibleWidget() == 0) {
			return editInstance;
		}

		return detailsForm.viewToModel(editInstance);
	}

	/**
	 * @param editionMode
	 * @return
	 */
	protected boolean fireDetailFormShowed(boolean editionMode) {
		for (SearchFormTransitionListener<T> listener : transitionListeners()) {
			if (!listener.beforeDetailFormShow(detailsForm, editionMode)) {
				return Boolean.FALSE;
			}
		}

		return Boolean.TRUE;
	}

	protected boolean fireSearchFormShowed() {
		for (SearchFormTransitionListener<T> listener : transitionListeners()) {
			if (!listener.beforeSearchFormShow(this)) {
				return Boolean.FALSE;
			}
		}
		return Boolean.TRUE;
	}

	public void addTransitionListener(SearchFormTransitionListener<T> transitionListener) {
		transitionListeners().add(transitionListener);
	}

	public void removeTransitionListener(SearchFormTransitionListener<T> transitionListener) {
		transitionListeners().remove(transitionListener);
	}

	private List<SearchFormTransitionListener<T>> transitionListeners() {
		if (transitionListeners == null) {
			transitionListeners = new ArrayList<SearchFormTransitionListener<T>>();
		}
		return transitionListeners;
	}

	public void addActionListener(SearchFormActionListener<T> actionListener) {
		actionListeners().add(actionListener);
	}

	public void removeActionListener(SearchFormActionListener<T> actionListener) {
		actionListeners().remove(actionListener);
	}

	public void setModel(final T model) {
		this.editInstance = model;

		searchForm.modelToView();
		showSearchForm();
	}

	public void showSearchForm() {
		if (fireSearchFormShowed()) {
			forms.showWidget(0);
		}
	}

	public void edit() {
		if (editInstance == null) {
			throw new IllegalStateException("Model is not ready!");
		}

		detailsForm.modelToView(editInstance, true);
		showDetailForm(true);
	}

	public void clear() {
		editInstance = null;
		searchForm.clear();
		detailsForm.clear();
		showSearchForm();
	}

	private List<SearchFormActionListener<T>> actionListeners() {
		if (actionListeners == null) {
			actionListeners = new ArrayList<SearchFormActionListener<T>>();
		}
		return actionListeners;
	}

	public void showDetailForm(final boolean editionMode) {
		if ((isEditionAllowed() || isInsertionAllowed()) && fireDetailFormShowed(editionMode)) {
			forms.showWidget(1);
		}
	}

	private void fireModelSelected(T bean) {
		for (SearchFormActionListener<T> listener : actionListeners()) {
			listener.onModelSelected(bean);
		}
	}

	private void fireNewEntry(T bean) {
		for (SearchFormActionListener<T> listener : actionListeners()) {
			listener.onNewEntry(bean);
		}
	}

	@Override
	public void addFocusListener(FocusListener listener) {
		searchForm.addFocusListener(listener);
		HasFocus[] focusSequence = getDetailsForm().getFocusSequence();
		if (focusSequence != null) {
			focusSequence[focusSequence.length - 1].addFocusListener(listener);
		}
	}

	@Override
	public void removeFocusListener(FocusListener listener) {
		searchForm.removeFocusListener(listener);
	}

	public class SearchForm extends FocusComposite implements BeanSuggestBoxListener<T> {

		protected final BeanSuggestBox<T> suggestBox;

		private GroupFormItem<Widget> suggestFormItem;

		private Hyperlink editLink;

		private boolean insertionAllowed = false;

		private boolean editionAllowed = false;

		public void setInsertionAllowed(boolean editable) {
			this.insertionAllowed = editable;
		}

		public void addEditListener(ClickListener listener) {
			editLink.addClickListener(listener);
		}

		public SearchForm() {
			suggestBox = new BeanSuggestBox<T>(getSuggestOracle(), this);
			editLink = new Hyperlink("Editar", "");
			editLink.setVisible(false);

			suggestFormItem = new GroupFormItem<Widget>(getSuggestBoxLabel(), new Widget[] { suggestBox, editLink },
					getSuggestBoxHint(), false, true);

			FormBuilder formBuilder = new FormBuilder();
			formBuilder.add(suggestFormItem);
			customizeSearchForm(formBuilder);
			initWidget(formBuilder.toPanel());
			setInsertionAllowed(false);

			modelToView();
		}

		public void clear() {
			suggestFormItem.clear();
		}

		void modelToView() {
			if (editInstance == null) {
				suggestBox.getSuggestBox().setText(null);
			}
			else {
				editLink.setVisible(isEditionAllowed());
				suggestBox.getSuggestBox().setText(getSuggestBoxContent(editInstance));
			}

			suggestBox.setFocus(true);
		}

		public void onBeanSelect(final T result) {
			editInstance = result;
			fireModelSelected(result);
			editLink.setVisible(isEditionAllowed());
		}

		public void onNewEntry(final String value) {
			if (isInsertionAllowed()) {
				editInstance = createNewEditableInstance(value);
				detailsForm.modelToView(editInstance, false);
				fireNewEntry(editInstance);
				showDetailForm(false);
			}
		}

		public void onKeyPress(Widget sender, char keyCode, int modifiers) {
			if (keyCode != KeyboardListener.KEY_ENTER) {
				editInstance = null;
				editLink.setVisible(false);
			}
		}

		@Override
		public void setFocus(boolean op) {
			suggestBox.setFocus(op);
		}

		@Override
		public void addFocusListener(FocusListener listener) {
			suggestBox.addFocusListener(listener);
		}

		@Override
		public void addKeyboardListener(KeyboardListener listener) {
			suggestBox.addKeyboardListener(listener);
		}

		@Override
		public void removeFocusListener(FocusListener listener) {
			suggestBox.removeFocusListener(listener);
		}

		@Override
		public void removeKeyboardListener(KeyboardListener listener) {
			suggestBox.removeKeyboardListener(listener);
		}

		public void setText(String value) {
			suggestBox.setText(value);
		}

		public void setSuggestLabel(String label) {
			suggestFormItem.getLabel().setText(label);
		}

		public void showSuggestions() {
			suggestBox.showSuggestions();
		}

		private boolean hasText() {
			return suggestBox.getText().trim().length() > 0;
		}

		public boolean isInsertionAllowed() {
			return insertionAllowed && hasText();
		}

		public boolean isEditionAllowed() {
			return editionAllowed;
		}

		public void setEditionAllowed(boolean editionAllowed) {
			this.editionAllowed = editionAllowed;
		}

		public boolean isEnabled() {
			return suggestBox.isEnabled();
		}

		public void setEnabled(boolean enabled) {
			suggestBox.setEnabled(enabled);
		}

	}

	public void setText(String value) {
		searchForm.setText(value);
	}

	public AbstractDetailsSearchFormTemplate<T> getDetailsForm() {
		return detailsForm;
	}

	@Override
	public void setFocus(boolean op) {
		searchForm.setFocus(op);
	}

	public SearchForm getSearchForm() {
		return searchForm;
	}

	public void setEnabled(boolean editable) {
		searchForm.setEnabled(editable);
	}

	public boolean isEnabled() {
		return searchForm.isEnabled();
	}

	public boolean isInsertionAllowed() {
		return searchForm.isInsertionAllowed();
	}

	public void setInsertionAllowed(boolean insertionAllowed) {
		searchForm.setInsertionAllowed(insertionAllowed);
	}

	public boolean isEditionAllowed() {
		return searchForm.isEditionAllowed();
	}

	public void setEditionAllowed(boolean editionAllowed) {
		searchForm.setEditionAllowed(editionAllowed);
	}

	public boolean validateView() {
		switch (forms.getVisibleWidget()) {
		case 0:
			return editInstance != null;
		case 1:
			return detailsForm.validateView();
		}

		return false;
	}

	private FormItem<AbstractSearchFormTemplate<T>> formItem;

	public void validate(AbstractSearchFormTemplate<T> widget, ValidatorChain<AbstractSearchFormTemplate<T>> chain) {
		boolean valid = validateView();
		if (valid) {
			chain.doChain(widget);
		}
		else {
			formItem.setErrorMessage(getInvalidValueMessage());
		}
		formItem.setValid(valid);
		formItem.refresh();
	}

	public String getInvalidValueMessage() {
		return "Campo obrigatório";
	}

	public void setFormItem(FormItem<AbstractSearchFormTemplate<T>> formItem) {
		this.formItem = formItem;
	}

	public T getEditInstance() {
		if (validateView()) {
			switch (forms.getVisibleWidget()) {
			case 0:
				return editInstance;
			case 1:
				return mergeViewToModel();
			}
		}
		return null;
	}

}
