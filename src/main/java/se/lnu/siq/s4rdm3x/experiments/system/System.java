package se.lnu.siq.s4rdm3x.experiments.system;

import org.graphstream.graph.Graph;
import se.lnu.siq.s4rdm3x.cmd.hugme.HuGMe;
import se.lnu.siq.s4rdm3x.cmd.util.Selector;

import java.nio.file.Path;

public abstract class System {
    public abstract HuGMe.ArchDef createAndMapArch(Graph a_g);
    public abstract boolean load(Graph a_g);
    public abstract String getName();

    public Path getCustomMetricsFile() { return null; }

    protected HuGMe.ArchDef.Component createAddAndMapComponent(Graph a_g, HuGMe.ArchDef a_ad, String a_componentName, String[] a_packages) {
        HuGMe.ArchDef.Component c = a_ad.addComponent(a_componentName);
        for(String p : a_packages) {
            c.mapToNodes(a_g, new Selector.Pkg(p));
        }
        return c;
    }
}
