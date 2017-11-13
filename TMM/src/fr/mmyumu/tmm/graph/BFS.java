package fr.mmyumu.tmm.graph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

public class BFS {
	private final Node root;
	private List<Node> visitedNodes;

	public BFS(Node root) {
		super();
		this.visitedNodes = new ArrayList<>();
		this.root = root;
	}

	public Node run() {
		visitedNodes.clear();

		Queue<Node> queue = new LinkedList<>();
		queue.add(root);
		visitedNodes.add(root);

		while (!queue.isEmpty()) {
			Node node = queue.remove();

			if(node.isOk()) {
				return node;
			}
			List<Node> neighbors = node.getNeighbors().stream()
					.filter(neighbor -> !visitedNodes.contains(neighbor))
					.collect(Collectors.toList());

			neighbors.forEach(neighbor -> {
				visitedNodes.add(neighbor);
				queue.add(neighbor);
			});
		}
		return null;
	}
}
