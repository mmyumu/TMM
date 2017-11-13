package fr.mmyumu.tmm.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import fr.mmyumu.tmm.graph.BFS;
import fr.mmyumu.tmm.graph.Node;

public class ResultComputer {
	private final int SCALE = 100;
	private final Event event;

	private BigDecimal average;
	private Map<Person, BigDecimal> personToBalance;
	private String oweTrace;
	private String trace;

	public ResultComputer(Event event) {
		super();
		this.event = event;
	}

	public BigDecimal getAverage() {
		return average;
	}

	public Map<Person, BigDecimal> getPersonToBalance() {
		return personToBalance;
	}

	public String getOweTrace() {
		return oweTrace;
	}

	public String getTrace() {
		return trace;
	}

	public void compute() {
		Map<Person, BigDecimal> personToTotal = event.getPeople().stream()
				.collect(Collectors.toMap(person -> person,
						person -> {
							BigDecimal total = new BigDecimal(person.getExpenses().stream()
									.mapToDouble(expense -> expense.getAmount()).sum());
							total = total.setScale(SCALE, RoundingMode.HALF_UP);
							return total;
						}));

		BigDecimal total = personToTotal.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
		average = total.divide(new BigDecimal(personToTotal.size()), SCALE, RoundingMode.HALF_UP);

		personToBalance = personToTotal.entrySet().stream()
				.sorted((o1, o2) -> o1.getValue().compareTo(o2.getValue()))
				.collect(Collectors.toMap(Entry::getKey, entry -> {
					BigDecimal balance = entry.getValue().subtract(average);
					balance = balance.setScale(SCALE, RoundingMode.HALF_UP);
					return balance;
				}, (o1, o2) -> o1, LinkedHashMap::new));

		BFS bfs = new BFS(new Node(null, personToBalance));
		Node node = bfs.run();

		oweTrace = node.getOweTrace();
		trace = node.getTrace();
	}
}
