
package n0tebook;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javax.swing.JFileChooser;
import static n0tebook.WordComparison.findSuggestion;


public class N0teBook extends Application {

    
 
    public static ArrayList<Point> missSpelled = new ArrayList<>();
    private static String allText = new String();
    private static TextArea txtDoc = new TextArea();

    private static HashTable hash;
    private static int skipValue = 1;

    @Override
    public void start(Stage primaryStage) {
        StackPane root = new StackPane();
        Scene scene = new Scene(root, 600, 500);

        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("_File");
        MenuItem newItem = new MenuItem("_New          Ctrl+N");
        MenuItem openItem = new MenuItem("_Open...      Ctrl+O");
        MenuItem saveItem = new MenuItem("_Save         Ctrl+S");
        fileMenu.getItems().addAll(newItem, openItem, saveItem);

        Menu editMenu = new Menu("_Edit");
        MenuItem spellCMenu = new MenuItem("Spell Check");
        editMenu.getItems().add(spellCMenu);
        Menu formatMenu = new Menu("_Format");
        Menu viewMenu = new Menu("_View");
        VBox menuPane = new VBox();

        
        menuPane.getChildren().addAll(menuBar, txtDoc);
        menuPane.setAlignment(Pos.TOP_CENTER);
        Menu themeMenu = new Menu("_Themes");
        MenuItem defaultItem = new MenuItem("Default");
        MenuItem darkItem = new MenuItem("Dark");
        themeMenu.getItems().addAll(defaultItem, darkItem);
     
        txtDoc.setStyle("-fx-control-inner-background:#373038; -fx-highlight-fill: red; -fx-highlight-text-fill: #000000; -fx-text-fill: #d3d3d3; ");
        menuBar.setStyle("-fx-background-color: #f28043");
        menuBar.getMenus().addAll(fileMenu, editMenu, formatMenu, viewMenu, themeMenu);
        root.getChildren().add(menuPane);
        txtDoc.prefWidthProperty().bind(primaryStage.widthProperty());
        txtDoc.prefHeightProperty().bind(primaryStage.heightProperty());
        primaryStage.setTitle("N0t3B00k");
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image("https://tailpuff.net/wp-content/themes/tailpuffv2/img/notepad.png"));

    
        primaryStage.show();
  

        darkItem.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                txtDoc.setStyle("-fx-control-inner-background:#373038; -fx-highlight-fill: red; -fx-highlight-text-fill: #000000; -fx-text-fill: #d3d3d3; ");
                menuBar.setStyle("-fx-background-color: #f28043");
            }

        });

        defaultItem.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                txtDoc.setStyle("-fx-control-inner-background: white");
                menuBar.setStyle("-fx-background-color: #885ead");
            }

        });
        
        newItem.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                allText = new String();
                txtDoc.setText("");
            }

        });

        saveItem.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                File save = getOpenPath();

                final JFileChooser fc = new JFileChooser();
                fc.setCurrentDirectory(new File("."));
                int returnVal;

                returnVal = fc.showSaveDialog(null);

                save = null;
                if (returnVal == 0) {
                    save = fc.getSelectedFile();
                }

                allText = txtDoc.getText();
                PrintWriter P;
                try {
                    P = new PrintWriter(save);
                    String holder = new String();
                    for (int f = 0; f < allText.length(); f++) {
                        if (allText.charAt(f) != '\n') {
                            holder += allText.charAt(f);
                        } else {
                            f++;
                            P.println(holder);
                            holder = new String();
                        }
                    }
                    P.close();
                } catch (Exception e) {
                }

            }

        });

        openItem.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                String filePath;

                allText = new String();
                filePath = getOpenPath().toString();

                try {
                    Scanner fileIn = new Scanner(new File(filePath));

                    while (fileIn.hasNext()) {
                        allText += fileIn.nextLine() + "\n ";
                    }
                    txtDoc.setText(allText);

                    PrintStream P = new PrintStream("Tzp.txt");
                    P.println(allText);
                    P.close();
                    hash = new HashTable(hash.size);

                    hash.insertDictionary();
                    hash.proofReadFile("Tzp.txt");
                } catch (Exception e) {}

                int start = 0;
                String temp = new String("");
                for (int k = 0; k < allText.length(); k++) {
                    if (Character.isLetter(allText.charAt(k))
                            && allText.charAt(k) != '-'
                            && allText.charAt(k) != '\''
                            && allText.charAt(k) != ' ') {
                        temp += allText.charAt(k);

                    } else {
                        int counter = 0;
                        for (int o = 0; o < hash.mispelledList.size(); o++) {
                            if (!temp.equals(hash.mispelledList.get(o))) {
                                counter++;
                            }
                        }
                        Point mp = new Point();
                        mp.x = start;
                        mp.y = k;

                        temp = new String();
                        start = k;
                        if (counter < hash.mispelledList.size()) {
                            missSpelled.add(mp);
                        }
                        counter = 0;
                    }

                }
            }

        });


        spellCMenu.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                skipValue = 1;
                try {
                    allText = txtDoc.getText();
                    PrintStream P = new PrintStream("Tzp.txt");
                    P.println(allText);
                    P.close();
                    hash = new HashTable(hash.size);

                    hash.insertDictionary();
                    hash.proofReadFile("Tzp.txt");
                    spellCheck();
                    changeWordWindow();
                } catch (Exception e) {
                }
            }

        });

        
    }

    
    public static void main(String[] args) throws IOException {
        HashTable.readDictionary();
        HashTable.countWords();
        HashTable.calculateHashSize();
        hash = new HashTable(hash.size);

        hash.insertDictionary();

        launch(args);

    }

    private static void spellCheck() {
        int start = 0;
        String temp = new String("");
        
        for (int k = 0; k < allText.length(); k++) {
            if (Character.isLetter(allText.charAt(k))
                    && allText.charAt(k) != '-'
                    && allText.charAt(k) != '\''
                    && allText.charAt(k) != ' ') {
                temp += allText.charAt(k);

            } else {
                int counter = 0;
                for (int o = 0; o < hash.mispelledList.size(); o++) {
                    if (!temp.equals(hash.mispelledList.get(o))) {
                        counter++;
                    }
                }
                Point mp = new Point();
                mp.x = start;
                mp.y = k;

                temp = new String();
                start = k;
                if (counter < hash.mispelledList.size()) {
                    missSpelled.add(mp);
                }
                counter = 0;
            }

        }
    }

    private static File getOpenPath() {
        File path;

        final JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new File("."));
        int returnVal;

        returnVal = fc.showOpenDialog(null);

        path = null;
        if (returnVal == 0) {
            path = fc.getSelectedFile();
        }

        return path;
    }

    private static void changeWordWindow() {
        final Stage dialog = new Stage();
        int i = 0;
        allText = txtDoc.getText();
        String currentWord = allText.substring(missSpelled.get(i).x, missSpelled.get(i).y);
        ObservableList<String> suggestedWords = findSuggestion(currentWord);
        ListView suggestionView = new ListView();
        
        dialog.setTitle("Spell Checker");
        suggestionView.setItems(suggestedWords);
        txtDoc.selectRange(missSpelled.get(i).x , missSpelled.get(i).y);
        VBox dialogVbox = new VBox(20);
        HBox firstLine = new HBox();
        TextField txtWord = new TextField(currentWord);
        firstLine.getChildren().addAll(new Text("Miss-Spelled: "), txtWord);
        dialogVbox.getChildren().addAll(firstLine, suggestionView);

        txtWord.setStyle("-fx-text-fill:red;");
        HBox buttonBox = new HBox();
        Button btnReplace = new Button("_Replace");
        Button btnSkip = new Button("_Skip");
        Button btnCancel = new Button("_Cancel");
        buttonBox.getChildren().addAll(btnReplace, btnSkip, btnCancel);
        dialogVbox.getChildren().add(buttonBox);
        Scene dialogScene = new Scene(dialogVbox, 300, 200);
        dialog.setScene(dialogScene);
        dialog.show();

        suggestionView.setOnMouseClicked((MouseEvent event) -> {
           
            txtWord.setText((suggestionView.getSelectionModel().getSelectedItem().toString()));
          

        });
        btnSkip.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                int i = skipValue;
                if (i < missSpelled.size() ) {
                    txtDoc.selectRange(missSpelled.get(i).x , missSpelled.get(i).y);

                    txtWord.setText(txtDoc.getSelectedText());
                     ObservableList<String> suggestedWords = findSuggestion(txtWord.getText());
                     suggestionView.setItems(suggestedWords);
                    skipValue++;
                } else {
                    dialog.close();
                }
            }

        });
        btnCancel.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                dialog.close();
            }
        });

        btnReplace.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                txtDoc.replaceSelection(" " + txtWord.getText());
                allText = txtDoc.getText();
                skipValue++;
                int i = skipValue;

                PrintWriter P;
                try {
                    P = new PrintWriter(new File("Tzp.txt"));
                    String holder = new String();
                    for (int f = 0; f < allText.length(); f++) {
                        if (allText.charAt(f) != '\n') {
                            holder += allText.charAt(f);
                        } else {
                            f++;
                            P.println(holder);
                            holder = new String();
                        }
                    }
                    P.close();
                } catch (Exception e) {
                }
                try {
                    Scanner fileIn = new Scanner(new File("Tzp.txt"));

                    while (fileIn.hasNext()) {
                        allText += fileIn.nextLine() + "\n ";
                    }
                    missSpelled = new ArrayList<>();
                    spellCheck();
                    hash.proofReadFile("Tzp.txt");
                } catch (IOException ex) {
                    Logger.getLogger(N0teBook.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (i < missSpelled.size()) {
                    txtDoc.selectRange(missSpelled.get(i).x , missSpelled.get(i).y);

                    txtWord.setText(txtDoc.getSelectedText());
                    ObservableList<String> suggestedWords = findSuggestion(txtWord.getText());
                     suggestionView.setItems(suggestedWords);
                    skipValue++;
                } else {
                    dialog.close();
                }
            }
        });

    }
}
