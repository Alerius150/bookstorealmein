
package com.fjordtek.bookstore.service;

import java.beans.PropertyEditorSupport;
import java.math.BigDecimal;



public class BigDecimalPropertyEditor extends PropertyEditorSupport {

	@Override
	public void setAsText(String input) {

		String[] decimalSeparators = {",", "."};

		for (int i = 0; i < decimalSeparators.length; i++) {

			if (input.contains(decimalSeparators[i])) {
				input = input.replace(decimalSeparators[i], ".");
				break;
			}
		}

		Number number = Double.parseDouble(input);
		BigDecimal bigDecimal = BigDecimal.valueOf(number.doubleValue());
		setValue(bigDecimal);
	}
}