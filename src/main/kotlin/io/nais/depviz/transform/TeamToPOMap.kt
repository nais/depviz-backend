package io.nais.depviz.transform

val teamToPO = mapOf(
        "aap" to "PO AAP",
        "alf" to "",
        "amt" to "",
        "arbeidsforhold" to "ITO Data og virksomhetsløsninger",
        "arbeidsgiver-inkludering" to "PO Arbeidsgiver",
        "arbeidsgiver" to "PO Arbeidsgiver",
        "aura" to "ITO Tjensteplattform",
        "bidrag" to "PO Familie",
        "dab" to "PO Arbeidsoppfølging",
        "datafolk" to "",
        "dataplattform" to "ITO Tjensteplattform",
        "debug" to "ITO Tjensteplattform",
        "designsystem" to "ITO Tjensteplattform",
        "detsombetyrnoe" to "",
        "disykefravar" to "ITO Data og virksomhetsløsninger",
        "dolly" to "ITO Data og virksomhetsløsninger",
        "dsopkontroll" to "",
        "dusseldorf" to "PO Familie",
        "dv-a-team" to "ITO Data og virksomhetsløsninger",
        "dv-familie" to "ITO Data og virksomhetsløsninger",
        "dv-team-pensjon" to "ITO Data og virksomhetsløsninger",
        "dvh-arbeid" to "ITO Data og virksomhetsløsninger",
        "eessibasis" to "PO Pensjon",
        "eessipensjon" to "PO Pensjon",
        "etterlatte" to "PO Pensjon",
        "fager" to "PO Arbeidsgiver",
        "farskapsportal" to "PO Familie",
        "fist" to "Nystøl",
        "flex" to "PO Helse",
        "frontendplattform" to "ITO Tjensteplattform",
        "helsearbeidsgiver" to "PO Arbeidsgiver",
        "historisk" to "PO Pensjon",
        "infotrygd" to "",
        "integrasjon" to "ITO Tjensteplattform",
        "isa" to "",
        "k9saksbehandling" to "PO Familie",
        "klage" to "",
        "leesah-quiz" to "",
        "log" to "ITIP",
        "medlemskap" to "",
        "meldekort" to "PO Arbeid",
        "min-side" to "",
        "nada" to "ITO Tjensteplattform",
        "nais-analyse" to "ITO Tjensteplattform",
        "nais-mac-mgmt" to "ITO Tjensteplattform",
        "nais-system" to "ITO Tjensteplattform",
        "nais" to "ITO Tjensteplattform",
        "naisdevice" to "ITO Tjensteplattform",
        "navdig" to "ITIP",
        "nom" to "ITO Data og virksomhetsløsninger",
        "obo" to "PO Arbeidsoppfølging",
        "okonomi" to "PO Utbetaling",
        "omsorgspenger" to "PO Familie",
        "oppgavehandtering" to "",
        "org" to "ITO Data og virksomhetsløsninger",
        "paw" to "PO Arbeid",
        "pdl" to "ITO Data og virksomhetsløsninger",
        "pensjon-person" to "PO Pensjon",
        "pensjon-regler" to "PO Pensjon",
        "pensjon-saksbehandling" to "PO Pensjon",
        "pensjondeployer" to "PO Pensjon",
        "pensjonopptjening" to "PO Pensjon",
        "pensjonsamhandling" to "PO Pensjon",
        "pensjonsbrev" to "PO Pensjon",
        "pensjonselvbetjening" to "PO Pensjon",
        "permittering-og-nedbemanning" to "PO Arbeidsgiver",
        "personbruker" to "",
        "personoversikt" to "",
        "pia" to "PO Arbeidsgiver",
        "pim" to "PO Arbeidsgiver",
        "plattformsikkerhet" to "ITO Tjensteplattform",
        "pleiepenger" to "PO Familie",
        "poao" to "PO Arbeidsoppfølging",
        "pto" to "PO Arbeidsoppfølging",
        "ptsak" to "PO Arbeidsoppfølging",
        "repr" to "",
        "risk" to "PO Helse",
        "skjemadigitalisering" to "ITO Data og virksomhetsløsninger",
        "supstonad" to "PO Pensjon",
        "tbd" to "PO Helse",
        "team-ai" to "",
        "team-atom" to "ITIP",
        "team-dialog" to "ITO Data og virksomhetsløsninger",
        "team-emottak" to "ITO Data og virksomhetsløsninger",
        "team-esyfo" to "PO Helse",
        "team-innhold" to "PO Arbeidsgiver",
        "team-inntekt" to "ITO Data og virksomhetsløsninger",
        "team-mulighetsrommet" to "PO Arbeid",
        "team-nof" to "Nystøl",
        "team-oebs" to "ITO Data og virksomhetsløsninger",
        "team-postgres" to "ITO Tjensteplattform",
        "team-researchops" to "ITO Tjensteplattform",
        "team-rocket" to "ITO Data og virksomhetsløsninger",
        "team-sigma" to "ITO Data og virksomhetsløsninger",
        "team-soknad" to "ITO Data og virksomhetsløsninger",
        "team-tiltak" to "PO Arbeidsoppfølging",
        "teamabac" to "ITO Data og virksomhetsløsninger",
        "teamarenanais" to "",
        "teamcoronapenger" to "PO Arbeidsgiver",
        "teamcrm" to "PO Arbeidsgiver",
        "teamdagpenger" to "PO Arbeid",
        "teamdatajegerne" to "ITO Digital sikkerhet",
        "teamdigihot" to "",
        "teamdigisos" to "",
        "teamdokumenthandtering" to "ITO Data og virksomhetsløsninger",
        "teameresept" to "",
        "teamfamilie" to "PO Familie",
        "teamforeldrepenger" to "PO Familie",
        "teamfrikort" to "",
        "teamia" to "PO Arbeidsgiver",
        "teamkuhr" to "",
        "teamloennskomp" to "PO Arbeidsgiver",
        "teammelosys" to "",
        "teamnks" to "",
        "teamoppfolging" to "PO Arbeidsoppfølging",
        "teampam" to "",
        "teampensjon" to "PO Pensjon",
        "teamserviceklage" to "",
        "teamsykefravr" to "PO Helse",
        "teamsykmelding" to "PO Helse",
        "toi" to "PO Arbeidsgiver",
        "tpts" to "PO Arbeidsoppfølging",
        "traktor" to "ITO Data og virksomhetsløsninger",
        "utvikleropplevelse" to "ITO Tjensteplattform",
        "yrkesskade" to "PO Pensjon"
    )