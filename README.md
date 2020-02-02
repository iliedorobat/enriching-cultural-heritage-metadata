# eHeritage: Enriching The Digital Representation of The Cultural Heritage Through EDM Conceptual Model

## Translating the Cultural Heritage metadata stored in LIDO format into Linked Data through EDM event-centric approach

### The Application Location
The application is located in the [src.ro.webdata.translator.edm.approach.event.lido](https://github.com/iliedorobat/enriching-cultural-heritage-metadata/tree/master/src/ro/webdata/translator/edm/approach/event/lido) package.

### Running the Application
The application can be run through [Main.java](https://github.com/iliedorobat/enriching-cultural-heritage-metadata/blob/master/src/ro/webdata/translator/edm/approach/event/lido/Main.java).

### The Input Datasets
* The input datasets need to be added in the [files/input/lido](https://github.com/iliedorobat/enriching-cultural-heritage-metadata/tree/master/files/input/lido) directory.
* These datasets have been downloaded from the [data.gov.ro portal](http://data.gov.ro/organization/institutul-national-al-patrimoniului).

### The Output Datasets
* After running the application, the output datasets are thrown into the [files/output/lido2edm](https://github.com/iliedorobat/enriching-cultural-heritage-metadata/tree/master/files/output/lido2edm) directory.<br/>
**Make sure you've created this directory!**

### Online Output Datasets
* The output datasets are available through the [opendata repository](http://opendata.cs.pub.ro/repo/core/admin/dataview.html)
and can be queried through the [opendata sparql endpoint](http://opendata.cs.pub.ro/repo/sparql/admin/squebi.html).

## Translating the Cultural Heritage metadata stored in DSpace into Linked Data through EDM object-centric approach

### The Application Location
The application is located in the [src.ro.webdata.translator.edm.approach.object.dspace](https://github.com/iliedorobat/enriching-cultural-heritage-metadata/tree/master/src/ro/webdata/translator/edm/approach/object/dspace) package.

### Running the Application
The application can be run through [Main.java](https://github.com/iliedorobat/enriching-cultural-heritage-metadata/blob/master/src/ro/webdata/translator/edm/approach/object/dspace/Main.java).

### The Input Datasets
* The input datasets need to be added in the [files/input/dspace](https://github.com/iliedorobat/enriching-cultural-heritage-metadata/tree/master/files/input/dspace) directory.
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

### The Output Datasets
* After running the application, the output datasets are thrown into the [files/output/dspace2edm](https://github.com/iliedorobat/enriching-cultural-heritage-metadata/tree/master/files/output/dspace2edm) directory.<br/>
**Make sure you've created this directory!**
