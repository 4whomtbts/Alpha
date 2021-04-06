package com.dna.rna.service;

import com.dna.rna.domain.ServerResource;
import com.dna.rna.domain.containerImage.ContainerImage;
import com.dna.rna.domain.containerImage.ContainerImageRepository;
import com.dna.rna.domain.gpu.Gpu;
import com.dna.rna.domain.instance.InstanceRepository;
import com.dna.rna.domain.instanceGpu.InstanceGpu;
import com.dna.rna.domain.instanceGpu.InstanceGpuRepository;
import com.dna.rna.domain.server.Server;
import com.dna.rna.domain.server.ServerRepository;
import com.dna.rna.domain.serverPort.ServerPort;
import com.dna.rna.domain.serverPort.ServerPortRepository;
import com.dna.rna.domain.user.User;
import com.dna.rna.dto.InstanceCreationDto;
import com.dna.rna.dto.InstanceDto;
import com.dna.rna.exception.DCloudException;
import com.dna.rna.service.util.DCloudError;
import com.dna.rna.service.util.SshExecutor;
import com.dna.rna.service.util.SshResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations="classpath:application-test.properties")
public class InstanceServiceTest {

    @Mock
    private InstanceRepository instanceRepository;

    @Mock
    private ServerRepository serverRepository;

    @Mock
    private ServerPortRepository serverPortRepository;

    @Mock
    private ContainerImageRepository containerImageRepository;

    @Mock
    private InstanceGpuRepository instanceGpuRepository;

    @Mock
    private SshExecutor sshExecutor;

    @InjectMocks
    private InstanceService instanceService;

    private static final int NUM_SERVER = 6;
    private static final int NUM_GPU_IN_SERVER = 8;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    private List<Server> getNewServerList() {
        List<Server> serverList = new ArrayList<>();
        List<Integer> allFreeGPU = new ArrayList<>();
        for (int i=0; i < NUM_SERVER; i++) {
            Server server = new Server(i+1, "192.168.1.1"+(i+1), 8081+i, 9000 + (i*100),
                    new ServerResource(new ArrayList<>(allFreeGPU)));
            List<Gpu> gpuList = new ArrayList<>();
            for (int j=0; j < NUM_GPU_IN_SERVER; j++) {
                gpuList.add(new Gpu(server, j, UUID.randomUUID().toString(), "GTX 3080"));
            }
            server.setGpuList(gpuList);
            serverList.add(server);
        }
        return serverList;
    }

    @Test
    public void createInstance_는서버에충분한자원이있다면_할당후_에러를_리턴하지않는다() throws Exception {
        User mockUser = User.of("loginId", "password", "John Doe", "organization", "010-0000-0000", "abc@dgu.ac.kr");
        Server mockServer = new Server(0, "192.168.1.11", 22, 10000, new ServerResource());
        final long CONT_IMAGE_ID = 0;
        when(containerImageRepository.findById(CONT_IMAGE_ID))
                .thenReturn(Optional.of(new ContainerImage("foo Image", "bravo", "desc")));
        when(serverRepository.findAll()).thenReturn(new LinkedList<>(Collections.singletonList(mockServer)));
        when(instanceRepository.save(any())).then(AdditionalAnswers.returnsFirstArg());
        when(instanceGpuRepository.saveAll(anyList())).then(AdditionalAnswers.returnsFirstArg());

        when(serverRepository.findAll()).thenReturn(getNewServerList());

        when(serverPortRepository.fetchFreeInternalPortOfServer(mockServer)).thenReturn(Collections.emptyList());
        when(serverPortRepository.fetchFreeExternalPortOfServer(mockServer)).thenReturn(Collections.emptyList());
        when(serverPortRepository.saveAll(anyList())).then(AdditionalAnswers.returnsFirstArg());
        when(sshExecutor.createNewInstance(any(), any(), anyList(), any(), any(), anyString()))
                .thenReturn(
                        new SshResult<>(null, new InstanceCreationDto("FOO_CONTAINER_ID", "FOO_HASH")));
        DCloudError error = instanceService.createInstance(
                UUID.randomUUID().toString(),
                new InstanceDto.Post("foo", mockUser, "sudoerId", "sudoerPwd",
                                     "purpose", CONT_IMAGE_ID, 1,
                                      false, 8, true,
                                      new ArrayList<>(), new ArrayList<>()), LocalDateTime.MAX);
        assertThat(error, is(nullValue()));
    }


    @Test
    public void createInstance_는서버에충분한자원이있다면_요청받은_갯수만큼의_InstanceGPU를_할당하여_새로운_인스턴스를_생성한다() throws Exception {
        final int REQUESTED_GPU = 8;

        User mockUser = User.of("loginId", "password", "John Doe", "organization", "010-0000-0000", "abc@dgu.ac.kr");
        Server mockServer = new Server(0, "192.168.1.11", 22, 10000, new ServerResource());
        final long CONT_IMAGE_ID = 0;
        when(containerImageRepository.findById(CONT_IMAGE_ID))
                .thenReturn(Optional.of(new ContainerImage("foo Image", "bravo", "desc")));
        when(serverRepository.findAll()).thenReturn(new LinkedList<>(Collections.singletonList(mockServer)));
        when(instanceRepository.save(any())).then(AdditionalAnswers.returnsFirstArg());
        when(instanceGpuRepository.saveAll(anyList())).then(AdditionalAnswers.returnsFirstArg());

        when(serverRepository.findAll()).thenReturn(getNewServerList());

        when(serverPortRepository.fetchFreeInternalPortOfServer(mockServer)).thenReturn(Collections.emptyList());
        when(serverPortRepository.fetchFreeExternalPortOfServer(mockServer)).thenReturn(Collections.emptyList());
        when(serverPortRepository.saveAll(anyList())).then(AdditionalAnswers.returnsFirstArg());
        when(sshExecutor.createNewInstance(any(), any(), anyList(), any(), any(), anyString()))
                .thenReturn(
                        new SshResult<>(null, new InstanceCreationDto("FOO_CONTAINER_ID", "FOO_HASH")));
        DCloudError error = instanceService.createInstance(
                UUID.randomUUID().toString(),
                new InstanceDto.Post("foo", mockUser, "sudoerId", "sudoerPwd",
                        "purpose", CONT_IMAGE_ID, 1,
                        false, REQUESTED_GPU, true,
                        new ArrayList<>(), new ArrayList<>()), LocalDateTime.MAX);
        assertThat(error, is(nullValue()));

        ArgumentCaptor<List<InstanceGpu>> captor = ArgumentCaptor.forClass((Class) List.class);
        verify(instanceGpuRepository, Mockito.times(1)).saveAll(captor.capture());
        List<InstanceGpu> SAVED_INSTANCE_GPUS = captor.getValue();
        assertThat(SAVED_INSTANCE_GPUS.size(), is(REQUESTED_GPU));
    }
}
