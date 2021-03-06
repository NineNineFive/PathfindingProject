package files.models;
import files.interfaces.Overlay;
import files.models.geometry.Line2D;
import files.models.geometry.Point;
import files.models.graph.Edge;
import files.models.graph.Vertex;

import java.util.ArrayList;

public class Graph {
    private ArrayList<Vertex> vertices = new ArrayList<>();
    private ArrayList<Overlay> shapes = new ArrayList<>();
    private String type;
    private int waypointSize = 50;
    private int waypointMargin = 50;
    private Vertex start;
    private Vertex end;

    public Graph(String type, Environment environment){
        this.type = type;
        switch(type){
            case "WayPoint":
                waypointGraph(environment);
                break;
            case "NavMesh":
                navMeshGraph(environment);
                break;
            default:
                System.err.println("Graph type doesn't exist");
                System.exit(0);
            break;
        }
    }

    public void waypointGraph(Environment environment){
        int id = 0;

        while (vertices.size() < waypointSize) {
            boolean creatable = true;
            double x = Math.round(10 + Math.random() * (760));
            double y = Math.round(10 + Math.random() * (560));

            for (Vertex vertex : vertices) { // check environment

                if (waypointMargin > Math.sqrt(Math.pow(vertex.getX() - x, 2) + Math.pow(vertex.getY() - y, 2))) {
                    creatable = false;
                }
            }

            for (Shape object : environment.getShapes()) { // check environment
                if (object.isColliding(new Point(x, y))) {
                    creatable = false;
                }
            }
            if (creatable) {
                // if the nodes is not inside unallowed terrain
                Vertex vertex = addVertex("" + id, x, y);
                shapes.add(vertex);
                id += 1;
            }
        }
        for(int i = 0; i<vertices.size(); i++){
            for(int j = 0; j<vertices.size(); j++){
                boolean creatableRelation = true;
                if(i!=j){
                    Vertex start = vertices.get(i);
                    Vertex end = vertices.get(j);
                    Line2D line = new Line2D(new Point(start.getX(),start.getY()),new Point(end.getX(),end.getY()));
                    for(Shape object : environment.getShapes()){
                        for(Line2D obstacleLine : object.getLines()){
                            if(line.isIntersecting(obstacleLine)){
                                creatableRelation = false;
                            }
                        }
                    }
                    // if we have creatable Relation, lets add it
                    if(creatableRelation){
                        double distance = Math.sqrt(Math.pow(end.getX() - start.getX(), 2) + Math.pow(end.getY() - start.getY(), 2));
                        newEdge(start,end, distance,  distance); // from, to, distance, time
                    }
                }
            }
        }

    }

    public void navMeshGraph(Environment environment){
        int id = 0;
        for(Shape object : environment.getShapes()){
            for(Point point : object.getGraphPoints()){
                // Create vertex
                Vertex vertex = addVertex(""+id, point.getX(), point.getY());
                shapes.add(vertex);
                id += 1;

            }
        }
        // Create edges (relations)
        for(Vertex i : vertices){
            for(Vertex j : vertices){
                if(!i.equals(j)) {
                    boolean creatableRelation = true;
                    Vertex start = i;
                    Vertex end = j;
                    for (Shape object : environment.getShapes()) { // 4 operations if a rectangle
                        for (Line2D obstacleLine : object.getLines()) {
                            if (obstacleLine.isIntersecting(new Line2D(new Point(start.getX(), start.getY()), new Point(end.getX(), end.getY())))) {
                                creatableRelation = false;
                            }

                        }
                    }
                    // if we have creatable Relation, lets add it
                    if (creatableRelation) {
                        double distance = Math.sqrt(Math.pow(end.getX() - start.getX(), 2) + Math.pow(end.getY() - start.getY(), 2));
                        newEdge(start, end, distance, distance); // from, to, distance, time
                    }
                }

            }
        }
    }


    public void recalculate(Environment environment){
        vertices.clear();
        shapes.clear();

        switch (type){
            case "WayPoint":
                waypointGraph(environment);
                break;
            case "NavMesh":
                navMeshGraph(environment);
                break;
        }
    }

    public void setStart(Vertex start,Environment environment){
        vertices.remove(this.getStart());
        shapes.remove(this.getStart());

        this.start = start;
        if(start!=null) {
            // Insert start and end nodes
            vertices.add(start);
            shapes.add(start);

            // Create edges (relations)
            for (Vertex other : vertices) {
                if (!start.equals(other)) {
                    boolean creatableRelation = true;
                    for (Shape object : environment.getShapes()) { // 4 operations if a rectangle
                        for (Line2D obstacleLine : object.getLines()) {
                            if (obstacleLine.isIntersecting(new Line2D(new Point(start.getX(), start.getY()), new Point(other.getX(), other.getY())))) {
                                creatableRelation = false;
                            }

                        }
                    }
                    // if we have creatable Relation, lets add it
                    if (creatableRelation) {
                        double distance = Math.sqrt(Math.pow(other.getX() - start.getX(), 2) + Math.pow(other.getY() - start.getY(), 2));
                        newEdge(start, other, distance, distance); // from, to, distance, time
                    }
                }

            }
        }
    }

    public void setEnd(Vertex end,Environment environment){
        for(Vertex vertex : vertices){
            vertex.edges.remove(this.getEnd());
        }
        vertices.remove(this.getEnd());
        shapes.remove(this.getEnd());
        if(end!=null){
            this.end = end;

            vertices.add(end);
            shapes.add(end);

            // Create edges (relations)
            for(Vertex other : vertices){
                if(!other.equals(end)) {
                    boolean creatableRelation = true;
                    for (Shape object : environment.getShapes()) { // 4 operations if a rectangle
                        for (Line2D obstacleLine : object.getLines()) {
                            if (obstacleLine.isIntersecting(new Line2D(new Point(other.getX(), other.getY()), new Point(end.getX(), end.getY())))) {
                                creatableRelation = false;
                            }

                        }
                    }
                    // if we have creatable Relation, lets add it
                    if (creatableRelation) {
                        double distance = Math.sqrt(Math.pow(end.getX() - other.getX(), 2) + Math.pow(end.getY() - other.getY(), 2));
                        newEdge(other, end, distance, distance); // from, to, distance, time
                    }
                }

            }
        }
    }

    public Vertex addVertex(String name, double x, double y) {
        Vertex newVertex = new Vertex(name,x,y);
        vertices.add(newVertex);
        return newVertex;
    }

    public Vertex getVertex(String name) {
        for(Vertex vertex : vertices) {
            if (vertex.name == name){
                return vertex;
            }
        }
        return null;
    }

    public Edge newEdge(Vertex from, Vertex to, double distance, double time) {
        Edge edge = new Edge(from,to);
        edge.distance = distance;
        edge.time = time;
        return edge;
    }

    public ArrayList<Vertex> getVertices(){
        return vertices;
    }

    public ArrayList<Overlay> getShapes() {
        return shapes;
    }

    public Vertex getStart() {
        return start;
    }

    public Vertex getEnd() {
        return end;
    }
}

