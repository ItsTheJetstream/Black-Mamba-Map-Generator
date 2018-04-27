package sample;

import bm.gameUtil.Biome;
import bm.gameUtil.Country;
import bm.generatorUtil.Console;
import bm.hexagonUtil.HexagonMap;
import bm.generatorUtil.NoiseGenerator;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
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
    public ColorPicker borderColorPicker;
    public ColorPicker capitalColorPicker;
    public Slider borderOpacitySlider;
    public CheckBox genCountriesCheck;
    public TextField countryAmountField;
    public ScrollPane countryList;
    public Button removeCountryBtn;
    public Button addCountryBtn;
    public TextField inputField;
    public Button inputBtn;
    public CheckBox offSetCheck;
    public VBox paneBox;

    private BufferedImage current;

    private Console con;

    private ArrayList<Pane> countryPanes = new ArrayList<Pane>();

    /**
     * Generates an image based on the current inputs on the different fields
     * @param actionEvent the trigger event for the button
     */
    public void generateImage(ActionEvent actionEvent) {
        generateImageMethod();
    }

    private void generateImage() {
        // TODO
    }

    public void addCountry(ActionEvent actionEvent) {
        Pane toAdd = new Pane();
        toAdd.setPrefSize(170, 40);

        CheckBox toRemBox = new CheckBox();
        toRemBox.setText("");
        toRemBox.setLayoutX(14); toRemBox.setLayoutY(12);

        TextField nameField = new TextField();
        nameField.setPrefSize(90, 25);
        nameField.setPromptText("Country Name");
        nameField.setLayoutX(37); nameField.setLayoutY(8);

        ColorPicker cP = new ColorPicker();
        cP.setPrefSize(120, 25);
        cP.setLayoutX(135); cP.setLayoutY(8);

        toAdd.getChildren().add(0, toRemBox);
        toAdd.getChildren().add(1, nameField);
        toAdd.getChildren().add(2, cP);

        paneBox.getChildren().add(toAdd);
        countryPanes.add(toAdd);
    }

    public void removeCountry(ActionEvent actionEvent) {
        ArrayList<Pane> toRemove = new ArrayList<Pane>();
        for (Pane p : countryPanes) {
            CheckBox current = (CheckBox) p.getChildren().get(0);
            if (current.isSelected()) {
                toRemove.add(p);
            }
        }

        for (Pane p : toRemove) {
            countryPanes.remove(p);
            paneBox.getChildren().remove(p);
        }
    }

    public void generateImageMethod() {
        int w = 800, h = 800, o = 4, se = 12345;
        int b = 0, hexS = 50, hexAx = 20, hexAy = 20;

        int biomeCount = 10;

        double waterHeight = 0;

        double s = 120, p = 0.3, l = 2.0;

        boolean useConsole = consoleCheck.isSelected();
        boolean no_err;

        boolean hexGrid = hexGridCheck.isSelected();
        boolean hexMap = hexGenCheck.isSelected();

        boolean genBiome = genBiomeCheck.isSelected() & hexMap;

        try {
            /*
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
*/
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

            HexagonMap map = new HexagonMap((Graphics2D) img.getGraphics(), se, true, b, hexS, hexAx, hexAy);

            map.initializeLandmass(img);

            Biome[] bio = {new Biome(new Color(255, 204, 0)), new Biome(new Color(102, 153, 0)),
                    new Biome(new Color(51, 153, 51)), new Biome(new Color(0, 102, 0))};

            map.initializeBiomes(biomeCount, bio);

            map.initializeCountries(10);

            System.out.println(map.drawCountryCapitials(Color.RED));

            map.drawFill();
            map.drawGrid();

            /*
            HexagonUtil hexu = new HexagonUtil(hexS, b, hexAx, hexAy, xOffSet, yOffSet, img); // initializes the HexagonUtil for this picture

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

        if (path.exists()){
            path.delete();
        }

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

    public void putCommand(ActionEvent actionEvent) {
        this.con.putCommand(inputField.getText());
    }

    private void setUpConsole() {
        if (this.con == null) {
            this.con = new Console(console);
        }
    }


}
