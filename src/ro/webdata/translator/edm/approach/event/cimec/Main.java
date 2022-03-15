package ro.webdata.translator.edm.approach.event.cimec;

import ro.webdata.echo.commons.Const;
import ro.webdata.echo.commons.Print;
import ro.webdata.translator.commons.EnvConstants;
import ro.webdata.translator.edm.approach.event.cimec.mapping.CimecMapper;

public class Main {
    public static void main(String[] args) {
        Print.operation(Const.OPERATION_START, EnvConstants.SHOULD_PRINT);
        if (!EnvConstants.IS_DEMO)
            CimecMapper.run();
        else
            CimecMapper.runDemo();
        Print.operation(Const.OPERATION_END, EnvConstants.SHOULD_PRINT);
    }
}
