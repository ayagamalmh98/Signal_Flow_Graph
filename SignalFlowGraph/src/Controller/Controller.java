package Controller;

import Model.SignalFlowGraph;
import Model.Edge;
import Model.Node;
import Model.SignalFlowGraph;

import java.util.ArrayList;

public class Controller {
    public SignalFlowGraph getSignalFlowGraph() {
        return signalFlowGraph;
    }

    private SignalFlowGraph signalFlowGraph;


    public String getFunction(ArrayList<Node> nodes, ArrayList<Edge> edges) {

        signalFlowGraph = new SignalFlowGraph();
        float transferFunction = signalFlowGraph.getTransferFunction(nodes, edges);
        return String.valueOf(transferFunction);

    }
}
