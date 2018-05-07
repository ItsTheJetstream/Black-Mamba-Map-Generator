package bm.guiUtil;

import bm.gameUtil.Biome;
import bm.gameUtil.BiomeUtil;
import bm.gameUtil.CountryUtil;
import bm.generatorUtil.Console;
import bm.hexagonUtil.HexagonMap;
import bm.generatorUtil.NoiseGenerator;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

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
    public VBox countryPaneBox;
    public ScrollPane biomeList;
    public VBox biomePaneBox;
    public Button addBiomeBtn;
    public Button removeBIomeBtn;
    public Button biomeResetBtn;
    public Button countryResetBtn;
    public Button genHexBtn;

    private BufferedImage current;

    private Console con;

    private ArrayList<Pane> countryPanes = new ArrayList<>();
    private ArrayList<Pane> biomePanes = new ArrayList<>();

    @FXML
    public void initialize() {
        Pattern patternNeg = Pattern.compile("-?+\\d*|-?+\\d+\\.\\d*"); // pattern for positive & negative doubles
        Pattern patternPos = Pattern.compile("\\d*|\\d+\\.\\d*"); // pattern for only positive doubles
        Pattern patternInt = Pattern.compile("\\d*"); // pattern for only positive integers

        TextFormatter formatLacunarity = new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> patternPos.matcher(change.getControlNewText()).matches() ? change : null);
        TextFormatter formatScale = new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> patternPos.matcher(change.getControlNewText()).matches() ? change : null);
        TextFormatter formatPersistance = new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> patternPos.matcher(change.getControlNewText()).matches() ? change : null);

        TextFormatter formatDimX = new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> patternInt.matcher(change.getControlNewText()).matches() ? change : null);
        TextFormatter formatDimY = new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> patternInt.matcher(change.getControlNewText()).matches() ? change : null);
        TextFormatter formatOctaves = new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> patternInt.matcher(change.getControlNewText()).matches() ? change : null);
        TextFormatter formatSeed = new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> patternInt.matcher(change.getControlNewText()).matches() ? change : null);

        TextFormatter formatGridDimX = new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> patternInt.matcher(change.getControlNewText()).matches() ? change : null);
        TextFormatter formatGridDimY = new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> patternInt.matcher(change.getControlNewText()).matches() ? change : null);
        TextFormatter formatHexHeight = new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> patternInt.matcher(change.getControlNewText()).matches() ? change : null);
        TextFormatter formatBorderSize = new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> patternInt.matcher(change.getControlNewText()).matches() ? change : null);

        TextFormatter formatWaterHeight = new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> patternNeg.matcher(change.getControlNewText()).matches() ? change : null);

        // Generator settings fields
        scaleField.setTextFormatter(formatScale);
        lacunarityField.setTextFormatter(formatLacunarity);
        persistanceField.setTextFormatter(formatPersistance);

        widthField.setTextFormatter(formatDimX);
        heightField.setTextFormatter(formatDimY);
        octavesField.setTextFormatter(formatOctaves);
        seedField.setTextFormatter(formatSeed);

        // Hexagon settings fields
        xAmountField.setTextFormatter(formatGridDimX);
        yAmountField.setTextFormatter(formatGridDimY);
        heightField.setTextFormatter(formatHexHeight);
        borderSizeField.setTextFormatter(formatBorderSize);

        // Biome settings fields
        waterHeightField.setTextFormatter(formatWaterHeight);
    }

    /**
     * Generates an image based on the current inputs on the different fields
     * @param actionEvent the trigger event for the button
     */
    public void generateImage(ActionEvent actionEvent) {
        generateImageMethod();
    }

    private void imageGen() {
        setUpConsole();
        con.setActivated(consoleCheck.isSelected());

        con.write("[GEN] Starting image Generation...");

    }

    public void addBiome() {
        Pane toAdd = BiomeUtil.newBiomePane();

        biomePaneBox.getChildren().add(toAdd);
        biomePanes.add(toAdd);
    }

    public void removeBiome() {
        ArrayList<Pane> toRemove = new ArrayList<>();
        for (Pane p : biomePanes) {
            CheckBox current = (CheckBox) p.getChildren().get(0);
            if (current.isSelected()) {
                toRemove.add(p);
            }
        }

        for (Pane p : toRemove) {
            biomePanes.remove(p);
            biomePaneBox.getChildren().remove(p);
        }
    }

    public void addCountry() {
        Pane toAdd = CountryUtil.newCountryPane();

        countryPaneBox.getChildren().add(toAdd);
        countryPanes.add(toAdd);
    }

    public void removeCountry() {
        ArrayList<Pane> toRemove = new ArrayList<>();
        for (Pane p : countryPanes) {
            CheckBox current = (CheckBox) p.getChildren().get(0);
            if (current.isSelected()) {
                toRemove.add(p);
            }
        }

        for (Pane p : toRemove) {
            countryPanes.remove(p);
            countryPaneBox.getChildren().remove(p);
        }
    }

    public void generateImageMethod() {
        int widthValueNoise = Integer.parseInt(widthField.getText());
        int heightValueNoise = Integer.parseInt(heightField.getText());

        int sizeXValueHexagon = Integer.parseInt(xAmountField.getText());
        int sizeYValueHexagon = Integer.parseInt(yAmountField.getText());
        int heightValueHexagon = Integer.parseInt(hexHeightField.getText());
        boolean offSetGrid = offSetCheck.isSelected();

        int octavesValue = Integer.parseInt(octavesField.getText());
        int seedValue = Integer.parseInt(seedField.getText());
        double persistanceValue = Double.parseDouble(persistanceField.getText());
        double lacunarityValue = Double.parseDouble(lacunarityField.getText());
        double scaleValue = Double.parseDouble(scaleField.getText());

        int biomeAmountValue = Integer.parseInt(biomeCountField.getText());
        double waterHeightValue = Double.parseDouble(waterHeightField.getText());

        int countryAmountValue = Integer.parseInt(countryAmountField.getText());

        imagePane.setFitWidth(widthValueNoise);
        imagePane.setFitHeight(heightValueNoise);

        boolean useConsole = consoleCheck.isSelected();

        if (useConsole) {
            //console.clear();
            console.appendText("\n");
            console.appendText("[GEN] Generation started with console! \n");
            console.appendText("[GEN] generating landmass... \n");
        }

        long startTime = System.currentTimeMillis(); //to calculate how long the generation took

        double noise[][] = NoiseGenerator.generateNoiseMap(widthValueNoise, heightValueNoise, scaleValue, octavesValue, persistanceValue, lacunarityValue, seedValue);

        BufferedImage img = new BufferedImage(widthValueNoise, heightValueNoise, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < heightValueNoise; y++) {
            for (int x = 0; x < widthValueNoise; x++) {
                if (noise[x][y] < waterHeightValue) {
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

        HexagonMap map = new HexagonMap((Graphics2D) img.getGraphics(), seedValue, offSetGrid, 0, heightValueHexagon, sizeXValueHexagon, sizeYValueHexagon);

        map.initializeLandmass(img);

        Biome[] bio = {new Biome(new Color(255, 204, 0)), new Biome(new Color(102, 153, 0)),
                new Biome(new Color(51, 153, 51)), new Biome(new Color(0, 102, 0))};

        map.initializeBiomes(biomeAmountValue, bio);

        map.initializeCountries(countryAmountValue);

        map.drawCountryCapitials(Color.RED);

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
        if (true) {
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
        setUpConsole(); // first settign up the console
        this.con.putCommand(inputField.getText()); // sending the command to the console
    }

    private void setUpConsole() {
        if (this.con == null) {
            this.con = new Console(console);
        }
    }

    public void biomeReset(ActionEvent actionEvent) {
    }

    public void countryReset(ActionEvent actionEvent) {
    }

    public void generateHexagons(ActionEvent actionEvent) {
    }
}
