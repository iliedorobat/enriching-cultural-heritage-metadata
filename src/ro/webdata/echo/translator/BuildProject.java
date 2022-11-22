package ro.webdata.echo.translator;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class BuildProject {
    public static void main(String[] args) throws Exception {
        ArrayList<String> commands = new ArrayList<>();

        commands.addAll(BuildProject.getEdmCommands());
        commands.addAll(BuildProject.getEdmDemoCommands());
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/Main.java");

        for (String command : commands) {
            runProcess(command);
        }
    }

    public static ArrayList<String> getEdmCommands() {
        ArrayList<String> commands = new ArrayList<>();

        commands.addAll(BuildCommons.getCommands());
        commands.addAll(BuildCimec.getCommands());
        commands.addAll(BuildLido.getCommands());
        commands.addAll(BuildDSpace.getCommands());

        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/EdmTranslator.java");

        return commands;
    }

    public static ArrayList<String> getEdmDemoCommands() {
        ArrayList<String> commands = new ArrayList<>();

        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata test/ro/webdata/echo/translator/edm/test/CimecDemo.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata test/ro/webdata/echo/translator/edm/test/LidoDemo.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata test/ro/webdata/echo/translator/edm/test/DSpaceDemo.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata test/ro/webdata/echo/translator/edm/test/EdmTranslatorDemo.java");

        return commands;
    }

    // https://www.digitalocean.com/community/tutorials/compile-run-java-program-another-java-program
    private static void runProcess(String command) throws Exception {
        Process pro = Runtime.getRuntime().exec(command);
        printLines(command + " stdout:", pro.getInputStream());
        printLines(command + " stderr:", pro.getErrorStream());
        pro.waitFor();
        System.out.println(command + " exitValue() " + pro.exitValue());
    }

    private static void printLines(String cmd, InputStream ins) throws Exception {
        String line = null;
        BufferedReader in = new BufferedReader(
                new InputStreamReader(ins));
        while ((line = in.readLine()) != null) {
            System.out.println(cmd + " " + line);
        }
    }
}

class BuildCommons {
    public static ArrayList<String> getCommands() {
        ArrayList<String> commands = new ArrayList<>();

        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/commons/Env.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/commons/FileConst.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/commons/GraphUtils.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/commons/MuseumUtils.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/commons/PropertyUtils.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/commons/SyncHttpClient.java");

        return commands;
    }
}

class BuildCimec {
    protected static ArrayList<String> getCommands() {
        ArrayList<String> commands = new ArrayList<>();

        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/cimec/DatasetUtils.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/cimec/mapping/leaf/Building.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/cimec/mapping/leaf/Collection.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/cimec/mapping/leaf/Contact.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/cimec/mapping/leaf/Description.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/cimec/mapping/leaf/GeneralInfo.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/cimec/mapping/leaf/Location.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/cimec/mapping/leaf/Publications.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/cimec/mapping/leaf/Subordination.java");

        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/cimec/mapping/core/Museum.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/cimec/CimecTranslator.java");

        return commands;
    }
}

class BuildDSpace {
    protected static ArrayList<String> getCommands() {
        ArrayList<String> commands = new ArrayList<>();

        commands.addAll(getCommonsCommands());
        commands.addAll(getMappingCoreCommands());

        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/dspace/DSpaceTranslator.java");

        return commands;
    }


    private static ArrayList<String> getCommonsCommands() {
        ArrayList<String> commands = new ArrayList<>();

        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/dspace/commons/PrintMessages.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/dspace/commons/ProvidedCHO.java");

        return commands;
    }

    private static ArrayList<String> getMappingCoreCommands() {
        ArrayList<String> commands = new ArrayList<>();

        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/dspace/mapping/core/dc/record/ContributorMapping.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/dspace/mapping/core/dc/record/CoverageMapping.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/dspace/mapping/core/dc/record/CreatorMapping.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/dspace/mapping/core/dc/record/DateMapping.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/dspace/mapping/core/dc/record/DescriptionMapping.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/dspace/mapping/core/dc/record/FormatMapping.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/dspace/mapping/core/dc/record/IdentifierMapping.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/dspace/mapping/core/dc/record/LanguageMapping.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/dspace/mapping/core/dc/record/PublisherMapping.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/dspace/mapping/core/dc/record/RelationMapping.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/dspace/mapping/core/dc/record/RightsMapping.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/dspace/mapping/core/dc/record/SourceMapping.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/dspace/mapping/core/dc/record/SubjectMapping.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/dspace/mapping/core/dc/record/TitleMapping.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/dspace/mapping/core/dc/record/TypeMapping.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/dspace/mapping/core/dc/DcMapping.java");

        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/dspace/mapping/core/europeana/record/IsShownAtMapping.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/dspace/mapping/core/europeana/record/IsShownByMapping.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/dspace/mapping/core/europeana/record/ProviderMapping.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/dspace/mapping/core/europeana/record/TypeMapping.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/dspace/mapping/core/europeana/EuropeanaMapping.java");

        return commands;
    }
}

class BuildLido {
    protected static ArrayList<String> getCommands() {
        ArrayList<String> commands = new ArrayList<>();

        commands.addAll(getCommonsCommands());
        commands.addAll(getMappingLeafCommands());
        commands.addAll(getMappingCoreCommands());

        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/lido/Statistics.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/lido/stats/MissingPlaces.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/lido/stats/TimeExpressions.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/lido/Stats.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/lido/LidoTranslator.java");

        return commands;
    }

    private static ArrayList<String> getCommonsCommands() {
        ArrayList<String> commands = new ArrayList<>();

        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/lido/commons/FileUtils.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/lido/commons/constants/PlaceConst.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/lido/commons/constants/LIDOType.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/lido/commons/constants/CHOType.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/lido/commons/ConceptService.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/lido/commons/PlaceMapUtils.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/lido/commons/RDFConceptService.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/lido/commons/ResourceUtils.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/lido/commons/StatsUtils.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/lido/commons/URIUtils.java");

        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/lido/mapping/core/administrativeMetadata/RecordWrapProcessing/CimecProvider.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/lido/mapping/core/administrativeMetadata/RecordWrapProcessing/UpbProvider.java");

        return commands;
    }

    private static ArrayList<String> getMappingLeafCommands() {
        ArrayList<String> commands = new ArrayList<>();

        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/lido/mapping/leaf/AppellationComplexTypeProcessing.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/lido/mapping/leaf/IdentifierComplexTypeProcessing.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/lido/mapping/leaf/LegalBodyRefComplexTypeProcessing.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/lido/mapping/leaf/RecordIDProcessing.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/lido/mapping/leaf/RecordRightsProcessing.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/lido/mapping/leaf/RecordSourceProcessing.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/lido/mapping/leaf/TextComplexTypeProcessing.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/lido/mapping/leaf/WorkIDProcessing.java");

        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/lido/mapping/leaf/eventComplexType/culture/CultureProcessing.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/lido/mapping/leaf/eventComplexType/eventActor/EventActorProcessing.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/lido/mapping/leaf/eventComplexType/eventDate/EventDateProcessing.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/lido/mapping/leaf/eventComplexType/eventMaterialsTech/EventMaterialsTechProcessing.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/lido/mapping/leaf/eventComplexType/eventPlace/EventPlaceProcessing.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/lido/mapping/leaf/eventComplexType/eventType/EventTypeProcessing.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/lido/mapping/leaf/eventComplexType/EventComplexTypeProcessing.java");

        return commands;
    }

    private static ArrayList<String> getMappingCoreCommands() {
        ArrayList<String> commands = new ArrayList<>();

        // administrativeMetadata
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/lido/mapping/core/administrativeMetadata/RecordWrapProcessing/RecordWrapProcessing.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/lido/mapping/core/administrativeMetadata/ResourceWrapProcessing/ResourceWrapProcessing.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/lido/mapping/core/administrativeMetadata/AdministrativeMetadataProcessing.java");

        // category
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/lido/mapping/core/category/CategoryProcessing.java");

        // descriptiveMetadata
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/lido/mapping/core/descriptiveMetadata/eventWrap/EventWrapProcessing.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/lido/mapping/core/descriptiveMetadata/objectClassificationWrap/classificationWrap/ClassificationProcessing.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/lido/mapping/core/descriptiveMetadata/objectClassificationWrap/objectWorkTypeWrap/ObjectWorkTypeWrapProcessing.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/lido/mapping/core/descriptiveMetadata/objectClassificationWrap/ObjectClassificationWrapProcessing.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/lido/mapping/core/descriptiveMetadata/objectIdentificationWrap/displayStateEditionWrap/DisplayStateEditionWrapProcessing.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/lido/mapping/core/descriptiveMetadata/objectIdentificationWrap/objectDescriptionWrap/ObjectDescriptionWrapProcessing.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/lido/mapping/core/descriptiveMetadata/objectIdentificationWrap/objectMeasurementsWrap/ObjectMeasurementsWrapProcessing.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/lido/mapping/core/descriptiveMetadata/objectIdentificationWrap/repositoryWrap/RepositoryWrapProcessing.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/lido/mapping/core/descriptiveMetadata/objectIdentificationWrap/titleWrap/TitleWrapProcessing.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/lido/mapping/core/descriptiveMetadata/objectIdentificationWrap/ObjectIdentificationWrapProcessing.java");
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/lido/mapping/core/descriptiveMetadata/DescriptiveMetadataProcessing.java");

        // lidoRecID
        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/lido/mapping/core/lidoRecID/LidoRecIDProcessing.java");

        commands.add("javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/edm/lido/mapping/core/LidoWrapProcessing.java");

        return commands;
    }
}
