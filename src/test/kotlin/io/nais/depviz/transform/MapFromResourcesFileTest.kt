package io.nais.depviz.transform

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class MapFromResourcesFileTest{




    @Test
    fun readDirectory(){
        assertThat(MapFromResourcesFile().readAndParseWithDelimiter()).isNotEmpty
    }
}