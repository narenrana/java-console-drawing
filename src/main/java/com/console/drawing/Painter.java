package com.console.drawing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.BiFunction;

public class Painter {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        WidgetDrawerService draw= new WidgetDrawerService();
      for(;;) {
          System.out.println("");
          System.out.print("#");
          String input = reader.readLine();
          // Printing the read line

          System.out.println(input);
          input=input.replace("#","").trim();
          String params[] = input.split(" ");
          if ("Q".equalsIgnoreCase(input)) {
              return;
          } else if (params.length >= 3 && "C".equalsIgnoreCase(params[0])) {
              draw.drawCanvas(Integer.parseInt(params[1]), Integer.parseInt(params[2]));

          } else if (params.length >= 5 && "L".equalsIgnoreCase(params[0])) {
              draw.drawLine(Integer.parseInt(params[1]), Integer.parseInt(params[2]), Integer.parseInt(params[3]), Integer.parseInt(params[4]));
          } else if (params.length >= 5 && "R".equalsIgnoreCase(params[0])) {
              draw.drawRectangle(Integer.parseInt(params[1]), Integer.parseInt(params[2]), Integer.parseInt(params[3]), Integer.parseInt(params[4]));
          } else if (params.length >= 4 && "B".equalsIgnoreCase(params[0])) {
              draw.fillShape(Integer.parseInt(params[1]), Integer.parseInt(params[2]), Integer.parseInt(params[3]), Integer.parseInt(params[4]));

          } else {
              System.out.println("No valid input found: " + input);
              System.out.println(" ");
              System.out.println("Command \t\tDescription\n" +
                      "C w h           Should create a new canvas of width w and height h.\n" +
                      "L x1 y1 x2 y2   Should create a new line from (x1,y1) to (x2,y2). Currently only\n" +
                      "                horizontal or vertical lines are supported. Horizontal and vertical lines\n" +
                      "                will be drawn using the 'x' character.\n" +
                      "R x1 y1 x2 y2   Should create a new rectangle, whose upper left corner is (x1,y1) and\n" +
                      "                lower right corner is (x2,y2). Horizontal and vertical lines will be drawn\n" +
                      "                using the 'x' character.\n" +
                      "B x y c         Should fill the entire area connected to (x,y) with \"colour\" c. The\n" +
                      "                behavior of this is the same as that of the \"bucket fill\" tool in paint\n" +
                      "                programs.\n" +
                      "Q               Should quit the program.");
          }
      }
    }
}


interface WidgetDrawer {
     void drawCanvas( int w, int h  );
     void drawLine( int x1,int y1, int x2, int y2 );
     void drawRectangle( int x1,int y1, int x2, int y2 );
     void fillShape( int x1,int y1, int x2, int y2 );
     void addWidgets(String key,  Shape shape);
     void repaint();
}

class Shape {

}

class Line extends Shape{
    int x1;
    int y1;
    int x2;
    int y2;
    public Line(int x1, int y1, int x2, int y2) {
        super();
    }

}

class Rectangle extends Shape{
    int x1;
    int y1;
    int x2;
    int y2;

    public Rectangle(int x1, int y1, int x2, int y2) {
        super();
    }
}

class WidgetDrawerService implements WidgetDrawer {

    Set<String> pixelStorage= new HashSet<>();
    Set<String> closedShapeStorage= new HashSet<>();
    BiFunction<Integer, Integer, String> pixelPair = ( i1,j1) -> "(" + i1 + "," + j1 + ")";
    Map<String, List<Shape>> shapes= new HashMap<>();


    int width;
    int height;

    @Override
    public void drawCanvas(int w, int h  ) {
        this.width = w;
        this.height = h;
        this.repaint();
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
        //Add widget to list
        //addWidgets("L", new Rectangle(x1,y1,x2,y2));
        //Create pixels
        for (int j=y1; j<=y2;j++){
            for (int i=x1; i<=x2;i++){
                //starting and ending points are already in
                if((i==x1 && j==y1) || (i==x2 && j==y2)) {
                    boolean added=pixelStorage.add(pixelPair.apply(i, j));
                    if(!added) {
                        //Element already exist
                    }
                }else {
                    pixelStorage.add(pixelPair.apply(i, j));
                }

            }
        }
        this.repaint();
        //Find any close shape exists




    }

    @Override
    public void drawRectangle(int x1, int y1, int x2, int y2) {
        //addWidgets("R", new Rectangle(x1,y1,x2,y2));
        for (int j=0; j<y2-y1;j++){
            for (int i=0; i<=(x2-x1);i=i+1){
                // Top Line (x1,y1), (x1+1, y1), (x1+2, y1)......(x2, y1)
                pixelStorage.add(pixelPair.apply( x1+i, this.height-y1));
               // System.out.print(pixelPair.apply( x1+i, this.height-y1+1));
                // Bottom Line (x1,y2), (x1+1, y2), (x1+2, y2)......(x2, y2)
                pixelStorage.add(pixelPair.apply( x1+i, this.height-y2));
                //System.out.print(pixelPair.apply( x1+i, this.height-y2+1));
                // Left Line (x1,y1), (x1, y1-1), (x1, y1-2)......(x1, y2)
                pixelStorage.add(pixelPair.apply(x1, this.height-y1-j));
                // Right Line (x2,y1), (x2, y1-1), (x2, y1-2)......(x2, y2)
                pixelStorage.add(pixelPair.apply(x2, this.height-y1-j));
            }
            System.out.println("");
        }
        this.repaint();

    }

    @Override
    public void fillShape(int x1, int y1, int x2, int y2) {

    }

    @Override
    public void addWidgets(String key, Shape shape) {
        Optional<List<Shape>> shapeListFount=Optional.ofNullable(this.shapes.get("L"));
        if(shapeListFount.isEmpty()){
            List<Shape> shapeList=new ArrayList<>();
            shapeList.add(shape);
            this.shapes.put(key, shapeList);
        }else {
            List<Shape> existingShape=shapeListFount.get();
            existingShape.add(shape);
            this.shapes.put(key, existingShape);
        }

        //Find if any close shape exists
        //For rectangle always a closed shape
        //For line need to find out
//        List<Shape>  lineShapeList=this.shapes.get("L");
//        for(int i=0; i<lineShapeList.size();i=i+1 ){
//            Line line= (Line)lineShapeList.get(i);
//            for(int j=i+1; j<lineShapeList.size();j=j+1 ){
//                Line nextLine= (Line)lineShapeList.get(i);
//                if(line.x1==nextLine.x1 &&  line.y1==nextLine.y1) {
//                       // Check
//                }
//                else  if(line.x2==nextLine.x2 &&  line.y2==nextLine.y2) {
//
//                }
//            }
//        }

    }


    @Override
    public void repaint() {
        System.out.println(pixelStorage);
        for (int j=0;j<this.height+2; j++){
            for (int i=0;i<this.width+2; i++){
                if(  j==0|| j==(this.height-1+2)|| i==0 || i== this.width-1+2){
                    System.out.print("-");
                }else {

                    if(pixelStorage.contains(pixelPair.apply(i, j))){
                        System.out.print("*");
                    }else {
                        System.out.print(" ");
                    }
                }
            }
            System.out.println("");

        }
    }

}
