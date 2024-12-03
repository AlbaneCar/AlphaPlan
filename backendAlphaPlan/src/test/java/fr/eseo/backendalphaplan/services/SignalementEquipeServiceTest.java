package fr.eseo.backendalphaplan.services;

import fr.eseo.backendalphaplan.model.Notification;

import fr.eseo.backendalphaplan.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class SignalementEquipeServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private SignalementEquipeService signalementEquipeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveSignalementEquipe() {
        Notification signalementEquipe = new Notification();

        when(notificationRepository.save(signalementEquipe))
                .thenReturn(signalementEquipe);

        Notification actualSignalementEquipe = signalementEquipeService
                .saveSignalementEquipe(signalementEquipe);

        assertEquals(signalementEquipe, actualSignalementEquipe);
    }
}
