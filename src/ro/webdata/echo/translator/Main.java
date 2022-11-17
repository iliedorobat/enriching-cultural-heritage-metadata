package ro.webdata.echo.translator;

import ro.webdata.echo.commons.Const;
import ro.webdata.echo.commons.Print;
import ro.webdata.echo.translator.commons.Env;
import ro.webdata.echo.translator.edm.EdmTranslator;
import ro.webdata.echo.translator.edm.test.EdmTranslatorDemo;
import ro.webdata.normalization.timespan.ro.TimeExpression;

import java.util.Arrays;
import java.util.List;

import static ro.webdata.echo.translator.commons.Env.IS_PRINT_ENABLED;

public class Main {
    public static void main(String[] args) {
        List<String> list = Arrays.asList(args);
        String dataType = Env.getDataType(list);
        boolean isDemo = Env.isDemo(list);
        boolean normalizeTimeExpression = Env.normalizeTimeExpression(list);

        Print.operation(Const.OPERATION_START, IS_PRINT_ENABLED);

        if (!normalizeTimeExpression) {
            if (!isDemo)
                EdmTranslator.run(dataType);
            else
                EdmTranslatorDemo.run(dataType);
        } else {
            String timeInput = Env.getInputTime(list);
            TimeExpression timeExpression = new TimeExpression(timeInput, null);

            System.out.println("input value: " + timeExpression.getValue());
            System.out.println("prepared value: " + timeExpression.getSanitizedValue());
            System.out.println("centuries: " + timeExpression.getNormalizedValues());
        }

        Print.operation(Const.OPERATION_END, IS_PRINT_ENABLED);
    }
}
