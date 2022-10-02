package ro.webdata.translator;

import ro.webdata.echo.commons.Const;
import ro.webdata.echo.commons.Print;
import ro.webdata.translator.commons.Env;
import ro.webdata.translator.edm.EdmTranslator;
import ro.webdata.translator.edm.test.EdmTranslatorDemo;

import java.util.Arrays;
import java.util.List;

import static ro.webdata.translator.commons.Env.IS_PRINT_ENABLED;

public class Main {
    public static void main(String[] args) {
        List<String> list = Arrays.asList(args);
        String approach = Env.getApproach(list);
        String dataType = Env.getDataType(list);
        boolean isDemo = Env.isDemo(list);

        Print.operation(Const.OPERATION_START, IS_PRINT_ENABLED);

        if (!isDemo)
            EdmTranslator.run(approach, dataType);
        else
            EdmTranslatorDemo.run(approach, dataType);

        Print.operation(Const.OPERATION_END, IS_PRINT_ENABLED);
    }
}
