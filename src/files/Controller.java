package files;

import files.controllers.Input;
import files.models.Graph;
import files.models.Shape;
import files.models.graph.Vertex;

import java.util.ArrayList;

public class Controller {
    private static boolean created = false;
    private final Model model;
    private final View view;
    private final Input input;

    Controller(Model model, View view){
        this.onlyOneInstance();
        this.model = model;
        this.view = view;
        this.input = new Input(model,view,this);
        this.update();
    }

    private void onlyOneInstance (){
        if (created) {
            System.err.println("You can only create one instance of the Controller class");
            System.exit(0);
        } else {
            created = true;
            System.out.println("Controller Created");
        }
    }

    public void update(){
        model.getGraph().recalculate(model.getEnvironment());
        model.getPathfinding().state = "unset";

        updatePath();
    }

    public void updatePath(){
        Graph graph = model.getGraph();
        ArrayList<Shape> shapes = model.getEnvironment().getShapes();
        view.getUI().getGraphic().setEnvironment(shapes);
        view.getUI().getGraphic().setGraph(graph);
        model.getPathfinding().run(graph);
        ArrayList<Vertex> resultPath = model.getPathfinding().getResult();
        if(model.getGraph().getStart()!=null&&model.getGraph().getEnd()!=null) {
            view.getUI().getGraphic().setResultPath(resultPath);
        } else {
            view.getUI().getGraphic().setResultPath(null);
        }
        view.getUI().getGraphic().draw();
    }
}