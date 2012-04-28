/*
Copyright 2008-2010 Gephi
Authors : Mathieu Bastian <mathieu.bastian@gephi.org>
Website : http://www.gephi.org

This file is part of Gephi.

DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.

Copyright 2011 Gephi Consortium. All rights reserved.

The contents of this file are subject to the terms of either the GNU
General Public License Version 3 only ("GPL") or the Common
Development and Distribution License("CDDL") (collectively, the
"License"). You may not use this file except in compliance with the
License. You can obtain a copy of the License at
http://gephi.org/about/legal/license-notice/
or /cddl-1.0.txt and /gpl-3.0.txt. See the License for the
specific language governing permissions and limitations under the
License.  When distributing the software, include this License Header
Notice in each file and include the License files at
/cddl-1.0.txt and /gpl-3.0.txt. If applicable, add the following below the
License Header, with the fields enclosed by brackets [] replaced by
your own identifying information:
"Portions Copyrighted [year] [name of copyright owner]"

If you wish your version of this file to be governed by only the CDDL
or only the GPL Version 3, indicate your decision by adding
"[Contributor] elects to include this software in this distribution
under the [CDDL or GPL Version 3] license." If you do not indicate a
single choice of license, a recipient has the option to distribute
your version of this file under either the CDDL, the GPL Version 3 or
to extend the choice of license to its licensees as provided above.
However, if you add GPL Version 3 code and therefore, elected the GPL
Version 3 license, then the option applies only if the new code is
made subject to such option by the copyright holder.

Contributor(s):

Portions Copyrighted 2011 Gephi Consortium.
 */
package org.gephi.plugins.filter.kbrace;

import java.util.*;
import javax.swing.Icon;
import javax.swing.JPanel;
import org.gephi.filters.api.FilterLibrary;
import org.gephi.filters.plugin.AbstractFilter;
import org.gephi.filters.spi.Category;
import org.gephi.filters.spi.ComplexFilter;
import org.gephi.filters.spi.Filter;
import org.gephi.filters.spi.FilterBuilder;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.Node;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author mbastian
 */
@ServiceProvider(service = FilterBuilder.class)
public class KBraceBuilder implements FilterBuilder {

    @Override
    public Category getCategory() {
        return FilterLibrary.TOPOLOGY;
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(KBraceFilter.class, "KBraceFilter.name");
    }

    @Override
    public Icon getIcon() {
        return null;
    }

    @Override
    public String getDescription() {
        return NbBundle.getMessage(KBraceFilter.class, "KBraceFilter.description");
    }

    @Override
    public Filter getFilter() {
        return new KBraceFilter();
    }

    @Override
    public JPanel getPanel(Filter filter) {
        KBraceUI ui = Lookup.getDefault().lookup(KBraceUI.class);
        if (ui != null) {
            return ui.getPanel((KBraceFilter) filter);
        }
        return null;
    }

    @Override
    public void destroy(Filter filter) {
    }

    public static class KBraceFilter extends AbstractFilter implements ComplexFilter {

        private int k = 1;

        public KBraceFilter() {
            super(NbBundle.getMessage(KBraceFilter.class, "KBraceFilter.name"));

            addProperty(Integer.class, "k");
        }

        @Override
        public Graph filter(Graph graph) {
            Graph undirectedGraph = graph.getView().getGraphModel().getUndirectedGraph(graph.getView());
            LinkedList<Edge> queue = new LinkedList<Edge>();
            Map<Edge, Integer> edgeInter = new HashMap<Edge, Integer>();
            for (Edge e : undirectedGraph.getEdges().toArray()) {
                Node node1 = e.getSource();
                Node node2 = e.getTarget();
                int interseciton = neighborsIntersection(undirectedGraph, node1, node2).size();
                edgeInter.put(e, interseciton);
                if (interseciton < k) {
                    queue.add(e);
                    undirectedGraph.removeEdge(e);
                }
            }
            while (!queue.isEmpty()) {
                Edge e = queue.pop();
                Node node1 = e.getSource();
                Node node2 = e.getTarget();
                List<Node> interseciton = neighborsIntersection(undirectedGraph, node1, node2);
                for (Node n : interseciton) {
                    Edge adj1 = undirectedGraph.getEdge(node1, n);
                    int em1 = edgeInter.get(adj1) - 1;
                    edgeInter.put(adj1, em1);
                    if (em1 < k) {
                        queue.add(adj1);
                        undirectedGraph.removeEdge(adj1);
                    }
                    Edge adj2 = undirectedGraph.getEdge(node2, n);
                    int em2 = edgeInter.get(adj2) - 1;
                    edgeInter.put(adj2, em2);
                    if (em2 < k) {
                        queue.add(adj2);
                        undirectedGraph.removeEdge(adj2);
                    }
                }
            }
            for (Node n : undirectedGraph.getNodes().toArray()) {
                if (undirectedGraph.getDegree(n) == 0) {
                    undirectedGraph.removeNode(n);
                }
            }
            return graph;
        }

        private List<Node> neighborsIntersection(Graph graph, Node node1, Node node2) {
            List<Node> intersection = new ArrayList<Node>();
            if (node1 == node2) {
                return intersection;
            }

            for (Node neighbor : graph.getNeighbors(node1)) {
                //Test if neigbor connected to node2
                if (graph.isAdjacent(neighbor, node2)) {
                    intersection.add(neighbor);
                }
            }
            return intersection;
        }

        public Integer getK() {
            return k;
        }

        public void setK(Integer k) {
            this.k = k;
        }
    }
}
