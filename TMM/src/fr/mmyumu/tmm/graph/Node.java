package fr.mmyumu.tmm.graph;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import fr.mmyumu.tmm.model.Person;
import fr.mmyumu.tmm.util.Util;

public class Node {
	private Node parent;
	private final Map<Person, BigDecimal> personToBalance;
	private String action;
	private String owe;

	public Node(Node parent, Map<Person, BigDecimal> personToBalance, String action, String owe) {
		super();
		this.parent = parent;
		this.personToBalance = new HashMap<>(personToBalance);
		this.action = action;
		this.owe = owe;
	}

	public Node(Node parent, Map<Person, BigDecimal> personToBalance) {
		this(parent, personToBalance, null, null);
		this.action = "Initial state -> state: " + getState(personToBalance);
	}

	public List<Node> getNeighbors() {
		List<Node> neighbors = new ArrayList<>();

		Map<Person, BigDecimal> creditors = personToBalance.entrySet().stream()
				.filter(entry -> entry.getValue().compareTo(Util.POSITIVE_THRESHOLD) >= 0)
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue));

		Map<Person, BigDecimal> debitors = personToBalance.entrySet().stream()
				.filter(entry -> entry.getValue().compareTo(Util.NEGATIVE_THRESHOLD) <= 0)
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue));

		creditors.forEach((creditor, credit) -> {
			debitors.forEach((debitor, debit) -> {
				debit = debit.abs();
				BigDecimal amountToTransfer = debit.min(credit);
				Map<Person, BigDecimal> neighborPersonToBalance = new HashMap<>(personToBalance);

				neighborPersonToBalance.compute(creditor, (k, v) -> v.subtract(amountToTransfer));
				neighborPersonToBalance.compute(debitor, (k, v) -> v.add(amountToTransfer));

				String action = debitor.getFullName() + " gave " + Util.AMOUNT_FORMATTER.format(amountToTransfer)
						+ " to "
						+ creditor.getFullName() + " -> state: " + getState(neighborPersonToBalance);

				String owe = debitor.getFullName() + " owes " + Util.AMOUNT_FORMATTER.format(amountToTransfer) + " to "
						+ creditor.getFullName();

				neighbors.add(new Node(this, neighborPersonToBalance, action, owe));
			});
		});

		return neighbors;
	}

	private static String getState(Map<Person, BigDecimal> neighborPersonToBalance) {
		return neighborPersonToBalance.entrySet().stream()
				.map(entry -> entry.getKey().getFullName() + "=" + Util.AMOUNT_FORMATTER.format(entry.getValue()))
				.collect(Collectors.joining(", "));
	}

	public String getTrace() {
		if (parent != null) {
			return parent.getTrace() + "\n" + action;
		}

		return action;
	}

	public String getOweTrace() {
		if (parent != null) {
			String parentOweTrace = parent.getOweTrace();
			if(parentOweTrace != null) {
				return parentOweTrace + "\n" + owe;
			}
		}

		return owe;
	}

	public boolean isEmpty() {
		return personToBalance.isEmpty();
	}

	public boolean isOk() {
		return personToBalance.values().stream().allMatch(balance -> balance.compareTo(Util.POSITIVE_THRESHOLD) < 0 && balance.compareTo(Util.NEGATIVE_THRESHOLD) > 0);
	}
}
