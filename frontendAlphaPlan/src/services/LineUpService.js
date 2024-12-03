import { customFetch } from "../utils/FetchUtil";

// Requête de création d'une proposition de changement de line-up
export const createLineUp = async (auteurId, propositionId) => {
    try {
        const requestBody = {
            auteur: auteurId,
            proposition: propositionId
        };

        const requestOptions = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(requestBody)
        };
        const response = await customFetch('lineUp/creer', requestOptions);
        return response.json();
    }
    catch (error) {
        throw error;
    }
};

// Requête pour obtenir la liste des propositions de changement de line-up
export const getAll = async () => {
    try {
        const requestOptions = {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            }
        };
        const response = await customFetch('lineUp/liste', requestOptions);
        return response.json();
    }
    catch (error) {
        throw error;
    }
};


// Requête permettant d'avoir les propositions soumises par un auteur
export const getAllBy = async (auteurId) => {
    try {
        const requestBody = {
            auteur: auteurId
        };

        const requestOptions = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(requestBody)
        };
        const response = await customFetch('lineUp/history', requestOptions);
        return response.json();
    }
    catch (error) {
        throw error;
    }
}

// Validation d'une proposition (côté TM/OS)
export const updateValidation = async (auteurId, propositionId, state) => {
    try {
        const requestBody = {
            auteur: auteurId,
            proposition: propositionId,
            reponse: state
        };
        const requestOptions = {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(requestBody)
        };
        const response = await customFetch('lineUp/validations/update', requestOptions);
        return response.json();
    }
    catch (error) {
        throw error;
    }
};


// Requête de création d'une proposition de changement de line-up
export const getValidationsOf = async (propositionId) => {
    try {
        const requestBody = {
            proposition: propositionId
        };
        const requestOptions = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(requestBody)
        };
        const response = await customFetch('lineUp/validations/liste', requestOptions);
        return response.json();
    }
    catch (error) {
        throw error;
    }
};


// Requête pour appliquer le changement de line-up
export const applyChanges = async (propositionId, state) => {
    try {
        const requestBody = {
            proposition: propositionId,
            reponse: state
        };
        const requestOptions = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(requestBody)
        };
        const response = await customFetch('lineUp/update', requestOptions);
        return response.json();
    }
    catch (error) {
        throw error;
    }
};


// Requête pour supprimer une demande de changement de line-up
export const deleteOf = async (auteurId, propositionId) => {
    try {
        const requestBody = {
            auteur: auteurId,
            proposition: propositionId
        };
        const requestOptions = {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(requestBody)
        };
        const response = await customFetch('lineUp/delete', requestOptions);
        return response.json();
    }
    catch (error) {
        throw error;
    }
};