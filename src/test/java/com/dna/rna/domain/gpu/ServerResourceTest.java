package com.dna.rna.domain.gpu;

import com.dna.rna.domain.ServerResource;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ServerResourceTest {

    @Test
    void GIVEN_ServerResource_객체를_만들고_WHEN_내부에서_gpu를_가져오면_THEN_8개의_UN_ALLOC() {
        ServerResource serverResource = new ServerResource();
        List<Integer> gpus = serverResource.getGpus();
        assertThat(gpus.size()).isEqualTo(8);
        for (Integer gpu : gpus) {
            assertThat(gpu).isEqualTo(ServerResource.UN_ALLOC);
        }
    }
}
