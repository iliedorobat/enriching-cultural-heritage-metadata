package ro.webdata.echo.translator.edm.lido.commons.constants;

import ro.webdata.echo.commons.graph.vocab.constraints.EDMType;
import ro.webdata.echo.translator.commons.FileConst;
import ro.webdata.echo.translator.edm.lido.commons.FileUtils;
import ro.webdata.parser.xml.lido.core.ParserDAO;
import ro.webdata.parser.xml.lido.core.ParserDAOImpl;
import ro.webdata.parser.xml.lido.core.leaf.classification.Classification;
import ro.webdata.parser.xml.lido.core.leaf.descriptiveMetadata.DescriptiveMetadata;
import ro.webdata.parser.xml.lido.core.leaf.lido.Lido;
import ro.webdata.parser.xml.lido.core.leaf.term.Term;
import ro.webdata.parser.xml.lido.core.wrap.classificationWrap.ClassificationWrap;
import ro.webdata.parser.xml.lido.core.wrap.lidoWrap.LidoWrap;
import ro.webdata.parser.xml.lido.core.wrap.objectClassificationWrap.ObjectClassificationWrap;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public final class CHOType {
    private static final ParserDAO parserDAO = new ParserDAOImpl();

    /**
     * The list with the types of CHOs from a controlled vocabulary, which
     * is going to be used as values for <b>dc:subject</b> property<br/>
     * E.g. archaeology, documents etc.
     */
    public static HashMap<String, Set<String>> SUBJECTS = new HashMap<>();

    static {
        readSubjects();
    }

    /**
     * Check if CHOType.SUBJECTS contains the input value
     * @param value Input string
     * @param lang The searching language
     * @return true/false
     */
    public static boolean isSubject(String value, String lang) {
        Set<String> subjects = CHOType.SUBJECTS.get(lang);

        return subjects != null && subjects.contains(value);
    }

    private static void readSubjects() {
        File lidoDirectory = new File(FileConst.PATH_INPUT_LIDO_DIR);
        File[] subDirectories = lidoDirectory.listFiles();

        if (subDirectories != null) {
            for (File file : subDirectories) {
                String fullName = file.getName();
                int dotIndex = fullName.lastIndexOf(".");
                String fileName = fullName.substring(0, dotIndex);

                if (!fileName.startsWith("demo")) {
                    String inputFilePath = FileUtils.getInputFilePath(fileName);
                    LidoWrap lidoWrap = parserDAO.parseLidoFile(inputFilePath);
                    ArrayList<Lido> lidoList = lidoWrap.getLidoList();

                    for (Lido lido : lidoList) {
                        ArrayList<DescriptiveMetadata> descriptiveMetadataList = lido.getDescriptiveMetadata();

                        DescriptiveMetadata descriptiveMetadata = descriptiveMetadataList.size() > 0
                                ? descriptiveMetadataList.get(0)
                                : null;
                        ObjectClassificationWrap objectClassificationWrap = descriptiveMetadata != null
                                ? descriptiveMetadata.getObjectClassificationWrap()
                                : null;
                        ClassificationWrap classificationWrap = objectClassificationWrap != null
                                ? objectClassificationWrap.getClassificationWrap()
                                : null;
                        ArrayList<Classification> classificationList = classificationWrap != null
                                ? classificationWrap.getClassification()
                                : new ArrayList<>();

                        for (Classification classification : classificationList) {
                            ArrayList<Term> termList = classification.getTerm();
                            String type = classification.getType().getType();

                            for (Term term : termList) {
                                addSubject(term, type);
                            }
                        }
                    }
                }
            }
        } else {
            System.err.println(FileConst.PATH_INPUT_LIDO_DIR + " does not contain any directories!");
        }
    }

    private static void addSubject(Term term, String classificationType) {
        String lang = term.getLang().getLang();
        String text = term.getText();
        boolean isEDMType = EDMType.contains(text);
        boolean isEDMTerm = classificationType != null && classificationType.equals(LIDOType.EUROPEANA_TYPE);

        if (!isEDMType && !isEDMTerm) {
            Set<String> subjects = SUBJECTS.get(lang);

            if (subjects == null) {
                subjects = new HashSet<>();
                subjects.add(text);
                SUBJECTS.put(lang, subjects);
            } else {
                subjects.add(text);
            }
        }
    }
}
