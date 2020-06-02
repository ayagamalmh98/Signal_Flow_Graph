package Model;

public class Edge  {

    private Node start;
    private Node end;
    private int gain;

    public Edge(Node start, Node end, int gain) {
        this.start = start;
        this.end = end;
        this.gain = gain;
    }

    public Node getStart() {
        return start;
    }

    public Node getEnd() {
        return end;
    }

    public int getGain() {

        return gain;
    }
}



