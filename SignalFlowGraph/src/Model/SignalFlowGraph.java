package Model;

import Model.Edge;
import Model.Node;
import Model.Path;
import Model.SignalFlowGraph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class SignalFlowGraph  {

    private ArrayList<Path> loops;
    private ArrayList<Path> forwardPaths;
    private ArrayList<Node> nodes;
    private ArrayList<Edge> edges;
    private float TransferFunction;
    private ArrayList<ArrayList<Path>>[] untouchedLoops;

    public ArrayList<Float> getDeltas() {
        return deltas;
    }

    private ArrayList<Float> deltas ;

    public SignalFlowGraph()
    {
        loops = new ArrayList<>();

    }

    public float getTransferFunction(ArrayList<Node> Nodes, ArrayList edgesList) {
        this.edges = edgesList;
        TransferFunction = 0;
        this.nodes = Nodes;
        this.forwardPaths = this.getForwardPaths(Nodes.get(0), Nodes.get(Nodes.size() - 1));
        this.constructLoops(Nodes);
        this.untouchedLoops = this.getUntouchedLoops(loops);
        deltas = new ArrayList(forwardPaths.size());
        deltas.add(this.getDelta(untouchedLoops, loops));
        for (Path path : forwardPaths) {
            ArrayList<Path> pathLoops = this.getPathLoops(path);
            ArrayList<ArrayList<Path>>[] pathUntouchedLoops = this.getUntouchedLoops(pathLoops);
            deltas.add(this.getDelta(pathUntouchedLoops, pathLoops));
            TransferFunction += path.getGain() * deltas.get(deltas.size() - 1);
        }
        TransferFunction /= deltas.get(0);
        return TransferFunction;
    }

    public ArrayList<Path> getForwardPaths (Node startNode, Node endNode)
    {
        ArrayList<Path> forwardPaths = new ArrayList<>();
        getPaths(forwardPaths , startNode, endNode, new Stack());
        return forwardPaths ;
    }
    public ArrayList<Path> constructLoops(ArrayList<Node> listOfNodes){
        for(Node currentNode : listOfNodes){
            ArrayList<Node> forwardNodes = currentNode.getForwardReferences();
            for(Node nextNode: forwardNodes){
                if(nextNode.getIndex() <= currentNode.getIndex()){
                    ArrayList<Path> paths = getForwardPaths(nextNode, currentNode);
                    for (Path path : paths) {
                        path.addNode(path.getStart());
                        path.addEdges(this.edges);
                    }
                    loops.addAll(paths);
                }
            }
        }
        return  loops;
    }

    public ArrayList<ArrayList<Path>>[] getUntouchedLoops(ArrayList<Path> paths) {
        if (paths.size() < 1) return null;
        ArrayList[] untouchedLoops = new ArrayList[paths.size()];
        int iterations = (int) (Math.pow(2, paths.size()));
        boolean loopNotTouched = true;
        Set touched = new HashSet();
        int binary = 3;

        while (binary < iterations){

            loopNotTouched = true;
            String binaryReperestation = Integer.toBinaryString(binary);
            ArrayList<Integer> arrayOfIndexes = new ArrayList<>();
            for (int index = binaryReperestation.indexOf('1'); index >= 0;
                 index = binaryReperestation.indexOf('1', index + 1)) {
                arrayOfIndexes.add(binaryReperestation.length() - 1 - index);
            }
            touched.clear();
            for (int i = 0; i < arrayOfIndexes.size() && arrayOfIndexes.size() > 1; i++) {
                if (!insertPath(touched, paths.get(arrayOfIndexes.get(i)))) {
                    loopNotTouched = false;
                    break;
                }
            }

            if (loopNotTouched && arrayOfIndexes.size() > 1) {
                ArrayList arrayList1 = new ArrayList();
                for (int i = 0; i < arrayOfIndexes.size(); i++) {
                    arrayList1.add(paths.get(arrayOfIndexes.get(i)));
                }

                if (untouchedLoops[arrayOfIndexes.size() - 2] == null) {
                    untouchedLoops[arrayOfIndexes.size() - 2] = new ArrayList();
                }
                untouchedLoops[arrayOfIndexes.size() - 2].add(arrayList1);
            }

            binary++;
        }

        return untouchedLoops;
    }


    public float getDelta(ArrayList<ArrayList<Path>>[] loops, ArrayList<Path> allLoops) {
        if (allLoops.size() < 1) return 1;
        int loopsGain = 0;
        for (Path path : allLoops) {
            loopsGain += path.getGain();
        }
        float delta = 1 - loopsGain;
        int sign = -1;
        for (int i = 0; i < loops.length; i++) {
            if (loops[i] != null) {
                ArrayList<ArrayList<Path>> untouchedLoops = loops[i];
                int sum = 0;
                for (int j = 0; j < untouchedLoops.size(); j++) {
                    int multiply = 1;
                    ArrayList<Path> untouchedSet = untouchedLoops.get(j);
                    for (int k = 0; k < untouchedSet.size(); k++) {
                        multiply *= untouchedSet.get(k).getGain();
                    }
                    sum += multiply;
                }
                delta += (Math.pow(sign, i)) * sum;
            }
        }
        return delta;
    }


    private void getPaths(ArrayList<Path> forwardPaths , Node start, Node end, Stack currentPath)
    {
        start.setVisited(true);
        if(currentPath.isEmpty()) currentPath.push(start);
        if(start == end){
            Path path = new Path();
            ArrayList<Node> pathNodes = new ArrayList<>(currentPath);
            path.setNodes(pathNodes );
            path.addEdges(this.edges);
            path.setStart(pathNodes.get(0));
            path.setEnd(end);
            forwardPaths.add(path);
            start.setVisited(false);
            return;
        }
        for(Node child : start.getForwardReferences()){
            if(!child.isVisited()){
                currentPath.push(child);
                getPaths(forwardPaths , child, end, currentPath);
                currentPath.pop();
                child.setVisited(false);
            }
        }
        start.setVisited(false);
    }
    private ArrayList<Path> getPathLoops(Path forwardPath) {
        boolean unTouched;
        Set pathNodes = new HashSet();
        ArrayList<Path> untouchedLoops = new ArrayList<>();
        for (Node node : forwardPath.getNodes()) {
            pathNodes.add(node);
        }
        for (Path loop : loops) {
            Set Nodes = new HashSet(pathNodes);
            unTouched = true;
            for (int i = 0 ; i < loop.getNodes().size() - 1 ; i++) {
                if (!Nodes.add(loop.getNodes().get(i))) {
                    unTouched = false;
                    break;
                }
            }
            if (unTouched) {
                untouchedLoops.add(loop);
            }
        }

        return untouchedLoops;
    }

    private boolean insertPath(Set touched, Path path)
    {
        ArrayList<Node> nodes = path.getNodes();
        for (int i = 0; i < nodes.size() - 1; i++) {
            if (!touched.add(nodes.get(i))) {
                return false;
            }
        }
        return true;
    }



    public ArrayList<Path> getLoops() {

        return loops;
    }

    public ArrayList<Path> getForwardPaths() {

        return forwardPaths;
    }

    public float getOverAllTrasferFun() {

        return TransferFunction;
    }
    public ArrayList<ArrayList<Path>>[] getUntouchedLoops() {

        return untouchedLoops;
    }

}
