package ro.webdata.translator;

import ro.webdata.echo.commons.Const;
import ro.webdata.echo.commons.Print;
import ro.webdata.normalization.timespan.ro.TimeExpression;
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
        boolean normalizeTimeExpression = Env.normalizeTimeExpression(list);

        Print.operation(Const.OPERATION_START, IS_PRINT_ENABLED);

        if (!normalizeTimeExpression) {
            if (!isDemo)
                EdmTranslator.run(approach, dataType);
            else
                EdmTranslatorDemo.run(approach, dataType);
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
