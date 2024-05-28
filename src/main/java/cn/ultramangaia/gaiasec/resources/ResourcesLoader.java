package cn.ultramangaia.gaiasec.resources;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.IconLoader;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.PropertyKey;

import javax.swing.Icon;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;


/**
 * $Date$
 *
 * @author Andre Pfeiler<andrep@twodividedbyzero.org>
 * @version $Revision$
 * @since 0.0.1
 */
@SuppressWarnings("HardcodedFileSeparator")
public final class ResourcesLoader {

    private static final Logger LOGGER = Logger.getInstance(ResourcesLoader.class.getName());

    private static volatile ResourceBundle _bundle;
    public static final String BUNDLE = "messages.Messages";
    private static final String ICON_RESOURCES_PKG = "/icons";


    private ResourcesLoader() {
    }


    @NotNull
    public static ResourceBundle getResourceBundle() {
        LOGGER.info("Loading locale properties for '" + Locale.getDefault() + ')');

        //noinspection StaticVariableUsedBeforeInitialization
        if (_bundle != null) {
            return _bundle;
        }

        //noinspection UnusedCatchParameter
        try {
            _bundle = ResourceBundle.getBundle(BUNDLE, Locale.getDefault());
        } catch (final MissingResourceException e) {
            throw new MissingResourceException("Missing Resource bundle: " + Locale.getDefault() + ' ', BUNDLE, "");
        }

        return _bundle;
    }


    @Nls
    @SuppressWarnings({"UnusedCatchParameter"})
    public static String getString(@NotNull @PropertyKey(resourceBundle = BUNDLE) final String key, @Nullable Object... params) {
        try {
            //noinspection StaticVariableUsedBeforeInitialization
            if (_bundle == null) {
                getResourceBundle();
            }
            String ret = _bundle.getString(key);
            if (params != null && params.length > 0 && ret.indexOf('{') >= 0) {
                return MessageFormat.format(ret, params);
            }
            return ret;
        } catch (final MissingResourceException e) {
            throw new MissingResourceException("Missing Resource: " + Locale.getDefault() + " - key: " + key + "  - resources: " + BUNDLE, BUNDLE, key);
        }
    }

    @NotNull
    public static Icon loadIcon(final String filename) {
        return IconLoader.getIcon(ICON_RESOURCES_PKG + '/' + filename, ResourcesLoader.class.getClassLoader());
    }

}