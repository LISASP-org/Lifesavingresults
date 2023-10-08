package org.lisasp.competition.results.imports.rescue2022.model.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Entry {
    @JsonProperty(value = "b")
    private String b;
    @JsonProperty(value = "c")
    private String c;
    @JsonProperty(value = "PlaCls")
    private String plaCls;
    @JsonProperty(value = "PlaClsIta")
    private String PlaClsIta;
    @JsonProperty(value = "PlaClsRil")
    private String PlaClsRil;
    @JsonProperty(value = "PlaLane")
    private String PlaLane;
    @JsonProperty(value = "PlaCod")
    private String PlaCod;
    @JsonProperty(value = "PlaOrd")
    private String plaOrd;
    @JsonProperty(value = "PlaNat")
    private String PlaNat;
    @JsonProperty(value = "PlaTeamCod")
    private String PlaTeamCod;
    @JsonProperty(value = "TeamDescrIta")
    private String TeamDescrIta;
    @JsonProperty(value = "Team2DescrIta")
    private String Team2DescrIta;
    @JsonProperty(value = "PlaSurname")
    private String PlaSurname;
    @JsonProperty(value = "PlaName")
    private String PlaName;
    @JsonProperty(value = "PlaBirth")
    private String PlaBirth;
    @JsonProperty(value = "MemIscr")
    private String MemIscr;
    @JsonProperty(value = "MemFC")
    private String MemFC;
    @JsonProperty(value = "MemNote")
    private String MemNote;
    @JsonProperty(value = "PlaCat")
    private String PlaCat;
    @JsonProperty(value = "DiffIntRec1")
    private String DiffIntRec1;
    @JsonProperty(value = "DiffIntRec2")
    private String DiffIntRec2;
    @JsonProperty(value = "DiffIntRec3")
    private String DiffIntRec3;
    @JsonProperty(value = "DiffIntRec4")
    private String DiffIntRec4;
    @JsonProperty(value = "DiffIntRec5")
    private String DiffIntRec5;
    @JsonProperty(value = "MemPrest")
    private String MemPrest;
    @JsonProperty(value = "MemQual")
    private String MemQual;
    @JsonProperty(value = "MemPoint")
    private String MemPoint;
    @JsonProperty(value = "MemRBattCls")
    private String MemRBattCls;
    @JsonProperty(value = "MemRBatt")
    private String MemRBatt;
    @JsonProperty(value = "MemRComment")
    private String MemRComment;
    @JsonProperty(value = "Gap")
    private String Gap;
    @JsonProperty(value = "Last50")
    private String Last50;
    @JsonProperty(value = "Last100")
    private String Last100;
    @JsonProperty(value = "Last200")
    private String Last200;
    @JsonProperty(value = "Last400")
    private String Last400;
    @JsonProperty(value = "MemFields")
    private MemField[] MemFields;
    @JsonProperty(value = "Players")
    private Player[] players;
}
