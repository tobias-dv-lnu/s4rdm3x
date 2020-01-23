import archviz.HRoot;
import experimenting.ExperimentsView;
import glm_.vec2.Vec2;
import glm_.vec2.Vec2i;
import glm_.vec4.Vec4;
import gui.ImGuiWrapper;
import imgui.*;
import imgui.impl.ImplGL3;
import imgui.impl.LwjglGlfw;
import imgui.internal.Rect;
import kotlin.Unit;
import mapping.MappingView;
import se.lnu.siq.s4rdm3x.GUIConsole;
import se.lnu.siq.s4rdm3x.StringCommandHandler;
import se.lnu.siq.s4rdm3x.model.Selector;
import se.lnu.siq.s4rdm3x.model.cmd.mapper.ArchDef;
import se.lnu.siq.s4rdm3x.model.CGraph;
import se.lnu.siq.s4rdm3x.model.CNode;
import se.lnu.siq.s4rdm3x.model.cmd.mapper.MapNode;
import uno.glfw.GlfwWindow;
import uno.glfw.windowHint;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.system.MemoryUtil.NULL;

public class RectsAndArrows {

    // The window handle
    private GlfwWindow window;
    private uno.glfw.glfw glfw = uno.glfw.glfw.INSTANCE;
    private LwjglGlfw lwjglGlfw = LwjglGlfw.INSTANCE;
    private ImplGL3 implGL3 = ImplGL3.INSTANCE;
    private ImGui imgui = ImGui.INSTANCE;
    private IO io;
    private Context ctx;



    public static void main(String[] a_args) {
        RectsAndArrows pt = new RectsAndArrows();

        pt.run();
    }

    private RectsAndArrows() {
        glfw.init("3.3", windowHint.Profile.core, true);

        window = new GlfwWindow(1280, 720, "Dear ImGui Lwjgl OpenGL3 example", NULL, new Vec2i(Integer.MIN_VALUE), true);
        window.init(true);

        glfw.setSwapInterval(1);    // Enable vsync

        // Setup ImGui binding
        //setGlslVersion(330); // set here your desidered glsl version
        ctx = new Context(null);
        //io.configFlags = io.configFlags or ConfigFlag.NavEnableKeyboard  // Enable Keyboard Controls
        //io.configFlags = io.configFlags or ConfigFlag.NavEnableGamepad   // Enable Gamepad Controls
        lwjglGlfw.init(window, true, LwjglGlfw.GlfwClientApi.OpenGL);

        TextEditState tes = new TextEditState();
        //tes.setBufSizeA();
        ctx.getInputTextState().setBufSizeA(2048);
        io = imgui.getIo();

        imgui.styleColorsDark(null);
    }

    private void run() {

        GUIConsole guic = new GUIConsole();
        StringCommandHandler sch = new StringCommandHandler();
        CGraph graph = new CGraph();

        // testing
        //sch.execute("load_arch data/systems/teammates/teammates-system_model.txt", graph).forEach(str -> guic.println(str));

        ArchDef theArch = new ArchDef();
        //theArch.addComponent("client.part1.part1_1");

        //theArch.addComponent("global");
        theArch.addComponent("client.part1");
        theArch.addComponent("client.part2");
        theArch.addComponent("server.part3");
        theArch.addComponent("server.part4");




        //theArch.addComponent("client");


        /*theArch.addComponent("optional");
        theArch.addComponent("compilers");
        theArch.addComponent("condition");
        theArch.addComponent("rmic");
        theArch.addComponent("cvslib");
        theArch.addComponent("email");
        theArch.addComponent("repository");
        theArch.addComponent("taskdefs");
        theArch.addComponent("listener");
        theArch.addComponent("types");*/
        //theArch.addComponent("ant");
        //theArch.addComponent("ant.util");
        /*theArch.addComponent("zip");
        theArch.addComponent("tar");
        theArch.addComponent("mail");
        theArch.addComponent("bzip2");*/



        try {
            FontConfig fc = new FontConfig();
            byte[] bytes;
            Class c = getClass();
            ClassLoader cl = c.getClassLoader();
            InputStream is = cl.getResourceAsStream("imgui/src/main/resources/fonts/Roboto-Medium.ttf");
            bytes =  Files.readAllBytes(Paths.get("data/visuals/fonts/Roboto-Medium.ttf"));
            char [] chars = new char[bytes.length];
            for (int ix = 0; ix < chars.length; ix++) {
                chars[ix] = (char)bytes[ix];
            }

            Font f = imgui.getIo().getFonts().addFontFromMemoryTTF(chars, 24, fc,new int[] {0x0020, 0x00FF} );
            imgui.setCurrentFont(f);
           // System.out.println("" + bytes[0]);
        } catch (Exception e) {
            System.out.println(e);
        }

       archviz.Command c = new archviz.Command(m_vizState);
       sch.addCommand(c);


        //sch.execute("load_arch_visuals data/slask/jabref_visuals.xml", graph).forEach(str -> guic.println(str));
        //sch.execute("load_jar data/systems/JabRef/3.7/JabRef-3.7.jar net.sf.jabref.", graph).forEach(str -> guic.println(str));
        //sch.execute("load_arch data/slask/jabref.txt", graph).forEach(str -> guic.println(str));



        //sch.execute("load_arch_visuals data/slask/jabref_visuals.xml", graph).forEach(str -> guic.println(str));
        //sch.execute("load_jar data/systems/ProM6.9/Pr3.7.jar net.sf.jabref.", graph).forEach(str -> guic.println(str));
        //sch.execute("load_arch data/slask/jabref.txt", graph).forEach(str -> guic.println(str));

        /*ProM69_dist/ProM-Contexts-6.9.56.jar
        ProM69_dist/ProM-Framework-6.9.97.jar
        ProM69_dist/ProM-Models-6.9.32.jar
        ProM69_dist/ProM-Models-6.9.32.jar*/

       /* sch.execute("load_jar data/systems/ProM6.9/ProM69_dist/ProM-Contexts-6.9.56.jar", graph).forEach(str -> guic.println(str));
        sch.execute("load_jar data/systems/ProM6.9/ProM69_dist/ProM-Framework-6.9.97.jar", graph).forEach(str -> guic.println(str));
        sch.execute("load_jar data/systems/ProM6.9/ProM69_dist/ProM-Models-6.9.32.jar", graph).forEach(str -> guic.println(str));
        sch.execute("load_jar data/systems/ProM6.9/ProM69_dist/ProM-Plugins-6.9.67.jar", graph).forEach(str -> guic.println(str));

        sch.execute("load_arch_visuals data/slask/prom_visuals.xml", graph).forEach(str -> guic.println(str));
        sch.execute("load_arch data/systems/ProM6.9/ProM_6_9.txt", graph).forEach(str -> guic.println(str));
*/

        //sch.execute("compute_metrics", graph).forEach(str -> guic.println(str));


        window.loop(() -> {

            if (guic.hasInput()) {

                String in = guic.popInput();
                sch.execute(in, graph).forEach(str -> guic.println(str));
            }

            mainLoop(sch.getArchDef() != null ? sch.getArchDef() : theArch, graph);

            return Unit.INSTANCE;
        });

        guic.close();
        lwjglGlfw.shutdown();
        ContextKt.destroy(ctx);

        window.destroy();
        glfw.terminate();
    }

    private float[] f = {1f};
    private Vec4 clearColor = new Vec4(0.45f, 0.55f, 0.6f, 1f);
    private boolean[] showArchitecture = {true};
    private boolean[] showTreeView = {true};
    private boolean[] showGraphView = {false};
    private boolean[] showHuGMeView = {true};
    private boolean[] showExperimentView = {true};
    private int[] counter = {0};

    private TreeView m_treeView = new TreeView();
    private GraphView m_graphView = new GraphView();
    private MappingView m_mappingView = new MappingView();
    private ExperimentsView m_experimentsView = new ExperimentsView();



    HRoot.State m_vizState = new HRoot.State();

    private void getRectTopLeft(int a_index, Vec2 a_offset, Vec2 a_size, Vec2 a_outTopLeftCorner) {
        a_offset.plus(a_size.times(a_index, a_outTopLeftCorner), a_outTopLeftCorner);
    }

    private boolean equalToLevel(final int a_level, ArchDef.Component a_c1, ArchDef.Component a_c2) {
        String [] names1 = getLevels(a_c1);
        String [] names2 = getLevels(a_c2);
        if (a_level < names1.length && a_level < names2.length) {
            for (int ix = 0; ix <= a_level; ix++) {
                if (!names1[ix].contentEquals(names2[ix])) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private String [] getLevels(ArchDef.Component a_c) {
        return a_c.getName().split("\\.");
    }

    private void mainLoop(ArchDef a_arch, CGraph a_g) {
        // Start the Dear ImGui frame
        lwjglGlfw.newFrame();

        boolean [] showDemo = {true};
        imgui.showDemoWindow(showDemo);

        imgui.text("Hello, world!");                                // Display some text (you can use a format string too)
        imgui.sliderFloat("float", f, 0.25f, 5f, "%.3f", 1f);       // Edit 1 float using a slider from 0.0f to 1.0f
        imgui.getFont().setScale(f[0]);
        imgui.colorEdit3("clear color", clearColor, 0);               // Edit 3 floats representing a color

        imgui.checkbox("Architectural Structure", showArchitecture);
        imgui.checkbox("Tree View", showTreeView);
        imgui.checkbox("Graph View", showGraphView);
        imgui.checkbox("Mapping View", showHuGMeView);
        imgui.checkbox("Experiment View", showExperimentView);

        if (imgui.button("Button", new Vec2()))                               // Buttons return true when clicked (NB: most widgets return true when edited/activated)
            counter[0]++;

        imgui.sameLine(0f, -1f);
        imgui.text("counter = $counter");

        imgui.text("Application average %.3f ms/frame (%.1f FPS)", 1_000f / io.getFramerate(), io.getFramerate());

        // 2. Show another simple window. In most cases you will use an explicit begin/end pair to name the window.
        if (showArchitecture[0]) {
            imgui.begin("Architectural Structure", showArchitecture, 0);
            doArchStructure(a_arch, m_treeView.getArchRootFilter(), a_g);
            imgui.end();
        }

        if (showTreeView[0]) {
            if (imgui.begin("Tree Views", showTreeView, 0)) {
                TreeView.Action a = m_treeView.doTreeView(imgui, a_arch, a_g, m_vizState.m_nvm);
                if (a != null && a.m_doMapAction != null) {
                    ArchDef.Component target = null;
                    if (a.m_doMapAction.a_toComponentName != null) {
                        target = a_arch.getComponent(a.m_doMapAction.a_toComponentName);
                    }
                    Selector.Pat selector = new Selector.Pat(a.m_doMapAction.a_whatNodeName.replace(".", "\\.") + (a.m_doMapAction.m_isLeaf ? "" : "\\..*"));
                    MapNode cmd = new MapNode(target, selector);
                    cmd.run(a_g);

                }
                imgui.end();
            }
        }

        if (showGraphView[0]) {
            if (imgui.begin("Graph View", showGraphView, 0)) {
                m_graphView.doGraphView(imgui, a_g.getNodes(), a_arch, m_vizState.m_nvm, io.getDeltaTime());
                imgui.end();

            }
        }

        if (showHuGMeView[0]) {
            if (imgui.begin("Mapping View", showHuGMeView, 0)) {

                m_mappingView.doView(imgui, a_arch, a_g, m_vizState.m_nvm);

                imgui.end();

                if (m_mappingView.getSelectedNode() != null) {
                    m_treeView.m_selectedClass = m_mappingView.getSelectedNode();
                }
            }
        }

        if (showExperimentView[0]) {
            if (imgui.begin("Experiment View", showExperimentView, 0)) {

                m_experimentsView.doView(new ImGuiWrapper(imgui));
                String selectedNodeLogicName = m_experimentsView.getSelectedNodeLogicName();
                if (selectedNodeLogicName != null) {
                    m_treeView.m_selectedClass = a_g.getNodeByLogicName(selectedNodeLogicName);
                }

                imgui.end();
            }
        }

        // Rendering
        gln.GlnKt.glViewport(window.getFramebufferSize());
        gln.GlnKt.glClearColor(clearColor);
        glClear(GL_COLOR_BUFFER_BIT);

        imgui.render();

        implGL3.renderDrawData(imgui.getDrawData());

        gln.GlnKt.checkError("loop", true); // TODO remove
    }


    private void doArchStructure(ArchDef a_arch, String a_rootComponentFilter, CGraph a_g) {
        if (a_arch !=  null) {

            HRoot root = new HRoot();
            for (ArchDef.Component c : a_arch.getComponents()) {

                if (c.getName().startsWith(a_rootComponentFilter)) {
                    root.add(c.getName());
                }
            }

            for (ArchDef.Component from : a_arch.getComponents()) {
                for (ArchDef.Component to : a_arch.getComponents()) {
                    if (from.allowedDependency(to)) {
                        if (from.getName().startsWith(a_rootComponentFilter) && to.getName().startsWith(a_rootComponentFilter)) {
                            root.addDependency(from.getName(), to.getName());
                        }
                    }
                }
            }

            //Arrays.sort(components, (o1, o2) -> o2.getAllowedDependencyCount() - o1.getAllowedDependencyCount());

            Rect r = imgui.getCurrentWindow().getContentsRegionRect();

            HRoot.Action action = root.render(r, imgui, m_vizState);

            if (action != null && action.m_addComponent != null) {
                a_arch.addComponent(action.m_addComponent);
            }

            if (action != null && action.m_deletedComponents != null) {
                for (String cName : action.m_deletedComponents) {
                    a_arch.removeComponent(a_arch.getComponent(cName));
                }
            }

            // we need to do resorting before renaming
            if (action != null && action.m_nodeOrder != null) {
                ArrayList<ArchDef.Component> newOrder = new ArrayList<>();

                for(String name : action.m_nodeOrder) {
                    newOrder.add(a_arch.getComponent(name));
                }
                a_arch.clear();
                for(ArchDef.Component c : newOrder) {
                    a_arch.addComponent(c);
                }
            }

            // add dependenices
            if (action != null && action.m_addDependenices != null) {
                for(HRoot.Action.NodeNamePair pair : action.m_addDependenices.getPairs()) {
                    ArchDef.Component sC = a_arch.getComponent(pair.m_oldName);
                    ArchDef.Component tC = a_arch.getComponent(pair.m_newName);
                    if (tC == null) {
                        System.out.println("Could not find component named: " + pair.m_newName);
                    } else if (sC == null) {
                        System.out.println("Could not find component named: " + pair.m_oldName);
                    } else {
                        sC.addDependencyTo(tC);
                    }
                }
            }

            // remove dependenices
            if (action != null && action.m_removeDependencies != null) {
                for(HRoot.Action.NodeNamePair pair : action.m_removeDependencies.getPairs()) {
                    ArchDef.Component sC = a_arch.getComponent(pair.m_oldName);
                    ArchDef.Component tC = a_arch.getComponent(pair.m_newName);
                    if (tC == null) {
                        System.out.println("Could not find component named: " + pair.m_newName);
                    } else if (sC == null) {
                        System.out.println("Could not find component named: " + pair.m_oldName);
                    } else {
                        sC.removeDependencyTo(tC);
                    }
                }
            }

            // node renaming
            if (action != null && action.m_hiearchyMove != null) {
                for(HRoot.Action.NodeNamePair pair : action.m_hiearchyMove.getPairs()) {
                    ArchDef.Component c = a_arch.getComponent(pair.m_oldName);
                    if (c == null) {
                        System.out.println("Could not find component named: " + pair.m_oldName);
                    } else {
                        ArrayList<CNode> nodes = new ArrayList<>();
                        for(CNode n : a_g.getNodes()) {
                            if (c.isMappedTo(n)) {
                                nodes.add(n);
                            }
                        }
                        a_arch.setComponentName(c, pair.m_newName, nodes);
                    }
                }
            }
        }
    }
}