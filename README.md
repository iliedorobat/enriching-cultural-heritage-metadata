# eCHO: Enriching The Digital Representation of Cultural Heritage Objects



## Local setup
### Requirements
- JDK 11 or OpenJDK 11.

### Setup
1. Clone the repository:
```bash
git clone https://github.com/iliedorobat/enriching-cultural-heritage-metadata.git
```
2. The main language is set to **Romanian** language by default. If the dataset uses a different language, you must modify the `LANG_MAIN` environment variable located in [EnvConstants.java](https://github.com/iliedorobat/enriching-cultural-heritage-metadata/blob/master/src/ro/webdata/echo/translator/commons/EnvConstants.java).
3. Open the Terminal/Command Prompt and navigate to the root directory (`enriching-cultural-heritage-metadata` directory).
4. Register the environment variables:
```bash
export ECHO_PATH=`pwd`
export CLASSPATH="${ECHO_PATH}/out/production/enriching-cultural-heritage-metadata:${ECHO_PATH}/lib/commons-compress-1.17.jar:${ECHO_PATH}/lib/commons-echo-1.5.3.jar:${ECHO_PATH}/lib/commons-io-2.6.jar:${ECHO_PATH}/lib/commons-lang3-3.4.jar:${ECHO_PATH}/lib/commons-text-1.6.jar:${ECHO_PATH}/lib/commons-validator-1.7.jar:${ECHO_PATH}/lib/Dublin-Core-Parser-1.1.jar:${ECHO_PATH}/lib/gson-2.3.1.jar:${ECHO_PATH}/lib/httpclient-4.5.5.jar:${ECHO_PATH}/lib/jackson-annotations-2.9.0.jar:${ECHO_PATH}/lib/jackson-core-2.9.6.jar:${ECHO_PATH}/lib/jackson-databind-2.9.6.jar:${ECHO_PATH}/lib/jena-arq-3.9.0.jar:${ECHO_PATH}/lib/jena-base-3.9.0.jar:${ECHO_PATH}/lib/jena-core-3.9.0.jar:${ECHO_PATH}/lib/jena-dboe-base-3.9.0.jar:${ECHO_PATH}/lib/jena-iri-3.9.0.jar:${ECHO_PATH}/lib/jena-shaded-guava-3.9.0.jar:${ECHO_PATH}/lib/jsonld-java-0.12.1.jar:${ECHO_PATH}/lib/libthrift-0.10.0.jar:${ECHO_PATH}/lib/LIDO-Parser-1.2.jar:${ECHO_PATH}/lib/log4j-1.2.17.jar:${ECHO_PATH}/lib/slf4j-api-1.7.25.jar:${ECHO_PATH}/lib/slf4j-log4j12-1.7.25.jar:${ECHO_PATH}/lib/timespan-normalization.jar"
```
4. Compile the project:
```bash
javac -d ./out/production/enriching-cultural-heritage-metadata src/ro/webdata/echo/translator/BuildProject.java
java ro/webdata/echo/translator/BuildProject
```

### Translate the XML files to EDM:
1. CIMEC to EDM **event-centric approach**:
```bash
## main command
java ro.webdata.echo.translator/Main --approach=EVENT_CENTRIC --dataType=CIMEC
## quick demo
java ro.webdata.echo.translator/Main --demo --approach=EVENT_CENTRIC --dataType=CIMEC
```
2. DSPACE to EDM **object-centric approach**:
```bash
## main command
java ro.webdata.echo.translator/Main --approach=OBJECT_CENTRIC --dataType=DSPACE
## quick demo
java ro.webdata.echo.translator/Main --demo --approach=OBJECT_CENTRIC --dataType=DSPACE
```
3. LIDO to EDM **event-centric approach**:
```bash
## main command
java ro.webdata.echo.translator/Main --approach=EVENT_CENTRIC --dataType=LIDO
## quick demo
java ro.webdata.echo.translator/Main --demo --approach=EVENT_CENTRIC --dataType=LIDO
```

### Normalize time expression
```bash
java ro.webdata.echo.translator/Main --expression="1/2 sec. 3 - sec. 1 a. chr."
```



## Input Datasets

**CIMEC datasets:**
* Datasets need to be added in the [files/input/cimec](https://github.com/iliedorobat/enriching-cultural-heritage-metadata/tree/master/files/input/cimec) directory.
* The existing datasets 

**DSPACE datasets:**
* The datasets need to be added in the [files/input/dspace](https://github.com/iliedorobat/enriching-cultural-heritage-metadata/tree/master/files/input/dspace) directory.
* Snapshot of dspace storage level:
```
main_directory/
    item1/
        dublin_core.xml
        contents
        contentFile1.ext
        contentFile2.ext
        ...
        contentFileM.ext
    item2/
    ...
    itemN/
```

**LIDO datasets:**
* The datasets need to be added in the [files/input/lido](https://github.com/iliedorobat/enriching-cultural-heritage-metadata/tree/master/files/input/lido) directory.
* The existing datasets have been downloaded from the [data.gov.ro portal](http://data.gov.ro/organization/institutul-national-al-patrimoniului).



## Output Datasets

**CIMEC datasets:**
The output datasets are located to the [files/output/cimec2edm](https://github.com/iliedorobat/enriching-cultural-heritage-metadata/tree/master/files/output/cimec2edm) directory.<br/>
* `*.rdf` contains the EDM prepared datasets;

**DSPACE datasets:**
The output datasets are located to the [files/output/dspace2edm](https://github.com/iliedorobat/enriching-cultural-heritage-metadata/tree/master/files/output/dspace2edm) directory.<br/>
* `*.rdf` contains the EDM prepared datasets;

**LIDO datasets:**
The output datasets are located to the [files/output/lido2edm](https://github.com/iliedorobat/enriching-cultural-heritage-metadata/tree/master/files/output/lido2edm) directory.<br/>
* `*.rdf` contains the EDM prepared datasets;
* `timespan_all.txt` contains all identified time expressions;
* `timespan_unique.txt` contains unique identified time expressions;
* `timespan_all-analysis.csv` contains the pair of input time expressions - normalized centuries;
* `timespan_unique-analysis.csv` contains the unique pair of input time expressions - normalized centuries;
* `properties.csv` contains the pair of parent property - child property.



## Online Datasets
* The output datasets are available on [opendata repository](http://opendata.cs.pub.ro/repo/core/admin/dataview.html)
and can be queried by using the [opendata sparql endpoint](http://opendata.cs.pub.ro/repo/sparql/admin/squebi.html).


  
## Publications:
TPDL 2019: [Enriching the Cultural Heritage Metadata Using Historical Events: A Graph-Based Representation](https://link.springer.com/chapter/10.1007/978-3-030-30760-8_30)
```
@InProceedings{10.1007/978-3-030-30760-8_30,
    author="Dorobat, Ilie Cristian and Posea, Vlad",
    title="Enriching the Cultural Heritage Metadata Using Historical Events: A Graph-Based Representation",
    booktitle="Digital Libraries for Open Knowledge",
    year="2019",
    publisher="Springer International Publishing",
    pages="344-347",
    isbn="978-3-030-30760-8"
    doi="10.1007/978-3-030-30760-8_30"
}
```

ECAI 2020: [Evolving the DSpace Storage into Linked Data](https://ieeexplore.ieee.org/document/9223189)
```
@InProceedings{9223189,
    author="Dorobat, Ilie Cristian and Posea, Vlad",
    booktitle="2020 12th International Conference on Electronics, Computers and Artificial Intelligence (ECAI)",
    title="Evolving the DSpace Storage into Linked Data",
    year="2020",
    pages="1-5",
    doi="10.1109/ECAI50035.2020.9223189"
}
```

EMCIS 2020: [Raising the Interoperability of Cultural Datasets: The Romanian Cultural Heritage Case Study](https://link.springer.com/chapter/10.1007/978-3-030-63396-7_3)
```
@InProceedings{10.1007/978-3-030-63396-7_3,
    author="Dorobat, Ilie Cristian and Posea, Vlad",
    title="Raising the Interoperability of Cultural Datasets: The Romanian Cultural Heritage Case Study",
    booktitle="Information Systems",
    year="2020",
    publisher="Springer International Publishing",
    pages="35-48",
    isbn="978-3-030-63396-7"
    doi="10.1007/978-3-030-63396-7_3"
}
```

EuroMed 2020: [The Usability of Romanian Open Data in the Development of Tourist Applications](https://link.springer.com/chapter/10.1007/978-3-030-73043-7_51)
```
@InProceedings{10.1007/978-3-030-73043-7_51,
    author="Dorobat, Ilie Cristian and Posea, Vlad",
    title="The Usability of Romanian Open Data in the Development of Tourist Applications",
    booktitle="Digital Heritage. Progress in Cultural Heritage: Documentation, Preservation, and Protection",
    year="2021",
    publisher="Springer International Publishing",
    pages="596-602",
    isbn="978-3-030-73043-7"
    doi="10.1007/978-3-030-73043-7_51"
}
```

ECAI 2021: [The Power of Regular Expressions in Recognizing Dates and Epochs](https://ieeexplore.ieee.org/document/9515139)
```
@InProceedings{9515139,
    author="Dorobat, Ilie Cristian and Posea, Vlad",
    booktitle="2021 13th International Conference on Electronics, Computers and Artificial Intelligence (ECAI)",
    title="The Power of Regular Expressions in Recognizing Dates and Epochs",
    year="2021",
    pages="1-3",
    doi="10.1109/ECAI52376.2021.9515139"
}
```
