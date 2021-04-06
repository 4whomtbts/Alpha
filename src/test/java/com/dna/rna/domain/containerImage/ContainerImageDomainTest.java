package com.dna.rna.domain.containerImage;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ContainerImageDomainTest {

    @Test
    @DisplayName("ContainerImage 제대로 만들기")
    void containerImageConstructor_O() {
        ContainerImage containerImage = new ContainerImage("imageName", "imageNickName", "Desc");
        assertThat(containerImage.getContainerImageName()).isEqualTo("imageName");
        assertThat(containerImage.getContainerImageNickName()).isEqualTo("imageNickName");
        assertThat(containerImage.getContainerImageDescription()).isEqualTo("Desc");
    }
    @Test
    @DisplayName("requireNonNull에 의한 NullPointerException 발생")
    void containerImageConstructor_X() {
        assertThrows(NullPointerException.class, () -> new ContainerImage(null, "nick", "desc"));
        assertThrows(NullPointerException.class, () -> new ContainerImage("image", null, "desc"));
        assertThrows(NullPointerException.class, () -> new ContainerImage("image", "nick", null));
    }
}
