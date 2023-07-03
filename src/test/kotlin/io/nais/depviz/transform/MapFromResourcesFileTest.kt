package io.nais.depviz.transform

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class MapFromResourcesFileTest{


    @Test
    fun test(){
        assertThat(MapFromResourcesFile("loc/loc_nais.txt", delimiter = " ").parseToIntValues()).hasSize(238)


    }
}