package translation;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;


// TODO Task D: Update the GUI for the program to align with UI shown in the README example.
//            Currently, the program only uses the CanadaTranslator and the user has
//            to manually enter the language code they want to use for the translation.
//            See the examples package for some code snippets that may be useful when updating
//            the GUI.
public class GUI {



    public static void main(String[] args) {

        JSONTranslation jsonTranslation = new JSONTranslation();
        LanguageCodeConverter languageCodeConverter = new LanguageCodeConverter();

        SwingUtilities.invokeLater(() -> {
            JPanel countryPanel = new JPanel();
            JTextField countryField = new JTextField(10);
            countryField.setText("can");
            countryField.setEditable(false); // we only support the "can" country code for now

            JPanel languagePanel = new JPanel();
            JTextField languageField = new JTextField(10);

            Translator json = new JSONTranslator();
            LanguageCodeConverter converter = new LanguageCodeConverter();

            JComboBox<String> languageComboBox = new JComboBox<>();

            JPanel buttonPanel = new JPanel();

            JLabel resultLabelText = new JLabel("Translation:");
            buttonPanel.add(resultLabelText);
            JLabel resultLabel = new JLabel("");  // use the converter variable to get the arguements for translate
            buttonPanel.add(resultLabel);



            languageComboBox.addItemListener(new ItemListener() {

                /**
                 * Invoked when an item has been selected or deselected by the user.
                 * The code written for this method performs the operations
                 * that need to occur when an item is selected (or deselected).
                 *
                 * @param e the event to be processed
                 */
                @Override
                public void itemStateChanged(ItemEvent e) {

                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        String language = languageComboBox.getSelectedItem().toString();
                        String languageCode = languageCodeConverter.fromLanguage(language);


                        resultLabelText.setText("Translation: " +  jsonTranslation.getCanadaCountryNameTranslation(languageCode));
                    }
                }


            });

            for(String languageCode : json.getLanguageCodes()) {
                String country = converter.fromLanguageCode(languageCode);
                languageComboBox.addItem(country);
            }

            languagePanel.add(languageComboBox);

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.add(countryPanel);
            mainPanel.add(languagePanel);
            mainPanel.add(buttonPanel);

            JFrame frame = new JFrame("Country Name Translator");
            frame.setContentPane(mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);


        });
    }
}

class JSONTranslation {

    public static final int CANADA_INDEX = 30;
    private final JSONArray jsonArray;

    public JSONTranslation() {
        try {
            // this next line of code reads in a file from the resources folder as a String,
            // which we then create a new JSONArray object from.
            String jsonString = Files.readString(Paths.get(getClass().getClassLoader()
                    .getResource("sample.json").toURI()));
            this.jsonArray = new JSONArray(jsonString);
        } catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    public String getCanadaCountryNameTranslation(String countryCode) {

        JSONObject canada = jsonArray.getJSONObject(CANADA_INDEX);
        return canada.getString(countryCode);
    }
}

class ComboBoxDemo {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            JPanel languagePanel = new JPanel();
            languagePanel.add(new JLabel("Language:"));

            Translator translator = new CanadaTranslator();

            // create combobox, add country codes into it, and add it to our panel
            JComboBox<String> languageComboBox = new JComboBox<>();
            for(String countryCode : translator.getLanguageCodes()) {
                languageComboBox.addItem(countryCode);
            }
            languagePanel.add(languageComboBox);

            // add listener for when an item is selected.
            languageComboBox.addItemListener(new ItemListener() {

                /**
                 * Invoked when an item has been selected or deselected by the user.
                 * The code written for this method performs the operations
                 * that need to occur when an item is selected (or deselected).
                 *
                 * @param e the event to be processed
                 */
                @Override
                public void itemStateChanged(ItemEvent e) {

                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        String country = languageComboBox.getSelectedItem().toString();
                        JOptionPane.showMessageDialog(null, "user selected " + country + "!");
                    }
                }


            });

            // assemble our full panel and create the JFrame to put everything in
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.add(languagePanel);

            JFrame frame = new JFrame("JComboBox Demo");
            frame.setContentPane(mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null); // place in centre of screen
            frame.setVisible(true);


        });
    }
}