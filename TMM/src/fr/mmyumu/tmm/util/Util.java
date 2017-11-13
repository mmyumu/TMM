package fr.mmyumu.tmm.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Util {
	public static final NumberFormat AMOUNT_FORMATTER = new DecimalFormat("#0.00");
	public static final BigDecimal POSITIVE_THRESHOLD = new BigDecimal("0.01");
	public static final BigDecimal NEGATIVE_THRESHOLD = new BigDecimal("-0.01");
}
