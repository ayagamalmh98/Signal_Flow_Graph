package GUI;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;
import com.mxgraph.swing.mxGraphComponent;
import Model.Node;
import Model.Edge;
import Model.Path;
import Controller.Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;
import java.util.Hashtable;
import java.awt.*;
import javax.swing.*;


public class GUI extends JFrame {
    ArrayList<Node> nodes;
    ArrayList<Object> graphNodes;
    mxGraph graph = new mxGraph();
    Object parent = graph.getDefaultParent();
    ArrayList<Edge> edges = new ArrayList<>();
    Controller controller = new Controller();
    private JPanel panel;
    private JPanel panelGraph;

    private JButton btnAddNode;
    private JButton btnAddEdge;
    private JButton btnSolve;

    private JLabel startlabel;
    private JLabel toLabel;
    private JLabel weightLabel;

    private JTextField txtStartNode;
    private JTextField txtEndNode;
    private JTextField txtWeight;

    private int numOfNodes = 0;
    private JPanel result;
    private JTable forwardPathsTable;
    private JTable LoopsTable;
    private JTable deltasTable;


    public GUI() {
        nodes = new ArrayList<>();
        graphNodes = new ArrayList<>();
        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        graphComponent.setBounds(0, 0, 300, 300);
        panelGraph.add(graphComponent);

        mxStylesheet myStyleSheet = graph.getStylesheet();
        Hashtable<String, Object> style = new Hashtable<String, Object>();
        style.put(mxConstants.STYLE_FILLCOLOR,"#f99dad");
        style.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RECTANGLE);
        style.put(mxConstants.STYLE_FONTCOLOR, "#774400");
        style.put(mxConstants.STYLE_FONTSIZE,18);
        style.put(mxConstants.STYLE_RESIZABLE,true);
        style.put(mxConstants.STYLE_IMAGE_BORDER, "#774400");
        myStyleSheet.putCellStyle("style", style);


        btnAddNode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                numOfNodes++;
                graphNodes.add(graph.insertVertex(parent, null, numOfNodes, (numOfNodes - 1) * 300, 200, 80,
                        30, "style"));
                Node x = new Node(numOfNodes);
                nodes.add(x);
            }

        });

        btnAddEdge.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int startIndex = Integer.valueOf(txtStartNode.getText()) - 1;
                int endIndex = Integer.valueOf(txtEndNode.getText()) - 1;
                int weight = Integer.valueOf(txtWeight.getText());
                if (endIndex - startIndex != 1) {
                    graph.insertEdge(parent, null, weight, graphNodes.get(startIndex),
                            graphNodes.get(endIndex), "strokeColor=#0057e7;fillColor=#0057e7;fontSize=18");
                }
                if (endIndex - startIndex == 1)
                    graph.insertEdge(parent, null, weight, graphNodes.get(startIndex), graphNodes.get(endIndex),"fontSize=18;strokeColor=#f50505;fillColor=#f50505");

                Node startNode = nodes.get(startIndex);
                Node endNode = nodes.get(endIndex);
                startNode.getForwardReferences().add(endNode);
                Edge edge = new Edge(startNode, endNode, weight);
                edges.add(edge);
            }
        });



        btnSolve.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String result =  controller.getFunction(nodes, edges);
                GUI.this.result = new JPanel();
                ArrayList<Path> paths = controller.getSignalFlowGraph().getForwardPaths();
                ArrayList<Path> loops = controller.getSignalFlowGraph().getLoops();
                Object[][] data1 = getData(paths);
                String[] columnNames1 = {"Forward Paths", "Gain"};
                forwardPathsTable = new JTable(data1, columnNames1);
                JScrollPane scrollPane1 = new JScrollPane(forwardPathsTable);
                String[] columnNames2 = {"Loops", "Gain"};
                Object[][] data2 = getData(loops);
                LoopsTable = new JTable(data2 , columnNames2);
                JScrollPane scrollPane2 = new JScrollPane(LoopsTable);

                ArrayList<Float> deltas = controller.getSignalFlowGraph().getDeltas() ;
                Float[][] Deltas=new Float[deltas.size()][2];
                for (int i = 0; i < deltas.size() ; i++) {
                    Deltas[i][0]=(float)i;
                    Deltas[i][1]=deltas.get(i);
                }
                String[] columnNames3 = {"i", "Delta"};
                deltasTable=new JTable(Deltas,columnNames3);
                JScrollPane scrollPane3 = new JScrollPane(deltasTable);
                GUI.this.result.add(scrollPane1);
                GUI.this.result.add(scrollPane2);
                GUI.this.result.add(scrollPane3);
                GUI.this.result.add(new Label("\n"+" Transfer Function = " + result));
                JOptionPane.showMessageDialog(null, GUI.this.result,"Solution",JOptionPane.INFORMATION_MESSAGE);
            }
        });

    }

    private Object[][] getData(ArrayList<Path> paths) {
        Object[][] data = new Object[paths.size()][2];
        int i = 0 ;
        for (Path path:paths) {
            String name = "";
            for (Node node:path.getNodes()) {
                name += String.valueOf(node.getIndex());
            }
            Object[] object = new Object[2];
            object[0] = name;
            object[1] = path.getGain();
            data[i] = object ;
            i++;
        }
        return data;
    }

    public static void main(String[] args) {
        GUI frame = new GUI();
        frame.setContentPane(new GUI().panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(Color.lightGray);
        frame.pack();
        frame.setVisible(true);
    }

}
