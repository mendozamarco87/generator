package %package;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

/**
 * Created by marco.mendoza on 14/08/2017.
 */

public class EsquemaGreenDao {

    public static void main(String args[]) throws Exception {

        Schema esquema = new Schema(1, "com.dsoft.nacionalvida.agendacomercial.model.data.local.entity");
        esquema.setDefaultJavaPackageDao("com.dsoft.nacionalvida.agendacomercial.model.data.local.daos");
        esquema.enableKeepSectionsByDefault();

        %content
    }

}