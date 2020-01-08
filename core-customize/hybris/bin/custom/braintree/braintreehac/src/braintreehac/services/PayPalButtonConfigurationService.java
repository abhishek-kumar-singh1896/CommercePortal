package braintreehac.services;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.Properties;

@Component
public class PayPalButtonConfigurationService {

    private static final Logger LOG = Logger.getLogger(PayPalButtonConfigurationService.class);
    private static final String CART_BUTTON_CONFIG_FILE = System.getProperty("HYBRIS_CONFIG_DIR") + "/paypalbuttonconfig.properties";
    private static final String CART_BUTTON_CONFIG = "cart.button.config";
    private static final String MARK_BUTTON_CONFIG = "mark.button.config";
    private static final String MINI_CART_BUTTON_CONFIG = "mini.cart.button.config";
    private static final String BUTTON_CONFIG_PLACEHOLDER = "button.config.placeholder";
    private static final String BUTTON_CONFIG_PLACEHOLDER_DEFAULT_VALUE = "Start typing here..."
            + "\r\n"
            + "Example:"
            + "\r\n\r\n"
            + "style: {"
            + "\r\n    color: 'gold',"
            + "\r\n    size: 'small'"
            + "\r\n},"
            + "\r\n\r\n"
            + "funding: {"
            + "\r\n    allowed: [paypal.FUNDING.CARD, paypal.FUNDING.CREDIT],"
            + "\r\n    disallowed: [ ]"
            + "\r\n}";

    private Properties prop;
    private File propFile;

    @PostConstruct
    public void init() {
        prop = new Properties();
        propFile = new File(CART_BUTTON_CONFIG_FILE);
        if (!propFile.isFile()) {
            LOG.info("Created new " + CART_BUTTON_CONFIG_FILE);
            setProperty(BUTTON_CONFIG_PLACEHOLDER, BUTTON_CONFIG_PLACEHOLDER_DEFAULT_VALUE);
            storeInPropertiesFile();
        } else {
            LOG.info("Loaded " + CART_BUTTON_CONFIG_FILE);
            loadPropertiesFromFile();
        }
    }

    public Object setProperty(String key, String value) {
        return prop.setProperty(key, value.replaceAll("'", "\""));
    }

    public String getProperty(String key) {
        String result = prop.getProperty(key);
        return result == null ? "" : result;
    }

    public void storeInPropertiesFile() {
        try (OutputStream out = new FileOutputStream(propFile)) {
            prop.store(out, "Save properties in file");
        } catch (Exception e) {
            LOG.error(CART_BUTTON_CONFIG_FILE + " is not saved,  error: " + e.getMessage(), e);
        }
    }

    private void loadPropertiesFromFile() {
        try (InputStream input = new FileInputStream(propFile)) {
            prop.load(input);
        } catch (Exception e) {
            LOG.error("Error during reading " + CART_BUTTON_CONFIG_FILE + ": " + e.getMessage(), e);
        }
    }

    public String getFormattedProperty(String key) {
        return getProperty(key)
                .trim()
                .replaceAll("//(.)*?\r\n|\r\n", "")
                .replaceAll("( ){2,}", " ")
                .replace("\"","\\\"");
    }
}
