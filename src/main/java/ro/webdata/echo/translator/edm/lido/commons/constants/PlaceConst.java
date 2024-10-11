package ro.webdata.echo.translator.edm.lido.commons.constants;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PlaceConst {
    public static final String[] RO_COUNTIES = {
            "Alba",
            "Arad",
            "Argeș",
            "Bacău",
            "Bihor",
            "Bistrița-Năsăud",
            "Botoșani",
            "Brașov",
            "Brăila",
            "București",
            "Buzău",
            "Caraș-Severin",
            "Călărași",
            "Cluj",
            "Constanța",
            "Covasna",
            "Dâmbovița",
            "Dolj",
            "Galați",
            "Giurgiu",
            "Gorj",
            "Harghita",
            "Hunedoara",
            "Ialomița",
            "Iași",
            "Ilfov",
            "Maramureș",
            "Mehedinți",
            "Mureș",
            "Neamț",
            "Olt",
            "Prahova",
            "Satu Mare",
            "Sălaj",
            "Sibiu",
            "Suceava",
            "Teleorman",
            "Timiș",
            "Tulcea",
            "Vaslui",
            "Vâlcea",
            "Vrancea"
    };

    public static final List<String> SANITIZED_RO_COUNTIES = Arrays.stream(RO_COUNTIES)
                    .map(county -> StringUtils.stripAccents(county.toLowerCase()))
                    .collect(Collectors.toList());
}
