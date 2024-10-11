# eCHO: Enriching The Digital Representation of Cultural Heritage Objects

## Requirements
JDK 11+ or OpenJDK 11+<br/>
Maven 3.x

## Setup
1. Download and install [JDK 11](https://www.oracle.com/nl/java/technologies/javase/jdk11-archive-downloads.html) or [OpenJDK 11](https://openjdk.org/install/) (or newer versions)
2. Download and install [Maven 3.x](https://maven.apache.org/install.html)
3. Clone the repository:
```bash
git clone https://github.com/iliedorobat/enriching-cultural-heritage-metadata.git
```
4. The main language is set to **Romanian** language by default. If the dataset uses a different language, you must modify the `LANG_MAIN` environment variable located in [EnvConstants.java](https://github.com/iliedorobat/enriching-cultural-heritage-metadata/blob/master/src/ro/webdata/echo/translator/commons/EnvConstants.java).
5. Open the Terminal/Command Prompt and navigate to the root directory (`enriching-cultural-heritage-metadata` directory).
6. Generate the library:
```bash
mvn validate && mvn clean package
```

## Translate XML files to EDM:
1. CIMEC to EDM:
```bash
## main command
java -jar target/eCHO-1.3-jar-with-dependencies.jar --dataType=CIMEC
## quick demo
java -jar target/eCHO-1.3-jar-with-dependencies.jar --demo --dataType=CIMEC
```
2. DSPACE to EDM:
```bash
## main command
java -jar target/eCHO-1.3-jar-with-dependencies.jar --dataType=DSPACE
## quick demo
java -jar target/eCHO-1.3-jar-with-dependencies.jar --demo --dataType=DSPACE
```
3. LIDO to EDM:
```bash
## main command
java -jar target/eCHO-1.3-jar-with-dependencies.jar --dataType=LIDO
## quick demo
java -jar target/eCHO-1.3-jar-with-dependencies.jar --demo --dataType=LIDO
```

### Normalize time expression
```bash
java -jar target/eCHO-1.3-jar-with-dependencies.jar --expression="1/2 sec. 3 a. chr - sec. 2 p. chr."
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
