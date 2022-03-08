package com.github.jonahbaayen.lab06;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

public class Lab06 extends Application {
    // Bar chart data
    private static double[] avgHousingPricesByYear = {
            247381.0,264171.4,287715.3,294736.1,
            308431.4,322635.9,340253.0,363153.7
    };

    private static double[] avgCommercialPricesByYear = {
            1121585.3,1219479.5,1246354.2,1295364.8,
            1335932.6,1472362.0,1583521.9,1613246.3
    };

    // Pie chart data
    private static String[] ageGroups = {
            "18-25", "26-35", "36-45", "46-55", "56-65", "65+"
    };
    private static int[] purchasesByAgeGroup = {
            648, 1021, 2453, 3173, 1868, 2247
    };
    private static Color[] pieColours = {
            Color.AQUA, Color.GOLD, Color.DARKORANGE,
            Color.DARKSALMON, Color.LAWNGREEN, Color.PLUM
    };

    // Any values shared between this and the pie chart data will be "exploded" visually
    private static int[] purchasesByAgeGroupToExplode = {
            2453, 2247
    };

    @Override
    public void start(Stage stage) throws IOException {
        Canvas canvas = new Canvas(1000.0f, 600.0f);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        // Constants for bar graph
        double barWidth = 15;
        double barBaseHeight = 600d - 20d;
        double barMaxHeight = 20d;
        double secondBarOffset = barWidth;
        double spacing = barWidth * 3;

        // Get max data value for bar graph (for scaling)
        double barDataMax = 0;
        for (double d : avgHousingPricesByYear) {
            if (d > barDataMax) {
                barDataMax = d;
            }
        }
        for (double d : avgCommercialPricesByYear) {
            if (d > barDataMax) {
                barDataMax = d;
            }
        }

        // Bar chart
        for (int i = 0; i < avgHousingPricesByYear.length; i++) {
            double d = avgHousingPricesByYear[i];
            graphicsContext.setFill(Color.PALEVIOLETRED);
            graphicsContext.fillRect(spacing * (i + 1), barBaseHeight - barMaxHeight - ((d / barDataMax) * barBaseHeight), barWidth, barMaxHeight + ((d / barDataMax) * barBaseHeight));

            d = avgCommercialPricesByYear[i];
            graphicsContext.setFill(Color.CADETBLUE);
            graphicsContext.fillRect(spacing * (i + 1) + secondBarOffset, barBaseHeight - barMaxHeight - ((d / barDataMax) * barBaseHeight), barWidth, barMaxHeight + ((d / barDataMax) * barBaseHeight));

        }

        // Constants for pie graph
        double chartOffset = 500d; // Half of canvas width
        double chartSpacing = 100d;
        double circumference = chartOffset - (2 * chartSpacing);
        double textOffset = 40d;
        double explodeOffset = 20d;

        // Get total data for pie graph
        double pieDataTotal = 0;
        for (double d : purchasesByAgeGroup) {
            pieDataTotal += d;
        }

        // Set up pie graph labels
        Font font = new Font("Arial", 12);
        graphicsContext.setFont(font);

        // Draw pie graph
        double previousAngle = 0;
        for (int i = 0; i < purchasesByAgeGroup.length; i++) {
            double d = purchasesByAgeGroup[i];

            // Check if we should explode this slice
            boolean shouldExplode = false;
            for (double de : purchasesByAgeGroupToExplode) {
                if (d == de) {
                    shouldExplode = true;
                    break;
                }
            }

            // Draw pie graph
            Color color = pieColours[i];
            graphicsContext.setFill(color);

            // Explode slice if needed
            double explodeOffsetX = 0;
            double explodeOffsetY = 0;
            if (shouldExplode) {
                double explodeAngle = previousAngle + (180 * (d / pieDataTotal));
                explodeOffsetX = explodeOffset * Math.cos(Math.toRadians(explodeAngle));
                explodeOffsetY = explodeOffset * Math.sin(Math.toRadians(explodeAngle));
            }

            // Pie graph fill
            graphicsContext.fillArc(
                    chartOffset + chartSpacing + explodeOffsetX,
                    chartSpacing - explodeOffsetY,
                    circumference,
                    circumference,
                    previousAngle,
                    360.0 * (d / pieDataTotal),
                    ArcType.ROUND);

            // Pie graph stroke
            graphicsContext.setStroke(color.darker());
            graphicsContext.strokeArc(
                    chartOffset + chartSpacing + explodeOffsetX,
                    chartSpacing - explodeOffsetY,
                    circumference,
                    circumference,
                    previousAngle,
                    360.0 * (d / pieDataTotal),
                    ArcType.ROUND);

            // Text
            double angle = -previousAngle - ((360.0 * (d / pieDataTotal)) / 2d);
            double textX = ((circumference / 2d) - textOffset) * Math.cos(Math.toRadians(angle));
            double textY = ((circumference / 2d) - textOffset) * Math.sin(Math.toRadians(angle));

            graphicsContext.setFill(color.deriveColor(0, 1, 0.3, 1));
            graphicsContext.fillText(ageGroups[i], chartOffset + (circumference / 2d) + chartSpacing + textX - 12d, chartSpacing + (circumference / 2d) + textY + 6d);

            // Shift angle for next slice
            previousAngle += 360.0 * (d / pieDataTotal);
        }

        Group group = new Group(canvas);

        Scene scene = new Scene(group);
        stage.setScene(scene);
        stage.setTitle("Lab 06 - Jonah Baayen 100783828");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
