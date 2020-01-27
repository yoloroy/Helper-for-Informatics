package e.pmart.project;

import android.graphics.Canvas;
import android.graphics.Paint;

import de.blox.graphview.Edge;
import de.blox.graphview.Graph;
import de.blox.graphview.Node;
import de.blox.graphview.Vector;
import de.blox.graphview.edgerenderer.EdgeRenderer;

public class NoArrowEdgeRenderer implements EdgeRenderer {
    @Override
    public void render(Canvas canvas, Graph graph, Paint paint) {
        Paint trianglePaint = new Paint(paint);
        trianglePaint.setStyle(Paint.Style.FILL);

        for (Edge edge : graph.getEdges()) {
            final Node source = edge.getSource();
            final Vector sourcePosition = source.getPosition();
            final Node destination = edge.getDestination();
            final Vector destinationPosition = destination.getPosition();

            final float startX = sourcePosition.getX() + source.getWidth() / 2f;
            final float startY = sourcePosition.getY() + source.getHeight() / 2f;
            float stopX = destinationPosition.getX() + destination.getWidth() / 2f;
            float stopY = destinationPosition.getY() + destination.getHeight() / 2f;

            canvas.drawLine(startX,
                    startY,
                    stopX,
                    stopY, paint);

        }
    }
}