package sample;

import bm.gameUtil.Country;
import bm.hexagonUtil.HexagonMap;
import bm.hexagonUtil.HexagonUtils;
import bm.generatorUtil.NoiseGenerator;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Controller {
    public TextField octavesField;
    public TextField scaleField;
    public TextField seedField;
    public TextField persistanceField;
    public TextField lacunarityField;
    public TextField heightField;
    public TextField widthField;

    public TextArea console;
    public CheckBox consoleCheck;
    public ImageView imagePane;
    public ScrollPane imageScrollPane;
    public TextField hexHeightField;
    public TextField borderSizeField;
    public TextField xAmountField;
    public TextField yAmountField;
    public CheckBox hexGridCheck;
    public CheckBox hexGenCheck;
    public TextField yOffsetField;
    public TextField xOffsetField;
    public CheckBox genBiomeCheck;
    public TextField biomeCountField;
    public TextField waterHeightField;

    private BufferedImage current;

    /**
     * Generates an image based on the current inputs on the different fields
     * @param actionEvent the trigger event for the button
     */
    public void generateImage(ActionEvent actionEvent) {
        generateImageMethod();
    }

    public void generateImageMethod() {
        int w = -1, h = -1, o = -1, se = -1;
        int b = 0, hexS = 0, hexAx = 0, hexAy = 0;
        int xOffSet = 0, yOffSet = 0;

        int biomeCount = 0;

        double waterHeight = 0;

        double s = -1, p = -1, l = -1;

        boolean useConsole = consoleCheck.isSelected();
        boolean no_err;

        boolean hexGrid = hexGridCheck.isSelected();
        boolean hexMap = hexGenCheck.isSelected();

        boolean genBiome = genBiomeCheck.isSelected() & hexMap;

        try {
            w = Integer.parseInt(widthField.getText());
            h = Integer.parseInt(heightField.getText());
            o = Integer.parseInt(octavesField.getText());
            se = Integer.parseInt(seedField.getText());

            s = Double.parseDouble(scaleField.getText());
            p = Double.parseDouble(persistanceField.getText());
            l = Double.parseDouble(lacunarityField.getText());

            b = Integer.parseInt(borderSizeField.getText());
            hexS = Integer.parseInt(hexHeightField.getText());
            hexAx = Integer.parseInt(xAmountField.getText());
            hexAy = Integer.parseInt(yAmountField.getText());

            biomeCount = Integer.parseInt(biomeCountField.getText());

            waterHeight = Double.parseDouble(waterHeightField.getText());

            xOffSet = Integer.parseInt(xOffsetField.getText());
            yOffSet = Integer.parseInt(yOffsetField.getText());

            no_err = true;
        } catch (Exception e) {
            // error occured, display error message in console (if needed)
            if (useConsole) {
                console.clear();
                console.appendText("[ERROR] Error with one of the values. \n");
                console.appendText("[HELP] Check if they are all the \n" +
                        "       correct type!");
            }

            no_err = false;
        }

        if (no_err) {
            // all values set
            imagePane.setFitWidth(w);
            imagePane.setFitHeight(h);

            if (useConsole) {
                //console.clear();
                console.appendText("\n");
                console.appendText("[GEN] Generation started with console! \n");
                console.appendText("[GEN] generating landmass... \n");
            }

            long startTime = System.currentTimeMillis(); //to calculate how long the generation took

            double noise[][] = NoiseGenerator.generateNoiseMap(w, h, s, o, p, l, se);

            BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    if (noise[x][y] < waterHeight) {
                        img.setRGB(x, y, Color.BLUE.getRGB());
                    } else {
                        img.setRGB(x, y, Color.GREEN.getRGB());
                    }
                    //int rgb = 0x010101 * (int)((noise[x][y] + 1) * 127.5);
                    //img.setRGB(x, y, rgb);
                }
            }

            long stopTime = System.currentTimeMillis(); // the ending time of Generation

            if (useConsole) {
                console.appendText("[GEN] Generation finished in " + (stopTime - startTime) + "ms\n");
            }

            HexagonMap map = new HexagonMap((Graphics2D) img.getGraphics(), 1, false, 0, 50, 10, 10);

            Country[][] banane = {{new Country(Color.RED, "a"), new Country(Color.RED, "a"), new Country(Color.RED, "a"), new Country(Color.RED, "a")},
                {new Country(Color.RED, "a"), new Country(Color.RED, "a"), new Country(Color.DARK_GRAY, "b"), new Country(Color.DARK_GRAY, "b")},
                {new Country(Color.RED, "a"), new Country(Color.DARK_GRAY, "b"), new Country(Color.DARK_GRAY, "b"), new Country(Color.DARK_GRAY, "b")},
                {new Country(Color.RED, "a"), new Country(Color.DARK_GRAY, "b"), new Country(Color.DARK_GRAY, "b"), new Country(Color.DARK_GRAY, "b")}};

            map.initializeLandmass(img);

            //map.initializeCountries(banane);

            map.colorHex(1,2,Color.RED);

            map.drawFill();
            map.drawGrid();

            /*
            HexagonUtils hexu = new HexagonUtils(hexS, b, hexAx, hexAy, xOffSet, yOffSet, img); // initializes the HexagonUtils for this picture

            // draws the map as hexagons if so wished
            if (hexMap) {
                if (useConsole) console.appendText("[GEN] Generating hexagonal grid... \n");

                for (int i = yOffSet; i < hexAx; i++) {
                    for (int j = xOffSet; j < hexAy; j++) {
                        hexu.fillHex(i, j);
                    }
                }

                if (useConsole) console.appendText("[GEN] Finished! \n");
            }

            if (genBiome) {
                if (biomeCount > (hexAx * hexAy)) {
                    console.appendText("[ERROR] Your biome count cannot be higher than the amount of fields you have!");
                } else {
                    hexu.fillBiomes(biomeCount, se);
                }
            }

            // draws the hexagonal grid if so wished
            if (hexGrid) {
                if (useConsole) console.appendText("[GEN] Generating hexagonal fields... \n");

                for (int i = yOffSet; i < hexAx; i++) {
                    for (int j = xOffSet; j < hexAy; j++) {
                        hexu.outlineHex(i, j, Color.BLACK);
                    }
                }

                if (useConsole) console.appendText("[GEN] Finished! \n");
            }

            */

            // draws the image to the pane
            WritableImage image = SwingFXUtils.toFXImage(img, null);
            imagePane.setImage(image);

            current = img; //sets the "current" (last generated image) to the newly generated one

        } else {
            console.appendText("[ERROR] Generation not succesful!");
        }
    }

    /**
     * Generates a random seed with a length of at least 3 bot no more than 7
     * @param actionEvent the trigger event for the button
     */
    public void randomizeSeed(ActionEvent actionEvent) {
        Random r = new Random();
        int i = r.nextInt(7 + 1 - 3) + 3;
        String randStr = "";

        for (int j = 0; j < i; j++) {
            int rand = r.nextInt(10 + 1) ;
            randStr = randStr + rand + "";
        }

        seedField.setText(randStr);
    }

    public void saveFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
        fileChooser.getExtensionFilters().add(extFilter);

        File path = fileChooser.showSaveDialog(null);

        if(current != null) {
            try {
                ImageIO.write(current, "png", path);
            } catch (IOException e) {
                if (consoleCheck.isSelected()) {
                    console.appendText("[ERROR] Something went wrong while saving the file!");
                }
            }
        } else {
            if (consoleCheck.isSelected()) {
                console.appendText("[ERROR] No save file selected!");
            }
        }
    }

    public void toggleBiomes(MouseEvent mouseEvent) {
        if (hexGenCheck.isSelected()) {
            biomeCountField.setEditable(true);
            biomeCountField.setDisable(false);
            genBiomeCheck.setDisable(false);
        } else {
            biomeCountField.setEditable(false);
            biomeCountField.setDisable(true);
            genBiomeCheck.setDisable(true);
        }
    }
}
