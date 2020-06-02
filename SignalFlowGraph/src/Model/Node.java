package Model;
import java.util.ArrayList;

public class Node  {

    private int index;
    private ArrayList<Node> forwardReferences;
    private boolean visited = false;

    public Node(int index) {
        this.index = index;
        this.forwardReferences = new ArrayList<>();
    }

    public boolean isVisited() {

        return visited;
    }

    public void setVisited(boolean visited) {

        this.visited = visited;
    }

    public int getIndex() {

        return index;
    }

    public void setIndex(int index) {

        this.index = index;
    }

    public ArrayList<Node> getForwardReferences() {

        return forwardReferences;
    }

    public void setForwardReferences(ArrayList<Node> forwardReferences) {

        this.forwardReferences = forwardReferences;
    }

    public Node addForwardReference(Node node) {
        this.forwardReferences.add(node);
        return this;
    }
}
