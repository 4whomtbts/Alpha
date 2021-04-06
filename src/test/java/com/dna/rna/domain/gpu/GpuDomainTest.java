package com.dna.rna.domain.gpu;

import com.dna.rna.domain.ServerResource;
import com.dna.rna.domain.server.Server;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GpuDomainTest {

    @Test
    void WHEN_Gpu_생성_성공() {
        Server server = new Server(1, "111.111.111.11", 11, 11, new ServerResource());
        Gpu gpu = new Gpu(server,1,"uuid", "GTX4096");
        assertThat(gpu.getServer()).isEqualTo(server);
    }
    @Test
    void WHEN_server_null_uuid_null_modelName_null_Gpu_생성_실패() {
        Server server = new Server(1, "111.111.111.11", 11, 11, new ServerResource());
        assertThrows(NullPointerException.class, () -> new Gpu(null,1,"uuid", "GTX4096"));

        assertThrows(NullPointerException.class, () -> new Gpu(server,1, null, "GTX4096"));

        assertThrows(NullPointerException.class, () -> new Gpu(server,1, "uuid", null));
    }
}
