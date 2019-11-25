package files;

import files.models.Environment;
import files.models.Graph;
import files.models.Pathfinding;

/**
 * The model takes data (from database and files etc)
 * It also handles logic and algorithms such as path finding
 */
public class Model {
    private static boolean created = false;
    private Graph graph;

    public Model(){
        this.onlyOneInstance(); // Limit to only 1 instance of the Model class
        Environment environment = new Environment();
        Pathfinding pathfinding = new Pathfinding();
        //graph = new Graph(6);
        //graph.graphTest(6);
    }

    private void onlyOneInstance (){
        if (created) {
            System.err.println("You can only create one instance of the Model class");
            System.exit(0);
        } else {
            created = true;
            System.out.println("Model Created");
        }
    }

    public Graph getGraph() {
        return graph;
    }
    public void setGraph(Graph graph) {
        this.graph = graph;
    }
}
