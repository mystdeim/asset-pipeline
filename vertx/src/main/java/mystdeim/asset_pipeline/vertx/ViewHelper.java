package mystdeim.asset_pipeline.vertx;

import io.vertx.core.json.JsonObject;
import mystdeim.asset_pipeline.commons.Const;
import mystdeim.asset_pipeline.vertx.impl.ViewHelperDevelopment;
import mystdeim.asset_pipeline.vertx.impl.ViewHelperProduction;

import java.io.FileNotFoundException;

public interface ViewHelper {

    static ViewHelper get(JsonObject conf) {
        if (null != conf.getString(Const.Env.KEY) && conf.getString(Const.Env.KEY).equals(Const.Env.PROD)) {
            return new ViewHelperProduction();
        } else {
            return new ViewHelperDevelopment();
        }
    }

    String css() throws FileNotFoundException;
    String js() throws FileNotFoundException;

    /**
     * Path to asset like
     *
     * @param name
     * @return
     * @throws FileNotFoundException
     */
    String asset(String name) throws FileNotFoundException;

}
