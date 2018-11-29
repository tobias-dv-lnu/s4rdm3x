package se.lnu.siq.s4rdm3x.experiments.metric.aggregated;

import org.graphstream.graph.Node;
import se.lnu.siq.s4rdm3x.experiments.metric.FanHelper;
import se.lnu.siq.s4rdm3x.experiments.metric.Metric;

public class MinFan extends Metric {

    public String getName() {
        return "minfan";
    }

    public void assignMetric(Iterable<Node> a_nodes) {
        FanHelper fh = new FanHelper(a_nodes);

        for(Node n : a_nodes) {
            setMetric(n, Math.max(fh.getFanIn(n),fh.getFanOut(n)));
        }
    }

    public void reassignMetric(Iterable<Node> a_nodes) {
        // the fan in will not change so...
    }
}

