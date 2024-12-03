import {customFetch} from "../utils/FetchUtil";
import {useToast} from 'vue-toastification';

const toast = useToast();

export async function creerEquipes(amount) {
    try {
        const response = await customFetch(`equipes/creerEquipes/${amount}`);

        if (!response.ok) {
            throw new Error('Failed to generate teams');
        }
        return response.json();
    } catch (error) {
        console.error('Error submitting form data:', error);
        throw error;
    }
};

export async function getEncadrants() {
    try {
        const response = await customFetch(`users/encadrants`);
        if (!response.ok) {
            throw new Error('Failed to get encadrants');
        }
        return response.json();
    } catch (error) {
        throw error;
    }
};

export async function envoyerEquipes(teamsData, etatEquipes) {
    try {
        const response = await customFetch(`equipes/envoyer/${etatEquipes}`, {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
            },
            body: JSON.stringify(teamsData)
          });

        if (!response.ok) {
            throw new Error('Failed to publish teams');
        }
        return response.json();
    } catch (error) {
        throw error;
    }
};

export async function setRole(userId, roleId) {
    try {
        const response = await customFetch(`roles/attribue/${userId}/${roleId}`, {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
            }
          });

        if (!response.ok) {
            throw new Error('Failed to set role');
        }
        return response.json();
    } catch (error) {
        throw error;
    }
};

export async function supprimerEquipes() {
    try {
        const response = await customFetch(`v1.0/equipes/supprimer`, {
            method: 'DELETE',
            headers: {
              'Content-Type': 'application/json'
            }
          });

        if (!response.ok) {
            throw new Error('Failed to delete teams');
        }
        return response.json();
    } catch (error) {
        throw error;
    }
};

export async function getValidationEquipes() {
    try {
        const response = await customFetch(`v1.0/validationequipes`, {
            method: 'GET',
            headers: {
              'Content-Type': 'application/json',
            }
          });

        if (!response.ok) {
            throw new Error('Failed to get teams validation');
        }
        return response.json();
    } catch (error) {
        throw error;
    }
};

export async function deleteValidationEquipes() {
    try {
        const response = await customFetch(`v1.0/validationequipes`, {
            method: 'DELETE',
            headers: {
              'Content-Type': 'application/json'
            }
          });

        if (!response.ok) {
            throw new Error('Failed to delete teams validation');
        }
        return response.json();
    } catch (error) {
        console.error(error);
        throw error;
    }
};

export async function postValidationEquipes(userId) {
    try {
        const response = await customFetch(`v1.0/validationequipes/${userId}`, {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
            }
          });

        if (!response.ok) {
            throw new Error('Failed to post teams validation');
        }
        toast.success('Les équipes ont bien été validées !', {position : 'top-center'});
        return response.json();
    } catch (error) {
        toast.error('Erreur ! Vous avez déjà validé les équipes', {position : 'top-center'});
        throw error;
    }
};

export async function modifierEquipes(etatEquipes) {
    try {
        const response = await customFetch(`equipes/etatEquipes/${etatEquipes}`, {
            method: 'PUT',
            headers: {
              'Content-Type': 'application/json',
            }
          });

        if (!response.ok) {
            throw new Error('Failed to modify teams');
        }
        const responseJson = await response.json();
        return responseJson;
    } catch (error) {
        throw error;
    }
};


