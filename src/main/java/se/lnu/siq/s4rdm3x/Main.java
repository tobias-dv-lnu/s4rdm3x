package se.lnu.siq.s4rdm3x;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerPipe;
import se.lnu.siq.s4rdm3x.cmd.*;
import se.lnu.siq.s4rdm3x.cmd.saerocon18.*;
import se.lnu.siq.s4rdm3x.cmd.util.AttributeUtil;
import se.lnu.siq.s4rdm3x.cmd.LoadArch;
import se.lnu.siq.s4rdm3x.cmd.util.Selector;
import se.lnu.siq.s4rdm3x.cmd.util.SelectorBuilder;
import se.lnu.siq.s4rdm3x.dmodel.dmClass;

import java.util.*;

public class Main {



    public static void main(String[] a_args) {
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");


        //C.io.setTitle("Hello World");
        GUIConsole guic = new GUIConsole();

        Graph graph = new MultiGraph("main_graph");
        graph.addAttribute("ui.antialias");
        graph.addAttribute("ui.quality", 4);
        graph.setAttribute("ui.stylesheet", "url(data/style.css);");
        graph.addAttribute("ui.title", "Graph");
        graph.addAttribute("layout.stabilization-limit", 1.0);


        Viewer view = graph.display();
        graph.setAttribute("view", view);


        ViewerPipe vp = view.newViewerPipe();
        vp.addViewerListener(new ClickListener(graph));
        vp.addSink(graph);

        /*{
            JabRef_3_5 jr = new JabRef_3_5();
            jr.load(graph);
            jr.createAndMapArch(graph);
        }*/


        while (true) {
            try {
                vp.pump();
                Thread.sleep(10);

                //view.
                //view.disableAutoLayout();
                /*for (Edge e : graph.getEachEdge()) {
                    if (!e.hasAttribute("layoutEdge")) {
                        GraphicEdge ge = view.getGraphicGraph().getEdge(e.getId());
                        GraphicNode g1, g2;
                        g1 = ge.getNode0();
                        g2 = ge.getNode1();

                        double x1 = g1.getX();
                        double y1 = g1.getY();
                        double x2 = g2.getX();
                        double y2 = g2.getY();
                        double len = Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2)) * 0.99;
                        if (len < 7) {
                            len = 7;
                        }
                        e.setAttribute("layout.weight", len);
                    }
                }*/
                //view.enableAutoLayout();

                if (guic.hasInput()) {

                    String in = guic.popInput();
                    try {

                        if (in.startsWith("set_spring_weight")) {
                            String[] cargs = in.split(" ");

                            SetSpringWeight c = new SetSpringWeight(Arrays.copyOfRange(cargs, 1, cargs.length - 1), Float.parseFloat(cargs[cargs.length - 1]));
                            c.run(graph);
                        } else if (in.startsWith("show") || in.startsWith("hide")) {
                            //String[] cargs = in.split(" ");
                            SelectorBuilder bs = new SelectorBuilder();
                            Selector.ISelector s = bs.buildFromString(in.substring(in.indexOf(" ")));
                            ShowNode c = new ShowNode(s, in.startsWith("show"));
                            c.run(graph);
                        } else if (in.startsWith("add_edges")) {
                            String[] cargs = in.split(" ");
                            SelectorBuilder bs = new SelectorBuilder();
                            Selector.ISelector s = bs.buildFromString(guic.join(cargs, 3, " "));
                            AddEdges c = new AddEdges(cargs[1], Float.parseFloat(cargs[2]), s);
                            c.run(graph);

                        } else if (in.startsWith("load_jar")) {
                            String[] cargs = in.split(" ");
                            String rootPkg = "";
                            if (cargs.length > 2) {
                                rootPkg = cargs[2];
                            }
                            LoadJar c = new LoadJar(cargs[1], rootPkg);
                            c.run(graph);
                        } else if (in.compareTo("clear_graph") == 0) {

                            graph.clear();
                            //view.getGraphicGraph().clear();

                            graph.addAttribute("ui.antialias");
                            graph.addAttribute("ui.quality", 4);
                            graph.setAttribute("ui.stylesheet", "url(data/style.css);");
                            graph.addAttribute("ui.title", "Graph");
                            graph.addAttribute("layout.stabilization-limit", 1.0);
                            graph.setAttribute("view", view);


                            //vp.addSink(graph);



                            /*graph = new MultiGraph("main");

                            graph.addAttribute("ui.antialias");
                            graph.addAttribute("ui.quality");
                            graph.setAttribute("ui.stylesheet", "url(data/style.css);");
                            graph.addAttribute("ui.title", "Graph");


                            view = graph.display();
                            graph.setAttribute("view", view);

                            vp = view.newViewerPipe();
                            vp.addViewerListener(new ClickListener(graph));
                            vp.addSink(graph);*/

                        } else if (in.startsWith("add_relations")) {
                            String[] cargs = in.split(" ");
                            String selection = guic.join(cargs, 3, " ");
                            String[] selections = selection.split("\\|");

                            SelectorBuilder bs = new SelectorBuilder();
                            Selector.ISelector from = bs.buildFromString(selections[0]);
                            Selector.ISelector to = bs.buildFromString(selections[1]);

                            AddRelationEdges c = new AddRelationEdges(cargs[1], Float.parseFloat(cargs[2]), from, to);
                            c.run(graph);

                        } else if (in.startsWith("add_node_tag_rnd")) {
                            String[] cargs = in.split(" ");
                            SelectorBuilder bs = new SelectorBuilder();
                            Selector.ISelector s = bs.buildFromString(guic.join(cargs, 3, " "));
                            AddNodeTagRandom c = new AddNodeTagRandom(cargs[1], s, Double.parseDouble(cargs[2]));
                            c.run(graph);
                        } else if (in.startsWith("add_node_tag")) {
                            String[] cargs = in.split(" ");
                            SelectorBuilder bs = new SelectorBuilder();
                            Selector.ISelector s = bs.buildFromString(guic.join(cargs, 2, " "));
                            AddNodeTag c = new AddNodeTag(cargs[1], s);
                            c.run(graph);
                        } else if (in.startsWith("set_attr")) {
                            String[] cargs = in.split(" ");
                            SelectorBuilder bs = new SelectorBuilder();
                            Selector.ISelector s = bs.buildFromString(guic.join(cargs, 3, " "));
                            SetAttr c = new SetAttr(cargs[1], cargs[2], s);
                            c.run(graph);
                        } else if (in.startsWith("delete")) {
                            SelectorBuilder bs = new SelectorBuilder();
                            Selector.ISelector s = bs.buildFromString(in.substring(in.indexOf(" ")));
                            DeleteNode c = new DeleteNode(s);
                            c.run(graph);
                        } else if (in.startsWith("contract")) {
                            SelectorBuilder bs = new SelectorBuilder();
                            Selector.ISelector s = bs.buildFromString(in.substring(in.indexOf(" ")));
                            ContractNode c = new ContractNode(true, s);
                            c.run(graph);
                        } else if (in.startsWith("expand")) {
                            SelectorBuilder bs = new SelectorBuilder();
                            Selector.ISelector s = bs.buildFromString(in.substring(in.indexOf(" ")));
                            ContractNode c = new ContractNode(false, s);
                            c.run(graph);
                        } else if (in.startsWith("set_edge_attr")) {
                            String[] cargs = in.split(" ");
                            SetEdgeAttr c;
                            if (cargs.length == 3) {
                                c = new SetEdgeAttr(cargs[1], null, cargs[2]);
                            } else {
                                c = new SetEdgeAttr(cargs[1], cargs[2], cargs[3]);
                            }
                            c.run(graph);
                        } else if (in.startsWith("cluster_1")) {
                            String[] cargs = in.split(" ");
                            Cluster1 c = new Cluster1(Double.parseDouble(cargs[1]), Double.parseDouble(cargs[2]), true);
                            c.run(graph);
                            guic.println("Considered Nodes: " + c.m_consideredNodes);
                            guic.println("Automatically Mapped Nodes: " + c.m_automaticallyMappedNodes);
                            guic.println("Manually Mapped Nodes: " + c.m_manuallyMappedNodes);
                            guic.println("Nodes with wrong suggestions: " + c.m_failedMappings);


                        } else if (in.startsWith("count_n")) {
                            SelectorBuilder bs = new SelectorBuilder();
                            Selector.ISelector s = bs.buildFromString(in.substring(in.indexOf(" ")));
                            CountNodes c = new CountNodes(s);
                            c.run(graph);

                            guic.println("Nodes: " + c.m_count);
                        } else if (in.startsWith("export")) {

                            String[] cargs = in.split(" ");
                            Selector.ISelector s = new Selector.All();
                            if (cargs.length > 2) {
                                SelectorBuilder bs = new SelectorBuilder();
                                s = bs.buildFromString(guic.join(cargs, 2, " "));
                            }

                            Exporter c = new Exporter(cargs[1], s);
                            c.run(graph);

                        } else if (in.startsWith("load_arch")) {
                            String[] cargs = in.split(" ");
                            LoadArch la = new LoadArch(cargs[1]);
                            la.run(graph);
                        } else if (in.startsWith("print_nodes")) {
                            String[] cargs = in.split(" ");
                            Selector.ISelector s = new Selector.All();
                            if (cargs.length > 1) {
                                SelectorBuilder bs = new SelectorBuilder();
                                s = bs.buildFromString(guic.join(cargs, 1, " "));
                            }

                            GetNodes c = new GetNodes(s);
                            c.run(graph);
                            AttributeUtil au = new AttributeUtil();
                            for (Node n : c.m_nodes) {
                                guic.println(au.getName(n));
                                guic.println("\ttags: " + au.getTags(n));
                                String classesStr = "";
                                for (dmClass dmc: au.getClasses(n)) {
                                    if (classesStr.length() > 0) {
                                        classesStr += ", ";
                                    }
                                    classesStr += dmc.getName();
                                }
                                guic.println("\tclasses: " + classesStr);
                            }
                        }
                        else if (in.startsWith("//")) {
                            // skip comment
                        } else if (in.startsWith("info")) {

                            int classes = 0;
                            int nodes = 0;
                            AttributeUtil au = new AttributeUtil();
                            for (Node n : graph.getNodeSet()) {
                                classes += au.getClasses(n).size();
                                nodes++;
                            }
                            guic.println("Nodes: " + nodes);
                            guic.println("Classes: " + classes);
                        } else if (in.length() > 0) {
                            guic.println("Unknown command: " + in);
                        }
                    } catch(Exception e) {
                        guic.println("Something when wrong when executing command: " + in);
                        guic.println(e.toString());
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                guic.println(e.toString());
                e.printStackTrace();
            }
        }
    }

}