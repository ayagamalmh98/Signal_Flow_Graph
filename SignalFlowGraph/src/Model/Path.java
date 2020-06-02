package Model;

import Model.Edge;
import Model.Node;
import Model.Path;

import java.util.ArrayList;

public class Path  {
    private Node start ;
    private Node end ;
    private ArrayList<Node> nodes ;
    private ArrayList<Edge> edges ;
    private float gain ;

    public Path(Node start, ArrayList<Edge> edges) {
        this(start , start , edges);

    }

    private void setGain() {
        this.gain = 1;

        for (Edge edge : edges) {
            this.gain *= edge.getGain();
        }

    }

    public Path(Node start, Node end, ArrayList<Edge> edges) {
        this(start , end , new ArrayList<>() , edges);

    }

    private ArrayList<Node> addNodes(ArrayList<Edge> edges) {
        this.nodes = new ArrayList<>();
        for (Edge edge:edges) {
            nodes.add(edge.getEnd());
        }
        return nodes;
    }

    public Path(Node start, Node end, ArrayList<Node> nodes, ArrayList<Edge> edges) {
        this.start = start;
        this.end = end;
        this.edges = edges;
        if(nodes.size() < 1){
            nodes = this.addNodes(edges);
        }
        this.nodes = nodes;
        setGain();
    }

    public  Path(){
        this(null , null , new ArrayList<Node>() , new ArrayList<Edge>());
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public void setNodes(ArrayList<Node> nodes) {
        this.nodes = nodes;
    }

    public void addEdges(ArrayList<Edge> allEdges) {
        this.edges = new ArrayList<>();
        for (int i = 0; i < nodes.size() - 1; i++) {
            Edge edge = getTheEdge(nodes.get(i), nodes.get(i + 1), allEdges);
            if (edge != null)
                this.edges.add(edge);
        }
        setGain();
    }

    private Edge getTheEdge(Node iNode, Node iNode1, ArrayList<Edge> allEdges) {
        for (Edge edge : allEdges) {
            if (edge.getStart().equals(iNode) && edge.getEnd().equals(iNode1)) {
                return edge;
            }
        }
        return null;

    }

    public Node getStart() {

        return start;
    }

    public Node getEnd() {

        return end;
    }

    public float getGain() {

        return this.gain;
    }

    public ArrayList getEdges() {

        return edges ;
    }

    public void addNode(Node node){

        nodes.add(node);
    }

    public void setStart(Node start) {

        this.start = start;
    }

    public void setEnd(Node end) {

        this.end = end;
    }
}
