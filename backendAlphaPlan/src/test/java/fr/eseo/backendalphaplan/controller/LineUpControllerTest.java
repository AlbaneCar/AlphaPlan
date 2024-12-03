package fr.eseo.backendalphaplan.controller;

import fr.eseo.backendalphaplan.model.LineUp;
import fr.eseo.backendalphaplan.model.ValidationLineUp;
import fr.eseo.backendalphaplan.services.LineUpService;
import fr.eseo.backendalphaplan.services.ValidationLineUpService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LineUpControllerTest {

    @InjectMocks
    private LineUpController lineUpController;

    @Mock
    private LineUpService lineUpService;

    @Mock
    private ValidationLineUpService validationLineUpService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createLineUpPropositionReturnsExpectedResponse() {
        LineUp lineUp = new LineUp();
        when(lineUpService.createLineUp(any())).thenReturn(lineUp);

        ResponseEntity<LineUpController.LineUpResponse> result = lineUpController.createLineUpProposition(new LineUpController.LineUpRequest());

        assertEquals(lineUp, result.getBody().getLineUp());
    }

    @Test
    void checkIfAlreadyCreatedReturnsExpectedLineUps() {
        List<LineUp> lineUps = Collections.singletonList(new LineUp());
        when(lineUpService.getLineUpByAuteur(any())).thenReturn(lineUps);

        ResponseEntity<LineUpController.LineUpResponse> result = lineUpController.checkIfAlreadyCreated(new LineUpController.LineUpRequest());

        assertEquals(lineUps, result.getBody().getLineUps());
    }

    @Test
    void getAllLineUpPropositionsReturnsExpectedLineUps() {
        List<LineUp> lineUps = Collections.singletonList(new LineUp());
        when(lineUpService.getAll()).thenReturn(lineUps);

        ResponseEntity<LineUpController.LineUpResponse> result = lineUpController.getAllLineUpPropositions();

        assertEquals(lineUps, result.getBody().getLineUps());
    }

    @Test
    void updateLineUpReturnsExpectedMessage() {
        String message = "LineUp updated";
        when(lineUpService.updateLineUp(any())).thenReturn(message);

        ResponseEntity<LineUpController.LineUpResponse> result = lineUpController.updateLineUp(new LineUpController.ValidationRequest());

        assertEquals(message, result.getBody().getMessage());
    }

    @Test
    void deleteLineUpReturnsExpectedMessage() {
        String message = "LineUp deleted";
        when(lineUpService.deleteLineUp(any())).thenReturn(message);

        ResponseEntity<LineUpController.LineUpResponse> result = lineUpController.deleteLineUp(new LineUpController.LineUpRequest());

        assertEquals(message, result.getBody().getMessage());
    }

    @Test
    void getLineUpStatusReturnsExpectedValidations() {
        List<ValidationLineUp> validations = Collections.singletonList(new ValidationLineUp());
        when(validationLineUpService.getStatusOf(any())).thenReturn(validations);

        ResponseEntity<LineUpController.ValidationResponse> result = lineUpController.getLineUpStatus(new LineUpController.ValidationRequest());

        assertEquals(validations, result.getBody().getValidations());
    }
    @Test
    void updateValidationSlotReturnsExpectedMessage() {
        String message = "Réponse soumise avec succès !";
        doNothing().when(validationLineUpService).analyseRequest(any());

        ResponseEntity<LineUpController.ValidationResponse> result = lineUpController.updateValidationSlot(new LineUpController.ValidationRequest());

        assertEquals(message, result.getBody().getMessage());
    }

    @Test
    void createLineUpPropositionReturnsBadRequestWhenInvalidData() {
        when(lineUpService.createLineUp(any())).thenThrow(IllegalArgumentException.class);

        ResponseEntity<LineUpController.LineUpResponse> result = lineUpController.createLineUpProposition(new LineUpController.LineUpRequest());

        assertEquals(400, result.getStatusCodeValue());
    }


    @Test
    void updateLineUpReturnsBadRequestWhenInvalidData() {
        when(lineUpService.updateLineUp(any())).thenThrow(IllegalArgumentException.class);

        ResponseEntity<LineUpController.LineUpResponse> result = lineUpController.updateLineUp(new LineUpController.ValidationRequest());

        assertEquals(400, result.getStatusCodeValue());
    }

    @Test
    void deleteLineUpReturnsBadRequestWhenInvalidData() {
        when(lineUpService.deleteLineUp(any())).thenThrow(IllegalArgumentException.class);

        ResponseEntity<LineUpController.LineUpResponse> result = lineUpController.deleteLineUp(new LineUpController.LineUpRequest());

        assertEquals(400, result.getStatusCodeValue());
    }

    @Test
    void getLineUpStatusReturnsBadRequestWhenInvalidData() {
        when(validationLineUpService.getStatusOf(any())).thenThrow(IllegalArgumentException.class);

        ResponseEntity<LineUpController.ValidationResponse> result = lineUpController.getLineUpStatus(new LineUpController.ValidationRequest());

        assertEquals(400, result.getStatusCodeValue());
    }


    @Test
    void checkIfAlreadyCreatedReturnsBadRequestWhenInvalidData() {
        when(lineUpService.getLineUpByAuteur(any())).thenThrow(IllegalArgumentException.class);

        ResponseEntity<LineUpController.LineUpResponse> result = lineUpController.checkIfAlreadyCreated(new LineUpController.LineUpRequest());

        assertEquals(400, result.getStatusCodeValue());
    }

    @Test
    void updateValidationSlotReturnsBadRequestWhenInvalidData() {
        doThrow(IllegalArgumentException.class).when(validationLineUpService).analyseRequest(any());

        ResponseEntity<LineUpController.ValidationResponse> result = lineUpController.updateValidationSlot(new LineUpController.ValidationRequest());

        assertEquals(400, result.getStatusCodeValue());
    }

    @Test
    void testConstructorAndGetters() {
        LineUpController.LineUpRequest lineUpRequest = new LineUpController.LineUpRequest(1, 2);

        assertEquals(1, lineUpRequest.getAuteur());
        assertEquals(2, lineUpRequest.getProposition());
    }

    @Test
    void testSetters() {
        LineUpController.LineUpRequest lineUpRequest = new LineUpController.LineUpRequest();
        lineUpRequest.setAuteur(3);
        lineUpRequest.setProposition(4);

        assertEquals(3, lineUpRequest.getAuteur());
        assertEquals(4, lineUpRequest.getProposition());
    }

    @Test
    void testConstructorAndGetters2() {
        LineUpController.ValidationRequest validationRequest = new LineUpController.ValidationRequest(1, 2, true);

        assertEquals(1, validationRequest.getAuteur());
        assertEquals(2, validationRequest.getProposition());
        assertEquals(true, validationRequest.getReponse());
    }

    @Test
    void testSetters2() {
        LineUpController.ValidationRequest validationRequest = new LineUpController.ValidationRequest();
        validationRequest.setAuteur(3);
        validationRequest.setProposition(4);
        validationRequest.setReponse(false);

        assertEquals(3, validationRequest.getAuteur());
        assertEquals(4, validationRequest.getProposition());
        assertEquals(false, validationRequest.getReponse());
    }






}


class ValidationResponseTest {

    @Test
    void testConstructorAndGetters() {
        List<ValidationLineUp> validations = new ArrayList<>();
        String message = "Test message";
        LineUpController.ValidationResponse validationResponse = new LineUpController.ValidationResponse(validations, message);

        assertEquals(validations, validationResponse.getValidations());
        assertEquals(message, validationResponse.getMessage());
    }

    @Test
    void testNoArgConstructor() {
        LineUpController.ValidationResponse validationResponse = new LineUpController.ValidationResponse();

        assertNotNull(validationResponse);
    }
}

class LineUpResponseTest {

    @Test
    void testConstructorAndGetters() {
        List<LineUp> lineUps = new ArrayList<>();
        LineUp lineUp = new LineUp();
        String message = "Test message";
        LineUpController.LineUpResponse lineUpResponse = new LineUpController.LineUpResponse(lineUps, lineUp, message);

        assertEquals(lineUps, lineUpResponse.getLineUps());
        assertEquals(lineUp, lineUpResponse.getLineUp());
        assertEquals(message, lineUpResponse.getMessage());
    }

    @Test
    void testNoArgConstructor() {
        LineUpController.LineUpResponse lineUpResponse = new LineUpController.LineUpResponse();

        assertNotNull(lineUpResponse);
    }
}